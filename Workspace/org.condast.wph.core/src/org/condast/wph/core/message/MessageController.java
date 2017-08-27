package org.condast.wph.core.message;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.strings.StringStyler;
import org.condast.symbiotic.core.def.IBehaviour;
import org.condast.wph.core.message.MessageHandler.Messages;
import org.condast.wph.core.message.MessageHandler.Parties;

public class MessageController {

	private MessageHandler handler = MessageHandler.getInstance();

	private Collection<Parties> pending;
	private IBehaviour behaviour;
	
	private int countdown;//in minutes
	
	public MessageController( IBehaviour behaviour ) {
		this.behaviour = behaviour;
		pending = new ArrayList<Parties>();
	}

	/**
	 * This time counts down to the first event, for instance a
	 * ship that has unloaded containers
	 * @param countdown
	 */
	public void next( int countdown ){
		this.countdown = countdown;
	}
	
	public MessageHandler getHandler() {
		return handler;
	}

	public synchronized boolean sendMessage( Parties party, String message ){
		if( pending.contains( party ))
			return false;
		this.pending.add( party );
		handler.sendMessage( party, message );	
		return true;
	}

	public synchronized boolean sendMessage( String party, String message ){
		return sendMessage( StringStyler.styleToEnum(party), message);
	}
	
	public boolean handleResponse( Parties party, Messages response){
		if(!pending.contains( party))
			return false;
		int value = behaviour.calculate( Messages.DENY.equals( response));
		return (this.countdown <= value);
	}

	public String handleResponse( String partystr, String response){
		Parties party = Parties.valueOf( StringStyler.styleToEnum( partystr ));
		Messages msg = Messages.valueOf( StringStyler.styleToEnum( response ));
		return handleResponse(party, msg)?"OK":"DENY";
	}
}
