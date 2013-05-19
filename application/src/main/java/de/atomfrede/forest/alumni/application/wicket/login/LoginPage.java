package de.atomfrede.forest.alumni.application.wicket.login;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.AbstractAuthPage;
import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;
import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.user.UserService;
import de.atomfrede.forest.alumni.service.user.UsernameAlreadyTakenException;

@SuppressWarnings("serial")
@MountPath(value = "/login", alt = "/login")
public class LoginPage extends AbstractAuthPage<Void> {

	@SpringBean
	private UserService userService;
	
	public LoginPage() {
		super();
		commonInit(new PageParameters());
		addDummyUser();

		UserAuthModel userModel = new UserAuthModel(User.class, -1L);
		add(new LoginPanel("loginPanel", userModel));
	}

	private void addDummyUser() {
		try {
			User fred = userService.createUser("fred", "Frederik", "Hahne",
					"fred@mail.de", "fred");
			
			
			
		} catch (UsernameAlreadyTakenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
