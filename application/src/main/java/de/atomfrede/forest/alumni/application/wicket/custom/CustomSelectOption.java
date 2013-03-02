package de.atomfrede.forest.alumni.application.wicket.custom;

import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class CustomSelectOption extends SelectOption {

	public CustomSelectOption(String id, String displayValue) {
		super(id, new Model(displayValue));
	}

	public  void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag,
				getDefaultModelObjectAsString());
	}
}
