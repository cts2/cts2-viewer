package edu.mayo.cts2Viewer.client.datasource;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.XmlNamespaces;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

import edu.mayo.cts2Viewer.client.Cts2Service;
import edu.mayo.cts2Viewer.client.Cts2ServiceAsync;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

public class ValueSetInfoXmlDS extends DataSource {

	private static final Logger logger = Logger.getLogger(ValueSetInfoXmlDS.class.getName());

	private static final String RECORD_X_PATH = "/cts2:ValueSetCatalogEntryMsg/cts2:valueSetCatalogEntry/core:property";

	private static final String X_PATH_BASE = "/cts2:ValueSetCatalogEntryMsg/cts2:valueSetCatalogEntry";
	private static final String X_PATH_RESOURCE_STATE = X_PATH_BASE + "/@entryState";
	private static final String X_PATH_RESOURCE_ABOUT_URI = X_PATH_BASE + "/@about";
	private static final String X_PATH_RESOURCE_FORMAL_NAME = X_PATH_BASE + "/@formalName";
	private static final String X_PATH_RESOURCE_VS_NAME = X_PATH_BASE + "/@valueSetName";

	private static final String X_PATH_RESOURCE_SOURCE = X_PATH_BASE + "/core:sourceAndRole/core:source";
	private static final String X_PATH_RESOURCE_SOURCE_URI = X_PATH_BASE + "/core:sourceAndRole/core:source/@uri";
	private static final String X_PATH_RESOURCE_ROLE = X_PATH_BASE + "/core:sourceAndRole/core:role";
	private static final String X_PATH_RESOURCE_SYNOPSIS = X_PATH_BASE + "/core:resourceSynopsis/core:value";
	private static final String X_PATH_RESOURCE_DEFINITIONS = X_PATH_BASE + "/cts2:definitions";

	private static final String X_PATH_RESOURCE_PREDICATE_URI = "core:predicate/@uri";
	private static final String X_PATH_RESOURCE_NAME = "core:predicate/core:name";
	private static final String X_PATH_RESOURCE_NAMESPACE = "core:predicate/core:namespace";
	private static final String X_PATH_RESOURCE_VALUE = "core:value/core:literal/core:value";

	private static ValueSetInfoXmlDS instance = null;
	private final XmlNamespaces i_xmlNamespaces;
	private final LinkedHashMap<String, String> i_nsMap;

	public static ValueSetInfoXmlDS getInstance() {
		if (instance == null) {
			instance = new ValueSetInfoXmlDS("ValueSetInfoXmlDS");
		}

		return instance;
	}

	public ValueSetInfoXmlDS(String id) {

		setID(id);
		setDataFormat(DSDataFormat.XML);

		i_nsMap = getNameSpaceHashMap();

		// Set the namespaces
		i_xmlNamespaces = new XmlNamespaces();
		i_xmlNamespaces.addNamespace("cts2", "http://schema.omg.org/spec/CTS2/1.0/ValueSet");
		i_xmlNamespaces.addNamespace("core", "http://schema.omg.org/spec/CTS2/1.0/Core");
		i_xmlNamespaces.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		setXmlNamespaces(i_xmlNamespaces);

		// set the XPath
		setRecordXPath(RECORD_X_PATH);

		DataSourceTextField aboutField = new DataSourceTextField("about", "About");
		aboutField.setValueXPath(X_PATH_RESOURCE_ABOUT_URI);

		DataSourceTextField formalNameField = new DataSourceTextField("formalName", "Formal Name");
		formalNameField.setValueXPath(X_PATH_RESOURCE_FORMAL_NAME);

		DataSourceTextField valueSetNameField = new DataSourceTextField("valueSetName", "Value Set Name");
		valueSetNameField.setValueXPath(X_PATH_RESOURCE_VS_NAME);

		DataSourceTextField definitionField = new DataSourceTextField("definition", "Definition");
		definitionField.setValueXPath(X_PATH_RESOURCE_DEFINITIONS);

		DataSourceTextField stateField = new DataSourceTextField("entryState", "State");
		stateField.setValueXPath(X_PATH_RESOURCE_STATE);

		DataSourceTextField sourceUriField = new DataSourceTextField("sourceUri", "Source URI");
		sourceUriField.setValueXPath(X_PATH_RESOURCE_SOURCE_URI);

		DataSourceTextField sourceField = new DataSourceTextField("source", "Source");
		sourceField.setValueXPath(X_PATH_RESOURCE_SOURCE);

		DataSourceTextField roleField = new DataSourceTextField("role", "Role");
		roleField.setValueXPath(X_PATH_RESOURCE_ROLE);

		DataSourceTextField descriptionField = new DataSourceTextField("description", "Description");
		descriptionField.setValueXPath(X_PATH_RESOURCE_SYNOPSIS);

		DataSourceTextField predicateUriField = new DataSourceTextField("uri", "URI");
		predicateUriField.setValueXPath(X_PATH_RESOURCE_PREDICATE_URI);

		DataSourceTextField nameField = new DataSourceTextField("name", "Name");
		nameField.setValueXPath(X_PATH_RESOURCE_NAME);

		DataSourceTextField nameSpaceField = new DataSourceTextField("namespace", "Name Space");
		nameSpaceField.setValueXPath(X_PATH_RESOURCE_NAMESPACE);

		DataSourceTextField valueField = new DataSourceTextField("value", "Value");
		valueField.setValueXPath(X_PATH_RESOURCE_VALUE);

		// setFields(entryStateField, aboutField, formalNameField,
		// valueSetNameField, nameField, nameSpaceField);
		setFields(valueField, nameField, nameSpaceField, predicateUriField, roleField, sourceField, sourceUriField,
		        descriptionField, stateField, definitionField, aboutField, formalNameField, valueSetNameField);

		// setDataURL("data/valueSetsReal.data.xml");
		// namespace doesn't work when setClientOnly(true)
		// http://forums.smartclient.com/showthread.php?t=7670
		// need setClientOnly(true); when making call to REST service
		setClientOnly(true);
	}

	/**
	 * Create a HashMap of the nameSpaces.
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getNameSpaceHashMap() {
		LinkedHashMap<String, String> nsMap = new LinkedHashMap<String, String>();
		nsMap.put("cts2", "http://schema.omg.org/spec/CTS2/1.0/ValueSet");
		nsMap.put("core", "http://schema.omg.org/spec/CTS2/1.0/Core");
		nsMap.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");

		return nsMap;
	}

	@Override
	public void fetchData(Criteria criteria, final DSCallback callback) {

		final String valueSetUrl = criteria.getAttribute("valueSetLink");

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		service.getValueSetInfo(valueSetUrl, new AsyncCallback<ValueSetInfo>() {

			@Override
			public void onSuccess(ValueSetInfo vsi) {
				String result = vsi.getXml();

				if (result != null && result.length() > 0) {
					Object results = XMLTools.selectNodes(result, RECORD_X_PATH, i_nsMap);
					Record[] fetchRecords = recordsFromXML(results);

					setTestData(fetchRecords);
				}

				// use the callback to let the widget know we got the data...
				callback.execute(null, vsi, null);
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error searching Value Set info: " + caught);
			}
		});

		// super.fetchData(criteria, callback);
	}

}
