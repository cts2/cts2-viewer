package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.cts2Viewer.client.PanelTypeEnum;

public class PanelChangeEvent extends GwtEvent<PanelChangeEventHandler>
{
	public static Type<PanelChangeEventHandler> TYPE = new Type<PanelChangeEventHandler>();

	private final PanelTypeEnum i_panelType;

	public PanelChangeEvent(PanelTypeEnum panelType) {
		super();

		i_panelType = panelType;
	}

	@Override
	public Type<PanelChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PanelChangeEventHandler handler) {
		handler.onPanelChanged(this);
	}

	public PanelTypeEnum getChangeType() {
		return i_panelType;
	}

}
