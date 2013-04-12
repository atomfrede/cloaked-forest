package de.atomfrede.forest.alumni.domain.dao.degree;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@Repository(value = "degreeDao")
public class DegreeDaoImpl extends AbstractDAO<Degree> implements DegreeDao {

	public DegreeDaoImpl() {
		super(Degree.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Degree> list(long offset, long count, String orderProperty,
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
