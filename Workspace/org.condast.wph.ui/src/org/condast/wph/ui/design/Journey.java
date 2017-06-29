package org.condast.wph.ui.design;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.condast.symbiotic.core.AbstractNeighbourhood;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.symbiot.Symbiot;

public class Journey {

	private Map<IModel.ModelTypes, Symbiot> stakeholders;
	private Date departure;
	private Date eta;
	private Environment environment;
	private ISymbiot transport;

	
	private static ModelProvider provider = ModelProvider.getInstance();

	public Journey( Date departure, Date eta ) {
		this.departure = departure;
		this.eta = eta;
		stakeholders = new TreeMap<IModel.ModelTypes, Symbiot>();
		environment = new Environment();
		createDependencies();
	}
	
	private void createDependencies(){
		int index = 0;
		ISymbiot client = create( IModel.ModelTypes.CLIENT );
		ISymbiot supplier = create( IModel.ModelTypes.SUPPLIER );
		environment.addNeighbourhood(client, supplier, new Neighbourhood(index++));
		ISymbiot shipagent = create( IModel.ModelTypes.SHIPPING_AGENT );
		index = createJourney(shipagent, environment, index, false);
		index = createJourney(shipagent, environment, index, false);
		environment.addNeighbourhood(transport, client, new Neighbourhood( index ));
	}

	private int createJourney( ISymbiot shipagent, Environment environment, int index, boolean destination){
		transport = create( IModel.ModelTypes.LOGISTICS );
		environment.addNeighbourhood(shipagent, transport, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot terminal = create( IModel.ModelTypes.TERMINAL );
		environment.addNeighbourhood(transport, terminal, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot shipowner = create( IModel.ModelTypes.SHIP_OWNER );
		environment.addNeighbourhood(terminal, shipowner, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot port = create( IModel.ModelTypes.PORT_AUTHORITY );
		environment.addNeighbourhood(shipowner, port, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot tug = create( IModel.ModelTypes.TUG_BOAT );
		environment.addNeighbourhood(port, tug, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot pilot = create( IModel.ModelTypes.PILOT );
		environment.addNeighbourhood(port, pilot, new Neighbourhood( index ));
		return getIndex( destination, index);
	}
	
	private int getIndex( boolean direction, int current ){
		return direction? current++: current--;
	}

	protected Date getDeparture() {
		return departure;
	}
	
	private static ISymbiot create( IModel.ModelTypes type ){
		IModel model = provider.getModel( IModel.ModelTypes.CLIENT );
		return new Symbiot( model, 4 );
	}

	protected Date getEta() {
		return eta;
	}

	/**
	 * In this simulation all interactions are based on time
	 * @author Kees
	 *
	 */
	private class Neighbourhood extends AbstractNeighbourhood<Integer, Integer>{

		private Date time;
		
		private Neighbourhood( int index) {
			super( String.valueOf( index ));
		}

		@Override
		protected Integer onTransform(Collection<Integer> inputs) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
