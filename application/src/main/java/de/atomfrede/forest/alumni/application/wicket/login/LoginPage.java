package de.atomfrede.forest.alumni.application.wicket.login;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.AbstractBasePage;
import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;
import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.user.UserService;
import de.atomfrede.forest.alumni.service.user.UsernameAlreadyTakenException;

@MountPath(value = "/login", alt = "/login")
public class LoginPage extends AbstractBasePage {

	@SpringBean
	public UserService userService;
	

	public LoginPage() {
//		addDummyBottles();
		addDummyUser();
		
		UserAuthModel userModel = new UserAuthModel(User.class, -1L);
		add(new LoginPanel("loginPanel", userModel));
	}
	
	private void addDummyUser(){
		try {
			User fred = userService.createUser("fred", "Frederik", "Hahne", "fred@mail.de", "fred");
			
		} catch (UsernameAlreadyTakenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
