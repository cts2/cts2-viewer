package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.mayo.cts2Viewer.shared.Credentials;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ServerProperties;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * The async counterpart of <code>Cts2Service</code>.
 */
public interface Cts2ServiceAsync {
	void getAvailableServices(AsyncCallback<LinkedHashMap<String, String>> callback) throws IllegalArgumentException;

	void getDefaultService(AsyncCallback<String> callback) throws IllegalArgumentException;

	void getCredentialsRequired(String serviceName, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void validateCredentials(Credentials credentials, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void getValueSets(String serviceName, String searchText, Map<String, String> filters, AsyncCallback<String> callback)
	        throws IllegalArgumentException;

	void getValueSetInfo(String serviceName, String valueSetName, AsyncCallback<ValueSetInfo> callback)
	        throws IllegalArgumentException;

	void getResolvedValueSetInfo(String serviceName, String valueSetName, AsyncCallback<ResolvedValueSetInfo> callback)
	        throws IllegalArgumentException;

	void getEntity(String serviceName, String url, AsyncCallback<String> callback) throws IllegalArgumentException;

	void logout(Credentials credentials, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void getServerProperties(String serviceName, AsyncCallback<ServerProperties> callback)
	        throws IllegalArgumentException;

	void getNqfNumbers(AsyncCallback<LinkedHashMap<String, String>> async);

	void geteMeasureIds(AsyncCallback<LinkedHashMap<String, String>> async);
}
