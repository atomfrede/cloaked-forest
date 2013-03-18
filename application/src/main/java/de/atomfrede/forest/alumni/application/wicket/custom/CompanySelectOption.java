package de.atomfrede.forest.alumni.application.wicket.custom;

import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.CompoundPropertyModel;

import de.atomfrede.forest.alumni.domain.entity.company.Company;

@SuppressWarnings("serial")
public class CompanySelectOption extends SelectOption<Company> {

	public CompanySelectOption(String id, Company company) {
		super(id, new CompoundPropertyModel<Company>(company));
	}

	public void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag,
				getDefaultModelObjectAsString());
	}
}
