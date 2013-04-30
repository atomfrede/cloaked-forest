package de.atomfrede.forest.alumni.domain.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

@Repository
public abstract class AbstractDAO<EntityClass extends AbstractEntity>
		implements DAO<EntityClass> {

	protected Class<EntityClass> clazz;
	protected Session session;

	public AbstractDAO(Class<EntityClass> clazz) {
		this.clazz = clazz;
	}

	@Resource
	protected SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
			return sessionFactory.openSession();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<EntityClass> list(long offset, long count, String orderProperty,
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
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<EntityClass> list(long offset, long count) {
		Session session = this.sessionFactory.getCurrentSession();

		Criteria crit = session.createCriteria(getClazz());
		crit.setFirstResult((int) offset);
		crit.setMaxResults((int) count);

		return crit.list();
	}

	@Transactional
	public void persist(EntityClass entity) {
		getSession().saveOrUpdate(entity);
	}

	@Transactional
	public void persistAll(List<EntityClass> entities) {
		Session se = getSession();
		for (EntityClass entity : entities) {
			se.saveOrUpdate(entity);
		}
	}

	@Transactional
	public void remove(EntityClass entity) {
		getSession().delete(entity);
	}

	@Transactional
	public void remove(Long id) {
		EntityClass objectToDelete = (EntityClass) getSession().load(
				getClazz(), id);
		getSession().delete(objectToDelete);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public EntityClass findById(Long id) {
		return (EntityClass) getSession().get(getClazz(), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<EntityClass> findAll() {
		Criteria criteria = getSession().createCriteria(getClazz());
		List<EntityClass> entities = (List<EntityClass>) criteria.list();
		return entities;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public EntityClass findByProperty(String propertyName, Object propertyValue) {
		Criteria crit = getSession().createCriteria(clazz);
		crit.add(Restrictions.eq(propertyName, propertyValue));
		return (EntityClass) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<EntityClass> findAllByProperty(String propertyName,
			Object propertyValue) {
		Criteria crit = getSession().createCriteria(clazz);
		crit.add(Restrictions.eq(propertyName, propertyValue));
		return crit.list();
	}

	@Override
	@Transactional(readOnly = true)
	public long count() {
		return ((Long) getSession().createQuery(
				"select count(*) from " + clazz.getSimpleName()).uniqueResult())
				.intValue();
	}

	@Override
	public Class<EntityClass> getClazz() {
		return clazz;
	}
}
