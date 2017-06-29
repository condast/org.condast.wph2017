package org.condast.wph.ui.swt;

import org.eclipse.swt.widgets.Composite;
import org.google.geo.mapping.ui.controller.GeoCoderController;
import org.google.geo.mapping.ui.model.MarkerModel;
import org.google.geo.mapping.ui.model.TilesAndPixelsModel;
import org.eclipse.swt.custom.SashForm;
import org.condast.commons.lnglat.LngLat;
import org.condast.js.commons.eval.EvaluationEvent;
import org.condast.js.commons.eval.IEvaluationListener;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.xml.XMLFactoryBuilder;
import org.condast.wph.ui.design.ModelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;

public class WPHFrontend extends Composite {
	private static final long serialVersionUID = 1L;

	private GeoCoderController controller; 
	private IEvaluationListener<Object[]> listener;
	private ModelTableViewer viewer;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WPHFrontend(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(this, SWT.VERTICAL);
		
		Browser browser = new Browser(sashForm, SWT.BORDER);
		controller = new GeoCoderController(browser);
		listener = new EvaluationListener();
		controller.addEvaluationListener(listener);
		
		viewer = new ModelTableViewer(sashForm, SWT.BORDER);
		viewer.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
		sashForm.setWeights(new int[] {2, 1});
	}

	public void setupFrontEnd(){
		TilesAndPixelsModel tpm = new TilesAndPixelsModel(controller);
		tpm.setLocation( new LngLat( 51.8926f, 4.4205f), 11);
		MarkerModel mkm = new MarkerModel( controller );
		ModelProvider provider = ModelProvider.getInstance();
		for( IModel model: provider.getModels() )
			mkm.addMarker(model.getLnglat(), model.getType().getImage());
		tpm.synchronize();
		viewer.setInput(provider.getModels());
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private class EvaluationListener implements IEvaluationListener<Object[]>{

		@Override
		public void notifyEvaluation(EvaluationEvent<Object[]> event) {
			switch( event.getEvaluationEvent() ){
			case INITIALISED:
				setupFrontEnd();
				break;
			default:
				break;
			}
			if( event.getData() == null )
				return;
			MarkerModel.Functions mf = MarkerModel.Functions.valueOf( event.getData()[0].toString() );
			switch( mf ){
			
			case MARKER_CLICKED:
				String name = event.getData()[1].toString();
				//LngLat lnglat = paddress.getLocation();
				//if( !name.equals( lnglat.getId()))
				//	continue;
				//selectMarker(input );
				//EvaluationEvent<IParticipant> nevent = new EvaluationEvent<IParticipant> ( matchBrowser, name, EvaluationEvents.EVENT, input );  
				//for( IEvaluationListener<IParticipant> el: listeners )
				//	el.notifyEvaluation(nevent);
				break;
			default:
				break;
			}
		}	
	}
}