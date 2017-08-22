package org.condast.wph.core.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.condast.symbiotic.core.transformation.AbstractTransformer;
import org.condast.wph.core.def.IIntervalProcess;

public class IntervalProcess<I, O extends Object> extends AbstractTransformer<I,O> implements IIntervalProcess<I, O>{

	public static final int DEFAULT_CAPACITY = 10;
	
	//The times that the job should end are depicted here
	private Map<I,Date> jobs;
	private int capacity;
	private long time;

	public IntervalProcess() {
		this(DEFAULT_CAPACITY );
	}
	
	public IntervalProcess( int capacity ) {
		jobs = new HashMap<I,Date>();
		this.capacity = capacity;
		this.time = 0;
	}
	
	public boolean addInput( I input, Date completion ){
		if( jobs.size() >= capacity )
			return false;
		this.jobs.put(input, completion);
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
		clearInputs();
		Collection<I> inputs = new ArrayList<I>( jobs.keySet());
		for( I input: inputs ){
			if( jobs.get( input ).getTime() <= current.getTime() ){
				//jobs.remove( input );
				super.addInput(input);
			}
		}
		transform(inputs.iterator());
	}

	@Override
	public O transform(Iterator<I> inputs) {
		// TODO Auto-generated method stub
		return null;
	}
}