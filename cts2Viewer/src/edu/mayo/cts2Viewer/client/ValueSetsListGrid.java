package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

import edu.mayo.cts2Viewer.client.datasource.ValueSetsXmlDS;

public class ValueSetsListGrid extends ListGrid {

	private final ValueSetsXmlDS i_valueSetsXmlDS;

	public ValueSetsListGrid() {
		super();

		i_valueSetsXmlDS = ValueSetsXmlDS.getInstance();

		setWidth100();
		setHeight(400);
		setShowAllRecords(true);
		setWrapCells(false);
		setDataSource(i_valueSetsXmlDS);

		ListGridField resourceTypeField = new ListGridField("resourceRoot", "Resource Type");
		resourceTypeField.setWidth(100);
		resourceTypeField.setWrap(false);
		resourceTypeField.setShowHover(false);

		ListGridField resourceNamefField = new ListGridField("resourceName", "Resource Name");
		resourceNamefField.setWidth(200);
		resourceNamefField.setWrap(false);
		resourceNamefField.setShowHover(false);

		ListGridField formalNameField = new ListGridField("formalName", "Formal Name");
		formalNameField.setWidth("*");
		formalNameField.setWrap(false);

		// ListGridField hrefField = new ListGridField("href", "HREF");
		// ListGridField aboutField = new ListGridField("about", "About");
		// ListGridField resourceSynopsisField = new ListGridField("value",
		// "Resource Synopsis");

		setFields(resourceTypeField, resourceNamefField, formalNameField);

		setAutoFetchData(true);
		setCanEdit(false);

		setCanHover(true);
		setShowHover(true);
		setShowHoverComponents(true);
	}

	@Override
	protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
		// only show a custom DetailViewer for column 2 only
		if (colNum == 2) {

			DetailViewer detailViewer = new DetailViewer();
			detailViewer.setWidth(400);

			// Define the fields that we want to display in the details popup.
			// These
			// fields are populated from the record of the selected ValueSets.
			DetailViewerField resourceSynopsisField = new DetailViewerField("value", "Resource Synopsis");
			detailViewer.setFields(resourceSynopsisField);

			detailViewer.setData(new Record[] { record });
			return detailViewer;
		}
		return null;
	}
}
