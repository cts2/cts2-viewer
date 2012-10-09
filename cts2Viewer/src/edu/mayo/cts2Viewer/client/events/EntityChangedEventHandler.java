package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface EntityChangedEventHandler extends EventHandler {

	void onEntityChanged(EntityChangedEvent event);
}