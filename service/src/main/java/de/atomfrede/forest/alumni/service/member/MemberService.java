package de.atomfrede.forest.alumni.service.member;

import java.util.Date;
import java.util.List;
import java.util.Map;

import de.atomfrede.forest.alumni.domain.dao.filter.FilterElement;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.EntityService;

public interface MemberService extends EntityService<Member> {

	Member createMember(String firstname, String lastname, String personalMail);

	boolean deleteMember(Member member);

	boolean deleteMember(long id);

	List<Member> list(long offset, long count, String orderProperty,
			boolean desc);

	List<Member> findAllByProperty(String propertName, Object propertyValue);

	void persistAll(List<Member> entities);

	List<Member> list(long offset, long count, FilterElement... elements);

	/**
	 * Retrieves the member count per year.
	 * 
	 * Usefull for generating a simple line plot of evolution of member count.
	 * 
	 * @return
	 */
	Map<Date, Integer> getMemberCountPerYear(Date from);

	Map<Date, Integer> getMemberCountPerYear(Date from, Date to);

	Map<String, Integer> getMembersPerSector();

	Map<String, Integer> getMembersPerSector(boolean withZero);

	Map<Degree, Integer> getMembersPerDegree();
	
	Member getNextMember(long id);
	
	Member getPrevMember(long id);

}
