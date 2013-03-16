package de.atomfrede.forest.alumni.application.wicket.graph;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class GraphPage extends BasePage<Void>{

	public GraphPage(){
		super();
		add(new MemberCountGraphPanel("member-count"));
		add(new SectorGraphPanel("sector-overview"));
		add(new DegreeGraphPanel("member-degree"));
	}
	
}
