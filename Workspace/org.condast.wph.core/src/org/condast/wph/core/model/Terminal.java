package org.condast.wph.core.model;

import java.util.Collection;

import org.condast.commons.latlng.LatLng;
import org.condast.symbiotic.core.utils.TimedNode;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.design.AbstractJobShopAgent;

public class Terminal extends AbstractModel<IModel.ModelTypes> {

	public static final int DEFAULT_UNLOAD_TIME = 3;//minutes per container
	
	public enum Strategies{
		ALLOW_ENTRY,
		ALLOW_DELAY
	}
	
	private int maxDocks;
	private int unloadTime;
	private JobShop jobs;

	public Terminal( String id, LatLng lnglat, int maxDocks) {
		this( id, lnglat, maxDocks, DEFAULT_UNLOAD_TIME );
	}
	
	public Terminal( String id, LatLng lnglat, int maxDocks, int unloadTime) {
		super(id, ModelTypes.TERMINAL, lnglat);
		this.maxDocks = maxDocks;
		this.unloadTime = unloadTime;
		this.jobs = new JobShop( maxDocks);
	}

	public int getMaxDocks() {
		return maxDocks;
	}

	public int getUnloadTime() {
		return unloadTime;
	}
	
	public boolean addJob( String name, int time ){
		return this.jobs.addJob( name, time );	
	}
	
	public boolean update( int interval ){
		return this.jobs.update(interval);
	}
	
	public boolean isAvailable(){
		return this.jobs.isAvailable();
	}
	
	private class JobShop extends AbstractJobShopAgent{

		JobShop(int maxJobs) {
			super(maxJobs);
		}

		@Override
		protected void setupJobs(Collection<TimedNode> data, int maxJobs) {
			// NOTHING
		}
	}
}