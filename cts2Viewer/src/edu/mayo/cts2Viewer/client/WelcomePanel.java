package edu.mayo.cts2Viewer.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.VLayout;

public class WelcomePanel extends VLayout 
{
	static Logger lgr = Logger.getLogger(WelcomePanel.class.getName());
	
	private static final String BACKGROUND_COLOR = "#F5F5F3";
	private static final String WELCOME_URL = "data/welcome.html";
	
	public WelcomePanel() {
		super();
		init();
	}

	private void init() 
	{
		lgr.log(Level.INFO, "init WelcomePanel...");
		
		// main layout to hold all of the value set info
		setWidth100();
		setHeight100();
		setBackgroundColor(BACKGROUND_COLOR);

		addMember(createHTMLPane());
	}
	
	private HTMLPane createHTMLPane() {
		HTMLPane pane = new HTMLPane();
		pane.setWidth100();
		pane.setHeight100();
		pane.setMargin(15);
		pane.setBackgroundColor(BACKGROUND_COLOR);

		// The name of the html file for the welcome page.
		pane.setContentsURL(WELCOME_URL);

		return pane;
	}

}
