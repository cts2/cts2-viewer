package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LoginCancelledEventHandler extends EventHandler {
	void onCancelRequest(LoginCancelledEvent loginCancelledEvent);
}
