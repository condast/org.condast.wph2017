package org.condast.wph.ui.swt;

import org.condast.commons.number.NumberUtils;
import org.condast.commons.ui.swt.AbstractAxis;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.IStressListener;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.def.StressEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class SymbiotGroup extends Group {
	private static final long serialVersionUID = 1L;

	private int[] current;


	private Label stress;
	private Label behaviour;
	private Label transformation;
	private StressCanvas stressCanvas;

	private IStressListener listener = new IStressListener() {

		@Override
		public void notifyStressChanged(StressEvent event) {
			try{
				ISymbiot symbiot = (ISymbiot) event.getSource();
				Integer[] values = new Integer[2];
				values[0] = (int)((float)stressCanvas.getRange() * symbiot.getStress());
				values[1] = 0;
				stressCanvas.addInput(values);
				refresh();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
		}
	};

	protected SymbiotGroup(Composite parent, int style) {
		super(parent, style);
		setLayout( new GridLayout(2,false));
		Label stressLabel = new Label( this, SWT.NONE );
		stressLabel.setText("Stress:" );
		stressLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		stress = new Label( this, SWT.BORDER );
		stress.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));

		Label behaviourLabel = new Label( this, SWT.NONE );
		behaviourLabel.setText("Behaviour:" );
		behaviourLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		behaviour = new Label( this, SWT.BORDER );
		behaviour.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));		

		Label transformationLabel = new Label( this, SWT.NONE );
		transformationLabel.setText("Transformation:" );
		transformationLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		transformation = new Label( this, SWT.BORDER );
		transformation.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));

		stressCanvas = new StressCanvas(this, SWT.NONE);
		stressCanvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true,2, 1 ));
	}

	public void setInput( IBehaviour<?,?> behaviour ){
		super.setText( behaviour.getId());
		this.behaviour.setText( String.valueOf( behaviour.getRange()));
		this.transformation.setText( String.valueOf( behaviour.getOutput().toString()));

		ISymbiot symbiot = behaviour.getOwner();
		symbiot.addStressListener(listener);
		this.stress.setText( String.valueOf( symbiot.getStress() ));
		stressCanvas.setInput(symbiot);
	}

	public void refresh(){
		if( getDisplay().isDisposed() )
			return;
		getDisplay().asyncExec( new Runnable(){

			@Override
			public void run() {
				stressCanvas.refresh();
				layout();
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
			return (int)( halfY * (1 - (float)NumberUtils.clip( super.getRange(), value )/super.getRange()));
		}
	}
}
