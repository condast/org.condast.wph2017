package org.condast.wph.core.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.condast.commons.Utils;
import org.condast.symbiotic.core.transformer.AbstractTransformer;
import org.condast.wph.core.def.IIntervalProcess;

public class IntervalProcess<I extends Object> extends AbstractTransformer<I,I> implements IIntervalProcess<I, I>{

	public static final int DEFAULT_CAPACITY = 10;

	public static final int TO_HOURS =  60*60*1000;

	//The times that the job should end are depicted here
	private Map<Date, I> jobs;
	private int capacity;
	private long time;
	private String name;

	public IntervalProcess( String name ) {
		this( name, DEFAULT_CAPACITY );
	}
	
	public IntervalProcess( String name, int capacity ) {
		jobs = new LinkedHashMap<Date,I>();
		this.capacity = capacity;
		this.name = name;
		this.time = 0;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getJobSize(){
		return super.getInputSize() + this.jobs.size();
	}
	
	public boolean addInput( I input, Date completion ){
		if( jobs.size() >= capacity )
			return false;
		this.jobs.put(completion, input);
		return true;
	}
	
	public boolean removeInput( I input ){
		jobs.remove( input );
		return super.removeInput(input);
	}

	/**
	 * Get the oldest pending job, or null if all are not overdue
	 * @return
	 */
	public Date getFirstDueDate( ){
		if( Utils.assertNull( this.jobs ))
			return null;
		Date current = getSimulatedTime(time);
		Date first = jobs.keySet().iterator().next();
		if( first.getTime() > current.getTime() )
			return null;
		return first;
	}

	/**
	 * Get the oldest pending job, or null if all are not overdue
	 * @return
	 */
	public I getFirstDueJob(){
		if( Utils.assertNull( this.jobs ))
			return null;
		Date current = getSimulatedTime(time);
		Map.Entry<Date, I> first = jobs.entrySet().iterator().next();
		if( first.getKey().getTime() > current.getTime() )
			return null;
		return this.jobs.entrySet().iterator().next().getValue();
	}

	@Override
	public void next( long time ) {
		Date current = Calendar.getInstance().getTime();
		current.setTime( current.getTime() + time );
		this.time = time;
		clearInputs();
		for( Date date: jobs.keySet() ){
			if( date.getTime() <= current.getTime() ){
				super.addInput(jobs.get(date));
			}
		}
		transform(super.getInputs().iterator() );
	}

	@Override
	public I transform(Iterator<I> inputs) {
		if( this.jobs.size() == 0)
			return null;
		return getFirstDueJob();
	}
	
	public static Date getSimulatedTime( long interval){
		Date current = Calendar.getInstance().getTime();
		current.setTime( current.getTime() + interval );
		return current;
	}

}