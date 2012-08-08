package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.cts2Viewer.shared.ValueSetInfo;

public class ValueSetInfoReceivedEvent extends GwtEvent<ValueSetInfoReceivedEventHandler> {

	public static Type<ValueSetInfoReceivedEventHandler> TYPE = new Type<ValueSetInfoReceivedEventHandler>();

	private ValueSetInfo i_valueSetInfo;

	public ValueSetInfoReceivedEvent(ValueSetInfo vsi) {
		super();

		i_valueSetInfo = vsi;
	}

	@Override
	public Type<ValueSetInfoReceivedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ValueSetInfoReceivedEventHandler handler) {
		handler.onValueSetInfoReceived(this);
	}

	public ValueSetInfo getValueSetInfo() {
		return i_valueSetInfo;
	}

	public void setValueSetInfo(ValueSetInfo valueSetInfo) {
		i_valueSetInfo = valueSetInfo;
	}
}
