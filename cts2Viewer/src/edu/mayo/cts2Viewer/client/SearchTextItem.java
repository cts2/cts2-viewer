package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.widgets.form.fields.TextItem;

public class SearchTextItem extends TextItem {
	
	private static final int WIDGET_WIDTH = 150;
	private String i_previousText = "";

	public SearchTextItem() {
		super("search", "<b>Search</b>");

		init();
	}

	private void init() {
		setHint("Enter Search Text");
		setShowHintInField(true);
		setWidth(WIDGET_WIDTH);
	}

	/**
	 * A valid search text would be different than the previous search text.
	 * 
	 * @return
	 */
	public boolean isValidSearchText() {

		String currentText = getValueAsString();
		currentText = currentText == null ? "" : currentText;

		boolean isValid = !i_previousText.equals(currentText);

		if (isValid) {
			i_previousText = currentText;
		}

		return isValid;
	}

}
