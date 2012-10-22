package edu.mayo.cts2Viewer.client.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.cts2Viewer.client.Cts2Service;
import edu.mayo.cts2Viewer.client.Cts2ServiceAsync;
import edu.mayo.cts2Viewer.client.Cts2Viewer;
import edu.mayo.cts2Viewer.client.events.FilterUpdatedEvent;

public class FilterPanel extends HLayout {

	private static final int HEIGHT_BUTTON = 20;
	private static final int WIDTH_BUTTON = 50;

	private ComboBoxItem nqfNumberCombo;
	private ComboBoxItem eMeasureCombo;
	private IButton clearFiltersButton;
	private final Map<String, String> filters;

	public FilterPanel() {
		filters = new HashMap<String, String>();
		initPanel();
	}

	private void initPanel() {
		DynamicForm filterForm = new DynamicForm();

		createNqfNumberCombo();
		createEMeasureCombo();

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

		filterForm.setFields(nqfNumberCombo, eMeasureCombo);

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

	private void createNqfNumberCombo() {
		final String filterComponent = "nqfnumber";
		String title = "NQF Number";
		nqfNumberCombo = new ComboBoxItem();
		nqfNumberCombo.setDefaultToFirstOption(true);
		nqfNumberCombo.setTitle("<b>" + title + "</b>");
		nqfNumberCombo.setType("comboBox");
		nqfNumberCombo.setWrapTitle(false);
		nqfNumberCombo.setWidth("30px");
		nqfNumberCombo.setAttribute("browserSpellCheck", false);

		nqfNumberCombo.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent changedEvent) {
				filters.put(filterComponent, nqfNumberCombo.getValueAsString());
				enableClearButton();
				Cts2Viewer.EVENT_BUS.fireEvent(new FilterUpdatedEvent());
			}
		});

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		service.getNqfNumbers(new AsyncCallback<LinkedHashMap<String, String>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(LinkedHashMap<String, String> result) {
				nqfNumberCombo.setValueMap(result);
			}
		});

	}

	private void createEMeasureCombo() {
		final String filterComponent = "emeasureid";
		String title = "eMeasure Id";
		eMeasureCombo = new ComboBoxItem();
		eMeasureCombo.setDefaultToFirstOption(true);
		eMeasureCombo.setTitle("<b>" + title + "</b>");
		eMeasureCombo.setType("comboBox");
		eMeasureCombo.setWrapTitle(false);
		eMeasureCombo.setWidth("30px");
		eMeasureCombo.setAttribute("browserSpellCheck", false);

		eMeasureCombo.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent changedEvent) {
				filters.put(filterComponent, eMeasureCombo.getValueAsString());
				enableClearButton();
				Cts2Viewer.EVENT_BUS.fireEvent(new FilterUpdatedEvent());
			}
		});

		Cts2ServiceAsync service = GWT.create(Cts2Service.class);
		service.geteMeasureIds(new AsyncCallback<LinkedHashMap<String, String>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(LinkedHashMap<String, String> result) {
				eMeasureCombo.setValueMap(result);
			}
		});

	}

	private void clearForm() {
		nqfNumberCombo.clearValue();
		eMeasureCombo.clearValue();
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

		if (enable) {
			clearFiltersButton.enable();
		} else {
			clearFiltersButton.disable();
		}
	}

}