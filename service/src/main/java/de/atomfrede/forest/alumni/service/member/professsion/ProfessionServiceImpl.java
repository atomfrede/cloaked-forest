package de.atomfrede.forest.alumni.service.member.professsion;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Service(value = "professionService")
@Transactional(rollbackFor = Exception.class)
public class ProfessionServiceImpl implements ProfessionService {

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
	@Transactional
	public List<String> getTypeaheadProfession() {
		Criteria crit = getSession().createCriteria(Member.class);
		crit.setProjection(Projections.property("profession"));
		crit.setProjection(Projections.groupProperty("profession"));
		@SuppressWarnings("rawtypes")
		List values = crit.list();
		
		List<String> results = new ArrayList<>();
		for(Object value:values){
			results.add(value+"");
		}
		return results;
	}

}
