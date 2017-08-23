package org.condast.wph.ui.swt;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.google.geo.mapping.ui.controller.GeoCoderController;
import org.google.geo.mapping.ui.model.MarkerModel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.util.EnumSet;

import org.condast.commons.strings.StringStyler;
import org.condast.commons.ui.player.PlayerImages;
import org.condast.commons.ui.player.PlayerImages.Images;
import org.condast.commons.ui.session.ISessionListener;
import org.condast.commons.ui.session.RefreshSession;
import org.condast.commons.ui.session.SessionEvent;
import org.condast.commons.ui.widgets.AbstractButtonBar;
import org.condast.js.commons.eval.EvaluationEvent;
import org.condast.js.commons.eval.IEvaluationListener;
import org.condast.symbiotic.core.environment.EnvironmentEvent;
import org.condast.symbiotic.core.environment.IEnvironmentListener;
import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.ui.rest.RestController;
import org.condast.wph.ui.rest.RestController.Pages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.CTabItem;

public class WPHFrontend extends Composite {
	private static final long serialVersionUID = 1L;

	private IEvaluationListener<Object[]> listener;
	private ModelTableViewer modelViewer;
	private JourneyTableViewer journeyViewer;
	private Browser browser;
	private GeoCoderController controller; 
	private RestController restController; 
	private SymbiotGroup sg;
	private PlayerComposite<IContainerEnvironment> buttonbar;
	private Label timeLabel;
		
	private IContainerEnvironment ce;
	
	private enum Tabs{
		OVERVIEW,
		JOURNEY,
		REST,
		SYMBIOT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	private IEnvironmentListener elistener = new IEnvironmentListener() {
		
		@Override
		public void notifyEnvironmentChanged(EnvironmentEvent event) {
			session.refresh();
		}
	};
	
	private RefreshSession<IContainerEnvironment> session;
	private ISessionListener<IContainerEnvironment> sessionListener = new ISessionListener<IContainerEnvironment>(){

		@Override
		public void notifySessionChanged(SessionEvent<IContainerEnvironment> event) {
			journeyViewer.setInput( ce.getJourneys());
			setTime();	
			layout();
		}
	};

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WPHFrontend(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false ));
		listener = new EvaluationListener();
		this.createComposite(parent, style);
		session = new RefreshSession<IContainerEnvironment>();
		session.init(getDisplay());
		session.addSessionListener(sessionListener);
	}
	
	protected void createComposite( Composite parent, int style ){
		CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ));
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
				case SYMBIOT:
					sg.redraw();
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

		CTabItem tbtmSymbiotItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmSymbiotItem.setText( Tabs.SYMBIOT.toString());
		tbtmSymbiotItem.setData( Tabs.SYMBIOT);
		sg = new SymbiotGroup(tabFolder, SWT.BORDER);
		sg.setText( Tabs.SYMBIOT.toString());
		sg.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
		tbtmSymbiotItem.setControl( sg);	

		tabFolder.setSelection(Tabs.SYMBIOT.ordinal());
		
		Composite statusBar = new Composite( this, SWT.BORDER );
		statusBar.setLayout( new GridLayout( 3, false ));
		GridData gd_status = new GridData( SWT.FILL, SWT.FILL, true, false );
		gd_status.widthHint = 100;
		statusBar.setLayoutData( gd_status);
		buttonbar = new PlayerComposite<IContainerEnvironment>( statusBar, SWT.BORDER );
		buttonbar.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, true));
		
		timeLabel = new Label( statusBar, SWT.BORDER );
		timeLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
	}
	
	public void setEnvironment(  IContainerEnvironment ce ){
		this.ce = ce;		
	}

	public void setupFrontEnd(){
		ce.addListener( elistener);
		buttonbar.setInput( ce );
		
		modelViewer.setInput(ce.getModels());
		setTime();
		session.start();
	}
	
	public IContainerEnvironment getInput(){
		return this.ce;
	}
	
	protected void setTime(){
		timeLabel.setText( ce.getSimulatedTime());	
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void refresh(){
		if( getDisplay().isDisposed() )
			return;
		getDisplay().asyncExec( new Runnable(){

			@Override
			public void run() {
				layout();
			}
		});
	}
	@Override
	public void dispose(){
		ce.removeListener( elistener);
		session.stop();
		session.removeSessionListener(sessionListener);
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
			
			default:
				break;
			}
		}	
	}
	
	private class PlayerComposite<I extends Object> extends AbstractButtonBar<PlayerImages.Images, I> {
		private static final long serialVersionUID = 1L;

		public PlayerComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		protected EnumSet<PlayerImages.Images> setupButtonBar() {
			return EnumSet.of(PlayerImages.Images.START, 
					PlayerImages.Images.STOP, 
					PlayerImages.Images.NEXT,
					PlayerImages.Images.RESET);
		}

		@Override
		protected Control createButton(PlayerImages.Images type) {
			Button button = new Button( this, SWT.FLAT );
			switch( type ){
			case STOP:
				button.setEnabled(false);
				break;
			default:
				break;
			}
			button.setData(type);
			button.addSelectionListener( new SelectionAdapter() {
				private static final long serialVersionUID = 1L;

				@Override
				public void widgetSelected(SelectionEvent e) {
					Button button = (Button) e.getSource();
					PlayerImages.Images image = (Images) button.getData();
					switch( image ){
					case START:
						ce.start();
						sg.setInput(ce.getSymbiots());
						getButton( PlayerImages.Images.STOP).setEnabled(true);
						button.setEnabled(false);
						break;
					case STOP:
						getButton( PlayerImages.Images.START).setEnabled(true);
						button.setEnabled(false);
						ce.stop();
						break;
					case NEXT:
						ce.step();
						break;
					case RESET:
						ce.clear();
						setTime();
					default:
						break;
					}
					
				}		
			});
			button.setImage( PlayerImages.getInstance().getImage(type));
			return button;
		}
	}
}
