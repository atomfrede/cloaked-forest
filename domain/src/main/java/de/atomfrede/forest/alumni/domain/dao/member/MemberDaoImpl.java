package de.atomfrede.forest.alumni.domain.dao.member;

import org.springframework.stereotype.Repository;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Repository(value = "memberDao")
public class MemberDaoImpl extends AbstractDAO<Member> implements MemberDao {

	public MemberDaoImpl() {
		super(Member.class);
	}

}
