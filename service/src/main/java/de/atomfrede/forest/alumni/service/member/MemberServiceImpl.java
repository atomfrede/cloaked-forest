package de.atomfrede.forest.alumni.service.member;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.contact.ContactDataDao;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.dao.filter.FilterElement;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@Service(value = "memberService")
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService {

	private static final int days = 31;
	
	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private ContactDataDao contactDao;

	@Autowired
	private SectorDao sectorDao;

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
	public List<Member> list(final long offset, final long count) {
		return memberDao.list(offset, count);
	}

	@Override
	public List<Member> findAll() {
		return memberDao.findAll();
	}

	@Override
	public Member findById(final Long id) {
		return memberDao.findById(id);
	}

	@Override
	public Member findByProperty(final String propertyName,
			final Object propertyValue) {
		return memberDao.findByProperty(propertyName, propertyValue);
	}

	@Override
	public void remove(Member entity) {
		memberDao.remove(entity);
	}

	@Override
	public void persist(Member entity) {
		memberDao.persist(entity);

	}

	@Override
	public long count() {
		return memberDao.count();
	}

	@Override
	public List<Member> list(final long offset, final long count,
			final FilterElement... elements) {
		return memberDao.list(offset, count, elements);
	}

	@Override
	public List<Member> list(final long offset, final long count,
			final String orderProperty, final boolean desc) {
		return memberDao.list(offset, count, orderProperty, desc);
	}

	@Override
	public List<Member> findAllByProperty(final String propertName,
			final Object propertyValue) {
		return memberDao.findAllByProperty(propertName, propertyValue);
	}

	@Override
	public void persistAll(List<Member> entities) {
		memberDao.persistAll(entities);
	}

	@Override
	public Member createMember(final String firstname, final String lastname,
			final String personalMail) {
		Member mem = new Member();
		mem.setFirstname(firstname);
		mem.setLastname(lastname);
		mem.setId(System.currentTimeMillis());

		ContactData contact = new ContactData();
		contact.setEmail(personalMail);

		contactDao.persist(contact);

		mem.setContactData(contact);
		memberDao.persist(mem);

		return mem;
	}

	@Override
	@Transactional
	public Map<Date, Integer> getMemberCountPerYear(final Date from,
			final Date to) {
		Map<Date, Integer> values = new HashMap<Date, Integer>();
		// Always use December 31 as fixed date

		DateTime start = new DateTime(from.getTime());
		start = start.monthOfYear().setCopy(DateTimeConstants.DECEMBER);
		start = start.dayOfMonth().setCopy(MemberServiceImpl.days);

		DateTime end = new DateTime(to.getTime());
		end = end.monthOfYear().setCopy(DateTimeConstants.DECEMBER);
		end = end.dayOfMonth().setCopy(MemberServiceImpl.days);

		while (!start.year().equals(end.year())) {
			Criteria crit = getSession().createCriteria(Member.class);
			crit.add(Restrictions.le("entryDate", start.toDate()));
			int size = crit.list().size();
			values.put(start.toDate(), size);
			start = start.year().addToCopy(1);
		}

		return values;
	}

	public Map<Date, Integer> getMemberCountPerYear(final Date from) {
		return getMemberCountPerYear(from, new Date());
	}

	@Transactional
	public Map<String, Integer> getMembersPerSector() {
		return getMembersPerSector(false);
	}

	@Transactional
	public Map<String, Integer> getMembersPerSector(final boolean withZero) {
		Map<String, Integer> values = new HashMap<>();

		List<Sector> sectors = sectorDao.findAll();

		for (Sector sec : sectors) {
			int value = memberDao.findAllByProperty("sector", sec).size();
			if (value == 0 && withZero) {
				values.put(sec.getSector(), value);
			} else if (value != 0) {
				values.put(sec.getSector(), value);
			}
		}

		return values;
	}

	@Override
	@Transactional
	public boolean deleteMember(Member member) {
		member.clearActivities();
		memberDao.persist(member);
		memberDao.remove(member);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteMember(final long id) {
		Member mem = memberDao.findById(id);
		return deleteMember(mem);
	}

	@Override
	@Transactional
	public Map<Degree, Integer> getMembersPerDegree() {
		Map<Degree, Integer> values = new HashMap<>();
		List<Degree> allDegrees = degreeDao.findAll();
		for (Degree deg : allDegrees) {
			int size = memberDao.findAllByProperty("degree", deg).size();
			values.put(deg, size);
		}
		return values;
	}

	@Override
	@Transactional
	public Member getNextMember(long id) {
		@SuppressWarnings("unchecked")
		List<String> lastnames = getSession().createSQLQuery("SELECT m.lastname as lastname FROM member m ORDER BY m.lastname").list();
		Member cMember = findById(id);
		String nameToFind = cMember.getLastname();
		int index = Collections.binarySearch(lastnames, nameToFind);
		String nextName = "";
		if(index+1 != lastnames.size()){
			nextName = lastnames.get(index+1);
		}else{
			nextName = lastnames.get(0);
		}
		Member nextMember = findByProperty("lastname", nextName);
		return nextMember;
	}

}
