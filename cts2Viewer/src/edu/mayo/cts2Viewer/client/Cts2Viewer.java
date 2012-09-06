package edu.mayo.cts2Viewer.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.authentication.LoginWindow;
import edu.mayo.cts2Viewer.client.events.LoginCancelledEvent;
import edu.mayo.cts2Viewer.client.events.LoginCancelledEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginRequestEvent;
import edu.mayo.cts2Viewer.client.events.LoginRequestEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEventHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cts2Viewer implements EntryPoint {

	private static Logger logger = Logger.getLogger(Cts2Viewer.class.getName());

	// Event Bus to capture global events and act upon them.
	public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

	private VLayout i_overallLayout;

	private ContextAreaPanel i_contextAreaPanel;
	private Cts2Panel i_cts2Panel;

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		logger.log(Level.INFO, "init onLoadModule().");

		i_contextAreaPanel = new ContextAreaPanel();
		i_cts2Panel = new Cts2Panel();

		// initialize the main layout container
		i_overallLayout = new VLayout();
		i_overallLayout.setWidth100();
		i_overallLayout.setHeight100();

		i_overallLayout.addMember(i_contextAreaPanel);
		createDownloadCallbackFrame();

		// Add the main layout to the root panel
		i_contextAreaPanel.setCurrentContextArea(i_cts2Panel);
		RootLayoutPanel.get().add(i_overallLayout);

		createLoginRequestEvent();
		createLoginCancelEvent();
		createLoginSuccessfulEvent();

		initWindowClosingConfirmationDialog();
	}

	private void showMainPage() {
		i_contextAreaPanel.setCurrentContextArea(i_cts2Panel);
	}

	private void showLogin(String server) {
		LoginWindow loginWindow = new LoginWindow(server);
		loginWindow.show();
	}

	/**
	 * Create a handler to listen for a login request.
	 */
	private void createLoginRequestEvent() {
		EVENT_BUS.addHandler(LoginRequestEvent.TYPE, new LoginRequestEventHandler() {

			@Override
			public void onLoginRequest(LoginRequestEvent loginRequestEvent) {
				String server = loginRequestEvent.getServer();
				showLogin(server);
			}
		});
	}

	/**
	 * Create a handler to listen for a login cancel request.
	 */
	private void createLoginCancelEvent() {
		EVENT_BUS.addHandler(LoginCancelledEvent.TYPE, new LoginCancelledEventHandler() {

			@Override
			public void onCancelRequest(LoginCancelledEvent loginCancelledEvent) {
				showMainPage();
			}
		});
	}

	/**
	 * Create a handler to listen for a login successful event.
	 */
	private void createLoginSuccessfulEvent() {
		EVENT_BUS.addHandler(LoginSuccessfulEvent.TYPE, new LoginSuccessfulEventHandler() {

			@Override
			public void onLoginSuccessful(LoginSuccessfulEvent loginSuccessfulEvent) {
				// i_contextAreaPanel.setCurrentContextArea(i_cts2Panel);
			}
		});
	}

	/**
	 * Hidden frame for the download callback
	 */
	private void createDownloadCallbackFrame() {

		NamedFrame callbackFrame = new NamedFrame("downloadCallbackFrame");
		callbackFrame.setHeight("1px");
		callbackFrame.setWidth("1px");
		callbackFrame.setVisible(false);
		i_overallLayout.addMember(callbackFrame);
	}

	/**
	 * Display a confirmation dialog to leave our site when the user refreshes
	 * or goes to another URL.
	 */
	protected void initWindowClosingConfirmationDialog() {
		Window.addWindowClosingHandler(new ClosingHandler() {
			@Override
			public void onWindowClosing(ClosingEvent event) {
				// This message doesn't show, but by adding this close handler,
				// we get the default dialog to display and confirm that the
				// user does want to leave our site.
				event.setMessage("Are you sure you want to leave?");
			}
		});
	}
}
