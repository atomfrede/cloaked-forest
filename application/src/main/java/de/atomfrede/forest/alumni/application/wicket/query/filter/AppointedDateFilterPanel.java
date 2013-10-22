package de.atomfrede.forest.alumni.application.wicket.query.filter;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.format.DateTimeFormat;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;

public class AppointedDateFilterPanel extends Panel {

	private Date _appointedDate;
	private DateTextField appointedDate;
	
	public AppointedDateFilterPanel(String id) {
		super(id);
		
		DateTextFieldConfig conf = new DateTextFieldConfig();
		conf.autoClose(true);
		String pattern = DateTimeFormat.patternForStyle("M-", getSession().getLocale());
		conf.withFormat(pattern);
		conf.withLanguage(getSession().getLocale().getLanguage());
		appointedDate = new DateTextField("appointedDate", new PropertyModel<Date>(
				this, "_appointedDate"), conf);
		
		appointedDate.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Just for updating the model object
				
			}
		});
		
		add(appointedDate);
	}
	
	public Date getValue() {
		return _appointedDate;
	}
}
