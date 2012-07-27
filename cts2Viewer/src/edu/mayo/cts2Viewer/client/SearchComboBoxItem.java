package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;

import edu.mayo.cts2Viewer.client.datasource.ValueSetsXmlDS;

public class SearchComboBoxItem extends ComboBoxItem {

	private ComboBoxItem i_searchCombo;
	private final ValueSetsXmlDS i_valueSetsXmlDS;

	public SearchComboBoxItem() {
		super("search", "Search");

		i_valueSetsXmlDS = ValueSetsXmlDS.getInstance();

		setAddUnknownValues(true);
		setDefaultToFirstOption(true);
		setCompleteOnTab(true);
		setHint("Enter Search Text");
		setShowHintInField(true);
		setWidth(240);
		setOptionDataSource(i_valueSetsXmlDS);
		setAutoFetchData(false);

		// set the id and display field mapping.
		setDisplayField("resourceName");
		setValueField("href");

		addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				// Don't start searching until there are at least 2 characters.
				if (getDisplayValue().length() >= 2) {
					Criteria criteria = new Criteria();
					criteria.addCriteria("searchText", getDisplayValue());
					i_valueSetsXmlDS.fetchData(criteria);
				} else {

				}
			}
		});
	}
}
