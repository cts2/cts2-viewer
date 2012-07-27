package edu.mayo.cts2Viewer.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.mayo.cts2Viewer.client.Cts2Service;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class Cts2ServiceImpl extends RemoteServiceServlet implements
		Cts2Service {

	private static Logger logger = Logger.getLogger(Cts2ServiceImpl.class
			.getName());

	@Override
	public String getValueSets(String input) throws IllegalArgumentException {

		String results = "";
		RestExecuter restExecuter = RestExecuter.getInstance();

		try {
			results = restExecuter.getValueSets(input);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error retrieving ValueSets" + e);
		}

		return results;
	}

}
