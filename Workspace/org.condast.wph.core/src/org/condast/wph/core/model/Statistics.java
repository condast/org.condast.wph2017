package org.condast.wph.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.condast.commons.Utils;
import org.condast.wph.core.def.IContainer;

public class Statistics {

	Collection<Integer> throughput;
	
	public Statistics() {
		this.throughput = new ArrayList<Integer>();
	}
	
	public void next( long time, Carrier carrier ){
		if(( carrier == null) || ( Utils.assertNull( carrier.getContainers() )))
			return;
		Date date = IntervalProcess.getSimulatedTime(time);
		long diff = 0;
		for( IContainer container: carrier.getContainers() ){
			diff += ( date.getTime() - container.getTimeStamp().getTime());
		}
		diff /= carrier.getContainers().size() * IntervalProcess.TO_HOURS;
		throughput.add(( int)( diff));
	}

	public Collection<Integer> getThroughput() {
		return throughput;
	}
}
