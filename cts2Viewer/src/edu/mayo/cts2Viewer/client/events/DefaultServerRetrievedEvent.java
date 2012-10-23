package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class DefaultServerRetrievedEvent extends GwtEvent<DefaultServerRetrievedEventHandler> {

	private final String i_server;

	public static Type<DefaultServerRetrievedEventHandler> TYPE = new Type<DefaultServerRetrievedEventHandler>();

	public DefaultServerRetrievedEvent(String server) {
		super();
		i_server = server;
	}

	@Override
	public Type<DefaultServerRetrievedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DefaultServerRetrievedEventHandler handler) {
		handler.onServerRetrieved(this);
	}

	/**
	 * Get the default server.
	 * 
	 * @return
	 */
	public String getServer() {
		return i_server;
	}
}
