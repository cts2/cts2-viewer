package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class AboutWindow extends Window {

	private static final int HEIGHT = 330;
	private static final int WIDTH = 400;
	private static final String TITLE = "About CTS2 Value Sets Viewer";

	protected Button i_okButton;

	public AboutWindow() {
		super();

		setWidth(WIDTH);
		setHeight(HEIGHT);
		setIsModal(true);
		setShowMinimizeButton(false);
		setTitle(TITLE);
		centerInPage();

		// Mayo image
		String logoImage = "logo-mc.gif";
		Img logoImg = new Img(logoImage, 197, 42);

		HLayout logoLayout = new HLayout();
		logoLayout.setWidth100();
		logoLayout.setHeight(43);
		logoLayout.setAlign(Alignment.LEFT);
		logoLayout.setMargin(10);
		logoLayout.addMember(logoImg);
		addItem(logoLayout);

		// main layout
		HLayout dataLayout = new HLayout();
		dataLayout.setWidth100();
		dataLayout.setHeight("*");
		dataLayout.setMargin(10);
		dataLayout.addMember(getDataContents());
		addItem(dataLayout);

		// Button bar at bottom
		addItem(getButtons());

		addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				destroy();
			}
		});
	}

	private Canvas getDataContents() {

		HTMLPane htmlpane = new HTMLPane();
		htmlpane.setWidth100();
		htmlpane.setHeight100();

		StringBuilder data = new StringBuilder();

		data.append("<b>CTS2 Value Sets Viewer</b> </br>");
		data.append("<hr>");
		data.append("<b>Version: 1.0 </b></br>");
		// data.append("<b>Built Using <a href=\"http://www.smartclient.com/\" \"target=\"_blank\">SmartGWT</a> </b></br>");
		data.append("<b>Built Using SmartGWT</b></br>");

		data.append("</br><b>Questions or Comments?  Email us at <a id=\"htpLinks\" href=\"mailto:cts2@informatics.mayo.edu\">cts2@informatics.mayo.edu</a></b></br>");

		/*
		 * data.append("<b>Credits: </b></br>");
		 * 
		 * data.append("<DIV style=\"color:sienna;margin-left:20px;\">");
		 * data.append("Scott Bauer </br>");
		 * data.append("Christopher Chute, M.D., Dr. P.H.</br>");
		 * data.append("Cory Endle </br>"); data.append("Kevin Peterson </br>");
		 * data.append("Sarah Ryan </br>"); data.append("Deepak Sharma </br>");
		 * data.append("Harold Solbrig </br>");
		 * data.append("Craig Stancl </br>"); data.append("Dale Suesse </br>");
		 * data.append("</DIV>");
		 */

		htmlpane.setContents(data.toString());
		return htmlpane;
	}

	private HLayout getButtons() {

		// Buttons on the bottom
		HLayout buttonLayout = new HLayout();
		buttonLayout.setWidth100();
		buttonLayout.setHeight(40);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(10);
		buttonLayout.setAlign(Alignment.RIGHT);

		i_okButton = new Button("Ok");
		i_okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		buttonLayout.addMember(i_okButton);

		return buttonLayout;
	}

}
