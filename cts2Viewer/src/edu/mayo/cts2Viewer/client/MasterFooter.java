package edu.mayo.cts2Viewer.client;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Footer panel for displaying widget at the bottom of the screen.
 */
public class MasterFooter extends HLayout {

	private static final int MASTER_FOOTER_HEIGHT = 30;
	private static final String HREF_LOGO = "http://informatics.mayo.edu/cts2/index.php/Value_Set_REST_API_and_Implementation_Examples";

	public MasterFooter() {
		super();

		// initialize the footer layout container
		this.addStyleName("cts2-MasterFooter");
		this.setHeight(MASTER_FOOTER_HEIGHT);

		String logoImage = "cts2Logo_ecosystem.png";
		final Img logoImg = new Img(logoImage, 184, 30);
		logoImg.setImageWidth(184);
		logoImg.setImageHeight(30);
		logoImg.addStyleName("cts2-MasterFooter-Logo");

		logoImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(HREF_LOGO, "_blank", "");
			}
		});

		logoImg.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				logoImg.setCursor(Cursor.HAND);
			}
		});
		logoImg.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				logoImg.setCursor(Cursor.AUTO);
			}
		});

		// initialize the East layout container
		HLayout eastLayout = new HLayout();
		eastLayout.setAlign(Alignment.RIGHT);
		eastLayout.setHeight(MASTER_FOOTER_HEIGHT);
		eastLayout.setWidth("50%");
		eastLayout.addMember(logoImg);

		addMember(eastLayout);
	}

}
