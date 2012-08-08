package edu.mayo.cts2Viewer.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.mayo.cts2Viewer.client.Cts2Service;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class Cts2ServiceImpl extends RemoteServiceServlet implements Cts2Service {

	private static Logger logger = Logger.getLogger(Cts2ServiceImpl.class.getName());

	private static final String SERVER_PROPERTIES_FILE = "cts2_servers.properties";

	/**
	 * Get ValueSets that match the criteria
	 */
	@Override
	public String getValueSets(String serverUrl, String searchText) throws IllegalArgumentException {

		String results = "";
		RestExecuter restExecuter = RestExecuter.getInstance();

		try {
			results = restExecuter.getValueSets(serverUrl, searchText);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error retrieving ValueSets" + e);
		}

		return results;
	}

	/**
	 * Get information on a specific ValueSet
	 */
	@Override
	public ValueSetInfo getValueSetInfo(String valueSet) throws IllegalArgumentException {

		String results = "";
		RestExecuter restExecuter = RestExecuter.getInstance();
		ValueSetInfo vsi = new ValueSetInfo();

		try {
			results = restExecuter.getValueSetInfo(valueSet);
			vsi = getValueSetGeneralInfo(results);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error retrieving ValueSets" + e);
		}

		return vsi;
	}

	/**
	 * Get resolved ValueSet information
	 */
	@Override
	public ResolvedValueSetInfo getResolvedValueSetInfo(String serverUrl, String valueSet)
	        throws IllegalArgumentException {

		String results = "";
		RestExecuter restExecuter = RestExecuter.getInstance();
		ResolvedValueSetInfo rvsi = new ResolvedValueSetInfo();

		try {

			results = restExecuter.getResolvedValueSetInfo(serverUrl, valueSet);
			if (results != null && results.length() > 0) {
				rvsi = getResolvedValueSetGeneralInfo(results);
			}
			// TODO CME this is the way for bioportal, but phinvads is different
			/*
			 * results = restExecuter.getResolvedValueSetLocation(serverUrl,
			 * valueSet); if (results != null && results.length() > 0) { String
			 * resolvedValueSetUrl = getResolvedValueSetUrl(results);
			 * 
			 * results =
			 * restExecuter.getResolvedValueSetInfo(resolvedValueSetUrl);
			 * 
			 * if (results != null && results.length() > 0) { rvsi =
			 * getResolvedValueSetGeneralInfo(results); } } else {
			 * logger.log(Level.SEVERE,
			 * "Error retrieving Resolved ValueSets for serverUrl " + serverUrl
			 * + " and value " + valueSet); }
			 */

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error retrieving ValueSets" + e);
		}

		return rvsi;
	}

	/**
	 * Parse the ValueSet to get the general information from it and put it into
	 * a container object.
	 * 
	 * @param xmlStr
	 * @return
	 */
	private ValueSetInfo getValueSetGeneralInfo(String xmlStr) {

		ValueSetInfo vsi = new ValueSetInfo();

		try {
			DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
			xmlFact.setNamespaceAware(true);
			DocumentBuilder builder = xmlFact.newDocumentBuilder();
			Document document = builder.parse(new java.io.ByteArrayInputStream(xmlStr.getBytes()));

			NamespaceContext namespaceContext = new NamespaceContext() {

				@Override
				public String getNamespaceURI(String prefix) {
					String uri;
					if (prefix.equals("cts2")) {
						uri = "http://schema.omg.org/spec/CTS2/1.0/ValueSet";
					} else if (prefix.equals("core")) {
						uri = "http://schema.omg.org/spec/CTS2/1.0/Core";
					} else if (prefix.equals("xsi")) {
						uri = "http://www.w3.org/2001/XMLSchema-instance";
					} else {
						uri = null;
					}
					return uri;
				}

				@Override
				public Iterator getPrefixes(String arg0) {
					return null;
				}

				@Override
				public String getPrefix(String arg0) {
					return null;
				}
			};

			XPathFactory xpathFact = XPathFactory.newInstance();
			XPath xpath = xpathFact.newXPath();
			xpath.setNamespaceContext(namespaceContext);

			vsi.setState(xpath.evaluate(ValueSetInfo.X_PATH_RESOURCE_STATE, document));
			vsi.setResourceName(xpath.evaluate(ValueSetInfo.X_PATH_RESOURCE_VS_NAME, document));
			vsi.setFormalName(xpath.evaluate(ValueSetInfo.X_PATH_RESOURCE_FORMAL_NAME, document));
			vsi.setDescription(xpath.evaluate(ValueSetInfo.X_PATH_RESOURCE_SYNOPSIS, document));
			vsi.setRole(xpath.evaluate(ValueSetInfo.X_PATH_RESOURCE_ROLE, document));
			vsi.setSource(xpath.evaluate(ValueSetInfo.X_PATH_RESOURCE_SOURCE, document));
			vsi.setSourceUri(xpath.evaluate(ValueSetInfo.X_PATH_RESOURCE_SOURCE_URI, document));

			// set the entire xml in the object as well.
			vsi.setXml(xmlStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vsi;
	}

	/**
	 * Parse the ResolvedValueSet to get the general information from it and put
	 * it into a container object.
	 * 
	 * @param xmlStr
	 * @return
	 */
	private ResolvedValueSetInfo getResolvedValueSetGeneralInfo(String xmlStr) {

		ResolvedValueSetInfo rvsi = new ResolvedValueSetInfo();

		try {
			DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
			xmlFact.setNamespaceAware(true);
			DocumentBuilder builder = xmlFact.newDocumentBuilder();
			Document document = builder.parse(new java.io.ByteArrayInputStream(xmlStr.getBytes()));

			NamespaceContext namespaceContext = new NamespaceContext() {

				@Override
				public String getNamespaceURI(String prefix) {
					String uri;
					if (prefix.equals("cts2")) {
						uri = "http://schema.omg.org/spec/CTS2/1.0/ValueSetDefinition";
					} else if (prefix.equals("core")) {
						uri = "http://schema.omg.org/spec/CTS2/1.0/Core";
					} else if (prefix.equals("xsi")) {
						uri = "http://www.w3.org/2001/XMLSchema-instance";
					} else {
						uri = null;
					}
					return uri;
				}

				@Override
				public Iterator getPrefixes(String arg0) {
					return null;
				}

				@Override
				public String getPrefix(String arg0) {
					return null;
				}
			};

			XPathFactory xpathFact = XPathFactory.newInstance();
			XPath xpath = xpathFact.newXPath();
			xpath.setNamespaceContext(namespaceContext);

			rvsi.setValueSetDefinition(xpath.evaluate(ResolvedValueSetInfo.X_PATH_VS_DEFINITION, document));
			rvsi.setValueSetDefinitionUri(xpath.evaluate(ResolvedValueSetInfo.X_PATH_VS_DEFINITION_URI, document));
			rvsi.setValueSetDefinitionHref(xpath.evaluate(ResolvedValueSetInfo.X_PATH_VS_DEFINITION_HREF, document));
			rvsi.setCodeSystem(xpath.evaluate(ResolvedValueSetInfo.X_PATH_CODE_SYSTEM, document));
			rvsi.setCodeSystemUri(xpath.evaluate(ResolvedValueSetInfo.X_PATH_CODE_SYSTEM_URI, document));
			rvsi.setCodeSystemHref(xpath.evaluate(ResolvedValueSetInfo.X_PATH_CODE_SYSTEM_HREF, document));
			rvsi.setCodeSystemVersion(xpath.evaluate(ResolvedValueSetInfo.X_PATH_CODE_SYSTEM_VERSION, document));
			rvsi.setCodeSystemVersionHref(xpath
			        .evaluate(ResolvedValueSetInfo.X_PATH_CODE_SYSTEM_VERSION_HREF, document));

			// set the entire xml in the object as well.
			rvsi.setXml(xmlStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rvsi;
	}

	private String getResolvedValueSetUrl(String xmlStr) {

		String result = "";

		try {
			DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
			xmlFact.setNamespaceAware(true);
			DocumentBuilder builder = xmlFact.newDocumentBuilder();
			Document document = builder.parse(new java.io.ByteArrayInputStream(xmlStr.getBytes()));

			NamespaceContext namespaceContext = new NamespaceContext() {

				@Override
				public String getNamespaceURI(String prefix) {
					String uri;
					if (prefix.equals("cts2")) {
						uri = "http://schema.omg.org/spec/CTS2/1.0/ValueSetDefinition";
					} else if (prefix.equals("core")) {
						uri = "http://schema.omg.org/spec/CTS2/1.0/Core";
					} else if (prefix.equals("xsi")) {
						uri = "http://www.w3.org/2001/XMLSchema-instance";
					} else {
						uri = null;
					}
					return uri;
				}

				@Override
				public Iterator getPrefixes(String arg0) {
					return null;
				}

				@Override
				public String getPrefix(String arg0) {
					return null;
				}
			};

			// XPath expression
			String xpathStr = "/cts2:ResolvedValueSetDirectory/cts2:entry[1]/@href";

			XPathFactory xpathFact = XPathFactory.newInstance();
			XPath xpath = xpathFact.newXPath();
			xpath.setNamespaceContext(namespaceContext);

			result = xpath.evaluate(xpathStr, document);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public LinkedHashMap<String, String> getServerOptions() throws IllegalArgumentException {

		LinkedHashMap<String, String> serverOptions = new LinkedHashMap<String, String>();
		FileInputStream in = null;

		Properties cts2ServerProps = new Properties();

		try {

			in = new FileInputStream(getBasePath() + "data/" + SERVER_PROPERTIES_FILE);
			cts2ServerProps.load(in);

		} catch (FileNotFoundException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}

		Set<Object> keys = cts2ServerProps.keySet();
		for (Object key : keys) {
			serverOptions.put((String) cts2ServerProps.get(key), key.toString());
		}

		return serverOptions;
	}

	public String getBasePath() {
		String dataPath;

		HttpSession httpSession = getThreadLocalRequest().getSession(true);
		ServletContext context = httpSession.getServletContext();

		String realContextPath = context.getRealPath(getThreadLocalRequest().getContextPath());

		if (isDevelopmentMode()) {
			dataPath = realContextPath;
		} else {
			dataPath = realContextPath + "/../";
		}

		return dataPath;
	}

	/**
	 * Determine if the app is in development mode. To do this get the request
	 * URL and if it contains 127.0.0.1, then it is in development mode.
	 * 
	 * @return
	 */
	private boolean isDevelopmentMode() {
		return getThreadLocalRequest().getHeader("Referer").contains("127.0.0.1");
	}
}
