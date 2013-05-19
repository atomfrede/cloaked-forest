package de.atomfrede.forest.alumni.application.wicket.query.filter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.activity.ActivityProvider;
import de.atomfrede.forest.alumni.domain.dao.activity.ActivityDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;

@SuppressWarnings("serial")
public class ActivityFilterPanel extends Panel {

	@SpringBean
	private ActivityDao activityDao;

	DataView<Activity> activities;

	public ActivityFilterPanel(String id) {
		super(id);
		populateItems();
	}

	/**
	 * Populates the last Tab with checkboxes for possible activites.
	 */
	private void populateItems() {
		activities = new DataView<Activity>("activities",
				new ActivityProvider()) {

			@Override
			protected void populateItem(Item<Activity> item) {
				// TODO check which must be the default value...
				CheckBox checkBox = null;
				boolean checked = false;
				checkBox = new CheckBox("activity", Model.of(Boolean.FALSE));

				checkBox.add(new AttributeModifier("data-label", item
						.getModelObject().getActivity()));
				item.add(checkBox);
			}
		};

		add(activities);

	}
}
