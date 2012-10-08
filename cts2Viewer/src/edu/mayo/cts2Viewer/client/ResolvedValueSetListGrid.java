package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import edu.mayo.cts2Viewer.client.datasource.ResolvedValueSetXmlDS;
import edu.mayo.cts2Viewer.client.events.ResolvedValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;

public class ResolvedValueSetListGrid extends ListGrid {

	private static final String EMPTY_MESSAGE = "No entities to display.";

	private final ResolvedValueSetXmlDS i_resolvedValueSetXmlDS;
	private String i_resolvedValueSet;

	private final int count = 0;

	public ResolvedValueSetListGrid() {
		super();

		i_resolvedValueSetXmlDS = ResolvedValueSetXmlDS.getInstance();

		setWidth100();
		setHeight100();
		setShowAllRecords(true);
		setShowAllColumns(true);
		setWrapCells(false);
		setDataSource(i_resolvedValueSetXmlDS);
		setEmptyMessage(EMPTY_MESSAGE);

		setCanHover(true);
		setHoverWidth(100);
		setHoverWrap(false);
		setSelectionType(SelectionStyle.SINGLE);

		setShowRecordComponents(true);
		setShowRecordComponentsByCell(true);

		ListGridField xmlIconField = new ListGridField("href", "XML");
		xmlIconField.setWidth(40);
		xmlIconField.setType(ListGridFieldType.LINK);
		xmlIconField.setAlign(Alignment.LEFT);
		xmlIconField.setLinkText(Canvas.imgHTML("xml.png", 34, 16, "Entity XML", "align=center", null));
		xmlIconField.setHoverCustomizer(new HoverCustomizer() {

			@Override
			public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
				return "Display the XML for this entity.";
			}
		});

		ListGridField nameSpaceField = new ListGridField("nameSpace", "Code System Name");
		nameSpaceField.setWrap(false);
		nameSpaceField.setWidth("25%");
		nameSpaceField.setShowHover(false);

		ListGridField nameField = new ListGridField("name", "Code");
		nameField.setWrap(false);
		nameField.setWidth("25%");
		nameField.setShowHover(false);

		ListGridField designationField = new ListGridField("designation", "Description");
		designationField.setWrap(false);
		designationField.setWidth("*");

		// only show the XML column if the user is an expert user (defined by
		// the showAll parameter).
		if (Cts2Viewer.s_showAll) {
			setFields(xmlIconField, nameField, nameSpaceField, designationField);
		} else {
			setFields(nameField, nameSpaceField, designationField);
		}

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
