package de.atomfrede.forest.alumni.service.company;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.department.Department;

@Service(value = "companyService")
@Transactional(rollbackFor = Exception.class)
public class CompanyServiceImpl implements CompanyService {

	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	private CompanyDao companyDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
			return sessionFactory.openSession();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getTypeAheadCompanies() {
		List<String> results = new ArrayList<>();
		Criteria crit = getSession().createCriteria(Company.class);
		crit.setProjection(Projections.property("company"));
		@SuppressWarnings("rawtypes")
		List values = crit.list();

		for (Object value : values) {
			results.add(value + "");
		}
		return results;
	}

	@Override
	@Transactional
	public Company createCompany(String company) throws CompanyAlreadyExistingException{
		if(alreadyExisting(company)){
			throw new CompanyAlreadyExistingException(company);
		}
		Company cp = new Company();
		cp.setId(System.currentTimeMillis());
		cp.setCompany(company);

		companyDao.persist(cp);
		return cp;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean alreadyExisting(String company) {
		return companyDao.findByProperty("company", company) != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Company> list(long offset, long count, String orderProperty,
			boolean desc, Long sectorId) {
		Session session = getSession();
		Criteria crit = session.createCriteria(Company.class);
		crit.add(Restrictions.eq("sector.id", sectorId));
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
	@Transactional(readOnly = true)
	public boolean departmentAlreadyExisting(String company, String department) {
		Company cmp = companyDao.findByProperty("company", company);
		if (cmp == null) {
			return false;
		}
		for (Department dmp : cmp.getDepartments()) {
			if (dmp.getDepartment().equals(department)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<Company> list(long offset, long count) {
		return companyDao.list(offset, count);
	}

	@Override
	public List<Company> findAll() {
		return companyDao.findAll();
	}

	@Override
	public Company findById(Long id) {
		return companyDao.findById(id);
	}

	@Override
	public Company findByProperty(String propertyName, Object propertyValue) {
		return companyDao.findByProperty(propertyName, propertyValue);
	}

	@Override
	public void remove(Company entity) {
		companyDao.remove(entity);
	}

	@Override
	public void persist(Company entity) {
		companyDao.persist(entity);
	}

	@Override
	public long count() {
		return companyDao.count();
	}
}
