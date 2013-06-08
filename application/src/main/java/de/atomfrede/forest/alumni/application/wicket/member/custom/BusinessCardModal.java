package de.atomfrede.forest.alumni.application.wicket.member.custom;

import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;

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

	public BusinessCardModal(String markupId, Long memberId) {
		super(markupId);

		add(new BusinessCardPanel("content", memberId));
	}
}
