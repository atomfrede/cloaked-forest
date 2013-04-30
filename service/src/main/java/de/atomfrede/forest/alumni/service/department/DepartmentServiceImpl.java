package de.atomfrede.forest.alumni.service.department;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;

@Service(value = "departmentService")
@Transactional(rollbackFor = Exception.class)
public class DepartmentServiceImpl implements DepartmentService{
	@Resource
	private SessionFactory sessionFactory;
	
	@Autowired
	private DepartmentDao departmentDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
			return sessionFactory.openSession();
		}
	}

}
