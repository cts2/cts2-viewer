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
	private static final String TITLE_RESOLVED_VS_MEMBERS = "Entities";
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
		SectionStack resolvedValueSetSectionStack = UiHelper.createSectionStack();

		String resolvedValueSetsTitle = UiHelper.getSectionTitle(TITLE_RESOLVED_VS_INFO);

		SectionStackSection sectionResolvedValueSetInfo = new SectionStackSection(resolvedValueSetsTitle);
		sectionResolvedValueSetInfo.setExpanded(false);
		sectionResolvedValueSetInfo.addItem(resolvedVsLayout);
		resolvedValueSetSectionStack.addSection(sectionResolvedValueSetInfo);

		String membersTitle = UiHelper.getSectionTitle(TITLE_RESOLVED_VS_MEMBERS);

		i_resolvedValueSetListGrid = new ResolvedValueSetListGrid();
		SectionStackSection sectionResolvedValueSetMembers = new SectionStackSection(membersTitle);
		sectionResolvedValueSetMembers.setExpanded(true);
		sectionResolvedValueSetMembers.setCanCollapse(false);
		sectionResolvedValueSetMembers.addItem(i_resolvedValueSetListGrid);
		resolvedValueSetSectionStack.addSection(sectionResolvedValueSetMembers);

		addMember(resolvedValueSetSectionStack);

		addListGridRecordClickedHandler();
		createResolvedValueSetInfoReceivedEvent();
	}

	public void updatePanel(String serviceName, String valueSet, String link) {
		// clear the Resolved Value Set info as the call to update this
		// information takes a few seconds.
		clearResolvedValueSetInfo();
		i_resolvedValueSetListGrid.getData(serviceName, valueSet);
	}

	private void clearResolvedValueSetInfo() {
		i_resolvedValueSetListGrid.setData(new ListGridRecord[0]);

		// resolved value set info
		i_valueSetDefinitionValue.setContents("");
		i_codeSystemVersionValue.setContents("");
		i_codeSystemValue.setContents("");
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
				if (record != null) {
					String href = record.getAttribute("href");
					String name = record.getAttribute("name");
					String description = record.getAttribute("designation");

					EntityWindow entityWindow = EntityWindow.getInstance(i_serverCombo.getValueAsString(), href, name,
					        description);
					entityWindow.show();
				}
			}
		});

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

					        i_valueSetDefinitionValue.setContents(UiHelper.getLink(valueSetDefinitionHref,
					                valueSetDefinition));
					        i_codeSystemVersionValue.setContents(UiHelper.getLink(codeSystemVersionHref,
					                codeSystemVersion));
					        i_codeSystemValue.setContents(UiHelper.getLink(codeSystemHref, codeSystem));

				        }
			        }
		        });
	}

}
