package edu.mayo.cts2Viewer.client;

import java.util.logging.Logger;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.cts2Viewer.client.utils.ModalWindow;

/**
 * Window to retrieve and display Entity information.
 */
public class EntityWindow extends Window {

	private static final Logger logger = Logger.getLogger(EntityWindow.class.getName());
	private ModalWindow i_busyIndicator;
	private static final String TITLE = "Entity Details";

	private static EntityWindow i_entityWindow;
	private final Label i_titleLabel;
	private final HTMLPane i_htmlPane;

	private static String i_href;
	private static String i_entityName;
	private static String i_description;
	private static String i_server;

	public static EntityWindow getInstance(String server, String href, String name, String desc) {
		if (i_entityWindow == null) {
			i_entityWindow = new EntityWindow();
		}
		i_entityWindow.setWindowData(server, href, name, desc);

		return i_entityWindow;
	}

	private EntityWindow() {
		super();

		setWidth(700);
		setHeight(450);

		// set a thinner window edge.
		setEdgeSize(4);

		setShowMinimizeButton(false);
		setIsModal(false);
		centerInPage();

		setAnimateShowTime(1000);
		setAnimateFadeTime(1000);
		setAnimateShowEffect(AnimationEffect.WIPE);

		i_titleLabel = createWindowTitle("");
		addItem(i_titleLabel);

		i_htmlPane = createHTMLPane();
		i_htmlPane.setHeight("*");
		addItem(i_htmlPane);

		addItem(addCloseButton());
	}

	private HTMLPane createHTMLPane() {
		HTMLPane pane = new HTMLPane();
		pane.setWidth100();
		pane.setHeight100();
		pane.setMargin(5);
		pane.setContentsType(ContentsType.PAGE);

		return pane;
	}

	private void setWindowData(String server, String href, String name, String desc) {
		i_href = href;
		i_entityName = name;
		i_description = desc;
		i_server = server;

		setTitle(TITLE);

		String windowTitle = "Details for " + i_entityName + ": " + i_description;
		String titleFormatted = "<b style=\"color: #000000;font-family: Arial,Helvetica,sans-serif;font-size:14px;font-weight:bold;text-decoration:none\">"
		        + windowTitle + "</b>";

		i_titleLabel.setContents(titleFormatted);

		getEntityInformation(i_server, href, name);
	}

	private void getEntityInformation(String serviceName, final String url, String id) {

		i_htmlPane.setContentsURL("http://informatics.mayo.edu/cts2/services/py4cts2/cts2/concept/" + id
		        + "?noq&xslt=namedEntity");

	}

	private static Label createWindowTitle(String title) {

		Label windowTitleLabel = new Label(title);
		windowTitleLabel.setWidth100();
		windowTitleLabel.setHeight(25);
		windowTitleLabel.setAlign(Alignment.CENTER);
		windowTitleLabel.setValign(VerticalAlignment.CENTER);
		windowTitleLabel.setWrap(true);
		windowTitleLabel.setBackgroundColor("#efefef");

		return windowTitleLabel;
	}

	private HLayout addCloseButton() {
		HLayout buttonLayout = new HLayout();
		buttonLayout.setWidth100();
		buttonLayout.setHeight(30);
		buttonLayout.setMargin(5);
		buttonLayout.setAlign(Alignment.RIGHT);

		IButton closeButton = new IButton("Close");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				i_entityWindow.hide();
			}
		});

		buttonLayout.addMember(closeButton);
		return buttonLayout;
	}

}
