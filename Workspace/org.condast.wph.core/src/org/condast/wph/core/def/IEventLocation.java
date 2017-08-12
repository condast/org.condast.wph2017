package org.condast.wph.core.def;

import java.util.Date;

public interface IEventLocation extends ILocation{

	public enum EventTypes{
		ACTUAL_TIME_OF_DEPARTURE,
		ESTIMATED_TIME_OF_ARRIVAL;

		@Override
		public String toString() {
			String str = super.toString();
			switch( this ){
			case ACTUAL_TIME_OF_DEPARTURE:
				str = "ATD";
				break;
			case ESTIMATED_TIME_OF_ARRIVAL:
				str = "ETA";
				break;
			default:
				break;
			}
			return str;
		}	
	}
	
	/**
	 * Get the time of the event
	 * @return
	 */
	public Date getTime();
	
	/**
	 * The event type
	 * @return
	 */
	public EventTypes getEvent();
}
