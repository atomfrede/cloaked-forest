package de.atomfrede.forest.alumni.application.wicket.custom;

import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.CompoundPropertyModel;

import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@SuppressWarnings("serial")
public class SectorSelectOption extends SelectOption<Sector> {

	public SectorSelectOption(String id, Sector sector) {
		super(id, new CompoundPropertyModel<Sector>(sector));
	}
	
	public  void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag,
				getDefaultModelObjectAsString());
	}
}
