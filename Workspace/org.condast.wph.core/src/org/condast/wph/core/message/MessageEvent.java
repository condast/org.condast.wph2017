package org.condast.wph.core.message;

import java.util.EventObject;

import org.condast.wph.core.message.MessageHandler.Parties;

public class MessageEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private Parties party;
	private String result;

	public MessageEvent(Object arg0, Parties party, String result ) {
		super(arg0);
		this.party = party;
		this.result = result;
	}

	public Parties getParty() {
		return party;
	}

	public String getResult() {
		return result;
	}
}
