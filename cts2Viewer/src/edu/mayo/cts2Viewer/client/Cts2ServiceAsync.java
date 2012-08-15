package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * The async counterpart of <code>Cts2Service</code>.
 */
public interface Cts2ServiceAsync {
	void getAvailableServices(AsyncCallback<LinkedHashMap<String, String>> callback) throws IllegalArgumentException;

	void getValueSets(String serviceName, String searchText, AsyncCallback<String> callback)
	        throws IllegalArgumentException;

	void getValueSetInfo(String serviceName, String valueSetName, AsyncCallback<ValueSetInfo> callback) throws IllegalArgumentException;

	void getResolvedValueSetInfo(String serviceName, String valueSetName, AsyncCallback<ResolvedValueSetInfo> callback)
	        throws IllegalArgumentException;

	//void getAvailableServices(AsyncCallback<Set<String>> callback);
}
