package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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

	private static final int RVS_TITLE_WIDTH = 125;

	// private VLayout i_resolvedValueSetInfoPanel;
	private ResolvedValueSetListGrid i_resolvedValueSetListGrid;

	// Resolved Value Set Information
	private Label i_valueSetDefinitionTitle;
	private Label i_valueSetDefinitionValue;

	private Label i_codeSystemVersionTitle;
	private Label i_codeSystemVersionValue;

	private Label i_codeSystemTitle;
	private Label i_codeSystemValue;

	public ResolvedValueSetPropertiesPanel() {
		super();
		init();
	}

	private void init() {
		setWidth("45%");
		setHeight100();
		setShowEdges(true);
		setBackgroundColor("#ffffff");

		Label resolvedValueSetLabel = UiHelper.createTitleLabel(TITLE_RESOLVED_VS_INFO);
		addMember(resolvedValueSetLabel);

		VLayout resolvedVsLayout = new VLayout();
		resolvedVsLayout.setWidth100();
		resolvedVsLayout.setHeight(150);
		resolvedVsLayout.setMembersMargin(4);

		i_valueSetDefinitionTitle = UiHelper.createTitleLabel(RVS_TITLE_WIDTH, "Value Set Definition:");
		i_valueSetDefinitionValue = UiHelper.createValueLabel("");
		resolvedVsLayout
		        .addMember(UiHelper.createNameValueLayout(i_valueSetDefinitionTitle, i_valueSetDefinitionValue));

		i_codeSystemVersionTitle = UiHelper.createTitleLabel(RVS_TITLE_WIDTH, "Code System Version:");
		i_codeSystemVersionValue = UiHelper.createValueLabel("");
		resolvedVsLayout.addMember(UiHelper.createNameValueLayout(i_codeSystemVersionTitle, i_codeSystemVersionValue));

		i_codeSystemTitle = UiHelper.createTitleLabel(RVS_TITLE_WIDTH, "Code System:");
		i_codeSystemValue = UiHelper.createValueLabel("");
		resolvedVsLayout.addMember(UiHelper.createNameValueLayout(i_codeSystemTitle, i_codeSystemValue));

		addMember(resolvedVsLayout);

		Label propertiesLabel = new Label("<b>Value Set Members</b>");
		propertiesLabel.setWidth100();
		propertiesLabel.setHeight(20);
		propertiesLabel.setAlign(Alignment.CENTER);
		propertiesLabel.setBackgroundColor("#dedad5");

		addMember(propertiesLabel);

		i_resolvedValueSetListGrid = new ResolvedValueSetListGrid();
		addMember(i_resolvedValueSetListGrid);

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
