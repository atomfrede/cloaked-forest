package de.atomfrede.forest.alumni.application.wicket.homepage;

import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@MountPath(value = "/", alt = "/home")
public class Homepage extends BasePage<Void> {

	private static final long serialVersionUID = -5907257010026952550L;


	public Homepage() {
		super();
//		add(new ConsumptionOverviewPanel("consumptions"));
	}


}
