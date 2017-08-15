package org.condast.wph.ui.swt;

import org.eclipse.swt.widgets.Composite;
import org.google.geo.mapping.ui.controller.GeoCoderController;
import org.google.geo.mapping.ui.model.MarkerModel;
import org.google.geo.mapping.ui.model.TilesAndPixelsModel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.condast.commons.latlng.LatLng;
import org.condast.commons.strings.StringStyler;
import org.condast.js.commons.eval.EvaluationEvent;
import org.condast.js.commons.eval.IEvaluationListener;
import org.condast.symbiotic.core.environment.EnvironmentEvent;
import org.condast.symbiotic.core.environment.IEnvironmentListener;
import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.ui.rest.RestController;
import org.condast.wph.ui.rest.RestController.Pages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;

public class WPHFrontend extends Composite {
	private static final long serialVersionUID = 1L;

	private IEvaluationListener<Object[]> listener;
	private ModelTableViewer modelViewer;
	private JourneyTableViewer journeyViewer;
	private Browser browser;
	private GeoCoderController controller; 
	private RestController restController; 
		
	private IContainerEnvironment ce;
	
	private enum Tabs{
		OVERVIEW,
		JOURNEY,
		REST;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	private IEnvironmentListener elistener = new IEnvironmentListener() {
		
		@Override
		public void notifyEnvironmentChanged(EnvironmentEvent event) {
			Display.getDefault().asyncExec( new Runnable() {
				
				@Override
				public void run() {
					journeyViewer.setInput( ce.getJourneys());				}
			});
			
		}
	};
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WPHFrontend(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		listener = new EvaluationListener();
		this.createComposite(parent, style);
	}
	
	protected void createComposite( Composite parent, int style ){
		CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolder.addSelectionListener( new SelectionAdapter(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				changeTab( (CTabFolder) e.getSource());
				super.widgetDefaultSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				changeTab( (CTabFolder) e.getSource());
				super.widgetSelected(e);
			}	
			
			private void changeTab( CTabFolder folder ){
				CTabItem item = folder.getSelection();
				Tabs tab = (Tabs) item.getData();
				switch( tab ){
				case REST:
					restController.setBrowser( Pages.INDEX );
					break;
				default:
					break;
				}
			}
		});
		CTabItem tbtmOverview = new CTabItem(tabFolder, SWT.NONE);
		tbtmOverview.setText( Tabs.OVERVIEW.toString());
		tbtmOverview.setData( Tabs.OVERVIEW);
		
		SashForm sashForm = new SashForm(tabFolder, SWT.VERTICAL);
		tbtmOverview.setControl(sashForm);
		
		browser = new Browser(sashForm, SWT.BORDER);
		controller = new GeoCoderController(browser);
		controller.addEvaluationListener(listener);		
		
		modelViewer = new ModelTableViewer(sashForm, SWT.BORDER);
		modelViewer.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
		sashForm.setWeights(new int[] {2, 1});
		
		CTabItem tbtmJourneyItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmJourneyItem.setText( Tabs.JOURNEY.toString());
		tbtmJourneyItem.setData( Tabs.JOURNEY);
		journeyViewer = new JourneyTableViewer(tabFolder, SWT.BORDER);
		journeyViewer.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
		tbtmJourneyItem.setControl( journeyViewer);	

		CTabItem tbtmRestItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmRestItem.setText( Tabs.REST.toString());
		tbtmRestItem.setData( Tabs.REST);
		browser = new Browser(tabFolder, SWT.BORDER);
		restController = new RestController(browser);
		tbtmRestItem.setControl( browser);	

		tabFolder.setSelection(Tabs.REST.ordinal());
	}
	
	public void setEnvironment(  IContainerEnvironment ce ){
		this.ce = ce;		
	}

	public void setupFrontEnd(){
		ce.addListener( elistener);
		ce.start();
		
		TilesAndPixelsModel tpm = new TilesAndPixelsModel(controller);
		tpm.setLocation( new LatLng( 51.8926f, 4.4205f), 11);
		MarkerModel mkm = new MarkerModel( controller );
		//for( IModel model: provider.getModels() )
		//	mkm.addMarker(model.getLnglat(), model.getType().getImage());
		tpm.synchronize();
		modelViewer.setInput(ce.getModels());
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void dispose(){
		ce.removeListener( elistener);
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
