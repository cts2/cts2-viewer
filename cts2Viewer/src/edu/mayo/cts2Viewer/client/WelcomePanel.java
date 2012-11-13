package edu.mayo.cts2Viewer.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.utils.UiHelper;

public class WelcomePanel extends VLayout 
{
	static Logger lgr = Logger.getLogger(WelcomePanel.class.getName());
	private static final String TITLE = "Welcome";
	
	private static final String BACKGROUND_COLOR = "#F5F5F3";
	
	public WelcomePanel() {
		super();
		init();
	}

	private void init() 
	{
		lgr.log(Level.INFO, "init WelcomePanel...");
		Label titleLabel = UiHelper.createMainTitleLabel(TITLE);
		titleLabel = UiHelper.createLabelWithBorders(titleLabel);

		// main layout to hold all of the value set info
		setWidth100();
		setHeight100();
		setBackgroundColor(BACKGROUND_COLOR);

		// layout for main title
		HLayout titleLayout = new HLayout();
		titleLayout.setWidth100();
		titleLayout.setAlign(Alignment.CENTER);
		titleLayout.setMargin(10);
		titleLayout.addMember(titleLabel);

		// layout for any content
		HLayout contentLayout = new HLayout();
		contentLayout.setWidth100();
		contentLayout.setHeight100();
		contentLayout.setAlign(VerticalAlignment.TOP);
		contentLayout.setMargin(10);
		contentLayout.setMembersMargin(15);
		contentLayout.setBackgroundColor(BACKGROUND_COLOR);

		addMember(contentLayout);
	}
}
