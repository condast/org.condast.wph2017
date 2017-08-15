package org.condast.wph.core.design;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.symbiotic.core.utils.TimedNode;

public abstract class AbstractJobShopAgent {

	private int maxJobs;
	private Collection<TimedNode> data;
	
	protected AbstractJobShopAgent( int maxJobs ) {
		data = new ArrayList<TimedNode>();
		this.maxJobs = maxJobs;
		this.setupJobs(data, maxJobs);
	}
	
	protected abstract void setupJobs( Collection<TimedNode> data, int maxJobs);
	
	public boolean addJob( String name, long time ){
		if( !isAvailable() )
			return false;
		this.data.add( new TimedNode( time));
		return true;
	}
	
	public boolean isAvailable(){
		return ( this.data.size() < this.maxJobs );
	}
	
	public long busy(){
		long shortest = Integer.MAX_VALUE;
		for( TimedNode jb: data ){
			if( !jb.isCompleted())
				continue;
			if( jb.getRemainingTime() < shortest)
				shortest = jb.getRemainingTime();
		}
		return shortest;
	}
	
	/**
	 * Update the jobs. Returns true if one or more have completed
	 * @param interval
	 * @return
	 */
	public boolean update( int interval ){
		Collection<TimedNode> temp = new ArrayList<TimedNode>( data );
		boolean retval = false;
		for( TimedNode jb: temp ){
			if( !jb.update(interval))
				continue;
			data.remove(jb);
			retval = true;
		}
		return retval;
	}
}
