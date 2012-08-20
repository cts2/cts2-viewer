package edu.mayo.cts2Viewer.client;

import java.util.LinkedHashMap;
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
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.events.MouseUpEvent;
import com.smartgwt.client.widgets.events.MouseUpHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.datasource.ValueSetsXmlDS;
import edu.mayo.cts2Viewer.client.events.ValueSetsReceivedEvent;
import edu.mayo.cts2Viewer.client.events.ValueSetsReceivedEventHandler;
import edu.mayo.cts2Viewer.client.utils.UiHelper;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cts2Viewer implements EntryPoint {

	private static final String BACKGROUND_COLOR = "#d5d9de";

	private static final String ROWS_RETRIEVED_TITLE = "Rows Matching Criteria:";
	private static final String TITLE = "CTS2 - Value Sets";
	private static final String SELECT_SERVER_MSG = "<Select a Server>";

	private static Logger logger = Logger.getLogger(Cts2Viewer.class.getName());

	// Event Bus to capture global events and act upon them.
	public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

	private VLayout i_mainLayout;
	private ValueSetsListGrid i_valueSetsListGrid;
	private ValueSetInfoPanel i_valueSetInfoPanel;
	private SearchTextItem i_searchItem;
	private Label i_rowsRetrievedLabel;
	private ComboBoxItem i_serverCombo;

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		logger.log(Level.INFO, "init OnLoadModule().");

		Label titleLabel = UiHelper.createTitleLabel(TITLE);

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

		HLayout searchLayout = createSearchComponentLayout();
		contentLayout.addMember(searchLayout);

		i_rowsRetrievedLabel = new Label(ROWS_RETRIEVED_TITLE);
		i_rowsRetrievedLabel.setWidth100();
		i_rowsRetrievedLabel.setHeight(15);

		// i_rowsRetrievedLabel.hide();
		contentLayout.addMember(i_rowsRetrievedLabel);

		i_valueSetsListGrid = new ValueSetsListGrid();
		contentLayout.addMember(i_valueSetsListGrid);

		i_valueSetInfoPanel = new ValueSetInfoPanel();
		contentLayout.addMember(i_valueSetInfoPanel);

		// TODO CME make eventBus calls to update components.
		linkWidgets();

		i_mainLayout.addMember(contentLayout);

		createValueSetsReceivedEvent();

		// Add the main layout to the root panel
		RootLayoutPanel.get().add(i_mainLayout);

		initWindowClosingConfirmationDialog();
	}

    private DynamicForm i_downloadForm = null;
    private Label i_downloadLabel = null;
    private SelectItem i_exportTypeItem = null;
    
	private HLayout createSearchComponentLayout() {

		HLayout searchLayout = new HLayout();
		searchLayout.setWidth100();
		searchLayout.setHeight(30);
		searchLayout.setMembersMargin(20);

		// i_searchComboBoxItem = new SearchComboBoxItem();
		i_searchItem = new SearchTextItem();

		DynamicForm searchForm = new DynamicForm();
		searchForm.setItems(i_searchItem);
		searchLayout.addMember(searchForm);

		// add a button to clear the search form.
		IButton clearButton = new IButton("Clear");
		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				i_searchItem.clearValue();

				// i_valueSetsListGrid.getData("");
				getValueSets("");
				i_valueSetInfoPanel.clearPanels();
			}
		});

        final DynamicForm exportForm = new DynamicForm();  
        exportForm.setWidth(300);  
  
        i_exportTypeItem = new SelectItem("exportType", "Export Type");  
        i_exportTypeItem.setWidth("*");  
        i_exportTypeItem.setDefaultToFirstOption(true);  
  
        LinkedHashMap valueMap = new LinkedHashMap();  
        valueMap.put("csv", "CSV (Excel)");  
        valueMap.put("xml", "XML");  
        valueMap.put("json", "JSON");  
  
        i_exportTypeItem.setValueMap(valueMap);  
  
        BooleanItem showInWindowItem = new BooleanItem();  
        showInWindowItem.setName("showInWindow");  
        showInWindowItem.setTitle("Show in Window");  
        showInWindowItem.setAlign(Alignment.LEFT);  
  
        exportForm.setItems(i_exportTypeItem, showInWindowItem);  
        
        /* Hidden frame for callback */
        NamedFrame callbackFrame = new NamedFrame("downloadCallbackFrame");
        callbackFrame.setHeight("1px");
        callbackFrame.setWidth("1px");
        callbackFrame.setVisible(false);
        searchLayout.addMember(callbackFrame);

        
        i_downloadForm = new DynamicForm();
        i_downloadForm.setTarget("downloadCallbackFrame");
        i_downloadForm.setAction(GWT.getModuleBaseURL() + "cts2download");
        i_downloadForm.setEncoding(Encoding.NORMAL);
        i_downloadForm.setCanSubmit(true);

 		searchLayout.addMember(clearButton);
		searchLayout.addMember(exportForm);
		//searchLayout.addMember(dlButton);

        i_downloadLabel = createDownload();
        //i_fileInfoPanel.addMember(i_downloadLabel);

		searchLayout.addMember(i_downloadForm);
		searchLayout.addMember(i_downloadLabel);
		
		
		// fill up all of the extra space to push the search combo to the
		// right.
		HLayout spacerLayout = new HLayout();
		spacerLayout.setWidth("*");
		searchLayout.addMember(spacerLayout);

		// add the drop down to choose the server to connect to.
		searchLayout.addMember(createServerCombo());

		return searchLayout;
	}

    /**
     * create a download "button".
     * 
     * @return
     */
    private Label createDownload() {

        final Label label = new Label();
        label.setHeight(36);
        label.setWidth(210);
        label.setPadding(3);
        label.setAlign(Alignment.CENTER);
        label.setValign(VerticalAlignment.CENTER);
        label.setWrap(false);
        //label.setIcon("download.png");
        label.setIconHeight(32);
        label.setIconWidth(32);
        label.setShowEdges(true);
        label.setEdgeSize(1);
        label.setEdgeBackgroundColor("#6056b7");
        label.setContents("Download");
        label.setBackgroundColor("#f5f4fa");

        label.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                label.setBackgroundColor("#d7d5ec");
                label.setContents("<i>Download</i>");
            }
        });

        label.addMouseOutHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                label.setBackgroundColor("#f5f4fa");
                label.setContents("Download");
            }
        });

        label.addMouseDownHandler(new MouseDownHandler() {

            @Override
            public void onMouseDown(MouseDownEvent event) {
                label.setContents("<b><i>Download</i><b>");
            }
        });

        label.addMouseUpHandler(new MouseUpHandler() {

            @Override
            public void onMouseUp(MouseUpEvent event) {
                label.setContents("<i>Download</i>");
            }
        });
        label.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) 
            {
            	FormItem[] allItems = new FormItem[3];
            	
            	ListGridRecord selectedTypeRecord = i_exportTypeItem.getSelectedRecord();
            	String downloadType = selectedTypeRecord.getAttributeAsString("exportType");
            	
                HiddenItem downloadTypeItem = new HiddenItem("downloadType");
                downloadTypeItem.setValue(downloadType);

            	
                HiddenItem zipFileName = new HiddenItem("ZipFileName");
                zipFileName.setValue("CTS2ValueSetdownload");

            	ListGridRecord[] selected = i_valueSetsListGrid.getSelectedRecords();
            	if ((selected == null) || (selected.length == 0))
            	{
            		logger.log(Level.WARNING, "No ValueSet Selected!!");
            		return;
            	}
            	
            	String valueSets = "";
            	
            	for (ListGridRecord record : selected)
            	{
            		String value = record.getAttributeAsString("valueSetName");
            		if ("".equals(valueSets))
            			valueSets = value;
            		else
            			valueSets += "," + value;
            	}

            	if ("".equals(valueSets.trim()))
            	{
            		logger.log(Level.WARNING, "No ValueSet Names found in selected records!!");
            		return;
            	}
            	
                HiddenItem vsNames = new HiddenItem("valueSetNames");
                vsNames.setValue(valueSets);
                
                allItems[0] = zipFileName;
                allItems[1] = vsNames;
                allItems[2] = downloadTypeItem;
                
                i_downloadForm.setFields(allItems);

                i_downloadForm.submit();
            }
        });

        return label;
    }
    
	/**
	 * Create the server ComboBoxItem to select the server to connect to.
	 * 
	 * @return
	 */
	private DynamicForm createServerCombo() {

		final DynamicForm serverForm = new DynamicForm();
		serverForm.setWidth(300);
		serverForm.setAlign(Alignment.RIGHT);

		i_serverCombo = new ComboBoxItem();
		i_serverCombo.setDefaultToFirstOption(true);
		i_serverCombo.setTitle("<b>CTS2 Server</b>");
		i_serverCombo.setType("comboBox");
		i_serverCombo.setWidth(250);
		i_serverCombo.setWrapTitle(false);
		i_serverCombo.setAttribute("browserSpellCheck", false);

		i_serverCombo.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				i_valueSetInfoPanel.clearPanels();
				getValueSets(i_searchItem.getValueAsString());
			}
		});

		serverForm.setFields(i_serverCombo);
		serverForm.setAlign(Alignment.RIGHT);

		retrieveServerOptions();

		return serverForm;
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
				public void onSuccess(LinkedHashMap<String, String> result) 
				{
					String selected = null;
					
					for (String key : result.keySet())
					{
						if (selected == null)
						{
							selected = UiHelper.getSelected(result.get(key));
							if (selected != null)
								result.put(key, selected);
						}
					}
					
					i_serverCombo.setValueMap(result);
					
					if (selected != null)
					{
						i_serverCombo.setValue(selected);
						i_valueSetInfoPanel.clearPanels();
						getValueSets(null);
					}
					else
						i_serverCombo.setValue(SELECT_SERVER_MSG);
				}

			});
		} catch (Exception e) {
			SC.warn("Unable to retrieve servers to connect to.");
		}
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

					getValueSets(i_searchItem.getValueAsString());
					// i_valueSetsListGrid.getData(i_searchItem.getValueAsString());
				}
			}
		});

		// add a handler to determine when a user clicks on a record in the
		// valueSets listgrid
		i_valueSetsListGrid.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				String link = i_valueSetsListGrid.getSelectedRecord().getAttribute("href");
				String valueSetName = i_valueSetsListGrid.getSelectedRecord().getAttribute("valueSetName");

				i_valueSetInfoPanel.updatePanel(i_serverCombo.getValueAsString(), valueSetName, link);
			}
		});
	}

	/**
	 * Listen for the event that ValueSets were retrieved.
	 */
	private void createValueSetsReceivedEvent() {
		EVENT_BUS.addHandler(ValueSetsReceivedEvent.TYPE, new ValueSetsReceivedEventHandler() {

			@Override
			public void onValueSetsReceived(ValueSetsReceivedEvent event) {
				DataClass[] dc = ValueSetsXmlDS.getInstance().getTestData();

				if (dc.length >= 1) {
					String numEntries = dc[0].getAttribute("numEntries");
					String complete = dc[0].getAttribute("complete");

					i_rowsRetrievedLabel.setContents(ROWS_RETRIEVED_TITLE + " " + numEntries);
					if (complete != null && !complete.equals("COMPLETE")) {
						i_rowsRetrievedLabel.setContents(ROWS_RETRIEVED_TITLE + " " + numEntries + "+");
					} else {
						i_rowsRetrievedLabel.setContents(ROWS_RETRIEVED_TITLE + " " + numEntries);
					}

					String searchText = i_searchItem.getValueAsString();
					if (searchText != null && searchText.length() > 0) {

					} else {
						i_rowsRetrievedLabel.setContents("");
					}
				}
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
