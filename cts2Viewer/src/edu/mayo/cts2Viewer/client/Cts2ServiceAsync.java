package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * The async counterpart of <code>Cts2Service</code>.
 */
public interface Cts2ServiceAsync {
	void getServerOptions(AsyncCallback<LinkedHashMap<String, String>> callback) throws IllegalArgumentException;

	void getValueSets(String serverUrl, String searchText, AsyncCallback<String> callback)
	        throws IllegalArgumentException;

	void getValueSetInfo(String url, AsyncCallback<ValueSetInfo> callback) throws IllegalArgumentException;

	void getResolvedValueSetInfo(String serverUrl, String valueSet, AsyncCallback<ResolvedValueSetInfo> callback)
	        throws IllegalArgumentException;
}
