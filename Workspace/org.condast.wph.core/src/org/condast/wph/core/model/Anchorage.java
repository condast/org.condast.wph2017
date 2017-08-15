package org.condast.wph.core.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.def.IShip;
import org.condast.wph.core.definition.IModel;

public class Anchorage extends AbstractModel<IModel.ModelTypes> {

	public static final int MAX_WAITING_TIME = 12;//hours
	public static final float TO_HOURS = 3600000;//scale from msec to hours

	private TreeMap<Date, IShip > ships;
	private int maxWaitingTime;//hours

	public Anchorage( String id, LatLng lnglat) {
		this( id, lnglat, MAX_WAITING_TIME );
	}
	
	/**
	 * 
	 * @param id
	 * @param lnglat
	 * @param maxWaitingTime (hors)
	 */
	public Anchorage( String id, LatLng lnglat, int maxWaitingTime) {
		super(id, ModelTypes.TERMINAL, lnglat);
		this.maxWaitingTime = maxWaitingTime;
		this.ships = new TreeMap<Date, IShip>();
	}

	public int getMaxWaitingTime() {
		return maxWaitingTime;
	}

	public int getNrOfShips() {
		return ships.size();
	}
	
	public boolean isEmpty(){
		return this.ships.isEmpty();
	}
	
	public void addShip( IShip ship ){
		this.ships.put( Calendar.getInstance().getTime(), ship );	
	}

	public void removeShip( IShip ship ){
		this.ships.remove( Calendar.getInstance().getTime(), ship );	
	}

	public IShip getFirst(){
		return this.ships.firstEntry().getValue();
	}
	
	/**
	 * Get the longest waiting time in hours
	 * @return
	 */
	public long getLongestWaitingTime(){
		Calendar calendar = Calendar.getInstance();
		long diff = calendar.getTimeInMillis() - this.ships.firstEntry().getKey().getTime();
		return (long) ((float)diff/TO_HOURS);
	}
}