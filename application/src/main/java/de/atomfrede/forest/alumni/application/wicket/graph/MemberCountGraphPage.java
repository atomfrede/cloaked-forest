package de.atomfrede.forest.alumni.application.wicket.graph;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class MemberCountGraphPage extends BasePage<Void>{

	public MemberCountGraphPage(){
		super();
		add(new MemberCountGraphPanel("member-count"));
	}
	
}
