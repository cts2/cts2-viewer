package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.mayo.cts2Viewer.shared.Credentials;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ServerProperties;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("cts2")
public interface Cts2Service extends RemoteService {
	LinkedHashMap<String, String> getAvailableServices() throws IllegalArgumentException;

	String getDefaultService() throws IllegalArgumentException;

	Boolean getCredentialsRequired(String serviceName) throws IllegalArgumentException;

	Boolean validateCredentials(Credentials credentials) throws IllegalArgumentException;

	String getValueSets(String serviceName, String searchText, Map<String, String> filters) throws IllegalArgumentException;

	ValueSetInfo getValueSetInfo(String serviceName, String valueSetName) throws IllegalArgumentException;

	ResolvedValueSetInfo getResolvedValueSetInfo(String serviceName, String valueSetName)
	        throws IllegalArgumentException;

	String getEntity(String serviceName, String url);

	Boolean logout(Credentials credentials);

	ServerProperties getServerProperties(String serviceName) throws IllegalArgumentException;
}
