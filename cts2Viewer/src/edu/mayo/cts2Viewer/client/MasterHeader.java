package edu.mayo.cts2Viewer.client;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.authentication.LoginInfoForm;
import edu.mayo.cts2Viewer.client.events.LogOutRequestEvent;
import edu.mayo.cts2Viewer.client.events.LogOutRequestEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEventHandler;

/**
 * Master Header panel that appears on the top of the page.
 */
public class MasterHeader extends HLayout {

	private static final int MASTHEAD_HEIGHT = 58;
	private static final String MASTERHEADER_BACKGROUND = "#F5F5F3";
	private static final String HREF_LOGO = "http://mayoclinic.org";

	private final Label signedInUser;

	private final LoginInfoForm i_loginForm;

	public MasterHeader() {
		super();
		createLoginHandler();
		createLogoutHandler();

		setBackgroundColor(MASTERHEADER_BACKGROUND);

		// initialize the MasterHeader layout container
		this.setStyleName("cts2-MasterHeader");
		this.setHeight(MASTHEAD_HEIGHT);

		// initialize the title image - we will use two different images based
		// on whether or not the showAll parameter is set.
		String titleImage;
		final Img titleImg;
		int headerWidth;
		int headerheight;

		if (Cts2Viewer.s_showAll) {
			headerWidth = 213;
			headerheight = 35;
			titleImage = "headerTitleShort.png";
		} else {
			headerWidth = 569;
			headerheight = 35;
			titleImage = "headerTitleLong.png";
		}

		titleImg = new Img(titleImage, headerWidth, headerheight);

		// initialize the Logo image
		String logoImage = "logo-mc.gif";
		final Img logoImg = new Img(logoImage, 197, 42);
		logoImg.setStyleName("cts2-MasterHeader-Logo");

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

		// initialize the West layout container
		HLayout westLayout = new HLayout();
		westLayout.setHeight(MASTHEAD_HEIGHT);
		westLayout.setWidth("30%");
		westLayout.setMargin(8);
		westLayout.addMember(logoImg);

		// westLayout.addMember(name);

		// initialize the Center layout container
		HLayout centerLayout = new HLayout();
		centerLayout.setAlign(Alignment.CENTER);
		centerLayout.setHeight(MASTHEAD_HEIGHT);
		centerLayout.setWidth("*");

		VLayout centerVLayout = new VLayout();
		centerVLayout.setHeight100();
		centerVLayout.setWidth(headerWidth);
		centerVLayout.setAlign(VerticalAlignment.CENTER);
		centerVLayout.setAlign(Alignment.CENTER);
		centerVLayout.addMember(titleImg);

		centerLayout.addMember(centerVLayout);

		// initialize the Signed In User label
		signedInUser = new Label();
		signedInUser.setStyleName("cts2-MasterHeader-SignedInUser");

		i_loginForm = new LoginInfoForm();

		// initialize the East layout container
		HLayout eastLayout = new HLayout();
		eastLayout.setAlign(Alignment.RIGHT);
		eastLayout.setHeight(MASTHEAD_HEIGHT);
		eastLayout.setWidth("30%");
		// eastLayout.addMember(signedInUser);
		eastLayout.addMember(i_loginForm);

		// add the West and East layout containers to the MasterHeader layout
		// container
		this.addMember(westLayout);
		this.addMember(centerVLayout);
		this.addMember(eastLayout);
	}

	private void createLogoutHandler() {
		Cts2Viewer.EVENT_BUS.addHandler(LogOutRequestEvent.TYPE, new LogOutRequestEventHandler() {
			@Override
			public void onLogOutRequest(LogOutRequestEvent logOutRequestEvent) {
				signedInUser.setContents("");
			}
		});
	}

	private void createLoginHandler() {
		Cts2Viewer.EVENT_BUS.addHandler(LoginSuccessfulEvent.TYPE, new LoginSuccessfulEventHandler() {
			@Override
			public void onLoginSuccessful(LoginSuccessfulEvent loginSuccessfulEvent) {
				signedInUser.setContents("Logged in as <b>" + loginSuccessfulEvent.getCredentials().getUser() + "</b>");
			}
		});
	}
}
