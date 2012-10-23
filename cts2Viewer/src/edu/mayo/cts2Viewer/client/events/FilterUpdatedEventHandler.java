package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface FilterUpdatedEventHandler extends EventHandler {
	void onFilterUpdate(FilterUpdatedEvent filterUpdatedEvent);
}