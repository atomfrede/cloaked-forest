package de.atomfrede.forest.alumni.service.department;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.service.company.CompanyService;

@Service(value = "departmentService")
@Transactional(rollbackFor = Exception.class)
public class DepartmentServiceImpl implements DepartmentService {
	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private CompanyService companyService;

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
	public List<Department> list(long offset, long count, String orderProperty,
			boolean desc, Long companyId) {
		Session session = getSession();
		Criteria crit = session.createCriteria(Department.class);
		crit.add(Restrictions.eq("company.id", companyId));
		if (desc) {
			crit.addOrder(Order.desc(orderProperty));
		} else {
			crit.addOrder(Order.asc(orderProperty));
		}
		crit.setFirstResult((int) offset);
		crit.setMaxResults((int) count);
		return crit.list();
	}

	@Override
	@Transactional
	public Department createDepartment(String department, String company)
			throws DepartmentAlreadyExistingException {
		if (companyService.departmentAlreadyExisting(company, department)) {
			throw new DepartmentAlreadyExistingException(department);
		}
		Department dp = new Department();
		dp.setId(System.currentTimeMillis());
		dp.setDepartment(department);

		departmentDao.persist(dp);
		return dp;
	}

	@Override
	public List<Department> list(long offset, long count) {
		return departmentDao.list(offset, count);
	}

	@Override
	public List<Department> findAll() {
		return departmentDao.findAll();
	}

	@Override
	public Department findById(Long id) {
		return departmentDao.findById(id);
	}

	@Override
	public Department findByProperty(String propertyName, Object propertyValue) {
		return departmentDao.findByProperty(propertyName, propertyValue);
	}

	@Override
	public void remove(Department entity) {
		departmentDao.remove(entity);
	}

	@Override
	public void persist(Department entity) {
		departmentDao.persist(entity);
	}

	@Override
	public long count() {
		return departmentDao.count();
	}
}
