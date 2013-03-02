package de.atomfrede.forest.alumni.application.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;
import de.atomfrede.forest.alumni.application.wicket.homepage.Homepage;
import de.atomfrede.forest.alumni.application.wicket.login.LoginPage;
import de.atomfrede.forest.alumni.application.wicket.security.ISecureApplication;
import de.atomfrede.forest.alumni.application.wicket.security.SimpleUserAuthorizationStrategy;
import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;
import de.atomfrede.forest.alumni.application.wicket.security.UserSession;

@Component(value = "wicketApplication")
public class WicketApplication extends WebApplication implements
		ISecureApplication {

	@Autowired
	private ApplicationContext mApplicationContext;

	@Override
	protected void init() {
		super.init();
		Bootstrap.install(WicketApplication.get(),
				new BootstrapSettings().minify(true));

		initSpring();

		// customized auth strategy
		getSecuritySettings().setAuthorizationStrategy(
				new SimpleUserAuthorizationStrategy());

		// don't throw exceptions for missing translations
		getResourceSettings().setThrowExceptionOnMissingResource(false);

		// enable ajax debug etc.
		getDebugSettings().setDevelopmentUtilitiesEnabled(true);

		new AnnotatedMountScanner().scanPackage(
				"de.atomfrede.virtual.web.tour.application.*").mount(this);

	}

	protected void initSpring() {
		// Initialize Spring Dependency Injection
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
	}

	private void configureBootstrap() {
		final BootstrapSettings settings = new BootstrapSettings();
		settings.useJqueryPP(false).useModernizr(false).useResponsiveCss(true)
				.setJsResourceFilterName("footer-container");

		// reactivate if new less4j version is available:
		// settings.getBootstrapLessCompilerSettings().setUseLessCompiler(usesDevelopmentConfig());
		// settings.getBootstrapLessCompilerSettings().setLessCompiler(new
		// Less4JCompiler());

		ThemeProvider themeProvider = new BootswatchThemeProvider() {
			{
				defaultTheme("united");
			}
		};
		settings.setThemeProvider(themeProvider);

		Bootstrap.install(this, settings);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return Homepage.class;
	}
	
	

	@Override
	public Class<? extends Page> getLoginPage() {
		return LoginPage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		UserSession<UserAuthModel> session = new UserSession<UserAuthModel>(
				request);
		return session;
	}

	

}
