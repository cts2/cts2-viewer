package edu.mayo.cts2Viewer.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;

public class UiHelper {

	private static final String BACKGROUND_COLOR_TITLE = "#dedad5";

	// return a link that will open a page in a new window/tab.
	public static String getLink(String url, String label) {
		return "<a href=\"" + url + " \" target='_blank'>" + label + "</a>";
	}

	public static Label createTitleLabel(String text) {
		Label titleLabel = new Label();
		titleLabel.setWidth100();
		titleLabel.setHeight(30);
		titleLabel.setBackgroundColor(BACKGROUND_COLOR_TITLE);
		titleLabel.setAlign(Alignment.CENTER);

		String title = "<b style=\"color: #51524e;font-family: Arial,Helvetica,sans-serif;font-size:24px;font-weight:bold;text-decoration:none\">"
		        + text + "</b>";

		titleLabel.setContents(title);

		return titleLabel;
	}

}
