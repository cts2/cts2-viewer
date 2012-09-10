package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import edu.mayo.cts2Viewer.client.datasource.ValueSetInfoXmlDS;
import edu.mayo.cts2Viewer.client.events.ValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.client.utils.UiHelper;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

public class ValueSetInfoListGrid extends ListGrid {

	private final ValueSetInfoXmlDS i_valueSetInfoXmlDS;

	private String i_valueSetName;

	public ValueSetInfoListGrid() {
		super();
		i_valueSetInfoXmlDS = ValueSetInfoXmlDS.getInstance();
		setWidth100();
		setHeight("*");
		setShowAllRecords(true);
		setShowAllColumns(true);
		setWrapCells(false);

		setDataSource(i_valueSetInfoXmlDS);

		ListGridField uriField = new ListGridField("uri", "URI");
		uriField.setWrap(false);

		ListGridField nameField = new ListGridField("name", "Name");
		nameField.setWrap(false);

		// create link from the name and uri attributes
		nameField.setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				String uri = record.getAttribute("uri");
				String name = record.getAttribute("name");

				// if uri is not present, don't create a link
				if (uri != null && uri.length() > 0) {
					return UiHelper.getLink(uri, name);
				} else {
					return name;
				}
			}
		});

		ListGridField valueField = new ListGridField("value", "Value");
		valueField.setWrap(false);

		setFields(/* uriField, */nameField, valueField);

		setAutoFetchData(false);
		setCanEdit(false);
	}

	public void getData(String serviceName, String name) {
		i_valueSetName = name;

		Criteria criteria = new Criteria();
		criteria.addCriteria("valueSetName", i_valueSetName);
		criteria.addCriteria("serviceName", serviceName);

		i_valueSetInfoXmlDS.fetchData(criteria, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				ValueSetInfo vsi = (ValueSetInfo) rawData;

				// set to empty
				setData(new ListGridRecord[0]);

				fetchData();
				redraw();

				// let others know that the data has been retrieved.
				Cts2Viewer.EVENT_BUS.fireEvent(new ValueSetInfoReceivedEvent(vsi));
			}
		});

	}

}
