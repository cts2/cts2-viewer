package edu.mayo.cts2Viewer.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.authentication.LoginWindow;
import edu.mayo.cts2Viewer.client.events.LoginCancelledEvent;
import edu.mayo.cts2Viewer.client.events.LoginCancelledEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginRequestEvent;
import edu.mayo.cts2Viewer.client.events.LoginRequestEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEventHandler;
import edu.mayo.cts2Viewer.client.events.PanelChangeEvent;
import edu.mayo.cts2Viewer.client.events.PanelChangeEventHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cts2Viewer implements EntryPoint {

	private static final int NORTH_HEIGHT = 85;
	private static Logger logger = Logger.getLogger(Cts2Viewer.class.getName());
	private static final String SHOW_ALL = "showAll";

	// Event Bus to capture global events and act upon them.
	public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

	public static boolean s_showAll = false;

	// keep track if this is the first time the cts2 panel is being 
	// displayed.  This will help determine if the login should pop up.
	private boolean i_firstTimeToDisplay = true;
	
	private VLayout i_overallLayout;
	private HLayout i_northLayout;

	private Cts2ToolStrip i_toolstrip;

	private ContextAreaPanel i_contextAreaPanel;
	private Cts2Panel i_cts2Panel;
	private WelcomePanel i_welcomePanel;

	// include our own css
	interface GlobalResources extends ClientBundle {
		@NotStrict
		@Source("Cts2Viewer.css")
		CssResource css();
	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		logger.log(Level.INFO, "init onLoadModule().");

		// inject global styles
		GWT.<GlobalResources> create(GlobalResources.class).css().ensureInjected();

		// get rid of scroll bars, and clear out the window's built-in margin,
		// because we want to take advantage of the entire client area
		Window.enableScrolling(false);
		Window.setMargin("0px");

		checkForInputParameters();

		i_contextAreaPanel = new ContextAreaPanel();
		i_cts2Panel = new Cts2Panel();

		i_welcomePanel = new WelcomePanel();

		// initialize the main layout container
		i_overallLayout = new VLayout();
		i_overallLayout.setWidth100();
		i_overallLayout.setHeight100();

		// initialize the North layout container
		i_northLayout = new HLayout();
		i_northLayout.setHeight(NORTH_HEIGHT);

		VLayout vLayout = new VLayout();
		// add the MasterHeader to the nested layout container
		vLayout.addMember(new MasterHeader());

		i_toolstrip = new Cts2ToolStrip();
		// add the Menu to the nested layout container
		vLayout.addMember(i_toolstrip);

		// add the nested layout container to the North layout container
		i_northLayout.addMember(vLayout);
		i_overallLayout.addMember(i_northLayout);
		i_overallLayout.addMember(i_contextAreaPanel);

		// add the MasterFooter to the bottom
		i_overallLayout.addMember(new MasterFooter());

		createDownloadCallbackFrame();

		// Add the main layout to the root panel
		i_contextAreaPanel.setCurrentContextArea(i_welcomePanel);
		RootLayoutPanel.get().add(i_overallLayout);

		createLoginRequestEvent();
		createLoginCancelEvent();
		createLoginSuccessfulEvent();
		createPanelChangeEvent();
	}

	/**
	 * See if any parameters were sent in on the URL
	 */
	private void checkForInputParameters() {

		String showAll = Window.Location.getParameter(SHOW_ALL);
		if (showAll != null && showAll.equalsIgnoreCase("true")) {
			s_showAll = true;
		} else {
			s_showAll = false;
		}
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
				// Make sure the cts2 viewer panel is set
				i_contextAreaPanel.setCurrentContextArea(i_cts2Panel);

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

				// Don't switch the page. We now have the main page and the view
				// page.
				// they can stay on the current page they are on if they cancel
				// showMainPage();
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
	 * Create a handler to listen for a panel Change request.
	 */
	private void createPanelChangeEvent() {
		EVENT_BUS.addHandler(PanelChangeEvent.TYPE, new PanelChangeEventHandler() {

			@Override
			public void onPanelChanged(PanelChangeEvent event) {
				switch (event.getChangeType()) {
				case VALUESET:
				{
					i_contextAreaPanel.setCurrentContextArea(i_cts2Panel);
					
					// automatically pop up the login window if there is only
					// one server (showAll == false) and this is the first time on this page.
					if(!s_showAll && i_firstTimeToDisplay) {
						i_firstTimeToDisplay = false;
						Cts2Viewer.EVENT_BUS.fireEvent(new LoginRequestEvent(i_cts2Panel.getDefaultServer()));
					}
					
					break;
				}
				case WELCOME:
					i_contextAreaPanel.setCurrentContextArea(i_welcomePanel);
				}

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

}
