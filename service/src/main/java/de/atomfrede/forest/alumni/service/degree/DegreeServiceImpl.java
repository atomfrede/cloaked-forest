package de.atomfrede.forest.alumni.service.degree;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@Service(value = "degreeService")
@Transactional(rollbackFor = Exception.class)
public class DegreeServiceImpl implements DegreeService {

	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	private DegreeDao degreeDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
			return sessionFactory.openSession();
		}
	}

	@Override
	public List<Degree> list(long offset, long count) {
		return degreeDao.list(offset, count);
	}

	@Override
	public List<Degree> findAll() {
		return degreeDao.findAll();
	}

	@Override
	public Degree findById(Long id) {
		return degreeDao.findById(id);
	}

	@Override
	public Degree findByProperty(String propertyName, Object propertyValue) {
		return degreeDao.findByProperty(propertyName, propertyValue);
	}

	@Override
	public void remove(Degree entity) {
		degreeDao.remove(entity);
	}

	@Override
	public void persist(Degree entity) {
		degreeDao.persist(entity);
	}

	@Override
	public long count() {
		return degreeDao.count();
	}

	@Override
	public Degree createDegree(String title, String shortform) {
		Degree deg = new Degree();
		deg.setShortForm(shortform);
		deg.setTitle(title);
		deg.setId(System.currentTimeMillis());

		degreeDao.persist(deg);
		return deg;
	}

	@Override
	public boolean deleteDegree(Degree degree) {
		degreeDao.remove(degree);
		return true;
	}

	@Override
	public boolean deleteDegree(long id) {
		degreeDao.remove(id);
		return true;
	}

	@Override
	public List<Degree> list(long offset, long count, String orderProperty,
			boolean desc) {
		return degreeDao.list(offset, count, orderProperty, desc);
	}

}
