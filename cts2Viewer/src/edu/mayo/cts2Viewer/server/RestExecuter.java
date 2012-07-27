package edu.mayo.cts2Viewer.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestExecuter extends BaseCts2Servlet {

	private static final long serialVersionUID = 1L;
	private static RestExecuter s_restExecuter;

	private static Logger logger = Logger.getLogger(RestExecuter.class.getName());

	// the base REST url.
	private static final String REST_URL_BASE = "http://informatics.mayo.edu/cts2/rest/";

	private static final String VALUE_SETS = "valuesets";
	private static final String MATCH_VALUE = "matchvalue";

	// private
	private RestExecuter() {
	}

	/**
	 * Set the Secure options on the new instance.
	 * 
	 * @return
	 */
	public static RestExecuter getInstance() {
		if (s_restExecuter == null) {
			s_restExecuter = new RestExecuter();
		}

		return s_restExecuter;
	}

	/**
	 * Make REST call to get valuesets based on a search string.
	 * 
	 * @param searchString
	 * @return
	 * @throws Exception
	 */
	public String getValueSets(String searchString) throws Exception {

		StringBuilder sb = new StringBuilder();

		try {

			URL url = getSearchUrl(searchString);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();

		} catch (Exception e) {
			sb = new StringBuilder(); // reset
			logger.log(Level.SEVERE, "Error searching for ValueSets: " + e);
		}

		return sb.toString();
	}

	/**
	 * Get the REST URL for searching for value sets.
	 * 
	 * @param searchString
	 * @return
	 * @throws MalformedURLException
	 */
	private URL getSearchUrl(String searchString) throws MalformedURLException {
		URL url;
		if (searchString != null && searchString.length() > 0) {
			url = new URL(REST_URL_BASE + VALUE_SETS + "?" + MATCH_VALUE + "=" + searchString);
		} else {
			url = new URL(REST_URL_BASE + VALUE_SETS);
		}

		return url;
	}

}