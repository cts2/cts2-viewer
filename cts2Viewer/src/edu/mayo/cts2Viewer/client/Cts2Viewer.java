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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.utils.ModalWindow;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cts2Viewer implements EntryPoint {

	private static final String BACKGROUND_COLOR = "#d5d9de";
	private static final String TITLE = "CTS2 - Value Sets";

	private static Logger logger = Logger.getLogger(Cts2Viewer.class.getName());

	// Event Bus to capture global events and act upon them.
	public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

	private VLayout i_mainLayout;
	private ValueSetsListGrid i_valueSetsListGrid;
	private SearchComboBoxItem i_searchComboBoxItem;

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		logger.log(Level.INFO, "init OnLoadModule().");

		Label titleLabel = new Label();
		titleLabel.setWidth100();
		titleLabel.setHeight(50);
		titleLabel.setAlign(Alignment.CENTER);

		String title = "<p style=\"color: #51524e;font-family: Arial,Helvetica,sans-serif;font-size: 24px;font-weight:bold;text-decoration:none\">"
		        + TITLE + "</p>";

		titleLabel.setContents(title);

		// overall layout
		i_mainLayout = new VLayout();
		i_mainLayout.setWidth100();
		i_mainLayout.setHeight100();
		i_mainLayout.setBackgroundColor(BACKGROUND_COLOR);

		// add the label to the main layout at the top
		i_mainLayout.addMember(titleLabel);

		// layout for any content
		VLayout contentLayout = new VLayout();
		contentLayout.setWidth100();
		contentLayout.setHeight100();
		contentLayout.setAlign(VerticalAlignment.TOP);
		contentLayout.setMargin(10);
		contentLayout.setMembersMargin(10);

		IButton button = new IButton("test");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SC.say("button clicked.");
				getValueSets();
			}
		});

		HLayout searchLayout = createSearchComponents();
		contentLayout.addMember(searchLayout);

		i_valueSetsListGrid = new ValueSetsListGrid();
		contentLayout.addMember(i_valueSetsListGrid);

		// contentLayout.addMember(button);
		i_mainLayout.addMember(contentLayout);

		// Add the main layout to the root panel
		RootLayoutPanel.get().add(i_mainLayout);

		initWindowClosingConfirmationDialog();
	}

	private HLayout createSearchComponents() {

		HLayout searchLayout = new HLayout();
		searchLayout.setWidth100();
		searchLayout.setHeight(50);
		searchLayout.setBackgroundColor("white");

		// SearchComboBoxItem i_searchComboBoxItem = new SearchComboBoxItem();

		// DynamicForm searchForm = new DynamicForm();
		// searchForm.setItems(i_searchComboBoxItem);
		// searchLayout.addMember(searchForm);

		return searchLayout;
	}

	private void getValueSets() {
		logger.log(Level.INFO, "calling getValueSets()");

		// Set the busy indicator to show while retrieving data
		final ModalWindow busyIndicator = new ModalWindow(i_mainLayout, 40, "#dedede");
		busyIndicator.setLoadingIcon("loading_circle.gif");
		busyIndicator.show("Retrieving Value Sets...", true);

		// make RPC call
		Cts2ServiceAsync cts2Service = GWT.create(Cts2Service.class);
		cts2Service.getValueSets("some string", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				busyIndicator.hide();
				SC.say("Return from server - " + result);
			}

			@Override
			public void onFailure(Throwable caught) {
				busyIndicator.hide();
				SC.say("ERROR " + caught.toString());
			}
		});
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
