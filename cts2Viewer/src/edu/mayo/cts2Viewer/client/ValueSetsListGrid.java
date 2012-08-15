package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

import edu.mayo.cts2Viewer.client.datasource.ValueSetsXmlDS;
import edu.mayo.cts2Viewer.client.events.ValueSetsReceivedEvent;

public class ValueSetsListGrid extends ListGrid {

	private final ValueSetsXmlDS i_valueSetsXmlDS;

	private String i_searchString;

	public ValueSetsListGrid() {
		super();

		// i_valueSetsXmlDS = ValueSetsXmlDS.getInstance(VALUE_SETS_DS_ID);
		i_valueSetsXmlDS = ValueSetsXmlDS.getInstance();

		setWidth100();
		setHeight(300);
		setShowAllRecords(true);
		setWrapCells(false);
		setDataSource(i_valueSetsXmlDS);

		// setSelectionType(SelectionStyle.SIMPLE);
		// setSelectionAppearance(SelectionAppearance.CHECKBOX);

		ListGridField resourceTypeField = new ListGridField("resourceRoot", "Resource Type");
		resourceTypeField.setWidth(100);
		resourceTypeField.setWrap(false);
		resourceTypeField.setShowHover(false);

		ListGridField resourceNamefField = new ListGridField("valueSetName", "Value Set Name");
		resourceNamefField.setWidth(250);
		resourceNamefField.setWrap(false);
		resourceNamefField.setShowHover(false);

		resourceNamefField.setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				if (value != null) {
					return addCellHighlights(value.toString());
				} else {
					return null;
				}
			}
		});

		ListGridField formalNameField = new ListGridField("formalName", "Formal Name");
		formalNameField.setWidth("250");
		formalNameField.setWrap(false);
		formalNameField.setShowHover(false);

		formalNameField.setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				if (value != null) {
					return addCellHighlights(value.toString());
				} else {
					return null;
				}
			}
		});

		ListGridField descriptionField = new ListGridField("value", "Description");
		descriptionField.setWidth("*");
		descriptionField.setShowHover(true);
		descriptionField.setWrap(true);

		descriptionField.setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				if (value != null) {
					return addCellHighlights(value.toString());
				} else {
					return null;
				}
			}
		});

		setFields(resourceTypeField, resourceNamefField, formalNameField, descriptionField);

		setAutoFetchData(false);
		setCanEdit(false);

		setCanHover(true);
		setShowHover(true);
		setShowHoverComponents(true);

		// set the initial sort
		SortSpecifier[] sortspec = new SortSpecifier[1];
		sortspec[0] = new SortSpecifier("formalName", SortDirection.ASCENDING);
		setInitialSort(sortspec);
	}

	@Override
	protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
		// only show a custom DetailViewer for the description column only
		if (colNum == 3) {

			DetailViewer detailViewer = new DetailViewer();
			detailViewer.setWidth(400);

			// Define the fields that we want to display in the details popup.
			// These
			// fields are populated from the record of the selected ValueSets.
			DetailViewerField descripitonField = new DetailViewerField("value", "Description");
			detailViewer.setFields(descripitonField);

			detailViewer.setData(new Record[] { record });
			return detailViewer;
		}
		return null;
	}

	/**
	 * Call the search to get the matching data.
	 * 
	 * @param searchText
	 */
	public void getData(String serviceName, String searchText) {

		i_searchString = searchText;

		Criteria criteria = new Criteria();
		criteria.addCriteria("searchText", searchText);
		criteria.addCriteria("serviceName", serviceName);

		i_valueSetsXmlDS.fetchData(criteria, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				// set to empty
				setData(new ListGridRecord[0]);

				fetchData();
				redraw();

				// let others know that the data has been retrieved.
				Cts2Viewer.EVENT_BUS.fireEvent(new ValueSetsReceivedEvent());
			}
		});

	}

	/**
	 * Highlight any of the text that matches the searchString.
	 * 
	 * @param value
	 * @return
	 */
	private String addCellHighlights(String cellText) {

		if (i_searchString == null || i_searchString.length() == 0) {
			return cellText;
		}

		String lowerCaseCellText = cellText.toLowerCase();
		int startIndex = lowerCaseCellText.indexOf(i_searchString);

		if (startIndex >= 0) {

			int first = startIndex;

			cellText = cellText.substring(0, first) + "<b style=\"color:#e33b74\">"
			        + cellText.substring(startIndex, startIndex + i_searchString.length()) + "</b>"
			        + cellText.substring(startIndex + i_searchString.length());
		}

		return cellText;
	}
}
