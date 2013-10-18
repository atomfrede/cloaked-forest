package de.atomfrede.forest.alumni.application.wicket.member.custom;

import java.util.Date;

import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.atomfrede.forest.alumni.service.member.MemberService;

public class LeaveMemberModal extends Modal {

	@SpringBean
	private MemberService memberService;
	
	private LeaveMemberPanel content;
	
	public LeaveMemberModal(String markupId, Long memberId) {
		super(markupId);
		content = new LeaveMemberPanel("content", memberId);
		add(content);
	}
	
	public Date getEnteredDate() {
		return content.getEnteredDate();
	}
}
