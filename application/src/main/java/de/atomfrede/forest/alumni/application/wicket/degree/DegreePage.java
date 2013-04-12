package de.atomfrede.forest.alumni.application.wicket.degree;

import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/degrees", alt = "/degrees")
public class DegreePage extends BasePage<Void>{

	public DegreePage() {
		super();
		add(new DegreeListPanel("degrees"));
	}
}
