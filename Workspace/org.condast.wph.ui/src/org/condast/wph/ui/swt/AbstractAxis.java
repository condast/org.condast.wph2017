package org.condast.wph.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractAxis<V extends Object> extends Canvas{
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_RANGE = 10;
	public static final int DEFAULT_STEP = 10;
	
	private PaintListener listener = new PaintListener(){
		private static final long serialVersionUID = 1L;

		@Override
		public void paintControl(PaintEvent event) {
			drawField( event.gc );
		}
	};

	private int range;//+ and - max values
	private int step;//steps between the range (< range)
	
	private List<V> values;
	
	protected AbstractAxis(Composite parent, int style) {
		super(parent, style);
		this.range = DEFAULT_RANGE;
		this.step = DEFAULT_STEP;
		setBackground(Display.getCurrent().getSystemColor( SWT.COLOR_WHITE));
		super.addPaintListener( listener );
		values = new ArrayList<V>();
	}
	
	public void clear(){
		this.values.clear();
	}
	
	public void addInput( V value ){
		this.values.add(value);
	}
	
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	protected void onDrawStart( GC gc ){/* NOTHING */ }
	protected abstract void onDrawValue( GC gc, int index, V value );
	protected void onDrawEnd( GC gc ){/* NOTHING */ }

	protected void drawField( GC gc ){
		this.onDrawStart(gc);
		Rectangle rect = super.getClientArea();
		int halfY = (int)( rect.height/2); 
		int stepSize = (rect.height/step);
		gc.drawLine(0, 0, 0, rect.height);//vertical line
		gc.drawLine(0, halfY, rect.width, halfY);//horizontal line
		for( int i=0; i<=(this.step/2); i++ ){
			int y = i*stepSize;
			gc.drawLine(0, halfY - y, 10, halfY - y);//horizontal line			
			gc.drawLine(0, halfY + y, 10, halfY + y);//horizontal line			
		}
		for( int i=0; i<values.size(); i++ )
			this.onDrawValue(gc, i, values.get(i));
		if( values.size() > rect.width )
			values.remove(0);
		this.onDrawEnd(gc);
	}
	
	public void refresh(){
		this.redraw();
	}
}
