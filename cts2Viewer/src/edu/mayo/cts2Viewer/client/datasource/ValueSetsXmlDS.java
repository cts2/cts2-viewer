package edu.mayo.cts2Viewer.client.datasource;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.XmlNamespaces;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

import edu.mayo.cts2Viewer.client.Cts2Service;
import edu.mayo.cts2Viewer.client.Cts2ServiceAsync;

/**
 * DataSource for encapsulating ValueSet data
 */
public class ValueSetsXmlDS extends DataSource {

	private static final Logger logger = Logger.getLogger(ValueSetsXmlDS.class.getName());

	private static final String RECORD_X_PATH = "/cts2:ValueSetCatalogEntryDirectory/cts2:entry";

	private static final String X_PATH_RESOURCE_ROOT = "/cts2:ValueSetCatalogEntryDirectory/core:heading/core:resourceRoot";
	private static final String X_PATH_RESOURCE_SYNOPSIS = "core:resourceSynopsis/core:value";

	private static ValueSetsXmlDS instance = null;
	private final XmlNamespaces i_xmlNamespaces;
	private final LinkedHashMap<String, String> i_nsMap;

	public static ValueSetsXmlDS getInstance() {
		if (instance == null) {
			instance = new ValueSetsXmlDS("ValueSetsXmlDS");
		}
		return instance;
	}

	public ValueSetsXmlDS(String id) {

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

		DataSourceTextField hrefField = new DataSourceTextField("href", "HREF");
		hrefField.setPrimaryKey(true);

		DataSourceTextField resourceTypefField = new DataSourceTextField("resourceRoot", "Resource Type");
		resourceTypefField.setValueXPath(X_PATH_RESOURCE_ROOT);

		// map the value returned to a displayable value
		LinkedHashMap<String, String> resourceTypeMap = new LinkedHashMap<String, String>();
		resourceTypeMap.put("valuesets", "Value Sets");
		resourceTypefField.setValueMap(resourceTypeMap);

		DataSourceTextField resourceNamefField = new DataSourceTextField("resourceName", "ResourceName");
		DataSourceTextField aboutField = new DataSourceTextField("about", "About");
		DataSourceTextField formalNameField = new DataSourceTextField("formalName", "Formal Name");
		DataSourceTextField resourceSynopsisValueField = new DataSourceTextField("value", "Resource Synopsis");
		resourceSynopsisValueField.setValueXPath(X_PATH_RESOURCE_SYNOPSIS);

		setFields(hrefField, resourceTypefField, resourceNamefField, aboutField, formalNameField,
		        resourceSynopsisValueField);

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
	protected void transformResponse(DSResponse response, DSRequest request, Object data) {

		if (request.getOperationType() != null) {

			switch (request.getOperationType()) {

			case ADD: {
			}
				break;
			case FETCH: {
				executeFetch(request);
			}
				break;
			case REMOVE: {
			}
				break;
			case UPDATE: {
			}
				break;

			default:
				break;
			}
		}
		super.transformResponse(response, request, data);
	}

	private void executeFetch(DSRequest request) {

		// request.getCriteria();
		System.out.println("StartRow: " + request.getStartRow());
		System.out.println("EndRow:   " + request.getEndRow());

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		service.getValueSets(/* "Ontology" */null, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {

				Object results = XMLTools.selectNodes(result, RECORD_X_PATH, i_nsMap);
				Record[] fetchRecords = recordsFromXML(results);

				if (fetchRecords != null) {
					// add each record
					for (Record record : fetchRecords) {
						addData(record);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error fetching Value Sets: " + caught);
			}
		});
	}

	public void fetchData(Criteria criteria) {

		final String searchText = criteria.getAttribute("searchText");

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		service.getValueSets(searchText, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {

				// REMOVE all data before setting new
				setTestData(new DataClass[] {});

				Object results = XMLTools.selectNodes(result, RECORD_X_PATH, i_nsMap);
				Record[] fetchRecords = recordsFromXML(results);

				if (fetchRecords != null) {
					// add each record
					for (Record record : fetchRecords) {
						addData(record);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error searching Value Sets: " + caught);
			}
		});

	}
}
