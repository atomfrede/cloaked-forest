package de.atomfrede.forest.alumni.application.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.apache.wicket.protocol.https.Scheme;
import org.apache.wicket.request.IRequestCycle;
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

		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

		Bootstrap.install(WicketApplication.get(), new BootstrapSettings());

		configureBootstrap();

		initSpring();

		// customized auth strategy
		getSecuritySettings().setAuthorizationStrategy(
				new SimpleUserAuthorizationStrategy());

		// don't throw exceptions for missing translations
		getResourceSettings().setThrowExceptionOnMissingResource(false);

		// enable ajax debug etc.
		getDebugSettings().setDevelopmentUtilitiesEnabled(false);

		
		new AnnotatedMountScanner().scanPackage(
				"de.atomfrede.forest.alumni.application.*").mount(this);
		
		setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig(8080, 8443)){
			
			@Override
			protected Scheme getDesiredSchemeFor(Class pageClass){
				if(getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT){
					return Scheme.HTTP;
				}else{
					return super.getDesiredSchemeFor(pageClass);
				}
			}
		});


	}

	protected void initSpring() {
		// Initialize Spring Dependency Injection
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
	}

	private void configureBootstrap() {
		final BootstrapSettings settings = new BootstrapSettings();

		ThemeProvider themeProvider = new BootswatchThemeProvider() {
			{
				defaultTheme("united");
			}
		};
		settings.setThemeProvider(themeProvider);

		Bootstrap.install(this, settings);

		// wicket markup leads to ui problems because css selectors doesn't
		// match.
		this.getMarkupSettings().setStripWicketTags(true);
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

	@Override
	public RuntimeConfigurationType getConfigurationType() {
		return RuntimeConfigurationType.DEPLOYMENT;
	}


}
