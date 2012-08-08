package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("cts2")
public interface Cts2Service extends RemoteService {
	LinkedHashMap<String, String> getServerOptions() throws IllegalArgumentException;

	String getValueSets(String serverUrl, String searchText) throws IllegalArgumentException;

	ValueSetInfo getValueSetInfo(String url) throws IllegalArgumentException;

	ResolvedValueSetInfo getResolvedValueSetInfo(String serverUrl, String valueSet) throws IllegalArgumentException;
}
