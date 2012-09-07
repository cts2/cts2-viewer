package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import edu.mayo.cts2Viewer.client.datasource.ResolvedValueSetXmlDS;
import edu.mayo.cts2Viewer.client.events.ResolvedValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.client.utils.UiHelper;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;

public class ResolvedValueSetListGrid extends ListGrid {

	private final ResolvedValueSetXmlDS i_resolvedValueSetXmlDS;
	private String i_resolvedValueSet;

	public ResolvedValueSetListGrid() {
		super();

		i_resolvedValueSetXmlDS = ResolvedValueSetXmlDS.getInstance();

		setWidth100();
		setHeight100();
		setShowAllRecords(true);
		setShowAllColumns(true);
		setWrapCells(false);
		setDataSource(i_resolvedValueSetXmlDS);

		ListGridField nameSpaceField = new ListGridField("nameSpace", "Code System Name");
		nameSpaceField.setWrap(false);
		nameSpaceField.setWidth("25%");

		ListGridField nameField = new ListGridField("name", "Code");
		nameField.setWrap(false);
		nameField.setWidth("25%");

		// create link from the name and uri attributes
		nameField.setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				String href = record.getAttribute("href");
				String name = record.getAttribute("name");

				// Don't create a link if the href is missing
				if (href == null) {
					return name;
				}

				return UiHelper.getLink(href, name);
			}
		});

		ListGridField designationField = new ListGridField("designation", "Description");
		designationField.setWrap(false);
		designationField.setWidth("*");

		setFields(nameField, nameSpaceField, designationField);

		setAutoFetchData(false);
		setCanEdit(false);
	}

	public void getData(String serviceName, String valueSetName) {
		i_resolvedValueSet = valueSetName;

		Criteria criteria = new Criteria();
		criteria.addCriteria("valueSetName", i_resolvedValueSet);
		criteria.addCriteria("serviceName", serviceName);

		i_resolvedValueSetXmlDS.fetchData(criteria, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {

				ResolvedValueSetInfo rvsi = (ResolvedValueSetInfo) rawData;
				// set to empty
				setData(new ListGridRecord[0]);

				fetchData();
				redraw();

				// let others know that the data has been retrieved.
				Cts2Viewer.EVENT_BUS.fireEvent(new ResolvedValueSetInfoReceivedEvent(rvsi));
			}
		});

	}
}
