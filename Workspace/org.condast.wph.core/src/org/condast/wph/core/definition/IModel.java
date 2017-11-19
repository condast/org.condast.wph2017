package org.condast.wph.core.definition;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.strings.StringStyler;
import org.google.geo.mapping.ui.images.IGoogleMapsImages;

public interface IModel<E extends Enum<E>> {

	public enum ModelTypes{
		CLIENT,
		SHIP,
		SHIPPING_AGENT,
		ANCHORAGE,
		TERMINAL,
		PILOT,
		BOAT_MEN,
		TUG_BOAT,
		PORT_AUTHORITY,
		SHIP_OWNER,
		LOGISTICS,
		SUPPLIER, 
		TRUCK, 
		BARGE, 
		TRAIN;
		
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

		public static String getAbbreviation( ModelTypes type ){
			String str = null;;
			switch( type ){
			case CLIENT:
				str = "clt";
				break;
			case SHIP:
				str = "shp";
				break;
			case SHIPPING_AGENT:
				str = "sha";
				break;
			case ANCHORAGE:
				str = "ptm";
				break;
			case TERMINAL:
				str = "trm";
				break;
			case PILOT:
				str = "plt";
				break;
			case BOAT_MEN:
				str = "btm";
				break;
			case TUG_BOAT:
				str = "tgb";
				break;
			case PORT_AUTHORITY:
				str = "pta";
				break;
			case SHIP_OWNER:
				str = "sho";
				break;
			case LOGISTICS:
				str = "lgt";
				break;
			case SUPPLIER:
				str = "spl";
				break;
			case TRUCK:
				str = "trk";
				break;
			case BARGE:
				str = "brg";
				break;
			case TRAIN:
				str = "trn";
				break;
			default:
				break;
			}
			return str;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}	
	}

	String getId();

	LatLng getLnglat();

	E getType();
}
