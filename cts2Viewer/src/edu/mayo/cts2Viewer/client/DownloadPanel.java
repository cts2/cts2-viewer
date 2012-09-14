package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Panel to hold the widgets to select the format and download button.
 * 
 */
public class DownloadPanel extends HLayout {

	private static Logger logger = Logger.getLogger(Cts2Viewer.class.getName());

	private final ValueSetsListGrid i_valueSetsListGrid;
	private final ComboBoxItem i_serverCombo;

	private DynamicForm i_downloadForm = null;
	private IButton i_downloadButton;
	private SelectItem i_exportTypeItem = null;

	private HiddenItem zipFileNameItem = null;
	private HiddenItem serviceNameItem = null;
	private HiddenItem downloadTypeItem = null;
	private HiddenItem vsNamesItem = null;

	public DownloadPanel(ComboBoxItem serverCombo, ValueSetsListGrid valueSetsListGrid) {
		super();
		i_serverCombo = serverCombo;
		i_valueSetsListGrid = valueSetsListGrid;
		init();
	}

	private void init() {
		setWidth100();
		setHeight(25);
		setAlign(Alignment.RIGHT);
		setMembersMargin(4);

		i_downloadForm = new DynamicForm();
		i_downloadForm.setTarget("downloadCallbackFrame");
		i_downloadForm.setAction(GWT.getModuleBaseURL() + "cts2download");
		i_downloadForm.setEncoding(Encoding.NORMAL);
		i_downloadForm.setCanSubmit(true);

		zipFileNameItem = new HiddenItem("ZipFileName");
		serviceNameItem = new HiddenItem("serviceName");
		downloadTypeItem = new HiddenItem("downloadType");
		vsNamesItem = new HiddenItem("valueSetNames");

		i_exportTypeItem = new SelectItem("exportType", "Format");
		i_exportTypeItem.setWidth(120);
		i_exportTypeItem.setWrapTitle(false);
		i_exportTypeItem.setDefaultToFirstOption(true);

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("csv", "CSV (Excel)");
		valueMap.put("xml", "CTS2 XML");
		valueMap.put("json", "CTS2 JSON");
		valueMap.put("svs", "SVS XML");
		valueMap.put("all", "All Formats");

		// set images for each row
		i_exportTypeItem.setImageURLSuffix(".png");
		LinkedHashMap<String, String> valueIcons = new LinkedHashMap<String, String>();
		valueIcons.put("csv", "csv");
		valueIcons.put("xml", "xml");
		valueIcons.put("json", "json");
		valueIcons.put("svs", "svs");
		valueIcons.put("all", "multiple");

		i_exportTypeItem.setValueIcons(valueIcons);
		i_exportTypeItem.setValueMap(valueMap);

		FormItem[] allItems = new FormItem[] { i_exportTypeItem, zipFileNameItem, serviceNameItem, downloadTypeItem,
		        vsNamesItem };

		i_downloadForm.setFields(allItems);

		i_downloadButton = new IButton("Download");
		i_downloadButton.setIcon("download.png");
		i_downloadButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {

				String downloadType = i_exportTypeItem.getValueAsString();
				String serviceNameValue = i_serverCombo.getValueAsString();

				ListGridRecord[] selected = i_valueSetsListGrid.getDownloadRecords();
				if (selected == null || selected.length == 0) {
					logger.log(Level.WARNING, "No ValueSet Selected!!");
					return;
				}

				String valueSets = "";

				for (ListGridRecord record : selected) {
					String value = record.getAttributeAsString("valueSetName");
					if ("".equals(valueSets)) {
						valueSets = value;
					} else {
						valueSets += "," + value;
					}
				}

				if ("".equals(valueSets.trim())) {
					logger.log(Level.WARNING, "No ValueSet Names found in selected records!");
					return;
				}

				serviceNameItem.setValue(serviceNameValue);
				downloadTypeItem.setValue(downloadType);
				zipFileNameItem.setValue("CTS2ValueSetdownload");
				vsNamesItem.setValue(valueSets);

				i_downloadForm.submitForm();
			}
		});

		addMember(i_downloadForm);
		addMember(i_downloadButton);
	}

	/**
	 * Enable/disable the download type pulldown and the button.
	 * 
	 * @param enabled
	 */
	public void setWidgetsEnabled(boolean enabled) {

		if (enabled) {
			i_downloadButton.enable();
		} else {
			i_downloadButton.disable();
		}

		i_exportTypeItem.setDisabled(!enabled);

	}
}
