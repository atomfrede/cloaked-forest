package de.atomfrede.forest.alumni.application.wicket.query;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.custom.DegreeSelectOption;
import de.atomfrede.forest.alumni.application.wicket.query.filter.ProfessionFilterPanel;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@SuppressWarnings("serial")
public class QueryConfigPanel extends Panel {

	@SpringBean
	private DegreeDao degreeDao;

	private Label header, subHeader;
	private ProfessionFilterPanel professionFilterPanel;
	
	private MemberResultsPanel memberResultPanel;

	public QueryConfigPanel(String id) {
		super(id);
		header = new Label("page-header", _("query.header"));
		subHeader = new Label("page-sub-header", _("query.sub.header"));

		add(header);
		add(subHeader);

		add(new QueryConfigForm("query-form"));
	}

	private void setupDegreeFilter() {
		List<Degree> degrees = degreeDao.findAll();

		Select<Degree> degreeSelect = new Select<Degree>("degree-select",
				new PropertyModel<Degree>(this, "selectedFilterDegree"));

		degreeSelect.add(new ListView<Degree>("degree-options", degrees) {

			@Override
			protected void populateItem(ListItem<Degree> item) {
				item.add(new DegreeSelectOption("degree-option", item
						.getModelObject()));
			}
		});

		add(degreeSelect);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem
				.forScript("$('.selectpicker').selectpicker();"));
		response.render(OnDomReadyHeaderItem
				.forScript("$('input.checkboxes-pretty').prettyCheckable({color: 'red'});"));

	}

}
