package edu.mayo.cts2Viewer.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/*
 * Panel to hold data for the main area (welcome, about, search/graphs, ...)
 */
public class ContextAreaPanel extends VLayout {

	static Logger lgr = Logger.getLogger(ContextAreaPanel.class.getName());

	private Canvas i_currentPanel;

	public ContextAreaPanel() {
		super();

		init();
	}

	private void init() {

		lgr.log(Level.INFO, "init ContextAreaPanel...");

		// set to take up all of the space that exists.
		setWidth100();
		setHeight100();
	}

	/**
	 * Set the layout to the one passed in.
	 * 
	 * @param layout
	 */
	public void setCurrentContextArea(Layout layout) {

		// remove the current, if it exists
		if (i_currentPanel != null) {
			removeChild(i_currentPanel);
		}

		// add the new panel
		i_currentPanel = layout;
		addMember(i_currentPanel);

		// redraw
		markForRedraw();
	}
}
