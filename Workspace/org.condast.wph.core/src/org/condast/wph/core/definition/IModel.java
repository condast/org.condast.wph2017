package org.condast.wph.core.definition;

import org.condast.commons.lnglat.LngLat;
import org.condast.commons.strings.StringStyler;
import org.google.geo.mapping.ui.images.IGoogleMapsImages;

public interface IModel {

	public enum ModelTypes{
		CLIENT,
		SHIP,
		SHIPPING_AGENT,
		TERMINAL,
		PILOT,
		BOAT_MEN,
		TUG_BOAT,
		PORT_AUTHORITY,
		SHIP_OWNER,
		LOGISTICS,
		SUPPLIER;
		
		public String getImage(){
			IGoogleMapsImages.MarkerImages image = IGoogleMapsImages.MarkerImages.RED;
			switch( this ){
			case TERMINAL:
				image = IGoogleMapsImages.MarkerImages.BLUE;
				break;
			case PILOT:
				image = IGoogleMapsImages.MarkerImages.BROWN;
				break;
			case TUG_BOAT:
				image = IGoogleMapsImages.MarkerImages.YELLOW;
				break;
			default:
				break;
			}
			char id = this.name().charAt(0);
			return image.getImage(id);
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}	
	}

	String getId();

	LngLat getLnglat();

	ModelTypes getType();
}