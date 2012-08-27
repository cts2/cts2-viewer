package edu.mayo.cts2Viewer.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationAcceleration;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;

public class UiHelper {

	private static final String BACKGROUND_COLOR_MAIN_TITLE = "#ffffff";
	private static final String BACKGROUND_COLOR_TITLE = "#dedad5";
	public static final String SELECTED_TAG = "_SELECTED";

	// return a link that will open a page in a new window/tab.
	public static String getLink(String url, String label) {
		return "<a href=\"" + url + " \" target='_blank'>" + label + "</a>";
	}

	public static Label createMainTitleLabel(String text) {
		Label titleLabel = new Label();
		titleLabel.setWidth100();
		titleLabel.setHeight(30);
		titleLabel.setBackgroundColor(BACKGROUND_COLOR_MAIN_TITLE);
		titleLabel.setAlign(Alignment.CENTER);

		String title = "<b style=\"color: #51524e;font-family: Arial,Helvetica,sans-serif;font-size:24px;font-weight:bold;text-decoration:none\">"
		        + text + "</b>";

		titleLabel.setContents(title);

		return titleLabel;
	}

	public static Label createTitleLabel(String text) {
		Label titleLabel = new Label();
		titleLabel.setWidth100();
		titleLabel.setHeight(30);
		titleLabel.setBackgroundColor(BACKGROUND_COLOR_TITLE);
		titleLabel.setAlign(Alignment.CENTER);

		String title = "<b style=\"color: #51524e;font-family: Arial,Helvetica,sans-serif;font-size:20px;font-weight:bold;text-decoration:none\">"
		        + text + "</b>";

		titleLabel.setContents(title);

		return titleLabel;
	}

	public static String getSectionTitle(String title) {
		return "<b style=\"font-size:14px;font-weight:bold;text-decoration:none\">" + title + "</b>";
	}

	public static String getSelected(String serviceName) {
		if (serviceName == null) {
			return null;
		}

		if (serviceName.endsWith(SELECTED_TAG)) {
			return serviceName.split(SELECTED_TAG)[0];
		}

		return null;
	}

	public static Label createTitleLabel(int width, String title) {
		Label label = new Label("<b>" + title + "</b>");
		label.setWrap(false);
		label.setWidth(width);
		label.setMargin(3);
		return label;
	}

	public static Label createValueLabel(String title) {
		Label label = new Label(title);
		label.setWidth("*");
		label.setMargin(3);
		return label;
	}

	public static HLayout createNameValueLayout(Label title, Label value) {
		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight(20);

		layout.addMember(title);
		layout.addMember(value);

		return layout;
	}

	/**
	 * Set the Layout to have a border.
	 * 
	 * @param layout
	 * @return
	 */
	public static Layout createLayoutWithBorders(Layout layout) {

		layout.setShowEdges(true);
		layout.setEdgeShowCenter(true);

		layout.setEdgeImage("corners/glow_35.png");
		layout.setEdgeSize(10);
		layout.setEdgeOffset(10);

		return layout;

	}

	/**
	 * Create a Label with a border.
	 * 
	 * @param layout
	 * @return
	 */
	public static Label createLabelWithBorders(Label label) {
		if (label == null) {
			label = new Label();
		}
		label.setShowEdges(true);
		label.setEdgeShowCenter(true);
		label.setEdgeImage("corners/glow_35.png");
		label.setEdgeSize(10);
		label.setEdgeOffset(10);
		return label;

	}

	/**
	 * Create a consistent operating SectionStack.
	 * 
	 * @return
	 */
	public static SectionStack createSectionStack() {

		SectionStack sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setWidth100();
		sectionStack.setHeight100();

		// set animate options
		sectionStack.setAnimateSections(true);
		sectionStack.setAnimateTime(2000);
		sectionStack.setAnimateHideAcceleration(AnimationAcceleration.SMOOTH_START_END);
		sectionStack.setAnimateShowAcceleration(AnimationAcceleration.SMOOTH_START_END);

		return sectionStack;
	}
}
