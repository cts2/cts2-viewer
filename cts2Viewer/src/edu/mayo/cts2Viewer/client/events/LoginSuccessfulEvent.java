package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.cts2Viewer.shared.Credentials;

public class LoginSuccessfulEvent extends GwtEvent<LoginSuccessfulEventHandler> {

	public static Type<LoginSuccessfulEventHandler> TYPE = new Type<LoginSuccessfulEventHandler>();

	private final Credentials i_credentials;

	public LoginSuccessfulEvent(Credentials credentials) {
		super();
		i_credentials = credentials;
	}

	@Override
	public Type<LoginSuccessfulEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginSuccessfulEventHandler handler) {
		handler.onLoginSuccessful(this);
	}

	public Credentials getCredentials() {
		return i_credentials;
	}
}
