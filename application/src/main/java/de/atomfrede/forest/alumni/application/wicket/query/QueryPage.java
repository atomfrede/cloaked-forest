package de.atomfrede.forest.alumni.application.wicket.query;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class QueryPage extends BasePage<Void> {

	public QueryPage() {
		super();
		add(new QueryConfigPanel("query-config"));
	}
}
