package edu.mayo.cts2Viewer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	private static final String BYPASS_OPTION = "?bypass=1";
	private static final String UNAVAILABLE_HTML = "<!doctype html><html><head></head><body><span style=\"color:#0A59A4;font-size:1.3em;font-weight:bold\">Entity details are unavailable.</span></body></html>";
	private static EntityWindow i_entityWindow;

	private final Label i_titleLabel;
	private HTMLPane i_htmlPane;
	private HLayout i_buttonPane;
	private HLayout i_headerPane;

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
		i_headerPane = createHeader();
		addItem(i_headerPane);

		i_htmlPane = createHTMLPane();
		i_htmlPane.setContentsType(ContentsType.PAGE);
		addItem(i_htmlPane);

		i_buttonPane = createClosePane();
		addItem(i_buttonPane);
		addCloseClickHandler(new CloseClickHandler() {

			@Override
			public void onCloseClick(CloseClickEvent event) {
				i_entityWindow.hide();
			}
		});
	}

	private HTMLPane createHTMLPane() {
		HTMLPane pane = new HTMLPane();
		pane.setWidth("100%");
		pane.setHeight("100%");
		pane.setMargin(5);
		pane.setScrollbarSize(0);

		return pane;
	}

	public void setWindowData(String serviceUrl, String href, String name, String description) {
		setTitle(TITLE);
		String windowTitle = "Details for " + name + ": " + description;
		String titleFormatted = "<b style=\"color: #000000;font-family: Arial,Helvetica,sans-serif;font-size:14px;font-weight:bold;text-decoration:none\">"
		  + windowTitle + "</b>";

		i_titleLabel.setContents(titleFormatted);
		getEntityInformation(serviceUrl, href);
	}

	private void getEntityInformation(String serviceUrl, final String entityUrl) {
		removeItem(i_htmlPane);
		removeItem(i_buttonPane);
		i_htmlPane = createHTMLPane();
		i_buttonPane = createClosePane();

		if (serviceUrl != null && !serviceUrl.trim().equals("") && entityUrl != null && !entityUrl.trim().equals("")) {
			String completeUrl = serviceUrl + entityUrl + BYPASS_OPTION;
			i_htmlPane.setContentsType(ContentsType.PAGE);
			Cts2ServiceAsync service = GWT.create(Cts2Service.class);
			service.getEntity(null, completeUrl, new AsyncCallback<String>() 
			{
					@Override
					public void onFailure(Throwable caught) 
					{
						i_htmlPane.setContents(UNAVAILABLE_HTML);
					}

					@Override
					public void onSuccess(String result) 
					{
						if (result == null)
							i_htmlPane.setContents(UNAVAILABLE_HTML);
						else
							i_htmlPane.setContents(result);
					}
				});
			}
		
			addItem(i_htmlPane);
			addItem(i_buttonPane);
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

	private HLayout createClosePane() {
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

	public void enableNavigationArrows(boolean enable) {
		if (enable) {
			i_headerPane.show();
		}
		else {
			i_headerPane.hide();
		}
	}
}
