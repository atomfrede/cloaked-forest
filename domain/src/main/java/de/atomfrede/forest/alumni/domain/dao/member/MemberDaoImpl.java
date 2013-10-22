package de.atomfrede.forest.alumni.domain.dao.member;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.dao.filter.FilterElement;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Repository(value = "memberDao")
public class MemberDaoImpl extends AbstractDAO<Member> implements MemberDao {

	public MemberDaoImpl() {
		super(Member.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Member> findAll() {
		Criteria criteria = getSession().createCriteria(getClazz());
		criteria.addOrder(Order.asc("lastname"));
		List<Member> entities = (List<Member>) criteria.list();
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Member> findAll(Date appointedDate) {
		Criteria crit = getSession().createCriteria(getClazz());
		crit.addOrder(Order.asc("lastname"));
		crit.add(Restrictions.disjunction()
				.add(Restrictions.isNull("leaveDate"))
				.add(Restrictions.ge("leaveDate", appointedDate)));
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	/**
	 * Returns a list of members starting the the given offset up to the specific count.
	 * Ordered ascending by lastname.
	 */
	public List<Member> list(long offset, long count, FilterElement... elements) {
		Session session = this.sessionFactory.getCurrentSession();

		Criteria crit = session.createCriteria(getClazz());
		crit.addOrder(Order.asc("lastname"));
		for (FilterElement elem : elements) {
			crit.add(Restrictions.ilike(elem.getPropertyName(),
					"%" + elem.getFilter() + "%"));
		}
		crit.setFirstResult((int) offset);
		crit.setMaxResults((int) count);

		return crit.list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Member> list(long offset, long count, Date appointedDate,
			FilterElement... elements) {
		if(appointedDate == null) {
			return list(offset, count, elements);
		} else {
			Session session = this.sessionFactory.getCurrentSession();

			Criteria crit = session.createCriteria(getClazz());
			crit.addOrder(Order.asc("lastname"));
			for (FilterElement elem : elements) {
				crit.add(Restrictions.ilike(elem.getPropertyName(),
						"%" + elem.getFilter() + "%"));
			}
			crit.add(Restrictions.disjunction()
					.add(Restrictions.isNull("leaveDate"))
					.add(Restrictions.ge("leaveDate", appointedDate)));
			crit.setFirstResult((int) offset);
			crit.setMaxResults((int) count);
			return crit.list();
		}
	}

	@Override
	public List<Member> list(long offset, long count, Date appointedDate) {
		Session session = this.sessionFactory.getCurrentSession();

		Criteria crit = session.createCriteria(getClazz());
		crit.addOrder(Order.asc("lastname"));
		crit.add(Restrictions.disjunction()
				.add(Restrictions.isNull("leaveDate"))
				.add(Restrictions.ge("leaveDate", appointedDate)));
		crit.setFirstResult((int) offset);
		crit.setMaxResults((int) count);
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Member> list(long offset, long count, String orderProperty,
			boolean desc) {
		Session session = this.sessionFactory.getCurrentSession();

		Criteria crit = session.createCriteria(getClazz());
		if (desc) {
			crit.addOrder(Order.desc(orderProperty));
		} else {
			crit.addOrder(Order.asc(orderProperty));
		}
		
		crit.setFirstResult((int) offset);
		crit.setMaxResults((int) count);

		return crit.list();
	}
}
