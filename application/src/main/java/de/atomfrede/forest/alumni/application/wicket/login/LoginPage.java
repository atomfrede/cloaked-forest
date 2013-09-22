package de.atomfrede.forest.alumni.application.wicket.login;

import org.apache.wicket.Session;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.AbstractAuthPage;
import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;
import de.atomfrede.forest.alumni.application.wicket.security.UserSession;
import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.user.UserService;
import de.atomfrede.forest.alumni.service.user.UsernameAlreadyTakenException;

@SuppressWarnings("serial")
@MountPath(value = "/login")
public class LoginPage extends AbstractAuthPage<Void> {

	@SpringBean
	private UserService userService;
	
	public LoginPage(PageParameters params) {
		commonInit(params);
		UserAuthModel userModel = new UserAuthModel(User.class, -1L);

		add(new LoginPanel("loginPanel", userModel));

		if (params.get("doLogout") != null) {
			String doLogoutParam = params.get("doLogout").toString();
			boolean doLogout = Boolean.parseBoolean(doLogoutParam);
			if (doLogout) {
				doLogout();
			}
		}

		addDummyUser();
	}
	
	public void doLogout() {
		getSession().invalidateNow();
		getSession().invalidate();
		@SuppressWarnings("unchecked")
		UserSession<UserAuthModel> session = (UserSession<UserAuthModel>) Session
				.get();
		session.setUser(new UserAuthModel(User.class, -1L));
		setResponsePage(LoginPage.class);
	}
	
	private void addDummyUser() {
		try {
			userService.createUser("fred", "Frederik", "Hahne",
					"fred@mail.de", "fred");
		} catch (UsernameAlreadyTakenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
