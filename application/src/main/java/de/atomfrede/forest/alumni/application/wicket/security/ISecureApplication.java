package de.atomfrede.forest.alumni.application.wicket.security;

import org.apache.wicket.Page;

public interface ISecureApplication {
	Class<? extends Page> getLoginPage();
}
