package edu.mayo.cts2Viewer.client.authentication;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.cts2Viewer.client.Cts2Viewer;
import edu.mayo.cts2Viewer.shared.Credentials;

/**
 * Layout that holds the logged in user and a button for logout
 */
public class LoginInfoPanel extends HLayout {

	private static final String HELPTEXT = "This service requires a valid username/password to view the value sets.\n\nTo log in or log out, click on the link in the upper right corner.";

	private DynamicForm i_helpButtonForm;

	public LoginInfoPanel() {
		super();
		init();
	}

	private void init() {
		setWidth100();

		int height = Cts2Viewer.s_showAll ? 28 : 20;
		setHeight(height);
		i_helpButtonForm = createHelpButton();
	}

	public void addWidgets() {

		addMember(i_helpButtonForm);
		
		// add widgets based on if we are showing all servers, or just one.
		if (Cts2Viewer.s_showAll) {
			// set hidden initially
			setVisibility(Visibility.HIDDEN);
		} else {
			setVisibility(Visibility.VISIBLE);
		}
	}

	private DynamicForm createHelpButton() {
		DynamicForm form = new DynamicForm();
		form.setWidth(5);

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
		setVisibility(Visibility.HIDDEN);
		removeMembers(getMembers());
		addMember(i_helpButtonForm);

		redraw();
	}

	public void clearUser() {
		removeMembers(getMembers());
		addWidgets();
		redraw();
	}

}
