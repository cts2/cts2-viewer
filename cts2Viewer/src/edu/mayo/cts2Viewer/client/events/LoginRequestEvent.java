package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LoginRequestEvent extends GwtEvent<LoginRequestEventHandler> {

	private final String i_server;

	public static Type<LoginRequestEventHandler> TYPE = new Type<LoginRequestEventHandler>();

	public LoginRequestEvent(String server) {
		super();
		i_server = server;
	}

	@Override
	public Type<LoginRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginRequestEventHandler handler) {
		handler.onLoginRequest(this);
	}

	/**
	 * Get the server that we need to login to.
	 * 
	 * @return
	 */
	public String getServer() {
		return i_server;
	}
}
