package de.atomfrede.forest.alumni.application.wicket.query.filter;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.form.IDataSource;
import de.agilecoders.wicket.markup.html.bootstrap.form.Typeahead;
import de.agilecoders.wicket.markup.html.bootstrap.form.TypeaheadConfig;
import de.agilecoders.wicket.markup.html.bootstrap.layout.SpanType;
import de.atomfrede.forest.alumni.service.member.professsion.ProfessionService;

@SuppressWarnings("serial")
public class ProfessionFilterPanel extends Panel {

	@SpringBean
	ProfessionService professionService;

	String value;

	Typeahead<String> typeahead;

	public ProfessionFilterPanel(String id) {
		super(id);
		add(addTypeahead("typeahead"));
		setupInput();
	}

	private Component addTypeahead(String markupId) {
		final IDataSource<String> dataSource = new IDataSource<String>() {

			@Override
			public List<String> load() {
				return professionService.getTypeaheadProfession();
			}
		};

		PropertyModel<String> model = new PropertyModel<>(this, "value");
		typeahead = new Typeahead<String>(markupId, model,
				dataSource, new TypeaheadConfig().withNumberOfItems(4));
		typeahead.size(SpanType.SPAN5);

		return typeahead;
	}
	
	private void setupInput(){
		typeahead.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Nothing special to do here, just to update the model value without  require to submit the form explicitly.
				
			}
		});
	}
	public String getValue(){
		return value;
	}

}
