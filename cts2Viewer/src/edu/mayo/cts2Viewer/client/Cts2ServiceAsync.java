package edu.mayo.cts2Viewer.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>Cts2Service</code>.
 */
public interface Cts2ServiceAsync {
	void getValueSets(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
