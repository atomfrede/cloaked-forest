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
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.sector.SectorService;

@SuppressWarnings("serial")
public class SectorFilterPanel extends Panel {

	@SpringBean
	private SectorService sectorService;

	@SpringBean
	private SectorDao sectorDao;

	private String sector;

	private Typeahead<String> typeahead;

	public SectorFilterPanel(String id) {
		super(id);
		add(addTypeahead("typeahead-sector"));
		setupInput();
	}

	private Component addTypeahead(String markupId) {
		final IDataSource<String> dataSource = new IDataSource<String>() {

			@Override
			public List<String> load() {
				return sectorService.getTypeAheadSectors();
			}
		};

		PropertyModel<String> model = new PropertyModel<>(this, "sector");
		typeahead = new Typeahead<String>(markupId, model, dataSource,
				new TypeaheadConfig().withNumberOfItems(15));
		typeahead.size(SpanType.SPAN5);

		return typeahead;
	}

	private void setupInput() {
		typeahead.add(new OnChangeAjaxBehavior() {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Nothing special to do here, just to update the model value
				// without require to submit the form explicitly.
			}
		});
	}

	/**
	 * Returns the company with the name currently entered inside the typeahead
	 * input field.
	 * 
	 * @return
	 */
	public Sector getValue() {
		return sectorDao.findByProperty("sector", sector);
	}

}
