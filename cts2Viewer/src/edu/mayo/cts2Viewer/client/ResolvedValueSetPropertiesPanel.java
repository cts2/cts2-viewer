package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.events.EntityChangedEvent;
import edu.mayo.cts2Viewer.client.events.EntityChangedEventHandler;
import edu.mayo.cts2Viewer.client.events.ResolvedValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.client.events.ResolvedValueSetInfoReceivedEventHandler;
import edu.mayo.cts2Viewer.client.utils.UiHelper;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;

/**
 * Layout to contain all the widgets for the Value Set information.
 * 
 */
public class ResolvedValueSetPropertiesPanel extends VLayout {

	private static final String TITLE_RESOLVED_VS_INFO = "Value Set Members";
	private static final int RVS_TITLE_WIDTH = 125;

	private ResolvedValueSetListGrid i_resolvedValueSetListGrid;

	private final ComboBoxItem i_serverCombo;

	// Resolved Value Set Information
	private Label i_valueSetDefinitionTitle;
	private Label i_valueSetDefinitionValue;

	private Label i_codeSystemVersionTitle;
	private Label i_codeSystemVersionValue;

	private Label i_codeSystemTitle;
	private Label i_codeSystemValue;

	private SectionStack i_resolvedValueSetSectionStack;
	private SectionStackSection i_sectionResolvedValueSetMembers;
	private SectionStackSection i_sectionResolvedValueSetInfo;

	public ResolvedValueSetPropertiesPanel(ComboBoxItem serverCombo) {
		super();

		i_serverCombo = serverCombo;
		init();
	}

	private void init() {
		setWidth("45%");
		setHeight100();
		setBackgroundColor("#ffffff");

		// create rounded edges to the layout
		UiHelper.createLayoutWithBorders(this);

		// layout for the resolved value set information
		VLayout resolvedVsLayout = new VLayout();
		resolvedVsLayout.setWidth100();
		resolvedVsLayout.setHeight(100);
		resolvedVsLayout.setMembersMargin(4);

		i_valueSetDefinitionTitle = UiHelper.createTitleLabel(RVS_TITLE_WIDTH, "Value Set Definition:");
		i_valueSetDefinitionValue = UiHelper.createValueLabel("");
		resolvedVsLayout
		        .addMember(UiHelper.createNameValueLayout(i_valueSetDefinitionTitle, i_valueSetDefinitionValue));

		i_codeSystemVersionTitle = UiHelper.createTitleLabel(RVS_TITLE_WIDTH, "Code System Name:");
		i_codeSystemVersionValue = UiHelper.createValueLabel("");
		resolvedVsLayout.addMember(UiHelper.createNameValueLayout(i_codeSystemVersionTitle, i_codeSystemVersionValue));

		i_codeSystemTitle = UiHelper.createTitleLabel(RVS_TITLE_WIDTH, "Code System:");
		i_codeSystemValue = UiHelper.createValueLabel("");
		resolvedVsLayout.addMember(UiHelper.createNameValueLayout(i_codeSystemTitle, i_codeSystemValue));

		// SectionStack for the collapsable sections - the resolved VS general
		// info and the members/entities
		i_resolvedValueSetSectionStack = UiHelper.createSectionStack();

		String resolvedValueSetsTitle = UiHelper.getSectionTitle(TITLE_RESOLVED_VS_INFO);

		i_sectionResolvedValueSetInfo = new SectionStackSection(resolvedValueSetsTitle);
		i_sectionResolvedValueSetInfo.setExpanded(false);
		i_sectionResolvedValueSetInfo.addItem(resolvedVsLayout);
		i_resolvedValueSetSectionStack.addSection(i_sectionResolvedValueSetInfo);

		i_resolvedValueSetListGrid = new ResolvedValueSetListGrid();
		i_sectionResolvedValueSetMembers = new SectionStackSection("");
		i_sectionResolvedValueSetMembers.setExpanded(true);
		i_sectionResolvedValueSetMembers.setCanCollapse(false);
		i_sectionResolvedValueSetMembers.addItem(i_resolvedValueSetListGrid);
		i_resolvedValueSetSectionStack.addSection(i_sectionResolvedValueSetMembers);

		addMember(i_resolvedValueSetSectionStack);

		addListGridRecordClickedHandler();
		createResolvedValueSetInfoReceivedEvent();
		createEntityChangedEvent();
	}

	public void updatePanel(String serviceName, String valueSetName, String formalName, String link) {
		// clear the Resolved Value Set info as the call to update this
		// information takes a few seconds.
		clearResolvedValueSetInfo();
		updateResolevedValueSetSectionTitle(formalName, valueSetName);

		i_resolvedValueSetListGrid.getData(serviceName, valueSetName);

	}

	/**
	 * Update the title of the resolved value set section with the value set
	 * selected.
	 * 
	 * @param formalName
	 * @param valueSetName
	 */
	private void updateResolevedValueSetSectionTitle(String formalName, String valueSetName) {
		String updatedTitle;

		if (formalName == null || formalName.length() == 0) {
			updatedTitle = UiHelper.getSectionTitle("");
		} else {
			updatedTitle = UiHelper.getSectionTitle(formalName + " (" + valueSetName + ")");
		}
		i_sectionResolvedValueSetMembers.setTitle(updatedTitle);
	}

	private void clearResolvedValueSetInfo() {
		i_resolvedValueSetListGrid.setData(new ListGridRecord[0]);

		// resolved value set info
		i_valueSetDefinitionValue.setContents("");
		i_codeSystemVersionValue.setContents("");
		i_codeSystemValue.setContents("");
		updateResolevedValueSetSectionTitle(null, null);
	}

	/**
	 * Find the next or previous record to select based on the users
	 * interaction.
	 * 
	 * @param direction
	 */
	public void selectRecord(int direction) {

		Record selectedRecord = i_resolvedValueSetListGrid.getSelectedRecord();

		String designation = selectedRecord.getAttribute("designation");
		String nameSpace = selectedRecord.getAttribute("nameSpace");
		String name = selectedRecord.getAttribute("name");

		ListGridRecord[] records = i_resolvedValueSetListGrid.getRecords();
		int i;
		for (i = 0; i < records.length; i++) {
			if (records[i].getAttribute("designation").equalsIgnoreCase(designation)
			        && records[i].getAttribute("nameSpace").equalsIgnoreCase(nameSpace)
			        && records[i].getAttribute("name").equalsIgnoreCase(name)) {
				break;
			}
		}

		i_resolvedValueSetListGrid.deselectRecord(i);

		// Determine which direction to move
		if (direction == EntityChangedEvent.PREVIOUS) {
			// Previous
			if (i == 0) {
				i_resolvedValueSetListGrid.selectRecord(records.length - 1);
			} else {
				i_resolvedValueSetListGrid.selectRecord(i - 1);
			}
		} else {
			// Next
			// check if we are at the end of the list
			if (i == records.length - 1) {
				i_resolvedValueSetListGrid.selectRecord(0);
			} else {
				i_resolvedValueSetListGrid.selectRecord(i + 1);
			}
		}

		showEntityInfo(i_resolvedValueSetListGrid.getSelectedRecord());

	}

	/**
	 * Clear out all of the ListGrids and panels
	 */
	public void clearPanels() {
		clearResolvedValueSetInfo();
	}

	/**
	 * Create a click handler for the Resolved Value Set List Grid.
	 */
	private void addListGridRecordClickedHandler() {
		i_resolvedValueSetListGrid.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				Record record = event.getRecord();
				showEntityInfo(record);
			}
		});

	}

	private void showEntityInfo(Record record) {
		if (record != null) {
			String href = record.getAttribute("href");
			String name = record.getAttribute("name");
			String description = record.getAttribute("designation");

			EntityWindow entityWindow = EntityWindow.getInstance();
			entityWindow.setWindowData(i_serverCombo.getValueAsString(), href, name, description);
			entityWindow.show();
		}
	}

	/**
	 * Listen for when a Resolved Value Set has been loaded.
	 */
	private void createResolvedValueSetInfoReceivedEvent() {
		Cts2Viewer.EVENT_BUS.addHandler(ResolvedValueSetInfoReceivedEvent.TYPE,
		        new ResolvedValueSetInfoReceivedEventHandler() {

			        @Override
			        public void onResolvedValueSetInfoReceived(ResolvedValueSetInfoReceivedEvent event) {
				        ResolvedValueSetInfo rvsi = event.getResolvedValueSetInfo();
				        if (rvsi != null) {

					        String valueSetDefinitionHref = rvsi.getValueSetDefinitionHref();
					        String valueSetDefinition = rvsi.getValueSetDefinition();
					        String codeSystemVersionHref = rvsi.getCodeSystemVersionHref();
					        String codeSystemVersion = rvsi.getCodeSystemVersion();
					        String codeSystemHref = rvsi.getCodeSystemHref();
					        String codeSystem = rvsi.getCodeSystem();

					        // if showAll=true, then we are in expert mode. Show
					        // the links. Otherwise, just show the information
							// without links.
					        if (Cts2Viewer.s_showAll) {
						        i_valueSetDefinitionValue.setContents(UiHelper.getLink(valueSetDefinitionHref,
						                valueSetDefinition));
						        i_codeSystemVersionValue.setContents(UiHelper.getLink(codeSystemVersionHref,
						                codeSystemVersion));
						        i_codeSystemValue.setContents(UiHelper.getLink(codeSystemHref, codeSystem));
					        } else {
						        i_valueSetDefinitionValue.setContents(valueSetDefinition);
						        i_codeSystemVersionValue.setContents(codeSystemVersion);
						        i_codeSystemValue.setContents(codeSystem);
					        }
				        }
			        }
		        });
	}

	private void createEntityChangedEvent() {

		Cts2Viewer.EVENT_BUS.addHandler(EntityChangedEvent.TYPE, new EntityChangedEventHandler() {

			@Override
			public void onEntityChanged(EntityChangedEvent event) {
				selectRecord(event.getChangeType());

			}
		});

	}

}
