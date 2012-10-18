package edu.mayo.cts2Viewer.client.utils;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.mayo.cts2Viewer.client.Cts2Viewer;
import edu.mayo.cts2Viewer.client.SearchTextItem;
import edu.mayo.cts2Viewer.client.events.FilterUpdatedEvent;

import java.util.HashMap;
import java.util.Map;

public class FilterPanel extends HLayout {

	private static final int HEIGHT_BUTTON = 20;
	private static final int WIDTH_BUTTON = 50;

	private TextItem nqfNumberText;
	private TextItem eMeasureIdText;
	private IButton clearFiltersButton;
	private Map<String, String> filters;

	public FilterPanel() {
		filters = new HashMap<String, String>();
		initPanel();
	}

	private void initPanel() {
		DynamicForm filterForm = new DynamicForm();

		nqfNumberText = createFilterTextItem("nqfnumber", "NQF Number");
		eMeasureIdText = createFilterTextItem("emeasureid", "eMeasure Id");

		clearFiltersButton = new IButton("Clear");
		clearFiltersButton.setHeight(HEIGHT_BUTTON);
		clearFiltersButton.setWidth(WIDTH_BUTTON);
		clearFiltersButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				clearForm();
				Cts2Viewer.EVENT_BUS.fireEvent(new FilterUpdatedEvent());
			}
		});
		clearFiltersButton.disable();

		filterForm.setFields(nqfNumberText, eMeasureIdText);

		setWidth(100);
		setHeight(55);
		setMembersMargin(5);
		setBackgroundColor("#f6faff");
		addMember(filterForm);

		VLayout buttonLayout = new VLayout();
		buttonLayout.setAlign(VerticalAlignment.BOTTOM);
		buttonLayout.setMembersMargin(5);
		buttonLayout.setMargin(5);
		buttonLayout.addMember(clearFiltersButton);

		addMember(buttonLayout);
	}

	public Map<String, String> getFilters() {
		return filters;
	};

	private SearchTextItem createFilterTextItem(final String filterComponent, String title) {
		final SearchTextItem textItem = new SearchTextItem();
		textItem.setTitle("<b>" + title + "</b>");
		textItem.setHint("Enter Filter Text");
		textItem.setWrapTitle(false);
		textItem.setWidth("30px");

		textItem.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent keyUpEvent) {
				if (textItem.isValidSearchText()) {
					filters.put(filterComponent, textItem.getValueAsString());
					Cts2Viewer.EVENT_BUS.fireEvent(new FilterUpdatedEvent());
				}

				enableClearButton();
			}
		});

		return textItem;
	}

	private void clearForm() {
		nqfNumberText.clearValue();
		eMeasureIdText.clearValue();
		filters.clear();
		enableClearButton();
	}

	private void enableClearButton() {
		boolean enable = false;

		for (String filter : filters.keySet()) {
			String value = filters.get(filter);
			if (value != null && !value.trim().equals("")) {
				enable = true;
				break;
			}
		}

		if (enable)
			clearFiltersButton.enable();
		else
			clearFiltersButton.disable();
	}

}