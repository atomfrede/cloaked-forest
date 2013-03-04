package de.atomfrede.forest.alumni.service.member;

import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.EntityService;

public interface MemberService extends EntityService<Member>, MemberDao{
	
	public Member createMember(String firstname, String lastname, String personalMail);

}
