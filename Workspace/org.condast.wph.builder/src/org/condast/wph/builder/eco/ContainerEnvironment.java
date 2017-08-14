package org.condast.wph.builder.eco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.condast.commons.thread.AbstractExecuteThread;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.core.environment.EnvironmentEvent;
import org.condast.symbiotic.core.environment.IEnvironmentListener;
import org.condast.symbiotic.def.ITransformation;
import org.condast.wph.builder.design.Journey;
import org.condast.wph.builder.design.ModelProvider;
import org.condast.wph.builder.design.ShipEntry;
import org.condast.wph.core.definition.IContainerEnvironment;
import org.condast.wph.core.definition.IJourney;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.definition.IModel.ModelTypes;

public class ContainerEnvironment extends AbstractExecuteThread implements IContainerEnvironment {

	public static final int DAY_TIME = 30*24*60*60*1000;
	public static final int INTERVAL = 24*60*60*1000;
	
	private Environment environment;
	private Lock lock;
	private Collection<IJourney> journeys;
	private Collection<IEnvironmentListener> listeners;
	private int counter;
	private ShipEntry shipentry;
	
	public ContainerEnvironment() {
		this.environment = new Environment();
		journeys = new ArrayList<IJourney>();
		lock = new ReentrantLock();
		this.listeners = new ArrayList<IEnvironmentListener>();
	}

	@Override
	public void addListener( IEnvironmentListener listener ){
		this.listeners.add( listener );
	}

	@Override
	public void removeListener( IEnvironmentListener listener ){
		this.listeners.remove( listener );
	}
	
	protected void notifyChangeEvent( EnvironmentEvent event ){
		for( IEnvironmentListener listener: listeners)
			listener.notifyEnvironmentChanged(event);
	}

	@Override
	public void onInitialise() {
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
		shipentry = new ShipEntry( environment );
		counter = 0;
		notifyChangeEvent( new EnvironmentEvent( this ));
	}

	@Override
	public void onExecute() {
		while( super.isRunning() ){
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
					
				counter = ( counter + 1)%10;
			}
			finally{
				lock.unlock();
			}
			try{
				Thread.sleep(1000);
			}
			catch( InterruptedException ex ){
				ex.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ITransformation<?,Boolean> getTransformation( ModelTypes type ){
		if( shipentry == null )
			return null;
		return (ITransformation<?, Boolean>) shipentry.getTransformation(type);
	}
	
	@Override
	public IModel<IModel.ModelTypes>[] getModels(){
		ModelProvider provider = ModelProvider.getInstance();
		return provider.getModels();
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
}
