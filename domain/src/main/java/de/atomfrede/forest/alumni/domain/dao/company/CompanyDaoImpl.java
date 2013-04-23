package de.atomfrede.forest.alumni.domain.dao.company;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

@Repository(value = "companyDao")
public class CompanyDaoImpl extends AbstractDAO<Company> implements CompanyDao {

	public CompanyDaoImpl() {
		super(Company.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Company> list(long offset, long count, String orderProperty,
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
