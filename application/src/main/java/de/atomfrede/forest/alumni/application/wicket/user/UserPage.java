package de.atomfrede.forest.alumni.application.wicket.user;

import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/users", alt = "/users")
public class UserPage extends BasePage<Void>{

	public UserPage() {
		super();
		add(new UserListPanel("users"));
	}
}
