package de.atomfrede.forest.alumni.service.sector;

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

import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@Service(value = "sectorService")
@Transactional(rollbackFor = Exception.class)
public class SectorServiceImpl implements SectorService {

	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	private SectorDao sectorDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
			return sessionFactory.openSession();
		}
	}

	@Override
	@Transactional
	public List<String> getTypeAheadSectors() {
		List<String> results = new ArrayList<>();
		Criteria crit = getSession().createCriteria(Sector.class);
		crit.setProjection(Projections.property("sector"));
		@SuppressWarnings("rawtypes")
		List values = crit.list();

		for (Object value : values) {
			results.add(value + "");
		}
		return results;
	}

	@Override
	@Transactional
	public Sector createSector(String sector) {
		Sector sec = new Sector();
		sec.setSector(sector);
		sec.setId(System.currentTimeMillis());

		sectorDao.persist(sec);
		return sec;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean alreadyExisting(String sector) {
		return sectorDao.findByProperty("sector", sector) != null;
	}

}
