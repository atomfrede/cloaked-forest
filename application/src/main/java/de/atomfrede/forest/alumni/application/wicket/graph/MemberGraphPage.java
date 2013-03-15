package de.atomfrede.forest.alumni.application.wicket.graph;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class MemberGraphPage extends BasePage<Void>{

	public MemberGraphPage(){
		super();
		add(new MemberCountGraphPanel("member-count"));
	}
	
}
