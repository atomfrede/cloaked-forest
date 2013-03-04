package de.atomfrede.forest.alumni.service.member;

import java.util.List;

import de.atomfrede.forest.alumni.domain.dao.filter.FilterElement;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.EntityService;

public interface MemberService extends EntityService<Member>{
	
	public Member createMember(String firstname, String lastname, String personalMail);
	
	public List<Member> list(long offset, long count, String orderProperty,
			boolean desc);
	
	public List<Member> findAllByProperty(String propertName,
			Object propertyValue);
	
	public void persistAll(List<Member> entities);
	
	public List<Member> list(long offset, long count, FilterElement... elements);

}
