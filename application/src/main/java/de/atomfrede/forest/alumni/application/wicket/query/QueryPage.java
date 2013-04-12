package de.atomfrede.forest.alumni.application.wicket.query;

import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/queries", alt = "/queries")
public class QueryPage extends BasePage<Void> {

	public QueryPage() {
		super();
		add(new QueryConfigPanel("query-config"));
	}
}
