package de.atomfrede.forest.alumni.service.member.country;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;

@Service(value = "countryService")
@Transactional(rollbackFor = Exception.class)
public class CountryServiceImpl implements CountryService {

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
	public List<String> getTypeaheadCountry() {
		Criteria crit = getSession().createCriteria(ContactData.class);
		crit.setProjection(Projections.property("country"));
		crit.setProjection(Projections.groupProperty("country"));
		@SuppressWarnings("rawtypes")
		List values = crit.list();

		List<String> results = new ArrayList<>();
		for (Object value : values) {
			results.add(value + "");
		}
		return results;
	}

}
