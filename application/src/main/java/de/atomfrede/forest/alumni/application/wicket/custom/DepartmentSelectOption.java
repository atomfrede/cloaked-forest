package de.atomfrede.forest.alumni.application.wicket.custom;

import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.CompoundPropertyModel;

import de.atomfrede.forest.alumni.domain.entity.department.Department;

@SuppressWarnings("serial")
public class DepartmentSelectOption extends SelectOption<Department> {

	public DepartmentSelectOption(String id, Department department) {
		super(id, new CompoundPropertyModel<Department>(department));
	}

	public void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag,
				getDefaultModelObjectAsString());
	}
}
