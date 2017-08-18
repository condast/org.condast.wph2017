package org.condast.wph.ui.swt;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.condast.commons.number.NumberUtils;
import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.IStressListener;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.StressEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

public class SymbiotGroup extends Group {
	private static final long serialVersionUID = 1L;

	private SymbiotCollection symbiots;
	private Map<ISymbiot, StressCanvas> canvases;
	private int[] current;
	
	private IStressListener listener = new IStressListener() {
		
		@Override
		public void notifyStressChanged(StressEvent event) {
			try{
				ISymbiot symbiot = (ISymbiot) event.getSource();
				Integer[] values = new Integer[2];
				StressCanvas stressCanvas = canvases.get( symbiot );
				values[0] = (int)( stressCanvas.getRange() * symbiot.getStress());
				values[1] = 0;
				stressCanvas.addInput(values);
				refresh();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
		}
	};
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SymbiotGroup(Composite parent, int style) {
		super(parent, style);
		canvases = new HashMap<ISymbiot, StressCanvas>();
		createGroup( parent, style );
	}

	private void createGroup(Composite parent, int style) {
		setLayout( new GridLayout(1,false));
	}

	public ISymbiot[] getInput(){
		return symbiots.toArray( new ISymbiot[ symbiots.size()]);
	}
	
	public void setInput( SymbiotCollection symbiots) {
		this.symbiots = symbiots;
		this.symbiots.addStressListener(listener);
		setLayout( new GridLayout(symbiots.size(),true));
		for( Control control: getChildren() )
			control.dispose();
		for( ISymbiot symbiot: symbiots ){
			Group group = new Group( this, SWT.BORDER );
			group.setText( symbiot.getId());
			group.setLayout( new FillLayout() );
			group.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
			StressCanvas stressCanvas = new StressCanvas(group, SWT.NONE);
			canvases.put(symbiot, stressCanvas);
			stressCanvas.setInput(symbiot);
		}
		refresh();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void refresh() {
		if( getDisplay().isDisposed() )
			return;
		getDisplay().asyncExec( new Runnable(){

			@Override
			public void run() {
				for( StressCanvas canvas: canvases.values())
					canvas.refresh();
				redraw();
				layout(true);
			}	
		});
	}

	/**
	 * The stress of the symbiot itself
	 * @author Kees
	 *
	 */
	private class StressCanvas extends AbstractAxis<Integer[]>{
		private static final long serialVersionUID = 1L;

		private ISymbiot symbiot;
		private Color temp;
		
		protected StressCanvas(Composite parent, int style) {
			super(parent, style);
		}
		
		private void setInput( ISymbiot symbiot ){
			this.symbiot = symbiot;
			this.symbiot.addStressListener(listener);
		}
		
		@Override
		protected void onDrawStart(GC gc) {
			temp = getForeground();
			Rectangle rect = super.getClientArea();
			int halfY = (int)( rect.height/2 ); 
			int[] arr = {halfY, halfY };
			current = arr;
			super.onDrawStart(gc);
		}

		@Override
		protected void onDrawEnd(GC gc) {
			setForeground( temp );
			super.onDrawEnd(gc);
		}

		@Override
		protected void onDrawValue(GC gc, int index, Integer[] value) {
			Rectangle rect = super.getClientArea();
			int halfY = (int)( rect.height/2 ); 
			int plotprevy = 0;
			int ploty = 0;

			int colour = SWT.COLOR_RED;
			gc.setForeground( getDisplay().getSystemColor( colour ) );
			plotprevy = plotY( halfY, current[0]);
			ploty = plotY( halfY, value[0]);
			gc.drawLine( index-1, plotprevy, index, ploty );
			colour = SWT.COLOR_GREEN;
			gc.setForeground( getDisplay().getSystemColor( colour ) );
			plotprevy = plotY( halfY, current[1]);
			ploty = plotY( halfY, value[1]);
			gc.drawLine( index-1, plotprevy, index, ploty );

			current[0] = value[0];
			current[1] = value[1];
		}
		
		/**
		 * Plot the Y coordinate
		 * @param value
		 * @return
		 */
		protected int plotY( int halfY, int value ){
			return halfY * (1 - NumberUtils.clip( super.getRange(), value )/super.getRange());
		}
	}
}
