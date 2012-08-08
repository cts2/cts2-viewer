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
import com.smartgwt.client.widgets.grid.ListGridRecord;

import edu.mayo.cts2Viewer.client.Cts2Service;
import edu.mayo.cts2Viewer.client.Cts2ServiceAsync;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;

public class ResolvedValueSetXmlDS extends DataSource {

	private static final Logger logger = Logger.getLogger(ResolvedValueSetXmlDS.class.getName());

	private static final String RECORD_X_PATH = "/cts2:IteratableResolvedValueSet/cts2:entry";

	private static final String X_PATH_NUMBER_OF_ENTRIES = "/cts2:IteratableResolvedValueSet/@numEntries";

	private static final String X_PATH_VS_DEFINITION = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolutionOf/core:valueSetDefinition";
	private static final String X_PATH_VS_DEFINITION_URI = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolutionOf/core:valueSet/@uri";
	private static final String X_PATH_VS_DEFINITION_HREF = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolutionOf/core:valueSet/@href";

	private static final String X_PATH_CODE_SYSTEM_VERSION = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolvedUsingCodeSystem/core:version";
	private static final String X_PATH_CODE_SYSTEM_VERSION_HREF = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolvedUsingCodeSystem/core:version/@href";

	private static final String X_PATH_CODE_SYSTEM = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolvedUsingCodeSystem/core:codeSystem";
	private static final String X_PATH_CODE_SYSTEM_URI = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolvedUsingCodeSystem/core:codeSystem/@uri";
	private static final String X_PATH_CODE_SYSTEM_HREF = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/cts2:resolvedUsingCodeSystem/core:codeSystem/@href";

	private static final String X_PATH_ENTRY_NAMESPACE = "core:namespace";
	private static final String X_PATH_ENTRY_NAME = "core:name";
	private static final String X_PATH_DESIGNATION = "core:designation";

	private static ResolvedValueSetXmlDS instance = null;
	private final XmlNamespaces i_xmlNamespaces;
	private final LinkedHashMap<String, String> i_nsMap;

	public static ResolvedValueSetXmlDS getInstance() {
		if (instance == null) {
			instance = new ResolvedValueSetXmlDS("ResolvedValueSetXmlDS");
		}

		return instance;
	}

	public ResolvedValueSetXmlDS(String id) {
		setID(id);
		setDataFormat(DSDataFormat.XML);

		i_nsMap = getNameSpaceHashMap();

		// Set the namespaces
		i_xmlNamespaces = new XmlNamespaces();
		i_xmlNamespaces.addNamespace("cts2", "http://schema.omg.org/spec/CTS2/1.0/ValueSetDefinition");
		i_xmlNamespaces.addNamespace("core", "http://schema.omg.org/spec/CTS2/1.0/Core");
		i_xmlNamespaces.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		setXmlNamespaces(i_xmlNamespaces);

		// set the XPath
		setRecordXPath(RECORD_X_PATH);

		DataSourceTextField vsDefinitionField = new DataSourceTextField("valueSetDefinition", "Value Set Definition");
		vsDefinitionField.setValueXPath(X_PATH_VS_DEFINITION);

		DataSourceTextField vsDefinitionUriField = new DataSourceTextField("valueSetDefinitionUri",
		        "Value Set Definition URI");
		vsDefinitionUriField.setValueXPath(X_PATH_VS_DEFINITION_URI);

		DataSourceTextField vsDefinitionHrefField = new DataSourceTextField("valueSetDefinitionHref",
		        "Value Set Definition HREF");
		vsDefinitionHrefField.setValueXPath(X_PATH_VS_DEFINITION_HREF);

		DataSourceTextField codeSystemVersionField = new DataSourceTextField("codeSystemVersion", "Code System Version");
		codeSystemVersionField.setValueXPath(X_PATH_CODE_SYSTEM_VERSION);

		DataSourceTextField codeSystemVersionHrefField = new DataSourceTextField("codeSystemVersionHref",
		        "Code System Version HREF");
		codeSystemVersionHrefField.setValueXPath(X_PATH_CODE_SYSTEM_VERSION_HREF);

		DataSourceTextField codeSystemField = new DataSourceTextField("codeSystem", "Code System");
		codeSystemField.setValueXPath(X_PATH_CODE_SYSTEM);

		DataSourceTextField codeSystemUriField = new DataSourceTextField("codeSystemUri", "Code System URI");
		codeSystemUriField.setValueXPath(X_PATH_CODE_SYSTEM_URI);

		DataSourceTextField codeSystemHrefField = new DataSourceTextField("codeSystemHref", "Code System HREF");
		codeSystemHrefField.setValueXPath(X_PATH_CODE_SYSTEM_HREF);

		DataSourceTextField uriField = new DataSourceTextField("uri", "URI");
		DataSourceTextField hrefField = new DataSourceTextField("href", "HREF");

		DataSourceTextField nameSpaceField = new DataSourceTextField("nameSpace", "Code System Version");
		nameSpaceField.setValueXPath(X_PATH_ENTRY_NAMESPACE);

		DataSourceTextField nameField = new DataSourceTextField("name", "Code");
		nameField.setValueXPath(X_PATH_ENTRY_NAME);

		DataSourceTextField designationField = new DataSourceTextField("designation", "Description");
		designationField.setValueXPath(X_PATH_DESIGNATION);

		setFields(vsDefinitionField, vsDefinitionUriField, vsDefinitionHrefField, codeSystemVersionField,
		        codeSystemVersionHrefField, codeSystemField, codeSystemUriField, codeSystemHrefField, uriField,
		        hrefField, nameSpaceField, nameField, designationField);

		setClientOnly(true);
	}

	/**
	 * Create a HashMap of the nameSpaces.
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getNameSpaceHashMap() {
		LinkedHashMap<String, String> nsMap = new LinkedHashMap<String, String>();
		nsMap.put("cts2", "http://schema.omg.org/spec/CTS2/1.0/ValueSetDefinition");
		nsMap.put("core", "http://schema.omg.org/spec/CTS2/1.0/Core");
		nsMap.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");

		return nsMap;
	}

	@Override
	public void fetchData(Criteria criteria, final DSCallback callback) {

		final String valueSetUrl = criteria.getAttribute("resolvedValueSet");
		final String serverUrl = criteria.getAttribute("serverUrl");

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		service.getResolvedValueSetInfo(serverUrl, valueSetUrl, new AsyncCallback<ResolvedValueSetInfo>() {

			@Override
			public void onSuccess(ResolvedValueSetInfo rvsi) {

				String result = rvsi.getXml();

				if (result != null && result.length() > 0) {
					Object results = XMLTools.selectNodes(result, RECORD_X_PATH, i_nsMap);
					Record[] fetchRecords = recordsFromXML(results);

					setTestData(fetchRecords);
				} else {
					setTestData(new ListGridRecord[0]);
				}
				// use the callback to let the widget know we got the data...
				callback.execute(null, rvsi, null);
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error searching Value Set info: " + caught);
			}
		});

		// super.fetchData(criteria, callback);
	}

}
