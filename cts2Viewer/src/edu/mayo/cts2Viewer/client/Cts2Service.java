package edu.mayo.cts2Viewer.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("cts2")
public interface Cts2Service extends RemoteService {
	String getValueSets(String name) throws IllegalArgumentException;
}
