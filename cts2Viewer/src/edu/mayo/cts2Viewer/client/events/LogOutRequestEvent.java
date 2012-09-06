package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.cts2Viewer.shared.Credentials;

public class LogOutRequestEvent extends GwtEvent<LogOutRequestEventHandler> {

	private final Credentials i_credential;

	public static Type<LogOutRequestEventHandler> TYPE = new Type<LogOutRequestEventHandler>();

	public LogOutRequestEvent(Credentials credential) {
		super();
		i_credential = credential;
	}

	@Override
	public Type<LogOutRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogOutRequestEventHandler handler) {
		handler.onLogOutRequest(this);
	}

	/**
	 * Get the server that we need to LogOut to.
	 * 
	 * @return
	 */
	public Credentials getCredential() {
		return i_credential;
	}
}
