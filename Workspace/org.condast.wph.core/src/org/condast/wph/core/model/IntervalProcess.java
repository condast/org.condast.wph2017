package org.condast.wph.core.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.condast.symbiotic.core.def.ITransformer;
import org.condast.wph.core.def.IIntervalProcess;

public class IntervalProcess<I, O extends Object> implements IIntervalProcess<I, O>{

	public static final int DEFAULT_CAPACITY = 10;
	
	//The times that the job should end are depicted here
	private Map<I,Date> jobs;
	private int capacity;
	private ITransformer<I,O> transformer;
	private long time;

	public IntervalProcess( ITransformer<I,O> transformer ) {
		this(transformer, DEFAULT_CAPACITY );
	}
	
	public IntervalProcess( ITransformer<I,O> transformer, int capacity ) {
		jobs = new HashMap<I,Date>();
		this.capacity = capacity;
		this.transformer = transformer;
		this.time = 0;
	}
	
	protected ITransformer<I, O> getTransformer() {
		return transformer;
	}

	public boolean addJob( I input, Date completion ){
		if( jobs.size() >= capacity )
			return false;
		this.jobs.put(input, completion);
		return true;
	}
	
	public boolean removeJob( I input ){
		jobs.remove( input );
		return transformer.removeInput(input);
	}

	/**
	 * Get the oldest pending job, or null if all are not overdue
	 * @return
	 */
	public Date getFirstDueJob(){
		Date current = Calendar.getInstance().getTime();
		current.setTime( current.getTime() + time );
		Date first = null;
		for( Date date: jobs.values() ){
			if( date.getTime() > current.getTime() ){
				if(( first == null ) || ( first.getTime() > date.getTime() ))
					first = date;
			}
		}
		return first;
	}

	/**
	 * Get the oldest pending job, or null if all are not overdue
	 * @return
	 */
	public Date getLongestOverdueJob(){
		Date current = Calendar.getInstance().getTime();
		current.setTime( current.getTime() + time );
		Date oldest = null;
		for( Date date: jobs.values() ){
			if( date.getTime() < current.getTime() ){
				if(( oldest == null ) || ( oldest.getTime() > date.getTime() ))
					oldest = date;
			}
		}
		return oldest;
	}

	@Override
	public void next( long time ) {
		Date current = Calendar.getInstance().getTime();
		current.setTime( current.getTime() + time );
		this.time = time;
		this.transformer.clearInputs();
		Collection<I> inputs = new ArrayList<I>( jobs.keySet());
		for( I input: inputs ){
			if( jobs.get( input ).getTime() <= current.getTime() ){
				//jobs.remove( input );
				this.transformer.addInput(input);
			}
		}
		transformer.transform(inputs.iterator());
	}
}