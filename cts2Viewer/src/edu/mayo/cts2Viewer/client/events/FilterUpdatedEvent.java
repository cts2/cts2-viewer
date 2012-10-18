package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class FilterUpdatedEvent extends GwtEvent<FilterUpdatedEventHandler> {

	public static Type<FilterUpdatedEventHandler> TYPE = new Type<FilterUpdatedEventHandler>();

	@Override
	public Type<FilterUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FilterUpdatedEventHandler handler) {
		handler.onFilterUpdate(this);
	}
}