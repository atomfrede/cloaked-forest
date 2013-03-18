package de.atomfrede.forest.alumni.application.wicket.custom;

import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.CompoundPropertyModel;

import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@SuppressWarnings("serial")
public class DegreeSelectOption extends SelectOption<Degree> {

	public DegreeSelectOption(String id, Degree degree) {
		super(id, new CompoundPropertyModel<Degree>(degree));
	}

	public void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag,
				getDefaultModelObjectAsString());
	}
}
