package de.atomfrede.forest.alumni.application.wicket.sector;

import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/sectors")
public class SectorPage extends BasePage<Void>{

	public SectorPage(){
		super();
		add(new SectorListPanel("sectors"));
	}
}
