package org.condast.wph.core.design;

import java.util.Collection;
import java.util.Date;

import org.condast.symbiotic.core.transformation.AbstractTransformation;

public class TimedNode extends AbstractTransformation<Date, Boolean>{

	private long time;//time needed to finish the job
	private long currentTime;
	private boolean completed;

	public TimedNode( long time ) {
		this( null, time );
	}
	
	public TimedNode( String id, long time ) {
		super( id );
		this.currentTime = 0;
		this.time = time;
		this.completed = false;
	}
	
	public boolean isCompleted(){
		return completed;
	}
			
	public long getRemainingTime(){
		return ( this.time  - this.currentTime );
	}
	
	protected boolean isCompleteWithinOffset( int offset ){
		if( completed)
			return true;
		return this.currentTime >= (this.time + offset );
	}
	
	protected boolean update( int interval){
		this.currentTime += interval;
		this.completed =  ( this.currentTime >= this.time );
		return completed;
	}

	@Override
	protected Boolean onTransform(Collection<Date> inputs) {
		return this.completed;
	}
}
