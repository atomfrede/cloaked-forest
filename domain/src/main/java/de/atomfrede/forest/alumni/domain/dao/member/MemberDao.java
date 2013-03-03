package de.atomfrede.forest.alumni.domain.dao.member;

import java.util.List;

import de.atomfrede.forest.alumni.domain.dao.DAO;
import de.atomfrede.forest.alumni.domain.dao.filter.FilterElement;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

public interface MemberDao extends DAO<Member> {
	
	public List<Member> list(long offset, long count, FilterElement...elements);

	public List<Member> list(long offset, long count, String orderProperty, boolean desc);
}
