package edu.mayo.cts2Viewer.client.authentication;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.Cts2Viewer;
import edu.mayo.cts2Viewer.client.events.DefaultServerRetrievedEvent;
import edu.mayo.cts2Viewer.client.events.DefaultServerRetrievedEventHandler;
import edu.mayo.cts2Viewer.client.events.LogOutRequestEvent;
import edu.mayo.cts2Viewer.client.events.LogOutRequestEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginRequestEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEventHandler;
import edu.mayo.cts2Viewer.shared.Credentials;

/**
 * VLayout that contains a DynamicForm for holding the login/logout components.
 */
public class LoginInfoForm extends VLayout {

	private static final int WIDTH = 150;
	private static final String TEXT_LOG_IN = "Log In";
	private static final String TEXT_LOG_OUT = "Log Out";

	private String i_defaultServer;
	private Credentials i_credentials;

	private LinkItem i_loginLinkItem;
	private LinkItem i_logoutLinkItem;
	private StaticTextItem i_loggedInUserStaticText;
	private StaticTextItem i_seperatorLinkItem;

	private DynamicForm i_form;

	public LoginInfoForm() {
		super();

		setWidth(WIDTH);
		setHeight100();
		setMembersMargin(15);
		setMargin(10);
		setAlign(VerticalAlignment.BOTTOM);

		createLoginForm(null);

		createLoginHandler();
		createLogoutHandler();
		createDefaultServerRetrievedEventHandler();
	}

	private void createLoginForm(String user) {

		i_form = new DynamicForm();

		i_form.setLayoutAlign(Alignment.RIGHT);
		i_form.setWidth100();
		i_form.setHeight(20);

		i_loginLinkItem = new LinkItem("loginLinkItem");
		i_loginLinkItem.setLinkTitle("<b>" + TEXT_LOG_IN + "</b>");
		i_loginLinkItem.setShowTitle(false);
		i_loginLinkItem.setWidth(47);
		i_loginLinkItem.setVAlign(VerticalAlignment.BOTTOM);
		i_loginLinkItem.setAlign(Alignment.RIGHT);

		i_loginLinkItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				// Fire event to let listeners know that that a user is
				// requesting to log in
				Cts2Viewer.EVENT_BUS.fireEvent(new LoginRequestEvent(i_defaultServer));
			}
		});

		i_logoutLinkItem = new LinkItem("logoutLinkItem");
		i_logoutLinkItem.setLinkTitle("<b>" + TEXT_LOG_OUT + "</b>");
		i_logoutLinkItem.setShowTitle(false);
		i_logoutLinkItem.setWidth(47);
		i_logoutLinkItem.setAlign(Alignment.RIGHT);
		i_logoutLinkItem.setVAlign(VerticalAlignment.CENTER);

		i_logoutLinkItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				// Fire a logout event
				Cts2Viewer.EVENT_BUS.fireEvent(new LogOutRequestEvent(i_credentials));
			}
		});

		i_seperatorLinkItem = new StaticTextItem();
		i_seperatorLinkItem.setShowTitle(false);
		i_seperatorLinkItem.setWidth(10);
		i_seperatorLinkItem.setValue("|");
		i_seperatorLinkItem.setAlign(Alignment.CENTER);
		i_seperatorLinkItem.setVAlign(VerticalAlignment.TOP);

		i_loggedInUserStaticText = new StaticTextItem();
		i_loggedInUserStaticText.setShowTitle(false);
		i_loggedInUserStaticText.setAlign(Alignment.RIGHT);
		i_loggedInUserStaticText.setVAlign(VerticalAlignment.TOP);

		if (user != null) {
			i_loggedInUserStaticText.setValue("<b>" + user + "</b>");
			i_form.setNumCols(3);
			i_form.setFields(i_loggedInUserStaticText, i_seperatorLinkItem, i_logoutLinkItem);
		} else {
			i_loggedInUserStaticText.setValue("");
			i_form.setNumCols(1);
			i_form.setFields(i_loginLinkItem);
		}

		addMember(i_form);
	}

	/**
	 * After login, set the form with the user and add the logout link.
	 */
	protected void setFormForLoggedInUser() {
		removeMember(i_form);
		createLoginForm(i_credentials.getUser());
		redraw();
	}

	/**
	 * After logout, clear the form.
	 */
	protected void clearForm() {
		removeMember(i_form);
		createLoginForm(null);
	}

	private void createLogoutHandler() {
		Cts2Viewer.EVENT_BUS.addHandler(LogOutRequestEvent.TYPE, new LogOutRequestEventHandler() {
			@Override
			public void onLogOutRequest(LogOutRequestEvent logOutRequestEvent) {
				clearForm();
			}
		});
	}

	private void createLoginHandler() {
		Cts2Viewer.EVENT_BUS.addHandler(LoginSuccessfulEvent.TYPE, new LoginSuccessfulEventHandler() {
			@Override
			public void onLoginSuccessful(LoginSuccessfulEvent loginSuccessfulEvent) {
				i_credentials = loginSuccessfulEvent.getCredentials();
				setFormForLoggedInUser();
			}
		});
	}

	private void createDefaultServerRetrievedEventHandler() {

		Cts2Viewer.EVENT_BUS.addHandler(DefaultServerRetrievedEvent.TYPE, new DefaultServerRetrievedEventHandler() {

			@Override
			public void onServerRetrieved(DefaultServerRetrievedEvent defaultServerRetrievedEvent) {
				i_defaultServer = defaultServerRetrievedEvent.getServer();

			}
		});
	}

}
