package edu.mayo.cts2Viewer.client;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Footer panel for displaying widget at the bottom of the screen.
 */
public class MasterFooter extends HLayout {

	private static final int MASTER_FOOTER_HEIGHT = 30;
	private static final String HREF_LOGO = "http://informatics.mayo.edu/cts2/index.php/Value_Set_REST_API_and_Implementation_Examples";
	private static final String HREF_NLM = "https://vsac.nlm.nih.gov";

	public MasterFooter() {
		super();

		// initialize the footer layout container
		this.setStyleName("cts2-MasterFooter");
		this.setHeight(MASTER_FOOTER_HEIGHT);

		int nlmWidth = 330;
		String nlmImage = "nlmData.png";
		final Img nlmImg = new Img(nlmImage, nlmWidth, 22);
		nlmImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(HREF_NLM, "_blank", "");
			}
		});

		nlmImg.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				nlmImg.setCursor(Cursor.HAND);
			}
		});
		nlmImg.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				nlmImg.setCursor(Cursor.AUTO);
			}
		});

		// initialize the West layout container
		HLayout westLayout = new HLayout();
		westLayout.setAlign(Alignment.LEFT);
		westLayout.setHeight(MASTER_FOOTER_HEIGHT);
		westLayout.setWidth("50%");

		// add this NLM image if the showAll variable is NOT passed in.
		if (!Cts2Viewer.s_showAll) {

			VLayout vlayout = new VLayout();
			vlayout.setWidth(nlmWidth);
			vlayout.setHeight(MASTER_FOOTER_HEIGHT);
			vlayout.setAlign(VerticalAlignment.CENTER);
			vlayout.addMember(nlmImg);
			westLayout.addMember(vlayout);
		}
		addMember(westLayout);

		String logoImage = "cts2Logo_ecosystem.png";
		final Img logoImg = new Img(logoImage, 248, 29);
		logoImg.setStyleName("cts2-MasterFooter-Logo");

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
