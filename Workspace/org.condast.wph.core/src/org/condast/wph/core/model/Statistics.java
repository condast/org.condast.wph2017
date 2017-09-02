package org.condast.wph.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.condast.commons.Utils;
import org.condast.wph.core.def.IContainer;

public class Statistics {

	public static final float MAX_TUE_WEIGHT = 21.600f;//kg
	public static final int YEAR_TO_MSEC = 365*24*60*12;//scaled to annual containers
	public static final int AVG_COST_SHIP_SAVING = 10*15000;//100 ships a day times 15000
	
	/**
	 * Derived from the Data set from Geodan (in 2012)
	 * @author Kees
	 *
	 */
	public enum Tonnage{
		BARGES,
		ROAD,
		RAIL;
		
		public float getTonnage(){
			float tonnage = 0;
			switch(this){
			case BARGES:
				tonnage = 1680.4f;
				break;
			case ROAD:
				tonnage = 1272.2f;
				break;
			case RAIL:
				tonnage = 840.2f;
				break;
			}
			return tonnage;
		}
		
		public static int scaleToTonnage( int index, int size, int simulated ){
			double total = BARGES.getTonnage() + ROAD.getTonnage() + RAIL.getTonnage();
			double result = 2500f - simulated * ( 30 * index/( index + size));
			return (int)( result * total * 1000/MAX_TUE_WEIGHT);
		}	
	}

	LinkedList<Integer> throughput;
	
	public Statistics() {
		this.throughput = new LinkedList<Integer>();
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
		long previous = throughput.isEmpty()? 0: throughput.getLast();
		if( diff != previous )
			throughput.add(( int)( diff));
	}

	protected Integer[] getThroughput() {
		return throughput.toArray( new Integer[ throughput.size()]);
	}

	public Integer[] getScaledThroughput( long interval ) {
		Collection<Integer> scaled = new ArrayList<Integer>();
		for( int i=0; i< this.throughput.size(); i++ ){
			double tp = Tonnage.scaleToTonnage(i, this.throughput.size(), this.throughput.get(i));
			int scaleIntterval = (int)( tp*interval/YEAR_TO_MSEC);
			scaled.add( scaleIntterval );
		}
		return scaled.toArray( new Integer[ scaled.size()]);
	}
	
	public float getCostSavingShips( long interval ){
		if( Utils.assertNull(throughput) || throughput.size() == 1)
			return 0;		
		Integer[] scaled = getScaledThroughput(interval);
		float value = scaled[ scaled.length - 1];
		float max = scaled[ 0];
		float result = (1 - value/max);
		return AVG_COST_SHIP_SAVING * result;
	}

}
