package de.atomfrede.forest.alumni.application.wicket.member.detail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class MemberDetailPanel extends Panel{

	@SpringBean
	MemberDao memberDao;
	
	Type editType;
	Long memberId;
	
	public MemberDetailPanel(String id, Type editType, Long memberId) {
		super(id);
		this.editType = editType;
		this.memberId = memberId;
		
		MembersDetailForm form = new MembersDetailForm("member-form", this.editType, new AbstractEntityModel<Member>(Member.class, memberId));
		add(form);
	}

}
