package de.atomfrede.forest.alumni.application.wicket.member.custom;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class BusinessCardPanel extends Panel {
	
	@SpringBean
	private MemberDao memberDao;
	
	public BusinessCardPanel(String id, Long memberId){
		super(id);
		
		Member mem = null;
		if(memberId != null){
			mem = memberDao.findById(memberId);
		}
		
		Label profession = new Label("profession");
				
		if(mem != null){
			profession = new Label("profession", Model.of(mem.getProfession()));
		}
		MultiLineLabel personal = new MultiLineLabel("contact-personal-content", Model.of("Text goes here"));
		
		add(personal);
		
		add(profession);
	}

}
