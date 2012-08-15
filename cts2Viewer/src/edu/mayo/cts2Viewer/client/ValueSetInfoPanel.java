package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.events.ResolvedValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.client.events.ResolvedValueSetInfoReceivedEventHandler;
import edu.mayo.cts2Viewer.client.events.ValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.client.events.ValueSetInfoReceivedEventHandler;
import edu.mayo.cts2Viewer.client.utils.UiHelper;
import edu.mayo.cts2Viewer.shared.ResolvedValueSetInfo;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

/**
 * Layout to contain all the widgets for the Value Set information.
 * 
 */
public class ValueSetInfoPanel extends HLayout {

	private static final String TITLE_VS_INFO = "Value Set Information";
	private static final String TITLE_RESOLVED_VS_INFO = "Resolved Value Set Information";

	private static final int VS_TITLE_WIDTH = 100;
	private static final int RVS_TITLE_WIDTH = 125;

	private VLayout i_valueSetInfoPanel;
	private ValueSetInfoListGrid i_valueSetInfoListGrid;

	private VLayout i_resolvedValueSetInfoPanel;
	private ResolvedValueSetListGrid i_resolvedValueSetListGrid;

	// Value Set Information
	private Label i_stateTitle;
	private Label i_stateValue;

	private Label i_resourceNameTitle;
	private Label i_resourceNameValue;

	private Label i_formalNameTitle;
	private Label i_formalNameValue;

	private Label i_descriptionTitle;
	private Label i_descriptionValue;

	private Label i_contactTitle;
	private Label i_contactValue;

	// Resolved Value Set Information
	private Label i_valueSetDefinitionTitle;
	private Label i_valueSetDefinitionValue;

	private Label i_codeSystemVersionTitle;
	private Label i_codeSystemVersionValue;

	private Label i_codeSystemTitle;
	private Label i_codeSystemValue;

	public ValueSetInfoPanel() {
		super();

		init();
	}

	private void init() {

		setWidth100();
		setHeight(400);
		setShowEdges(true);

		// layout for data on the left side
		i_valueSetInfoPanel = createValueSetInfoPanel();

		// layout for resolved value set information (right side)
		i_resolvedValueSetInfoPanel = createResolvedValueSetInfoPanel();

		addMember(i_valueSetInfoPanel);
		addMember(createSpacerPanel());
		addMember(i_resolvedValueSetInfoPanel);

		createValueSetInfoReceivedEvent();
		createResolvedValueSetInfoReceivedEvent();
	}

	private HLayout createSpacerPanel() {
		HLayout spacerLayout = new HLayout();
		spacerLayout.setWidth(10);
		spacerLayout.setHeight("100%");

		return spacerLayout;
	}

	private VLayout createValueSetInfoPanel() {
		VLayout layout = new VLayout();
		// layout.setWidth(400);
		layout.setWidth("50%");
		layout.setHeight100();
		layout.setBackgroundColor("#ffffff");

		Label valueSetLabel = UiHelper.createTitleLabel(TITLE_VS_INFO);
		layout.addMember(valueSetLabel);

		VLayout vsLayout = new VLayout();
		vsLayout.setWidth100();
		vsLayout.setHeight(150);
		vsLayout.setMembersMargin(4);

		i_resourceNameTitle = createTitleLabel(VS_TITLE_WIDTH, "Resource Name:");
		i_resourceNameValue = createValueLabel("");
		vsLayout.addMember(createNameValueLayout(i_resourceNameTitle, i_resourceNameValue));

		i_formalNameTitle = createTitleLabel(VS_TITLE_WIDTH, "Formal Name:");
		i_formalNameValue = createValueLabel("");
		vsLayout.addMember(createNameValueLayout(i_formalNameTitle, i_formalNameValue));

		i_descriptionTitle = createTitleLabel(VS_TITLE_WIDTH, "Description:");
		i_descriptionValue = createValueLabel("");
		vsLayout.addMember(createNameValueLayout(i_descriptionTitle, i_descriptionValue));

		i_stateTitle = createTitleLabel(VS_TITLE_WIDTH, "State:");
		i_stateValue = createValueLabel("");
		vsLayout.addMember(createNameValueLayout(i_stateTitle, i_stateValue));

		i_contactTitle = createTitleLabel(VS_TITLE_WIDTH, "Contact:");
		i_contactValue = createValueLabel("");
		vsLayout.addMember(createNameValueLayout(i_contactTitle, i_contactValue));

		layout.addMember(vsLayout);

		Label propertiesLabel = new Label("<b>Properties</b>");
		propertiesLabel.setWidth100();
		propertiesLabel.setHeight(20);
		propertiesLabel.setAlign(Alignment.CENTER);
		propertiesLabel.setBackgroundColor("#dedad5");

		layout.addMember(propertiesLabel);

		i_valueSetInfoListGrid = new ValueSetInfoListGrid();
		layout.addMember(i_valueSetInfoListGrid);

		return layout;
	}

	private VLayout createResolvedValueSetInfoPanel() {

		VLayout layout = new VLayout();
		layout.setWidth("50%");
		layout.setHeight100();
		layout.setBackgroundColor("#ffffff");

		Label resolvedValueSetLabel = UiHelper.createTitleLabel(TITLE_RESOLVED_VS_INFO);
		layout.addMember(resolvedValueSetLabel);

		VLayout resolvedVsLayout = new VLayout();
		resolvedVsLayout.setWidth100();
		resolvedVsLayout.setHeight(150);
		resolvedVsLayout.setMembersMargin(4);

		i_valueSetDefinitionTitle = createTitleLabel(RVS_TITLE_WIDTH, "Value Set Definition:");
		i_valueSetDefinitionValue = createValueLabel("");
		resolvedVsLayout.addMember(createNameValueLayout(i_valueSetDefinitionTitle, i_valueSetDefinitionValue));

		i_codeSystemVersionTitle = createTitleLabel(RVS_TITLE_WIDTH, "Code System Version:");
		i_codeSystemVersionValue = createValueLabel("");
		resolvedVsLayout.addMember(createNameValueLayout(i_codeSystemVersionTitle, i_codeSystemVersionValue));

		i_codeSystemTitle = createTitleLabel(RVS_TITLE_WIDTH, "Code System:");
		i_codeSystemValue = createValueLabel("");
		resolvedVsLayout.addMember(createNameValueLayout(i_codeSystemTitle, i_codeSystemValue));

		layout.addMember(resolvedVsLayout);

		Label propertiesLabel = new Label("<b>Value Set Members</b>");
		propertiesLabel.setWidth100();
		propertiesLabel.setHeight(20);
		propertiesLabel.setAlign(Alignment.CENTER);
		propertiesLabel.setBackgroundColor("#dedad5");

		layout.addMember(propertiesLabel);

		i_resolvedValueSetListGrid = new ResolvedValueSetListGrid();
		layout.addMember(i_resolvedValueSetListGrid);

		return layout;
	}

	private Label createTitleLabel(int width, String title) {
		Label label = new Label("<b>" + title + "</b>");
		label.setWrap(false);
		label.setWidth(width);
		label.setMargin(3);
		return label;
	}

	private Label createValueLabel(String title) {
		Label label = new Label(title);
		label.setWidth("*");
		label.setMargin(3);
		return label;
	}

	private HLayout createNameValueLayout(Label title, Label value) {
		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight(20);

		layout.addMember(title);
		layout.addMember(value);

		return layout;
	}

	public void updatePanel(String serviceName, String valueSet, String link) {
		i_valueSetInfoListGrid.getData(serviceName, valueSet);

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

	private void clearValueSetInfo() {
		i_valueSetInfoListGrid.setData(new ListGridRecord[0]);

		// value set info
		i_stateValue.setContents("");
		i_resourceNameValue.setContents("");
		i_formalNameValue.setContents("");
		i_descriptionValue.setContents("");
		i_contactValue.setContents("");
	}

	/**
	 * Clear out all of the ListGrids and panels
	 */
	public void clearPanels() {
		clearValueSetInfo();
		clearResolvedValueSetInfo();
	}

	/**
	 * When the ValueSetsInfoXmlDS DataSource is populated with new information,
	 * then update the other widgets.
	 */
	private void createValueSetInfoReceivedEvent() {
		Cts2Viewer.EVENT_BUS.addHandler(ValueSetInfoReceivedEvent.TYPE, new ValueSetInfoReceivedEventHandler() {

			@Override
			public void onValueSetInfoReceived(ValueSetInfoReceivedEvent event) {

				ValueSetInfo vsi = event.getValueSetInfo();
				if (vsi != null) {
					i_stateValue.setContents(vsi.getState());
					i_resourceNameValue.setContents(vsi.getResourceName());
					i_formalNameValue.setContents(vsi.getFormalName());
					i_descriptionValue.setContents(vsi.getDescriptionValue());

					String title = vsi.getRole() != null ? vsi.getRole() : "";
					String source = vsi.getSource() != null ? vsi.getSource() : "";
					String sourceUri = vsi.getSourceUri() != null ? vsi.getSourceUri() : "";
					i_contactTitle.setContents("<b>" + title + "</b>");
					i_contactValue.setContents(UiHelper.getLink(sourceUri, source));
				}
			}
		});

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
