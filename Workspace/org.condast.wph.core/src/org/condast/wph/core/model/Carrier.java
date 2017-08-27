package org.condast.wph.core.model;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.latlng.LatLng;
import org.condast.wph.core.def.IContainer;
import org.condast.wph.core.def.IEventLocation;

public class Carrier extends AbstractCarrier {

	private Collection<IContainer> containers;
	
	private String type;
	
	public Carrier( String name, String type ) {
		super( name );
		this.type = type;
		this.containers = new ArrayList<IContainer>();
	}

	public String getType() {
		return type;
	}

	public void addContainer( IContainer container ){
		this.containers.add( container );
	}

	public void removeContainer( IContainer container ){
		this.containers.remove( container );
	}

	public Collection<IContainer> getContainers() {
		return containers;
	}

	@Override
	public int getNrOfContainers() {
		return containers.size();
	}

	@Override
	public IEventLocation getATD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEventLocation getETA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LatLng getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
}
