package de.atomfrede.forest.alumni.application.wicket.graph;

import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/graphs", alt = "/graphs")
public class GraphPage extends BasePage<Void> {

	public GraphPage() {
		super();
		add(new MemberCountGraphPanel("member-count"));
		add(new SectorGraphPanel("sector-overview"));
		add(new DegreeGraphPanel("member-degree"));
	}

}
