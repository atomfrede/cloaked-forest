package de.atomfrede.forest.alumni.service.member;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	/**
	 * Retrieves the member count per year.
	 * 
	 * Usefull for generating a simple line plot of evolution of member count.
	 * @return
	 */
	public Map<Date, Integer> getMemberCountPerYear(Date from);
	
	public Map<Date, Integer> getMemberCountPerYear(Date from, Date to);
	
	public Map<String, Integer> getMembersPerSector();
	
	public Map<String, Integer> getMembersPerSector(boolean withZero);


}
