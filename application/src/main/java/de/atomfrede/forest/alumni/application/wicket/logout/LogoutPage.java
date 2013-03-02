package de.atomfrede.forest.alumni.application.wicket.logout;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.AbstractBasePage;
import de.atomfrede.forest.alumni.application.wicket.login.LoginPanel;
import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;
import de.atomfrede.forest.alumni.application.wicket.security.UserSession;
import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.user.UserService;

@MountPath(value = "/logout", alt = "/logout")
public class LogoutPage extends AbstractBasePage {

	private static final long serialVersionUID = 2053750825891259558L;

	private static boolean loggedOut = false;
	private static int counter = 0;

	@SpringBean
	public UserService userService;

	WebMarkupContainer successContainer;

	public LogoutPage() {
		UserAuthModel userModel = new UserAuthModel(User.class, -1L);

		successContainer = new WebMarkupContainer("logout-success");
		add(successContainer);
		successContainer.setVisible(false);
		
		add(new LoginPanel("loginPanel", userModel));

		if (!loggedOut) {
			loggedOut = true;
			doLogout();
		}
		
		
		
	}
	
	@Override
	public void onBeforeRender(){
		super.onBeforeRender();
		if(counter < 2){
			successContainer.setVisible(true);
		}else{
			successContainer.setVisible(false);
		}
		counter++;
	}

	public void doLogout() {
		getSession().invalidateNow();
		getSession().invalidate();
		@SuppressWarnings("unchecked")
		UserSession<UserAuthModel> session = (UserSession<UserAuthModel>) Session.get();
		session.setUser(new UserAuthModel(User.class, -1L));
		loggedOut = true;
	}

	public static void reset() {
		loggedOut = false;
		counter = 0;
	}

}
