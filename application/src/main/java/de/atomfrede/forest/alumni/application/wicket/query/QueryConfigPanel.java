package de.atomfrede.forest.alumni.application.wicket.query;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class QueryConfigPanel extends Panel {

	private Label header, subHeader;

	public QueryConfigPanel(String id) {
		super(id);
		header = new Label("page-header", _("query.header"));
		subHeader = new Label("page-sub-header", _("query.sub.header"));

		add(header);
		add(subHeader);

		add(new QueryConfigForm("query-form"));
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
