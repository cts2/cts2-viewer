package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;

import edu.mayo.cts2Viewer.client.datasource.ValueSetsXmlDS;

/**
 * Search comboBoxItem that is hooked up to a DataSource.
 */
public class SearchComboBoxItem extends ComboBoxItem {

	private static final String EMPTY_MESSAGE = "No Values Matching Criteria";
	private static final String VALUE_SETS_DS_ID = "SearchComboBoxItem_ID";

	private final ValueSetsXmlDS i_valueSetsXmlDS;

	public SearchComboBoxItem() {
		super("search", "Search");

		i_valueSetsXmlDS = ValueSetsXmlDS.getInstance(VALUE_SETS_DS_ID);

		setAddUnknownValues(true);
		setDefaultToFirstOption(true);
		setCompleteOnTab(true);
		setHint("Enter Search Text");
		setShowHintInField(true);
		setWidth(400);

		setOptionDataSource(i_valueSetsXmlDS);
		setAutoFetchData(false);

		setEmptyPickListMessage(EMPTY_MESSAGE);

		// set the id and display field mapping.
		setDisplayField("resourceName");
		setValueField("resourceName");

		// filter on multiple fields.

		setTextMatchStyle(TextMatchStyle.SUBSTRING);
		setFilterFields("resourceName", "formalName", "value");

		addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {

				String keyName = event.getKeyName();

				// TODO CME - filter what characters are allowed. Don't allow
				// up/down arrows.
				System.out.println(keyName);

				// ignore the arrow keys
				if (keyName != null && keyName.indexOf("Arrow") < 0) {

					getData(getDisplayValue());

				} else {
					// nothing.
				}
			}
		});

	}

	/**
	 * Call the search to get the matching data.
	 * 
	 * @param searchText
	 */
	protected void getData(String searchText) {

		Criteria criteria = new Criteria();
		criteria.addCriteria("searchText", searchText);

		i_valueSetsXmlDS.fetchData(criteria, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				System.out.println("in callback... going to call fetchData() now");
				fetchData();
			}
		});

	}

	/**
	 * Clear the records for the DataSource.
	 */
	private void clearRecords() {
		System.out.println("Clearing records... List box item count =  " + getClientPickListData().length);

		invalidateDisplayValueCache();
		i_valueSetsXmlDS.setTestData(null);
		i_valueSetsXmlDS.invalidateCache();

		System.out.println("List box item count =  " + getClientPickListData().length);

	}

}
