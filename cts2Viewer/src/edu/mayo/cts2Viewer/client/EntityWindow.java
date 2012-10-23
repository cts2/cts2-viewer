package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.cts2Viewer.client.events.EntityChangedEvent;

/**
 * Window to retrieve and display Entity information.
 */
public class EntityWindow extends Window {

	private static final String TITLE = "Entity Details";

	private static String BASE_URL = "http://informatics.mayo.edu/cts2/services/xsltserver/transform?encoding=text/html&xsltname=namedEntity.xsl&xmlurl=";
	private static final String BYPASS_OPTION = "?bypass=1";

	private static EntityWindow i_entityWindow;
	private final Label i_titleLabel;
	private final HTMLPane i_htmlPane;

	private static String i_href;
	private static String i_entityName;
	private static String i_description;
	private static String i_server;

	public static EntityWindow getInstance() {
		if (i_entityWindow == null) {
			i_entityWindow = new EntityWindow();
		}
		return i_entityWindow;
	}

	private EntityWindow() {
		super();

		setWidth(750);
		setHeight(600);

		// set a thinner window edge.
		setEdgeSize(4);

		setShowMinimizeButton(false);
		setIsModal(false);
		centerInPage();

		i_titleLabel = createWindowTitle("");
		addItem(createHeader());

		i_htmlPane = createHTMLPane();
		i_htmlPane.setHeight("*");
		addItem(i_htmlPane);

		addItem(addCloseButton());
		addCloseClickHandler(new CloseClickHandler() {

			@Override
			public void onCloseClick(CloseClickEvent event) {
				i_entityWindow.hide();
			}
		});
	}

	private HTMLPane createHTMLPane() {
		HTMLPane pane = new HTMLPane();
		pane.setWidth100();
		pane.setHeight100();
		pane.setMargin(5);
		pane.setScrollbarSize(0);
		pane.setContentsType(ContentsType.PAGE);

		return pane;
	}

	public void setWindowData(String server, String href, String name, String desc) {
		i_href = href;
		i_entityName = name;
		i_description = desc;
		i_server = server;

		setTitle(TITLE);

		String windowTitle = "Details for " + i_entityName + ": " + i_description;
		String titleFormatted = "<b style=\"color: #000000;font-family: Arial,Helvetica,sans-serif;font-size:14px;font-weight:bold;text-decoration:none\">"
		        + windowTitle + "</b>";

		i_titleLabel.setContents(titleFormatted);

		getEntityInformation(i_server, i_href, name);
	}

	private void getEntityInformation(String serviceName, final String entityUrl, String id) {

		String completeUrl = BASE_URL + entityUrl + BYPASS_OPTION;
		i_htmlPane.setContentsURL(completeUrl);
	}

	private HLayout createHeader() {

		HLayout headerLayout = new HLayout();
		headerLayout.setWidth100();
		headerLayout.setHeight(32);
		headerLayout.setAlign(Alignment.RIGHT);

		String upArrow = "arrow_up.png";
		final Img upArrowImg = new Img(upArrow, 32, 32);

		HLayout upLayout = new HLayout();
		upLayout.setWidth(32);
		upLayout.setHeight(32);
		upLayout.setPrompt("Previous Entity");
		upLayout.addMember(upArrowImg);
		headerLayout.addMember(upLayout);

		String downArrow = "arrow_down.png";
		final Img downArrowImg = new Img(downArrow, 32, 32);

		downArrowImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// fire the entity change event
				Cts2Viewer.EVENT_BUS.fireEvent(new EntityChangedEvent(EntityChangedEvent.NEXT));
			}
		});
		downArrowImg.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				downArrowImg.setCursor(Cursor.HAND);
			}
		});
		downArrowImg.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				downArrowImg.setCursor(Cursor.AUTO);
			}
		});

		upArrowImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// fire the entity change event
				Cts2Viewer.EVENT_BUS.fireEvent(new EntityChangedEvent(EntityChangedEvent.PREVIOUS));
			}
		});

		upArrowImg.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				upArrowImg.setCursor(Cursor.HAND);
			}
		});
		upArrowImg.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				upArrowImg.setCursor(Cursor.AUTO);
			}
		});

		HLayout downLayout = new HLayout();
		downLayout.setWidth(32);
		downLayout.setHeight(32);
		downLayout.setPrompt("Next Entity");
		downLayout.addMember(downArrowImg);
		headerLayout.addMember(downLayout);

		return headerLayout;
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
