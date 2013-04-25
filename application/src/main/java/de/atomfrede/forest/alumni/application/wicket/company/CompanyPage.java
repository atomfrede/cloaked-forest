package de.atomfrede.forest.alumni.application.wicket.company;

import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/companies")
public class CompanyPage extends BasePage<Void> {

	public CompanyPage(){
		super();
		add(new CompanyListPanel("companies"));
	}
}
