package de.atomfrede.forest.alumni.application.wicket.base;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;

import de.agilecoders.wicket.Bootstrap;

public abstract class AbstractBasePage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3218440126562724757L;

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				AbstractBasePage.class, "base.css")));
		Bootstrap.renderHead(response);

	}
}
