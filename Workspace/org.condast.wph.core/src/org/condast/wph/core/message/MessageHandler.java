package org.condast.wph.core.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.condast.commons.http.AbstractHttpRequest;

public class MessageHandler {

	public static final String WPH_CONTEXT = "http://www.condast.com/wph/";

	public enum Parties{
		PORTMASTER,
		TERMINAL,
		BARGE,
		TRAIN,
		TRUCK;

		public String getPath(){
			switch( this ){
			case BARGE:
				return "brg";
			case PORTMASTER:
				return "hm";
			case TERMINAL:
				return "trm";
			case TRAIN:
				return "trn";
			case TRUCK:
				return "trk";
			}
			return null;
		}
	}

	public enum Messages{
		OK,
		DENY;
	}
	
	private ExecutorService service;
	private String path;
	private Collection<IMessageListener> listeners;
	private boolean enabled;

	private static MessageHandler handler = new MessageHandler( false );
	
	private MessageHandler( boolean enabled ) {
		super();
		this.enabled = enabled;
		this.path = WPH_CONTEXT;
		this.listeners = new ArrayList<IMessageListener>();
		service = Executors.newCachedThreadPool();
	}

	public static MessageHandler getInstance(){
		return handler;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Send a message if the handler is enabled. Returns true if the
	 * the message was sent succesfully
	 * @param party
	 * @param request
	 * @return
	 */
	public boolean sendMessage( Parties party, String request ){
		if( this.enabled )
			service.submit( new MessageCallable( this, party, path, request ));
		return this.enabled;
	}

	public void addMessageListener( IMessageListener listener ){
		this.listeners.add(listener);
	}

	public void removeMessageListener( IMessageListener listener ){
		this.listeners.remove(listener);
	}
	
	protected void notifyMessageEvent( MessageEvent event ){
		for( IMessageListener listener: listeners )
			listener.notifyMessageReceived(event);
	}
	
	public void shutdown(){
		this.service.shutdown();
	}

	private static class MessageCallable implements Callable<String>{

		private Parties party;
		private String message;
		private String path;
		private MessageHandler handler;

		public MessageCallable( MessageHandler handler, Parties party, String path, String message) {
			super();
			this.handler = handler;
			this.path = path;
			this.party = party;
			this.message = message;//not used yet
		}

		@Override
		public String call() throws Exception {
			try{
				HttpMessages msg = new HttpMessages(party, path);
				msg.sendGet();
				this.message = msg.getResponse();
				handler.notifyMessageEvent( new MessageEvent( this.handler, party, message ));
				this.message = "notusedyet";
				return msg.getResponse();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
			return null;
		}

		private class HttpMessages extends AbstractHttpRequest{

			private String response;
			
			public HttpMessages( Parties party, String path) throws MalformedURLException {
				super(path + party.getPath());
			}

			private String getResponse() {
				return response;
			}

			@Override
			protected void onHandleResponse(int responseCode, BufferedReader reader) throws IOException{
				String inputLine;
				StringBuffer buffer = new StringBuffer();

				while ((inputLine = reader.readLine()) != null) {
					buffer.append(inputLine);
				}
				response = buffer.toString();
				System.out.println(response);	
			}	
		}
	}
}
