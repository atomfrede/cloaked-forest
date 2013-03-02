package de.atomfrede.forest.alumni.application.wicket.login;

import org.apache.wicket.markup.html.panel.Panel;

import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;

@SuppressWarnings("serial")
public class LoginPanel extends Panel {

	public LoginPanel(String id, UserAuthModel user) {
		super(id);
		LoginForm loginForm = new LoginForm("loginForm", user);
		add(loginForm);
	}

}
