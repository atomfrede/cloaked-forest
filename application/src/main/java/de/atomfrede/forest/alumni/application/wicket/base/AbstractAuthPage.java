package de.atomfrede.forest.alumni.application.wicket.base;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

import de.agilecoders.wicket.Bootstrap;

@SuppressWarnings("serial")
@RequireHttps
public class AbstractAuthPage<T> extends GenericWebPage<T> {

	protected Label pageTitel;

	protected void commonInit(PageParameters pageParameters) {
		pageTitel = new Label("pageTitle", _("global.page.title", "global.page.title"));
		add(pageTitel);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				BasePage.class, "base-content.css")));
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				BasePage.class, "base.css")));
		Bootstrap.renderHead(response);
	}
}
