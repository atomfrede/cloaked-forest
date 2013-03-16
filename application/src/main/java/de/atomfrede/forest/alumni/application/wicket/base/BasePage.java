package de.atomfrede.forest.alumni.application.wicket.base;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarComponents;
import de.atomfrede.forest.alumni.application.wicket.graph.GraphPage;
import de.atomfrede.forest.alumni.application.wicket.homepage.Homepage;
import de.atomfrede.forest.alumni.application.wicket.logout.LogoutPage;
import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;
import de.atomfrede.forest.alumni.application.wicket.security.UserSession;
import de.atomfrede.forest.alumni.domain.entity.user.User;

public abstract class BasePage<T> extends GenericWebPage<T> {

	protected User currentUser;
	Label pageTitel;

	public BasePage() {
		super();
		commonInit(new PageParameters());
	}

	/**
	 * Construct.
	 * 
	 * @param model
	 *            The model to use for this page
	 */
	public BasePage(IModel<T> model) {
		super(model);
		commonInit(new PageParameters());
	}

	/**
	 * Construct.
	 * 
	 * @param parameters
	 *            current page parameters
	 */
	public BasePage(PageParameters parameters) {
		super(parameters);
		commonInit(parameters);
	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
	}

	private void commonInit(PageParameters pageParameters) {
		pageTitel = new Label("pageTitle", _("global.page.title",
				"global.page.title"));
		add(pageTitel);

		currentUser = getSession().getUser().getObject();
		add(newNavbar("navbar"));
		// add(new Footer("footer"));
		//
	}

	protected Navbar newNavbar(String markupId) {
		Navbar navbar = new Navbar(markupId);

		navbar.setPosition(Navbar.Position.TOP);

		// show brand name
		navbar.brandName(Model.of(_("global.page.title")
				.getString()));
		

		

		navbar.addComponents(NavbarComponents.transform(
				Navbar.ComponentPosition.LEFT,
				new NavbarButton<Homepage>(Homepage.class, Model.of(_(
						"nav.home").getString()))
						.setIconType(IconType.home)

		));

		navbar.addComponents(NavbarComponents.transform(
				Navbar.ComponentPosition.LEFT,
				new NavbarButton<GraphPage>(
						GraphPage.class, Model.of(_("nav.graph").getString()))
						.setIconType(IconType.picture)));

		navbar.addComponents(new ImmutableNavbarComponent(
				new NavbarButton<LogoutPage>(LogoutPage.class, Model
						.of("Logout")).setIconType(IconType.off),
				Navbar.ComponentPosition.RIGHT));

		return navbar;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				BasePage.class, "base-content.css")));
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				BasePage.class, "base.css")));
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				BasePage.class, "bootstrap-select.min.css")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(BasePage.class,
						"bootstrap-select.min.js")));
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				BasePage.class, "prettyCheckable.css")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(BasePage.class,
						"prettyCheckable.js")));
		
		Bootstrap.renderHead(response);
	}

	@SuppressWarnings("unchecked")
	public UserSession<UserAuthModel> getSession() {
		return (UserSession<UserAuthModel>) Session.get();
	}

	public UserAuthModel getUser() {
		return getSession().getUser();
	}
}
