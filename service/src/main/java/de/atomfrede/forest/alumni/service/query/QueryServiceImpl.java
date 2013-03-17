package de.atomfrede.forest.alumni.service.query;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.jta.platform.internal.ResinJtaPlatform;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.service.query.filter.BetweenFilter;
import de.atomfrede.forest.alumni.service.query.filter.Filter;

@Service(value = "queryService")
@Transactional(rollbackFor = Exception.class)
public class QueryServiceImpl implements QueryService {

	@Resource
	protected SessionFactory sessionFactory;
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<AbstractEntity> queryDatabase(Query<?> query) {
		Criteria crit = getSession().createCriteria(query.clazz);
		for(Filter filter:query.filters){
			switch (filter.getType()) {
			case LIKE:
				crit.add(Restrictions.like(filter.getPropertyName(), filter.getValue()));
				break;
			case EQ:
				crit.add(Restrictions.eq(filter.getPropertyName(), filter.getValue()));
				break;
			case BETWEEN:
				BetweenFilter bf = (BetweenFilter)filter;
				crit.add(Restrictions.between(filter.getPropertyName(), bf.getValue(), bf.getValue2()));
				break;
			default:
				break;
			}
		}
		return crit.list();
	}
}
