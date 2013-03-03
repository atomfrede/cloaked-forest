package de.atomfrede.forest.alumni.domain.dao.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Repository(value = "memberDao")
public class MemberDaoImpl extends AbstractDAO<Member> implements MemberDao {

	public MemberDaoImpl() {
		super(Member.class);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	/**
	 * Returns a list of members starting the the given offset up to the specific count.
	 * Ordered ascending by lastname.
	 */
	public List<Member> list(long offset, long count) {
		Session session = this.sessionFactory.getCurrentSession();

		Criteria crit = session.createCriteria(getClazz());
		crit.setFirstResult((int)offset);
		crit.setMaxResults((int)count);
		crit.addOrder(Order.asc("lastname"));

		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Member> list(long offset, long count, String orderProperty, boolean desc) {
		Session session = this.sessionFactory.getCurrentSession();

		Criteria crit = session.createCriteria(getClazz());
		crit.setFirstResult((int)offset);
		crit.setMaxResults((int)count);
		if(desc){
			crit.addOrder(Order.desc(orderProperty));
		}else{
			crit.addOrder(Order.asc(orderProperty));
		}
		return crit.list();
	}
	
	

}
