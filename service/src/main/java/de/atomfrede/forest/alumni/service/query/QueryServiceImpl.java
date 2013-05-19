package de.atomfrede.forest.alumni.service.query;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.service.query.filter.BetweenFilter;
import de.atomfrede.forest.alumni.service.query.filter.Filter;

@Service(value = "queryService")
@Transactional(rollbackFor = Exception.class)
public class QueryServiceImpl implements QueryService {

	@Resource
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
			return sessionFactory.openSession();
		}
	}

	@Override
	@Transactional
	public List<?> queryDatabase(Query<?> query) {
		if (query == null) {
			return new ArrayList<>();
		}
		Criteria crit = getSession().createCriteria(query.clazz);
		if (query.filters != null && !query.filters.isEmpty()) {
			for (Filter filter : query.filters) {
				crit.add(getRestriction(filter));
			}
		}

		if (query.or != null && !query.or.isEmpty()) {
			List<Criterion> crits = new ArrayList<>();
			for (Filter or : query.or) {
				crits.add(getRestriction(or));
			}
			crit.add(Restrictions.or(crits.toArray(new Criterion[] {})));
		}

		if (query.and != null && !query.and.isEmpty()) {
			List<Criterion> crits = new ArrayList<>();
			for (Filter and : query.and) {
				crits.add(getRestriction(and));
			}
			crit.add(Restrictions.and(crits.toArray(new Criterion[] {})));
		}
		return crit.list();
	}

	private Criterion getRestriction(Filter filter) {
		switch (filter.getType()) {
		case LIKE:
			return Restrictions.like(filter.getPropertyName(),
					filter.getValue());
		case EQ:
			return Restrictions.eq(filter.getPropertyName(), filter.getValue());
		case BETWEEN:
			BetweenFilter bf = (BetweenFilter) filter;
			return Restrictions.between(filter.getPropertyName(),
					bf.getValue(), bf.getValue2());
		default:
			return null;
		}
	}
}
