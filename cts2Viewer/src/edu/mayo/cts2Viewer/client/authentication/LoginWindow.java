package edu.mayo.cts2Viewer.client.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.Cts2Service;
import edu.mayo.cts2Viewer.client.Cts2ServiceAsync;
import edu.mayo.cts2Viewer.client.Cts2Viewer;
import edu.mayo.cts2Viewer.client.events.LoginCancelledEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEvent;
import edu.mayo.cts2Viewer.client.utils.ModalWindow;
import edu.mayo.cts2Viewer.shared.Credentials;

public class LoginWindow extends Window {

	private static final Logger logger = Logger.getLogger(LoginWindow.class.getName());
	private static final String TITLE = "Service Login";

	private static final int WIDTH = 350;
	private static final int HEIGHT = 100;

	private static final int WIDGET_WIDTH = 150;

	private final Label i_titleLabel;
	private final HTMLPane i_htmlPane;
	private DynamicForm i_form;
	private TextItem i_userIdItem;
	private PasswordItem i_passwordItem;
	private VLayout i_mainLayout;
	private final VLayout i_overallLayout;

	private final String i_server;

	public LoginWindow(String server) {
		super();

		setWidth(750);
		setHeight(530);

		// set a thinner window edge.
		setEdgeSize(4);

		setShowMinimizeButton(false);
		setIsModal(true);
		centerInPage();

		setTitle(TITLE);

		i_server = server;

		i_overallLayout = new VLayout();
		i_overallLayout.setWidth100();
		i_overallLayout.setHeight100();
		i_overallLayout.setBackgroundColor("#f4f4f4");

		i_titleLabel = createWindowTitle(i_server);
		i_overallLayout.addMember(i_titleLabel);

		i_htmlPane = createHTMLPane();
		i_overallLayout.addMember(i_htmlPane);

		createLoginForm();

		addItem(i_overallLayout);

		// if user clicks the "x" to close the window, cancel the login.
		addCloseClickHandler(new CloseClickHandler() {

			@Override
			public void onCloseClick(CloseClickEvent event) {
				cancelLogin();
				destroy();
			}
		});
	}

	private void createLoginForm() {
		TitleOrientation titleOrientation = TitleOrientation.LEFT;

		i_mainLayout = new VLayout();
		i_mainLayout.setAlign(Alignment.CENTER);

		
		// Add a message to the login IF we are using the default server.
		String registerMsg = "<center><b>Sign in using UMLS UTS Username/Password. If you do not have one, you may <a href=\"https://uts.nlm.nih.gov/license.html\" target=\"_blank\">register here</b></a>.</center>";

		if (i_server.equals(Cts2Viewer.DEFAULT_SERVER)) {
			Label registerLabel = new Label(registerMsg);
			registerLabel.setWidth100();
			registerLabel.setHeight(25);
			i_mainLayout.addMember(registerLabel);
		}
				
		HLayout formLayout = new HLayout(30);
		formLayout.setWidth100();
		formLayout.setHeight(HEIGHT);
		formLayout.setAlign(Alignment.CENTER);

		i_form = new DynamicForm();
		i_form.setMargin(10);
		i_form.setCellPadding(10);
		i_form.setWidth(WIDTH);
		i_form.setHeight(HEIGHT);
		i_form.setTitleOrientation(titleOrientation);
		i_form.setAutoFocus(true);
		i_form.setNumCols(2);
		i_form.setAlign(Alignment.CENTER);

		i_userIdItem = new TextItem("userId");
		i_userIdItem.setSelectOnFocus(true);
		i_userIdItem.setTitle("User Id");
		i_userIdItem.setWidth(WIDGET_WIDTH);
		i_userIdItem.setRequired(true);

		// if user clicks on the ENTER key, with this button in focus then have
		// this act as clicking the login button.
		i_userIdItem.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {

				if (event != null && event.getKeyName() != null && event.getKeyName().equals(KeyNames.ENTER)) {
					authenticateUser();
				}
			}
		});

		i_passwordItem = new PasswordItem("password");
		i_passwordItem.setTitle("Password");
		i_passwordItem.setWidth(WIDGET_WIDTH);
		i_passwordItem.setRequired(true);

		// if user clicks on the ENTER key, with this button in focus then have
		// this act as clicking the login button.
		i_passwordItem.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event != null && event.getKeyName() != null && event.getKeyName().equals(KeyNames.ENTER)) {
					authenticateUser();
				}
			}
		});

		i_form.setFields(new FormItem[] { i_userIdItem, i_passwordItem });
		formLayout.addMember(i_form);

		HLayout buttonLayout = new HLayout(20);
		buttonLayout.setWidth100();
		buttonLayout.setHeight(30);
		buttonLayout.setAlign(Alignment.CENTER);

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelLogin();
				destroy();
			}
		});

		IButton loginButton = new IButton("Login");
		loginButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				authenticateUser();
			}
		});

		buttonLayout.addMember(loginButton);
		buttonLayout.addMember(cancelButton);

		i_mainLayout.addMember(formLayout);
		i_mainLayout.addMember(buttonLayout);

		i_overallLayout.addMember(i_mainLayout);
	}

	/**
	 * Validate the the user entered a user id and pw
	 * 
	 * @return
	 */
	private boolean validateFields() {
		boolean valid = false;

		String userName = i_form.getValueAsString("userId");
		String pw = i_form.getValueAsString("password");

		valid = userName != null && userName.length() > 0 && pw != null && pw.length() > 0;
		if (!valid) {
			String message = "Please enter a user id and password.";
			SC.warn(message);
		}

		return valid;
	}

	/**
	 * Call server to authenticate the user
	 */
	private void authenticateUser() {

		if (!validateFields()) {
			return;
		}

		final String userName = i_form.getValueAsString("userId");
		String password = i_form.getValueAsString("password");

		final Credentials credentials = new Credentials();
		credentials.setPassword(password);
		credentials.setUser(userName);
		credentials.setServer(i_server);

		// Set the busy indicator to show while validating user credentials.
		final ModalWindow busyIndicator = new ModalWindow(i_mainLayout, 40, "#dedede");
		busyIndicator.setLoadingIcon("loading_circle.gif");
		busyIndicator.show("Validating Credentials...", true);

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		try {
			service.validateCredentials(credentials, new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean valid) {
					busyIndicator.hide();

					if (!valid) {
						// set the user id as selected.
						i_userIdItem.setSelectionRange(0, userName.length());

						String message = "Invalid Id/Password.  Please try again.";
						SC.warn(message);

					} else {
						// login was successful.
						Authentication.getInstance().addAuthenticatedCredential(credentials);
						Cts2Viewer.EVENT_BUS.fireEvent(new LoginSuccessfulEvent(credentials));
						destroy();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					busyIndicator.hide();

					String message = "Login failed - " + caught.getMessage();
					SC.warn(message);

				}
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to validate credentials for server " + userName + "\n" + e);
			SC.warn("Unable to validate credentials. " + e.toString());
		}
	}

	protected void cancelLogin() {
		// fire the login cancel event
		Cts2Viewer.EVENT_BUS.fireEvent(new LoginCancelledEvent());
	}

	private HTMLPane createHTMLPane() {
		HTMLPane pane = new HTMLPane();
		pane.setWidth100();
		pane.setHeight(300);
		pane.setMargin(5);
		pane.setBackgroundColor("#ffffff");

		// The name of the html file is the same as the server name.
		pane.setContentsURL("data/login/" + i_server + ".html");

		return pane;
	}

	private Label createWindowTitle(String server) {

		String titleServer = server; //server.equals(Cts2Viewer.DEFAULT_SERVER) ? "CTS2 Service" : server;

		Label windowTitleLabel = new Label("<b>Login for " + titleServer + "</b>");
		windowTitleLabel.setWidth100();
		windowTitleLabel.setHeight(25);
		windowTitleLabel.setAlign(Alignment.CENTER);
		windowTitleLabel.setValign(VerticalAlignment.CENTER);
		windowTitleLabel.setWrap(false);
		windowTitleLabel.setBackgroundColor("#efefef");

		return windowTitleLabel;
	}
}
