package org.condast.wph.ui.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.condast.symbiotic.core.AbstractNeighbourhood;
import org.condast.symbiotic.core.environment.Environment;
import org.condast.symbiotic.def.ISymbiot;
import org.condast.wph.core.definition.IModel;
import org.condast.wph.core.symbiot.Symbiot;

public class Journey {
	
	private Environment environment;
	private ISymbiot<IModel> transport;
	private Container container;
	private List<ISymbiot<IModel>> chain;
	private int index;
	
	private static ModelProvider provider = ModelProvider.getInstance();

	public Journey( Container container, Environment environment) {
		this.container = container;
		this.environment = environment;
		chain = new ArrayList<ISymbiot<IModel>>();
		this.index = 0;
		createDependencies();
	}
	
	private void createDependencies(){
		int index = 0;
		ISymbiot<IModel> client = create( IModel.ModelTypes.CLIENT );
		chain.add( client );
		ISymbiot<IModel> supplier = create( IModel.ModelTypes.SUPPLIER );
		container.setLnglat( supplier.getModel().getLnglat());
		chain.add( supplier );
		environment.addNeighbourhood(client, supplier, new Neighbourhood(index++));
		ISymbiot<IModel> shipagent = create( IModel.ModelTypes.SHIPPING_AGENT );
		chain.add( shipagent );
		index = createJourney(shipagent, environment, index, false);
		index = createJourney(shipagent, environment, index, false);
		environment.addNeighbourhood(transport, client, new Neighbourhood( index ));
	}

	private int createJourney( ISymbiot<IModel> shipagent, Environment environment, int index, boolean destination){
		transport = create( IModel.ModelTypes.LOGISTICS );
		chain.add( transport );
		environment.addNeighbourhood(shipagent, transport, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<IModel> terminal = create( IModel.ModelTypes.TERMINAL );
		chain.add( terminal );
		environment.addNeighbourhood(transport, terminal, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<IModel> shipowner = create( IModel.ModelTypes.SHIP_OWNER );
		chain.add( shipowner );
		environment.addNeighbourhood(terminal, shipowner, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<IModel> port = create( IModel.ModelTypes.PORT_AUTHORITY );
		chain.add( port );
		environment.addNeighbourhood(shipowner, port, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<IModel> tug = create( IModel.ModelTypes.TUG_BOAT );
		chain.add( tug );
		environment.addNeighbourhood(port, tug, new Neighbourhood( index ));
		index = getIndex( destination, index);
		ISymbiot<IModel> pilot = create( IModel.ModelTypes.PILOT );
		chain.add( pilot );
		environment.addNeighbourhood(port, pilot, new Neighbourhood( index ));
		return getIndex( destination, index);
	}
	
	public Container getContainer() {
		return container;
	}
	
	public ISymbiot<IModel> next(){
		if( index < chain.size()-1 )
			this.index += 1;
		ISymbiot<IModel> symbiot = chain.get(index);
		container.setLnglat( symbiot.getModel().getLnglat() );
		return symbiot;
	}
	
	public boolean isCompleted(){
		return this.index >= chain.size();
	}
	
	private int getIndex( boolean direction, int current ){
		return direction? current++: current--;
	}

	private static ISymbiot<IModel> create( IModel.ModelTypes type ){
		IModel model = provider.getModel( IModel.ModelTypes.CLIENT );
		return new Symbiot( model, 4 );
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
