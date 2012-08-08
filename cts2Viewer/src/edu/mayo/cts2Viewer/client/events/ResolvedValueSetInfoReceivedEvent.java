package edu.mayo.cts2Viewer.client.events;

import com.google.gwt.event.shared.GwtEvent;

import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;

public class ResolvedValueSetInfoReceivedEvent extends GwtEvent<ResolvedValueSetInfoReceivedEventHandler> {

	public static Type<ResolvedValueSetInfoReceivedEventHandler> TYPE = new Type<ResolvedValueSetInfoReceivedEventHandler>();

	private ResolvedValueSetInfo i_resolvedValueSetInfo;

	public ResolvedValueSetInfoReceivedEvent(ResolvedValueSetInfo resolvedValueSetInfo) {
		super();
		i_resolvedValueSetInfo = resolvedValueSetInfo;
	}

	@Override
	public Type<ResolvedValueSetInfoReceivedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResolvedValueSetInfoReceivedEventHandler handler) {
		handler.onResolvedValueSetInfoReceived(this);
	}

	public ResolvedValueSetInfo getResolvedValueSetInfo() {
		return i_resolvedValueSetInfo;
	}

	public void ResolvedValueSetInfo(ResolvedValueSetInfo resolvedValueSetInfo) {
		i_resolvedValueSetInfo = resolvedValueSetInfo;
	}
}