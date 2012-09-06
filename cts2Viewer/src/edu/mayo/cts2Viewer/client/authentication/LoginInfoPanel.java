package edu.mayo.cts2Viewer.client.authentication;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.cts2Viewer.client.Cts2Viewer;
import edu.mayo.cts2Viewer.client.events.LogOutRequestEvent;
import edu.mayo.cts2Viewer.shared.Credentials;

/**
 * Layout that holds the logged in user and a button for logout
 */
public class LoginInfoPanel extends HLayout {

	private static final String HELPTEXT = "This is the user name that is currently logged into the selected server.";

	private static final int HEIGHT = 25;
	private static final int LABEL_WIDTH = 100;
	private static final String BUTTON_TITLE = "Logout";

	private Label i_loggedInUser;
	private IButton i_logoutButton;
	private Credentials i_credentials;
	private DynamicForm i_helpButtonForm;

	public LoginInfoPanel() {
		super();
		init();
	}

	private void init() {
		setWidth100();
		setHeight(HEIGHT);

		i_helpButtonForm = createHelpButton();

		i_loggedInUser = new Label("");
		i_loggedInUser.setWidth(LABEL_WIDTH);
		i_loggedInUser.setMargin(5);

		i_logoutButton = new IButton(BUTTON_TITLE);
		i_logoutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				clearUser();

				// Fire a logout event
				Cts2Viewer.EVENT_BUS.fireEvent(new LogOutRequestEvent(i_credentials));
			}
		});

		addMember(i_logoutButton);
		addMember(i_helpButtonForm);
		addMember(i_loggedInUser);

		// set hidden initially
		setVisibility(Visibility.HIDDEN);
	}

	private DynamicForm createHelpButton() {
		DynamicForm form = new DynamicForm();
		form.setWidth(20);

		FormItemIcon icon = new FormItemIcon();
		icon.setSrc("[SKIN]/actions/help.png");

		final StaticTextItem blankTextItem = new StaticTextItem();
		blankTextItem.setName("blank");
		blankTextItem.setTitle("");
		blankTextItem.setIcons(icon);
		blankTextItem.addIconClickHandler(new IconClickHandler() {

			@Override
			public void onIconClick(IconClickEvent event) {
				SC.say(HELPTEXT);
			}
		});

		form.setFields(blankTextItem);
		return form;
	}

	public void setUser(Credentials credentials) {
		i_credentials = credentials;

		i_loggedInUser.setContents("<b>" + i_credentials.getUser() + "</b>");
		setVisibility(Visibility.VISIBLE);
	}

	public void clearUser() {
		i_loggedInUser.setContents("");
		setVisibility(Visibility.HIDDEN);
	}

}
