package edu.mayo.cts2Viewer.client.authentication;

import java.util.HashMap;

import edu.mayo.cts2Viewer.shared.Credentials;

public class Authentication {

	private final HashMap<String, Credentials> i_serverCredentials;
	private static Authentication s_authentication;

	public static Authentication getInstance() {
		if (s_authentication == null) {
			s_authentication = new Authentication();
		}
		return s_authentication;
	}

	/**
	 * Private constructor
	 */
	private Authentication() {
		i_serverCredentials = new HashMap<String, Credentials>();
	}

	public void addAuthenticatedCredential(Credentials credentials) {
		// use the server for the key
		i_serverCredentials.put(credentials.getServer(), credentials);
	}

	public Credentials getCredentials(String server) {
		return i_serverCredentials.get(server);
	}

	public void removeCredential(String server) {
		i_serverCredentials.remove(server);
	}

}
