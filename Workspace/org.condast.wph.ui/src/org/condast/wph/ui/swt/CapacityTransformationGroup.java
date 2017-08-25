package org.condast.wph.ui.swt;

import org.condast.commons.ui.date.DateUtils;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.IStressListener;
import org.condast.symbiotic.core.def.StressEvent;
import org.condast.wph.core.def.ICapacityProcess;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CapacityTransformationGroup<I,O extends Object> extends AbstractTransformationGroup<I,O, ICapacityProcess<I,O>> {
	private static final long serialVersionUID = 1L;
	
	private SymbiotGroup sg;

	private IStressListener listener = new IStressListener() {

		@Override
		public void notifyStressChanged(StressEvent event) {
			try{
				if( getDisplay().isDisposed())
					return;
				getDisplay().asyncExec( new Runnable(){

					@Override
					public void run() {
						setInputSize( getInput().getInputSize());
					}
				});
				refresh();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
		}
	};
	
	private Label jobSize;
	private Label capacity;
	private Label dueDate;

	private IBehaviour<?,?> behaviour;

	public CapacityTransformationGroup(Composite parent, int style) {
		super(parent, style);
		sg = new SymbiotGroup(this, SWT.NONE);
		sg.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ));
	}

	@Override
	protected void setupDashboard(Composite dashBoard, int style) {
		Label jobsizeLabel = new Label( dashBoard, SWT.NONE );
		jobsizeLabel.setText("Job Size:" );
		jobsizeLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		jobSize = new Label( dashBoard, SWT.BORDER );
		jobSize.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));

		Label capacityLabel = new Label( dashBoard, SWT.NONE );
		capacityLabel.setText("Capacity:" );
		capacityLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		capacity = new Label( dashBoard, SWT.BORDER );
		capacity.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));		

		Label duedateLabel = new Label( dashBoard, SWT.NONE );
		duedateLabel.setText("Due Date:" );
		duedateLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ));
		dueDate = new Label( dashBoard, SWT.BORDER );
		dueDate.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));		

		super.setupDashboard(dashBoard, style);
	}

	protected IBehaviour<?, ?> getBehaviour() {
		return behaviour;
	}

	public void setInput( ICapacityProcess<I,O> input, IBehaviour<I,O> behaviour ){
		super.setInput(input);
		this.behaviour = behaviour;
		behaviour.addStressListener(listener);
		sg.setInput( behaviour);		
	}
	
	@Override
	protected void onSetInput( ICapacityProcess<I,O> input) {
		jobSize.setText( String.valueOf( input.getJobSize()));
		capacity.setText( String.valueOf( input.getCapacity()));
		
		String datestr = ( input.getFirstDueJob() == null )? "": 
			DateUtils.getFormatted( DateUtils.S_DEFAULT_TIME_FORMAT, input.getFirstDueJob() );
		dueDate.setText( datestr );
		super.onSetInput(input);
	}

	@Override
	protected Integer[] onPlotValues( ICapacityProcess<I,O> input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onRefresh() {
		ICapacityProcess<I,O> input = super.getInput();
		jobSize.setText( String.valueOf( input.getJobSize()));
		capacity.setText( String.valueOf( input.getCapacity()));
		
		String datestr = ( input.getFirstDueJob() == null )? "": 
			DateUtils.getFormatted( DateUtils.S_DEFAULT_TIME_FORMAT, input.getFirstDueJob() );
		dueDate.setText( datestr );
		super.onRefresh();
	}

	
}
