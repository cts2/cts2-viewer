package edu.mayo.cts2Viewer.client;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

import edu.mayo.cts2Viewer.client.events.PanelChangeEvent;

public class Cts2ToolStrip extends ToolStrip {

	private static final int HEIGHT = 27;
	private static final String WIDTH = "100%";

	private static final String HREF_HELP_CONTENTS = "http://informatics.mayo.edu/cts2/index.php?title=Value_Set_UI&action=edit&redlink=1";
	private static final String WINDOW_PROPERTIES = "width=900,height=800,location=no,menubar=no,status=no,toolbar=no,scrollbars=yes,resizable=yes";

	private final ToolStripButton i_valueSetsButton;
	private final ToolStripButton i_welcomesButton;
	private MenuItem i_contentsItem;
	private MenuItem i_aboutItem;

	public Cts2ToolStrip() {
		super();

		addStyleName("cts2-ToolStripMenu");

		setWidth(WIDTH);
		setHeight(HEIGHT);

		i_welcomesButton = new ToolStripButton();
		i_welcomesButton.setTitle("<b>Welcome</b>");

		i_welcomesButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) 
			{
				Cts2Viewer.EVENT_BUS.fireEvent(new PanelChangeEvent(PanelTypeEnum.WELCOME));
			}
		});

		addButton(i_welcomesButton);
		addSeparator();

		i_valueSetsButton = new ToolStripButton();
		i_valueSetsButton.setTitle("<b>Value Sets</b>");

		i_valueSetsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) 
			{
				Cts2Viewer.EVENT_BUS.fireEvent(new PanelChangeEvent(PanelTypeEnum.VALUESET));
			}
		});

		addButton(i_valueSetsButton);
		addSeparator();

		ToolStripMenuButton helpButton = getToolStripHelpButton();
		addMenuButton(helpButton);

		addSeparator();
	}

	private ToolStripMenuButton getToolStripHelpButton() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		i_contentsItem = new MenuItem("Contents", "help.png");
		i_contentsItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				Window.open(HREF_HELP_CONTENTS, "help_contents", WINDOW_PROPERTIES);
			}
		});

		i_aboutItem = new MenuItem("About", "message.png");
		i_aboutItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				AboutWindow aboutWindow = new AboutWindow();
				aboutWindow.show();
			}
		});

		menu.setItems(i_contentsItem, i_aboutItem);

		ToolStripMenuButton menuButton = new ToolStripMenuButton("<b>Help</b>", menu);
		menuButton.setWidth(100);
		return menuButton;
	}
}
