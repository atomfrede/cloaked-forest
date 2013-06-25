package de.atomfrede.forest.alumni.application.wicket.member.custom;

import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

/**
 * Content to display a simple "buisnessCard" for a specific user inside a modal
 * dialog.
 * 
 * @author fred
 * 
 */
@SuppressWarnings("serial")
public class BusinessCardModal extends Modal {

	@SpringBean
	private MemberDao memberDao;
	
	private BusinessCardPanel content;

	public BusinessCardModal(String markupId, Long memberId) {
		super(markupId);
		content = new BusinessCardPanel("content", memberId);
		add(content);
	}
	
	public void update(Long memberId){
		content.replaceWith(new BusinessCardPanel("content", memberId));
		Member mem = memberDao.findById(memberId);
		String headerTxt = " " + mem.getFirstname() + " " + mem.getLastname();
		header(Model.of(headerTxt));
	}
}
