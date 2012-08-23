package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.events.ValueSetInfoReceivedEvent;
import edu.mayo.cts2Viewer.client.events.ValueSetInfoReceivedEventHandler;
import edu.mayo.cts2Viewer.client.utils.UiHelper;
import edu.mayo.cts2Viewer.shared.ValueSetInfo;

public class ValueSetPropertiesPanel extends VLayout {

	private static final String TITLE_VS_INFO = "Value Set Properties";
	private static final int VS_TITLE_WIDTH = 100;

	private ValueSetInfoListGrid i_valueSetInfoListGrid;

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

	public ValueSetPropertiesPanel() {
		super();
		init();
	}

	private void init() {
		setWidth100();
		setHeight(300);
		setBackgroundColor("#ffffff");

		Label valueSetLabel = UiHelper.createTitleLabel(TITLE_VS_INFO);
		addMember(valueSetLabel);

		VLayout vsLayout = new VLayout();
		vsLayout.setWidth100();
		vsLayout.setHeight(150);
		vsLayout.setMembersMargin(4);

		i_resourceNameTitle = UiHelper.createTitleLabel(VS_TITLE_WIDTH, "Resource Name:");
		i_resourceNameValue = UiHelper.createValueLabel("");
		vsLayout.addMember(UiHelper.createNameValueLayout(i_resourceNameTitle, i_resourceNameValue));

		i_formalNameTitle = UiHelper.createTitleLabel(VS_TITLE_WIDTH, "Formal Name:");
		i_formalNameValue = UiHelper.createValueLabel("");
		vsLayout.addMember(UiHelper.createNameValueLayout(i_formalNameTitle, i_formalNameValue));

		i_descriptionTitle = UiHelper.createTitleLabel(VS_TITLE_WIDTH, "Description:");
		i_descriptionValue = UiHelper.createValueLabel("");
		vsLayout.addMember(UiHelper.createNameValueLayout(i_descriptionTitle, i_descriptionValue));

		i_stateTitle = UiHelper.createTitleLabel(VS_TITLE_WIDTH, "State:");
		i_stateValue = UiHelper.createValueLabel("");
		vsLayout.addMember(UiHelper.createNameValueLayout(i_stateTitle, i_stateValue));

		i_contactTitle = UiHelper.createTitleLabel(VS_TITLE_WIDTH, "Contact:");
		i_contactValue = UiHelper.createValueLabel("");
		vsLayout.addMember(UiHelper.createNameValueLayout(i_contactTitle, i_contactValue));

		addMember(vsLayout);

		Label propertiesLabel = new Label("<b>Properties</b>");
		propertiesLabel.setWidth100();
		propertiesLabel.setHeight(20);
		propertiesLabel.setAlign(Alignment.CENTER);
		propertiesLabel.setBackgroundColor("#dedad5");

		addMember(propertiesLabel);

		i_valueSetInfoListGrid = new ValueSetInfoListGrid();
		addMember(i_valueSetInfoListGrid);

		createValueSetInfoReceivedEvent();
	}

	public void clearValueSetInfo() {
		i_valueSetInfoListGrid.setData(new ListGridRecord[0]);

		// value set info
		i_stateValue.setContents("");
		i_resourceNameValue.setContents("");
		i_formalNameValue.setContents("");
		i_descriptionValue.setContents("");
		i_contactValue.setContents("");
	}

	public void updatePanel(String serviceName, String valueSet, String link) {
		i_valueSetInfoListGrid.getData(serviceName, valueSet);
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
}
