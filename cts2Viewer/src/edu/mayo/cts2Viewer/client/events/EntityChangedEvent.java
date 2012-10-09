package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class EntityChangedEvent extends GwtEvent<EntityChangedEventHandler> {

	public static Type<EntityChangedEventHandler> TYPE = new Type<EntityChangedEventHandler>();

	public static final int PREVIOUS = 0;
	public static final int NEXT = 1;

	private final int i_changeType;

	public EntityChangedEvent(int changeType) {
		super();

		i_changeType = changeType;
	}

	@Override
	public Type<EntityChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EntityChangedEventHandler handler) {
		handler.onEntityChanged(this);
	}

	public int getChangeType() {
		return i_changeType;
	}

}
