package edu.mayo.cts2Viewer.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.mayo.bsi.cts.cts2connector.cts2search.ConvenienceMethods;
import edu.mayo.bsi.cts.cts2connector.cts2search.aux.CTS2Utils;
import edu.mayo.bsi.cts.cts2connector.cts2search.aux.SearchException;
import edu.mayo.bsi.cts.cts2connector.cts2search.aux.ServiceResultFormat;
import edu.mayo.bsi.cts.cts2connector.cts2search.aux.VocabularyId;
import edu.mayo.cts2Viewer.client.Cts2Service;
import edu.mayo.cts2Viewer.server.properties.PropertiesHelper;
import edu.mayo.cts2Viewer.shared.Credentials;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class Cts2ServiceImpl extends RemoteServiceServlet implements Cts2Service {

	private static Logger logger = Logger.getLogger(Cts2ServiceImpl.class.getName());

	private static final int RESULT_LIMIT = 100;
	private ConvenienceMethods cm = null;

	/**
	 * Get ValueSets that match the criteria
	 */
	@Override
	public String getValueSets(String serviceName, String searchText) throws IllegalArgumentException {
		String results = "";

		try {
			initCM(serviceName);

			cm.getCurrentContext().resultLimit = RESULT_LIMIT;
			if (CTS2Utils.isNull(searchText)) {
				results = cm.getAvailableValueSets(false, false, false, ServiceResultFormat.XML);
			} else {
				results = cm.getMatchingValueSets(searchText, false, false, false, ServiceResultFormat.XML);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error retrieving ValueSets" + e);
		}

		return results;
	}

	/**
	 * Get information on a specific ValueSet
	 */
	@Override
	public ValueSetInfo getValueSetInfo(String serviceName, String valueSetName) throws IllegalArgumentException {

		String results = "";
		// RestExecuter restExecuter = RestExecuter.getInstance();
		ValueSetInfo vsi = new ValueSetInfo();

		try {
			initCM(serviceName);
			VocabularyId valueSetId = new VocabularyId();
			valueSetId.name = valueSetName;
			results = cm.getValueSetInformation(valueSetId, ServiceResultFormat.XML);
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
	public ResolvedValueSetInfo getResolvedValueSetInfo(String serviceName, String valueSetName)
	        throws IllegalArgumentException {

		String results = "";
		ResolvedValueSetInfo rvsi = new ResolvedValueSetInfo();

		try {
			initCM(serviceName);
			results = cm.getValueSetMembers(valueSetName, ServiceResultFormat.XML);

			if (results != null && results.length() > 0) {
				rvsi = getResolvedValueSetGeneralInfo(results);
			}

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

	private void initCM(String serviceName) {
		try {
			if (this.cm == null) {
				this.cm = ConvenienceMethods.instance(PropertiesHelper.getInstance().getPropertiesDirectory());
			}

			if (!CTS2Utils.isNull(serviceName) && !cm.getCurrentProfileName().equals(serviceName)) {
				cm.setCurrentProfileName(serviceName);
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	@Override
	public LinkedHashMap<String, String> getAvailableServices() throws IllegalArgumentException {
		LinkedHashMap<String, String> serverOptions = new LinkedHashMap<String, String>();
		Set<String> services = null;
		String selectedService = null;

		try {
			initCM(null);
			services = cm.getAvailableProfiles();
			selectedService = cm.getCurrentProfileName();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}

		for (String service : services) {
			if (selectedService.endsWith(service)) {
				serverOptions.put(service, service + CTS2Utils.SELECTED_TAG);
			} else {
				serverOptions.put(service, service);
			}
		}

		return serverOptions;
	}

	@Override
	public String getEntity(String serviceName, String url) {
		try {
			if (CTS2Utils.isNull(url)) {
				return "request url is null!!";
			}

			initCM(serviceName);

			new URI(url).toString();
			return cm.getVocabularyEntityByURI(url, ServiceResultFormat.XML);

		} catch (SearchException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * Determine if the user needs to provide credentials for the selected
	 * server.
	 */
	@Override
	public Boolean getCredentialsRequired(String serviceName) throws IllegalArgumentException {

		Boolean required;

		try {
			initCM(serviceName);
			required = new Boolean(cm.getCurrentContext().secure);
		} catch (Exception e) {
			required = new Boolean(true);
			logger.log(Level.SEVERE, "Error determining if credentials are required" + e);
		}
		return required;
	}

	/**
	 * Validate the credentials for the given service
	 */
	@Override
	public Boolean validateCredentials(edu.mayo.cts2Viewer.shared.Credentials credentials)
	        throws IllegalArgumentException {

		Boolean valid;

		// just do a simple search that will return quick.
		String searchText = "abcd";
		try {
			initCM(credentials.getServer());

			cm.getCurrentContext().userName = credentials.getUser();
			cm.getCurrentContext().password = credentials.getPassword();

			cm.getCurrentContext().resultLimit = 1;
			cm.getMatchingValueSets(searchText, false, false, false, ServiceResultFormat.XML);
			valid = new Boolean(true);

		} catch (Exception e) {
			valid = new Boolean(false);
			logger.log(Level.WARNING, "User " + credentials.getUser() + " on server " + credentials.getServer()
			        + " entered invalid credentials.");
		}

		return valid;
	}

	@Override
	public Boolean logout(Credentials credentials) {
		// cm.setCurrentProfileName(null);
		return new Boolean(true);
	}
}
