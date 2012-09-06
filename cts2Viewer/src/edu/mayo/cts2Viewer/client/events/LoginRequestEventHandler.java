package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LoginRequestEventHandler extends EventHandler {
	void onLoginRequest(LoginRequestEvent loginRequestEvent);
}
