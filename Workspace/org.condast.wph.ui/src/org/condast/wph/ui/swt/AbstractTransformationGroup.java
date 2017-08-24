package org.condast.wph.ui.swt;

import org.condast.commons.ui.swt.AbstractCanvasGroup;
import org.condast.symbiotic.core.def.ITransformation;
import org.condast.symbiotic.core.transformation.ITransformListener;
import org.condast.symbiotic.core.transformation.TransformEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class AbstractTransformationGroup<I,O extends Object, T extends ITransformation<I,O>> extends AbstractCanvasGroup<T> {
	private static final long serialVersionUID = 1L;

	private Label inputs;
	private Label behaviour;
	private Label transformation;

	private ITransformListener<O> listener = new ITransformListener<O>() {
		
		@Override
		public void notifyChange(TransformEvent<O> event) {
			try{
				plotValues();
				refresh();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
		}
	};

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractTransformationGroup(Composite parent, int style) {
		super(parent, style);
	}

	
	@Override
	protected void setupDashboard(Composite dashBoard, int style) {
		Label inputLabel = new Label( dashBoard, SWT.NONE );
		inputLabel.setText("Inputs:" );
		inputLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		inputs = new Label( dashBoard, SWT.BORDER );
		inputs.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));

		Label behaviourLabel = new Label( dashBoard, SWT.NONE );
		behaviourLabel.setText("Behaviour:" );
		behaviourLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		behaviour = new Label( dashBoard, SWT.BORDER );
		behaviour.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));		

		Label transformationLabel = new Label( dashBoard, SWT.NONE );
		transformationLabel.setText("Transformation:" );
		transformationLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		transformation = new Label( dashBoard, SWT.BORDER );
		transformation.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
	}

	protected void setInputSize( int size ){
		this.inputs.setText( String.valueOf( size ));
	}
	
	@Override
	protected void onSetInput( T transformation ) {
		transformation.addTransformationListener(listener);
		setText( transformation.getName() );
		inputs.setText( String.valueOf( transformation.getInputSize()));
	}


	@Override
	protected void onRefresh() {
		inputs.setText( String.valueOf( super.getInput().getInputSize()));		
	}
}
