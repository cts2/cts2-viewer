package edu.mayo.cts2Viewer.client;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.cts2Viewer.client.datasource.ResolvedValueSetXmlDS;
import edu.mayo.cts2Viewer.client.events.ResolvedValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;

public class ResolvedValueSetListGrid extends ListGrid {

	private static final String EMPTY_MESSAGE = "No entities to display.";

	private final ResolvedValueSetXmlDS i_resolvedValueSetXmlDS;
	private String i_resolvedValueSet;

	private int count = 0;

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

		setShowRecordComponents(true);
		setShowRecordComponentsByCell(true);

		ListGridField xmlIconField = new ListGridField("xmlIconField", "XML");
		xmlIconField.setWidth(40);

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

		setFields(xmlIconField, nameField, nameSpaceField, designationField);

		setAutoFetchData(false);
		setCanEdit(false);
	}

	@Override
	protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

		String fieldName = this.getFieldName(colNum);

		if (fieldName.equals("xmlIconField")) {

			System.out.println("xml icon - " + count++);

			HLayout recordCanvas = new HLayout(1);
			recordCanvas.setHeight(22);
			recordCanvas.setWidth(34);
			recordCanvas.setAlign(Alignment.CENTER);
			ImgButton xmlImg = new ImgButton();
			xmlImg.setShowDown(false);
			xmlImg.setShowRollOver(false);
			xmlImg.setLayoutAlign(Alignment.CENTER);
			xmlImg.setSrc("xml.png");
			xmlImg.setPrompt("Disply the entity XML in a new tab.");
			xmlImg.setShowHover(true);
			xmlImg.setHoverWidth(100);
			xmlImg.setHoverWrap(false);
			xmlImg.setHeight(19);
			xmlImg.setWidth(32);

			xmlImg.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					String href = record.getAttribute("href");
					record.getAttribute("name");

					// Don't create a link if the href is missing
					if (href == null) {
						SC.say("There is no XML available for this Entity.");
					} else {
						// open new tab
						Window.open(href, "_blank", "");
					}
				}
			});

			recordCanvas.addMember(xmlImg);
			return recordCanvas;
		}
		return null;
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
