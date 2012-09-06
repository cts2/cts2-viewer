package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.authentication.Authentication;
import edu.mayo.cts2Viewer.client.authentication.LoginInfoPanel;
import edu.mayo.cts2Viewer.client.datasource.ValueSetsXmlDS;
import edu.mayo.cts2Viewer.client.events.LogOutRequestEvent;
import edu.mayo.cts2Viewer.client.events.LogOutRequestEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginCancelledEvent;
import edu.mayo.cts2Viewer.client.events.LoginCancelledEventHandler;
import edu.mayo.cts2Viewer.client.events.LoginRequestEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEvent;
import edu.mayo.cts2Viewer.client.events.LoginSuccessfulEventHandler;
import edu.mayo.cts2Viewer.client.events.ValueSetsReceivedEvent;
import edu.mayo.cts2Viewer.client.events.ValueSetsReceivedEventHandler;
import edu.mayo.cts2Viewer.client.utils.UiHelper;
import edu.mayo.cts2Viewer.shared.Credentials;

/*
 * Panel to hold data for the main area
 */
public class Cts2Panel extends VLayout {

	static Logger lgr = Logger.getLogger(Cts2Panel.class.getName());

	private static final String BACKGROUND_COLOR = "#eff0f2";

	private static final String ROWS_RETRIEVED_TITLE = "Rows Matching Criteria:";
	private static final String TITLE = "CTS2 Value Sets";
	private static final String TITLE_VS_INFO = "Value Set Properties";
	private static final String TITLE_SEARCH_RESULTS = "Search Results";

	public static final String SELECT_SERVER_MSG = "<Select a Server>";

	private ValueSetsListGrid i_valueSetsListGrid;
	private ValueSetPropertiesPanel i_valueSetPropertiesPanel;

	private ResolvedValueSetPropertiesPanel i_resolvedValueSetPropertiesPanel;
	private SearchTextItem i_searchItem;
	private IButton i_clearButton;
	private Label i_rowsRetrievedLabel;
	private ComboBoxItem i_serverCombo;

	private DownloadPanel i_downloadPanel;
	private String i_lastValidServer;

	private LoginInfoPanel i_loginInfoPanel;

	public Cts2Panel() {
		super();
		init();
	}

	private void init() {
		lgr.log(Level.INFO, "init Cts2Panel...");

		i_lastValidServer = SELECT_SERVER_MSG;

		Label titleLabel = UiHelper.createMainTitleLabel(TITLE);
		titleLabel = UiHelper.createLabelWithBorders(titleLabel);

		// main layout to hold all of the value set info
		setWidth100();
		setHeight100();
		setBackgroundColor(BACKGROUND_COLOR);

		// layout for main title
		HLayout titleLayout = new HLayout();
		titleLayout.setWidth100();
		titleLayout.setAlign(VerticalAlignment.TOP);
		titleLayout.setMargin(10);
		titleLayout.setBackgroundColor(BACKGROUND_COLOR);

		titleLayout.addMember(titleLabel);
		addMember(titleLayout);

		// layout for any content
		HLayout contentLayout = new HLayout();
		contentLayout.setWidth100();
		contentLayout.setHeight100();
		contentLayout.setAlign(VerticalAlignment.TOP);
		contentLayout.setMargin(10);
		contentLayout.setMembersMargin(15);
		contentLayout.setBackgroundColor(BACKGROUND_COLOR);

		// left side that contains the server, search and search results
		VLayout leftLayout = createLeftSideComponentsLayout();
		contentLayout.addMember(leftLayout);

		// right side that contains the value set and resolved value set
		i_resolvedValueSetPropertiesPanel = new ResolvedValueSetPropertiesPanel(i_serverCombo);
		contentLayout.addMember(i_resolvedValueSetPropertiesPanel);

		// TODO CME make eventBus calls to update components.
		linkWidgets();

		addMember(contentLayout);

		setClearButtonEnablement();
		createValueSetsReceivedEvent();
		createLoginSuccessfulEvent();
		createLoginCancelEvent();
		createLogOutEvent();
	}

	private VLayout createLeftSideComponentsLayout() {

		VLayout componentsLayout = new VLayout();
		componentsLayout.setWidth("55%");
		componentsLayout.setHeight100();
		componentsLayout.setMembersMargin(10);

		// add rounded borders to the layout.
		UiHelper.createLayoutWithBorders(componentsLayout);

		componentsLayout.setShowResizeBar(true);

		i_valueSetsListGrid = new ValueSetsListGrid();

		componentsLayout.addMember(createSearchWidgetLayout());

		i_downloadPanel = new DownloadPanel(i_serverCombo, i_valueSetsListGrid);
		i_downloadPanel.setWidgetsEnabled(false);

		i_rowsRetrievedLabel = new Label(ROWS_RETRIEVED_TITLE);
		i_rowsRetrievedLabel.setWidth100();
		i_rowsRetrievedLabel.setMargin(5);
		i_rowsRetrievedLabel.setHeight(20);

		// layout to hold the rows retrieved label and the download form.
		HLayout rowsRetrievedAndDownloadLayout = new HLayout();
		rowsRetrievedAndDownloadLayout.setWidth100();
		rowsRetrievedAndDownloadLayout.setHeight(20);

		rowsRetrievedAndDownloadLayout.addMember(i_rowsRetrievedLabel);
		rowsRetrievedAndDownloadLayout.addMember(i_downloadPanel);

		componentsLayout.addMember(rowsRetrievedAndDownloadLayout);

		// add the value set properties panel to the bottom
		i_valueSetPropertiesPanel = new ValueSetPropertiesPanel();

		SectionStack sectionStack = createSectionStack(i_valueSetsListGrid, i_valueSetPropertiesPanel);
		componentsLayout.addMember(sectionStack);

		return componentsLayout;
	}

	/**
	 * Create a SectionStack for the search results and collapsable value set
	 * section
	 * 
	 * @param valueSetsGrid
	 * @param panel
	 * @return
	 */
	private SectionStack createSectionStack(ValueSetsListGrid valueSetsGrid, ValueSetPropertiesPanel panel) {

		SectionStack sectionStack = UiHelper.createSectionStack();

		String searchResultsTitle = UiHelper.getSectionTitle(TITLE_SEARCH_RESULTS);

		SectionStackSection sectionSearchResults = new SectionStackSection(searchResultsTitle);
		sectionSearchResults.setExpanded(true);
		sectionSearchResults.addItem(valueSetsGrid);
		sectionStack.addSection(sectionSearchResults);

		String resolvedValueSetsTitle = UiHelper.getSectionTitle(TITLE_VS_INFO);

		SectionStackSection sectionValueSetInfo = new SectionStackSection(resolvedValueSetsTitle);
		sectionValueSetInfo.setExpanded(false);
		sectionValueSetInfo.addItem(panel);
		sectionStack.addSection(sectionValueSetInfo);

		return sectionStack;
	}

	private VLayout createSearchWidgetLayout() {

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight(50);

		// add rounded borders to the layout.
		UiHelper.createLayoutWithBorders(layout);

		// add the drop down to choose the server to connect to.
		i_serverCombo = createServerCombo();

		HLayout searchLayout = new HLayout();
		searchLayout.setWidth100();
		searchLayout.setHeight(60);
		searchLayout.setMembersMargin(5);
		searchLayout.setBackgroundColor("#f6faff");

		i_searchItem = new SearchTextItem();

		DynamicForm searchForm = new DynamicForm();
		searchForm.setItems(i_serverCombo, i_searchItem);
		searchForm.setHeight(55);
		searchLayout.addMember(searchForm);

		VLayout buttonLayout = new VLayout();
		buttonLayout.setHeight(55);
		buttonLayout.setMargin(4);
		buttonLayout.setAlign(VerticalAlignment.BOTTOM);

		i_loginInfoPanel = new LoginInfoPanel();
		buttonLayout.addMember(i_loginInfoPanel);

		// add a button to clear the search form.
		i_clearButton = new IButton("Clear");
		i_clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				i_searchItem.clearValue();
				i_clearButton.setDisabled(true);
				getValueSets("");
				i_valueSetPropertiesPanel.clearValueSetInfo();
				i_resolvedValueSetPropertiesPanel.clearPanels();
			}
		});
		buttonLayout.addMember(i_clearButton);
		searchLayout.addMember(buttonLayout);
		layout.addMember(searchLayout);

		return layout;
	}

	/**
	 * Create the server ComboBoxItem to select the server to connect to.
	 * 
	 * @return
	 */
	private ComboBoxItem createServerCombo() {
		i_serverCombo = new ComboBoxItem();
		i_serverCombo.setDefaultToFirstOption(true);
		i_serverCombo.setTitle("<b>CTS2 Server</b>");
		i_serverCombo.setType("comboBox");
		i_serverCombo.setWidth(300);
		i_serverCombo.setWrapTitle(false);
		i_serverCombo.setAttribute("browserSpellCheck", false);

		i_serverCombo.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				checkCredentials();
			}
		});

		retrieveServerOptions();
		return i_serverCombo;
	}

	private void updateServiceSelection() {
		i_valueSetPropertiesPanel.clearValueSetInfo();
		i_resolvedValueSetPropertiesPanel.clearPanels();
		getValueSets(i_searchItem.getValueAsString());
	}

	/**
	 * Update UI with the logged in user.
	 * 
	 * @param credentials
	 */
	private void updateWithLoggedInUser(Credentials credentials) {
		i_loginInfoPanel.setUser(credentials);

		// update the last valid server
		i_lastValidServer = i_serverCombo.getValueAsString();

		updateServiceSelection();
	}

	/**
	 * Check if credentials are needed for this server selection.
	 */
	protected void checkCredentials() {

		final String selectedServer = i_serverCombo.getValueAsString();

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		try {
			service.getCredentialsRequired(selectedServer, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// reset server selection
					i_serverCombo.setValue(i_lastValidServer);
					SC.warn("Unable to determine if server credentials are needed.");
				}

				@Override
				public void onSuccess(Boolean credentialsRequired) {
					if (credentialsRequired) {
						System.out.println("Credentials REQUIRED for server " + selectedServer);

						// Check if we have cached the credentials in
						// Authentication before asking user to login again.
						Credentials credentials = Authentication.getInstance().getCredentials(selectedServer);

						if (credentials != null) {
							updateWithLoggedInUser(credentials);

						} else {
							// fire the login event to show the login panel
							Cts2Viewer.EVENT_BUS.fireEvent(new LoginRequestEvent(selectedServer));

							// Listen for the login successful event and then
							// continue
						}

					} else {
						System.out.println("Credentials NOT REQUIRED for server " + selectedServer);

						// clear out the logged in user for this server
						i_loginInfoPanel.clearUser();

						// update the last valid server
						i_lastValidServer = i_serverCombo.getValueAsString();

						updateServiceSelection();
					}
				}
			});

		} catch (Exception e) {
			SC.warn("Unable to determine if server credentials are needed.");
		}
	}

	/**
	 * Create a handler to listen for a login cancel request.
	 */
	private void createLoginCancelEvent() {
		Cts2Viewer.EVENT_BUS.addHandler(LoginCancelledEvent.TYPE, new LoginCancelledEventHandler() {

			@Override
			public void onCancelRequest(LoginCancelledEvent loginCancelledEvent) {
				// reset the server selection
				i_serverCombo.setValue(i_lastValidServer);
			}
		});
	}

	/**
	 * Create a handler to listen for a login successful event.
	 */
	private void createLoginSuccessfulEvent() {
		Cts2Viewer.EVENT_BUS.addHandler(LoginSuccessfulEvent.TYPE, new LoginSuccessfulEventHandler() {

			@Override
			public void onLoginSuccessful(LoginSuccessfulEvent loginSuccessfulEvent) {

				Credentials credentials = loginSuccessfulEvent.getCredentials();
				Authentication.getInstance().addAuthenticatedCredential(credentials);

				updateWithLoggedInUser(credentials);
			}
		});
	}

	/**
	 * Create a handler to listen for a logout event.
	 */
	private void createLogOutEvent() {
		Cts2Viewer.EVENT_BUS.addHandler(LogOutRequestEvent.TYPE, new LogOutRequestEventHandler() {

			@Override
			public void onLogOutRequest(LogOutRequestEvent logOutRequestEvent) {
				Credentials credentials = logOutRequestEvent.getCredential();
				i_loginInfoPanel.clearUser();
				Authentication.getInstance().removeCredential(credentials.getServer());

				// reset the server selection and the server selection
				i_lastValidServer = SELECT_SERVER_MSG;
				i_serverCombo.setValue(i_lastValidServer);
				updateServiceSelection();
			}
		});
	}

	/**
	 * Listen for the event that ValueSets were retrieved.
	 */
	private void createValueSetsReceivedEvent() {
		Cts2Viewer.EVENT_BUS.addHandler(ValueSetsReceivedEvent.TYPE, new ValueSetsReceivedEventHandler() {

			@Override
			public void onValueSetsReceived(ValueSetsReceivedEvent event) {

				// initially disable the download because nothing will be
				// selected.
				i_downloadPanel.setWidgetsEnabled(false);

				DataClass[] dc = ValueSetsXmlDS.getInstance().getTestData();

				if (dc.length >= 1) {

					String numEntries = dc[0].getAttribute("numEntries");
					String complete = dc[0].getAttribute("complete");

					i_rowsRetrievedLabel.setContents(ROWS_RETRIEVED_TITLE + " <b>" + numEntries + "</b>");
					if (complete != null && !complete.equals("COMPLETE")) {
						i_rowsRetrievedLabel.setContents(ROWS_RETRIEVED_TITLE + "<b> " + numEntries + "</b>+");
					} else {
						i_rowsRetrievedLabel.setContents(ROWS_RETRIEVED_TITLE + " <b>" + numEntries + "</b>");
					}

					String searchText = i_searchItem.getValueAsString();
					if (searchText != null && searchText.length() > 0) {

					} else {
						i_rowsRetrievedLabel.setContents("");
					}
				} else {
					i_rowsRetrievedLabel.setContents(ROWS_RETRIEVED_TITLE + " <b>0</b>");
				}
			}
		});
	}

	protected void getValueSets(String searchText) {

		i_valueSetsListGrid.getData(i_serverCombo.getValueAsString(), searchText);
	}

	private void linkWidgets() {

		i_searchItem.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {

				event.getKeyName();

				// TODO CME - filter what characters are allowed. Don't allow
				// up/down arrows.
				// System.out.println(keyName);

				// ignore the arrow keys
				if (i_searchItem.isValidSearchText()) {
					setClearButtonEnablement();
					getValueSets(i_searchItem.getValueAsString());
				}
			}
		});

		// add a handler to determine when a user clicks on a record in the
		// valueSets listgrid
		i_valueSetsListGrid.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {

				Record record = event.getRecord();
				if (record != null) {
					String link = record.getAttribute("href");
					String valueSetName = record.getAttribute("valueSetName");

					i_valueSetPropertiesPanel.updatePanel(i_serverCombo.getValueAsString(), valueSetName, link);
					i_resolvedValueSetPropertiesPanel.updatePanel(i_serverCombo.getValueAsString(), valueSetName, link);
				}
			}
		});

		// handler for when the selection changes via the checkbox selection.
		i_valueSetsListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record[] selectedRecords = i_valueSetsListGrid.getSelectedRecords();
				if (selectedRecords != null && selectedRecords.length > 0) {
					i_downloadPanel.setWidgetsEnabled(true);
				} else {
					i_downloadPanel.setWidgetsEnabled(false);
				}
			}
		});
	}

	/**
	 * Make RPC call to retrieve the server(s) to connect to.
	 */
	private void retrieveServerOptions() {
		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		try {
			service.getAvailableServices(new AsyncCallback<LinkedHashMap<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					SC.warn("Unable to retrieve servers to connect to");
				}

				@Override
				public void onSuccess(LinkedHashMap<String, String> result) {
					String selected = null;

					for (String key : result.keySet()) {
						if (selected == null) {
							selected = UiHelper.getSelected(result.get(key));
							if (selected != null) {
								result.put(key, selected);
							}
						}
					}

					i_serverCombo.setValueMap(result);

					// Always show this message and let user select a server.
					i_serverCombo.setValue(SELECT_SERVER_MSG);
				}

			});
		} catch (Exception e) {
			SC.warn("Unable to retrieve servers to connect to.");
		}
	}

	/**
	 * The clear button should be disabled if the search text is empty.
	 */
	protected void setClearButtonEnablement() {
		String currentText = i_searchItem.getValueAsString();
		i_clearButton.setDisabled(currentText == null || currentText.length() == 0);
	}
}
