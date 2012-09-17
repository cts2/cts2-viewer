package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.cts2Viewer.client.datasource.ValueSetsXmlDS;
import edu.mayo.cts2Viewer.client.events.ValueSetsReceivedEvent;
import edu.mayo.cts2Viewer.client.events.ValueSetsReceivedEventHandler;

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

	private IButton i_selectAllButton;
	private IButton i_clearAllButton;

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
		setHeight(21);
		setAlign(Alignment.RIGHT);
		setAlign(VerticalAlignment.CENTER);
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
		i_exportTypeItem.setHeight(20);
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

		i_selectAllButton = new IButton("Select All");
		i_selectAllButton.setWidth(55);
		i_selectAllButton.setHeight(20);
		i_selectAllButton.setValign(VerticalAlignment.CENTER);
		i_selectAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ListGridRecord[] records = i_valueSetsListGrid.getRecords();

				for (ListGridRecord record : records) {
					record.setAttribute("download", true);
					i_valueSetsListGrid.updateData(record);
				}
				setDownloadEnabled(true);
			}
		});

		i_clearAllButton = new IButton("Clear All");
		i_clearAllButton.setWidth(55);
		i_clearAllButton.setHeight(20);
		i_clearAllButton.setValign(VerticalAlignment.CENTER);
		i_clearAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ListGridRecord[] records = i_valueSetsListGrid.getRecords();

				for (ListGridRecord record : records) {
					record.setAttribute("download", false);
					i_valueSetsListGrid.updateData(record);
				}
				setDownloadEnabled(false);
			}
		});

		i_downloadButton = new IButton("Download");
		i_downloadButton.setWidth(90);
		i_downloadButton.setHeight(20);
		i_downloadButton.setValign(VerticalAlignment.CENTER);
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
		addMember(i_clearAllButton);
		addMember(i_selectAllButton);
		addMember(i_downloadButton);

		i_downloadButton.setDisabled(true);
		i_clearAllButton.setDisabled(true);
		i_selectAllButton.setDisabled(true);
		i_exportTypeItem.setDisabled(true);

		createValueSetsReceivedEvent();
	}

	/**
	 * Enable/disable the download type pulldown and the button.
	 * 
	 * @param enabled
	 */
	public void setDownloadEnabled(boolean enabled) {

		if (enabled) {
			i_downloadButton.enable();

		} else {
			i_downloadButton.disable();
		}
	}

	/**
	 * Listen for the event that ValueSets were retrieved.
	 */
	private void createValueSetsReceivedEvent() {
		Cts2Viewer.EVENT_BUS.addHandler(ValueSetsReceivedEvent.TYPE, new ValueSetsReceivedEventHandler() {

			@Override
			public void onValueSetsReceived(ValueSetsReceivedEvent event) {

				// initially disable the download because nothing will be
				// selected.
				setDownloadEnabled(false);

				DataClass[] dc = ValueSetsXmlDS.getInstance().getTestData();

				if (dc.length >= 1) {
					i_selectAllButton.enable();
					i_clearAllButton.enable();
					i_exportTypeItem.setDisabled(false);
				} else {
					i_selectAllButton.disable();
					i_clearAllButton.disable();
					i_exportTypeItem.setDisabled(true);
				}
			}
		});
	}
}
