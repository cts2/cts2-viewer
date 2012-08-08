package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class ValueSetsReceivedEvent extends GwtEvent<ValueSetsReceivedEventHandler> {

	public static Type<ValueSetsReceivedEventHandler> TYPE = new Type<ValueSetsReceivedEventHandler>();

	@Override
	public Type<ValueSetsReceivedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ValueSetsReceivedEventHandler handler) {
		handler.onValueSetsReceived(this);
	}
}
