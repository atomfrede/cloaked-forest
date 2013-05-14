package de.atomfrede.forest.alumni.service.company;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

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
	public Company createCompany(String company) {
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

}
