package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LoginSuccessfulEventHandler extends EventHandler {
	void onLoginSuccessful(LoginSuccessfulEvent loginSuccessfulEvent);
}