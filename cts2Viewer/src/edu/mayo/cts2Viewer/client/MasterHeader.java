package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Master Header panel that appears on the top of the page.
 */
public class MasterHeader extends HLayout {

	private static final int MASTHEAD_HEIGHT = 58;
	private static final String TITLE = "Value Sets Service";

	private Label i_titleLabel;

	public MasterHeader() {
		super();

		// initialize the MasterHeader layout container
		this.addStyleName("cts2-MasterHeader");
		this.setHeight(MASTHEAD_HEIGHT);

		// initialize the Logo image
		String logoImage = "cts2_logo.png";
		Img logoImg = new Img(logoImage, 104, 46);
		logoImg.setImageWidth(104);
		logoImg.setImageHeight(46);

		// initialize the Name label
		Label i_titleLabel = new Label();
		i_titleLabel.addStyleName("cts2-MasterHeader-Title");
		i_titleLabel.setWrap(false);
		i_titleLabel.setWidth("*");
		i_titleLabel.setContents(TITLE);

		// initialize the West layout container
		HLayout westLayout = new HLayout();
		westLayout.setHeight(MASTHEAD_HEIGHT);
		westLayout.setWidth("20%");
		// westLayout.addMember(logoImg);
		// westLayout.addMember(name);

		// initialize the Center layout container
		HLayout centerLayout = new HLayout();
		centerLayout.setAlign(Alignment.CENTER);
		centerLayout.setHeight(MASTHEAD_HEIGHT);
		centerLayout.setWidth("*");
		centerLayout.addMember(i_titleLabel);

		// initialize the Signed In User label
		Label signedInUser = new Label();
		signedInUser.addStyleName("cts2-MasterHeader-Title");
		// signedInUser.setContents("<b>User Name Here</b><br />upTick");

		// initialize the East layout container
		HLayout eastLayout = new HLayout();
		eastLayout.setAlign(Alignment.RIGHT);
		eastLayout.setHeight(MASTHEAD_HEIGHT);
		eastLayout.setWidth("20%");
		eastLayout.addMember(signedInUser);

		// add the West and East layout containers to the MasterHeader layout
		// container
		this.addMember(westLayout);
		this.addMember(centerLayout);
		this.addMember(eastLayout);
	}
}
