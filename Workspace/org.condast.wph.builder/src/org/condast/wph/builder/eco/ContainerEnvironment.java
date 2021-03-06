package org.condast.wph.builder.eco;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.condast.commons.autonomy.env.EnvironmentEvent;
import org.condast.commons.autonomy.env.IEnvironmentListener;
import org.condast.commons.thread.AbstractExecuteThread;
import org.condast.symbiotic.core.collection.SymbiotCollection;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.symbiotic.core.def.ISymbiot;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.wph.builder.design.Journey;
import org.condast.wph.builder.design.Passage;
import org.condast.wph.core.def.IStakeHolder;
import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.core.definition.IJourney;
import org.condast.wph.core.definition.IModel.ModelTypes;
import org.condast.wph.core.model.Statistics;

public class ContainerEnvironment extends AbstractExecuteThread implements IContainerEnvironment {

	public static final int DAY_TIME = 30*24*60*60*1000;
	public static final int 	 DAY = 24*60*60*1000;
	public static final int INTERVAL = 3*60*1000;//3 min
	
	private Environment environment;
	private Lock lock;
	private Collection<IJourney> journeys;
	private Collection<IEnvironmentListener<Object>> listeners;
	private int counter;
	private Passage shipentry;
	private int interval;
	private Date startDate;
	private SymbiotCollection symbiots;

	public ContainerEnvironment() {
		this( INTERVAL );
	}
	
	public ContainerEnvironment( int interval ) {
		this.environment = new Environment();
		journeys = new ArrayList<IJourney>();
		lock = new ReentrantLock();
		this.listeners = new ArrayList<IEnvironmentListener<Object>>();
		this.interval = interval;
	}

	@Override
	public void clear(){
		this.startDate = Calendar.getInstance().getTime();
		//this.shipentry.clear();
		this.counter = 0;
	}
	
	public int getInterval() {
		return interval;
	}

	@Override
	public Collection<ISymbiot> getSymbiots() {
		return symbiots;
	}

	@Override
	public Statistics getStatistics() {
		if( this.shipentry == null )
			return null;
		return this.shipentry.getStatistics();
	}

	@Override
	public Map<IStakeHolder<?,?>,IBehaviour> getModels(){
		if( this.shipentry == null )
			return null;
		return this.shipentry.getModels();
	}
	
	@Override
	public long getElapsedTime() {
		return (counter * interval )/60000;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}
	

	@Override
	public void addListener( IEnvironmentListener<Object> listener ){
		this.listeners.add( listener );
	}

	@Override
	public void removeListener( IEnvironmentListener<Object> listener ){
		this.listeners.remove( listener );
	}
	
	protected void notifyChangeEvent( EnvironmentEvent<Object> event ){
		for( IEnvironmentListener<Object> listener: listeners)
			listener.notifyEnvironmentChanged(event);
	}

	@Override
	public boolean onInitialise() {
		/*
		for( int i=0; i<20; i++ ){
			int tag = 50000 + (int)( 1000000 * Math.random());
			Date departure = Calendar.getInstance().getTime();
			long time = ( long)( DAY_TIME*Math.random());
			Calendar eta = Calendar.getInstance();
			eta.setTimeInMillis( eta.getTimeInMillis() + time); 
			IContainer container = new Container( String.valueOf( tag ), departure, eta.getTime());
			IJourney journey = new Journey( container, environment );
			journeys.add( journey);
		}
		*/
		this.clear();
		this.symbiots =new SymbiotCollection();
		shipentry = new Passage( environment, symbiots );
		notifyChangeEvent( new EnvironmentEvent<Object>( this ));
		return true;
	}

	@Override
	public void onExecute() {
		lock.lock();
		try{

			/*
				for( IJourney journey: journeys ){
					if( journey.isCompleted() )
						continue;
					IContainer container = journey.getContainer();
					Calendar eta = Calendar.getInstance();
					long elapsed = ( int)( INTERVAL*Math.random() );
					eta.setTimeInMillis( container.getETA().getTime() + elapsed);
					if( counter == 9 )
						journey.next(); 
				}
			 */
			shipentry.next();

			counter++;
			notifyChangeEvent( new EnvironmentEvent<Object>( this ));
		}
		finally{
			lock.unlock();
		}
		sleep(1000);
	}
	
	/**
	 * Get the simulated time
	 * @return
	 */
	@Override
	public String getSimulatedTime(){
		Date date = ( this.startDate == null)?Calendar.getInstance().getTime(): this.startDate;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss");
		String formattedDate = formatter.format( date );
		return String.valueOf( formattedDate ) + " + " + getElapsedTime() + " min";	
	}

	@Override
	public IStakeHolder<?,?> getStakeHolder( ModelTypes type ){
		if( shipentry == null )
			return null;
		for( IStakeHolder<?,?> trf: shipentry.getModels().keySet() ){
			if( trf.getName().toLowerCase().contains( type.name().toLowerCase()))
				return trf;
		}
		return null;
	}
		
	@Override
	public synchronized IJourney[] getJourneys(){
		lock.lock();
		try{
			return journeys.toArray( new Journey[ journeys.size() ]);
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	public int getTimer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTimer(int timer) {
		// TODO Auto-generated method stub
		
	}
}
