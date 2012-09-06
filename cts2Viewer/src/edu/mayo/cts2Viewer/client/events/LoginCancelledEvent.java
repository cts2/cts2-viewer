package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LoginCancelledEvent extends GwtEvent<LoginCancelledEventHandler> {

	public static Type<LoginCancelledEventHandler> TYPE = new Type<LoginCancelledEventHandler>();

	public LoginCancelledEvent() {
		super();
	}

	@Override
	public Type<LoginCancelledEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginCancelledEventHandler handler) {
		handler.onCancelRequest(this);
	}

}
