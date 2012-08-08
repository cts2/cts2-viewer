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
	// private static final String REST_URL_BASE =
	// "http://informatics.mayo.edu/cts2/rest/";

	// private static final String REST_URL_RESOLVED_VS = REST_URL_BASE +
	// "resolvedvaluesets?valueset=";

	private static final String RESOLVED_VALUE_SETS = "resolvedvaluesets?valueset=";

	private static final String VALUE_SET = "valueset";
	private static final String VALUE_SETS = "valuesets";
	private static final String RESOLUTION = "resolution";
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
	 * @param serverUrl
	 * @param searchString
	 * @return
	 * @throws Exception
	 */
	public String getValueSets(String serverUrl, String searchString) throws Exception {

		StringBuilder sb = new StringBuilder();

		try {

			URL url = getSearchUrl(serverUrl, searchString);
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
	 * Make REST call to get the resolved ValueSet xml that contains the
	 * resolved ValueSet info.
	 * 
	 * @param valueSet
	 * @return
	 * @throws Exception
	 */
	public String getResolvedValueSetLocation(String serverUrl, String valueSet) throws Exception {

		StringBuilder sb = new StringBuilder();

		try {

			// TODO CME FIX URL
			// URL url = new URL(/* REST_URL_RESOLVED_VS + valueSet */"");
			URL url = new URL(serverUrl + RESOLVED_VALUE_SETS + valueSet);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();

		} catch (Exception e) {
			sb = new StringBuilder(); // reset
			logger.log(Level.SEVERE, "Error getting Resolved ValueSet information for  " + valueSet + "  " + e);
		}

		return sb.toString();
	}

	/**
	 * Make REST call to get the resolved ValueSet xml.
	 * 
	 * @param urlString
	 * @param valueSet
	 * @return
	 * @throws Exception
	 */
	public String getResolvedValueSetInfo(String urlString, String valueSet) throws Exception {

		StringBuilder sb = new StringBuilder();

		try {

			URL url = new URL(urlString + VALUE_SET + "/" + valueSet + "/" + RESOLUTION);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();

		} catch (Exception e) {
			sb = new StringBuilder(); // reset
			logger.log(Level.SEVERE, "Error getting Resolved ValueSet information for  " + urlString + "  " + e);
		}

		return sb.toString();
	}

	/**
	 * Make REST call to get valueSet info.
	 * 
	 * @param urlString
	 * @return
	 * @throws Exception
	 */
	public String getValueSetInfo(String urlString) throws Exception {

		StringBuilder sb = new StringBuilder();

		try {

			URL url = new URL(urlString);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();

		} catch (Exception e) {
			sb = new StringBuilder(); // reset
			logger.log(Level.SEVERE, "Error getting ValueSet information for  " + urlString + "  " + e);
		}

		return sb.toString();
	}

	/**
	 * Get the REST URL for searching for value sets.
	 * 
	 * @param serverUrl
	 * @param searchString
	 * @return
	 * @throws MalformedURLException
	 */
	private URL getSearchUrl(String serverUrl, String searchString) throws MalformedURLException {
		URL url;
		String urlString;
		new URLParamEncoder();

		if (searchString != null && searchString.length() > 0) {
			urlString = serverUrl + VALUE_SETS + "?" + MATCH_VALUE + "=" + URLParamEncoder.encode(searchString);
			url = new URL(urlString);
		} else {
			urlString = serverUrl + VALUE_SETS;

			url = new URL(urlString);
		}

		return url;
	}

}