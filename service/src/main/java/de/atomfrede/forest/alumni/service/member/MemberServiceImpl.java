package de.atomfrede.forest.alumni.service.member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import org.hibernate.jdbc.ReturningWork;
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
	@Transactional(readOnly = true)
	public List<Member> list(final long offset, final long count) {
		return memberDao.list(offset, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> findAll() {
		return memberDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Member findById(final Long id) {
		return memberDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Member findByProperty(final String propertyName,
			final Object propertyValue) {
		return memberDao.findByProperty(propertyName, propertyValue);
	}

	@Override
	@Transactional
	public void remove(Member entity) {
		memberDao.remove(entity);
	}

	@Override
	@Transactional
	public void persist(Member entity) {
		memberDao.persist(entity);

	}

	@Override
	@Transactional(readOnly = true)
	public long count() {
		return memberDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> list(final long offset, final long count,
			final FilterElement... elements) {
		return memberDao.list(offset, count, elements);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> list(final long offset, final long count,
			final String orderProperty, final boolean desc) {
		return memberDao.list(offset, count, orderProperty, desc);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> findAllByProperty(final String propertName,
			final Object propertyValue) {
		return memberDao.findAllByProperty(propertName, propertyValue);
	}

	@Override
	@Transactional
	public void persistAll(List<Member> entities) {
		memberDao.persistAll(entities);
	}

	@Override
	@Transactional
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
	@Transactional(readOnly = true)
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
			// Leave date is null or <= end
			crit.add(Restrictions.disjunction()
					.add(Restrictions.isNull("leaveDate"))
					.add(Restrictions.ge("leaveDate", start.toDate())));
			int size = crit.list().size();
			values.put(start.toDate(), size);
			start = start.year().addToCopy(1);
		}
		return values;
	}

	@Transactional(readOnly = true)
	public Map<Date, Integer> getMemberCountPerYear(final Date from) {
		return getMemberCountPerYear(from, new Date());
	}

	@Transactional(readOnly = true)
	public Map<String, Integer> getMembersPerSector() {
		return getMembersPerSector(false);
	}

	@Transactional(readOnly = true)
	public Map<String, Integer> getMembersPerSector(final boolean withZero) {
		Map<String, Integer> values = new HashMap<>();

		List<Sector> sectors = sectorDao.findAll();

		for (Sector sec : sectors) {
			Criteria crit = getSession().createCriteria(Member.class);
			crit.add(Restrictions.eq("sector", sec));
			crit.add(Restrictions.isNull("leaveDate"));

			int value = crit.list().size();
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
	public boolean leaveMember(Member member, Date leaveDate) {
		member.setLeaveDate(leaveDate);
		memberDao.persist(member);
		return true;
	}

	@Override
	@Transactional
	public boolean leaveMember(long id, Date leaveDate) {
		Member mem = memberDao.findById(id);
		return leaveMember(mem, leaveDate);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Degree, Integer> getMembersPerDegree() {
		Map<Degree, Integer> values = new HashMap<>();
		List<Degree> allDegrees = degreeDao.findAll();
		for (Degree deg : allDegrees) {
			Criteria crit = getSession().createCriteria(Member.class);
			crit.add(Restrictions.eq("degree", deg));
			crit.add(Restrictions.isNull("leaveDate"));
			int size = crit.list().size();
			values.put(deg, size);
		}
		return values;
	}

	@Override
	@Transactional(readOnly = true)
	public Member getNextMember(final long id) {
		return getSession().doReturningWork(new ReturningWork<Member>() {

			@Override
			public Member execute(Connection connection) throws SQLException {
				Statement stm = connection.createStatement();
				final String sql = "SELECT m.id as id, m.lastname as lastname, m.firstname as firstname FROM member m ORDER BY m.lastname";
				stm.execute(sql);

				ResultSet rs = stm.getResultSet();

				ArrayList<Long> idList = new ArrayList<>();
				ArrayList<String> lastnames = new ArrayList<>();

				ArrayList<String> fullnames = new ArrayList<>();
				while (rs.next()) {
					long id = rs.getLong("id");
					String lastname = rs.getString("lastname");
					String firstname = rs.getString("firstname");

					idList.add(id);
					lastnames.add(lastname);
					fullnames.add(lastname + " " + firstname);
				}

				Member cMember = findById(id);
				String nameToFind = cMember.getLastname() + " "
						+ cMember.getFirstname();
				int index = 0;
				for(String name:fullnames){
					if(name.equals(nameToFind)){
						break;
					}
					index++;
				}
				//int index = Collections.binarySearch(fullnames, nameToFind);
				long nextId;
				if (index + 1 != lastnames.size()) {
					nextId = idList.get(index + 1);
				} else {
					nextId = idList.get(0);
				}
				while (nextId == id) {
					index++;
					if (index + 1 < lastnames.size()) {
						nextId = idList.get(index + 1);
					} else {
						nextId = idList.get(0);
					}
				}

				Member nextMember = findById(nextId);
				return nextMember;
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public Member getPrevMember(final long id) {
		return getSession().doReturningWork(new ReturningWork<Member>() {

			@Override
			public Member execute(Connection connection) throws SQLException {
				Statement stm = connection.createStatement();
				final String sql = "SELECT m.id as id, m.lastname as lastname, m.firstname as firstname FROM member m ORDER BY m.lastname";
				stm.execute(sql);

				ResultSet rs = stm.getResultSet();

				ArrayList<Long> idList = new ArrayList<>();
				ArrayList<String> lastnames = new ArrayList<>();

				ArrayList<String> fullnames = new ArrayList<>();
				while (rs.next()) {
					long id = rs.getLong("id");
					String lastname = rs.getString("lastname");
					String firstname = rs.getString("firstname");

					idList.add(id);
					lastnames.add(lastname);
					fullnames.add(lastname + " " + firstname);
				}

				Member cMember = findById(id);
				String nameToFind = cMember.getLastname() + " "
						+ cMember.getFirstname();
				int index = 0;
				for(String name:fullnames){
					if(name.equals(nameToFind)){
						break;
					}
					index++;
				}
				
//				int index = Collections.binarySearch(fullnames, nameToFind
//						+ " " + cMember.getFirstname());
				long nextId;
				if (index - 1 >= 0) {
					nextId = idList.get(index - 1);
				} else {
					nextId = idList.get(idList.size() - 1);
				}
				
				while (nextId == id) {
					index--;
					if (index - 1 > -1) {
						nextId = idList.get(index - 1);
					} else {
						nextId = idList.get(idList.size() - 1);
					}
				}

				Member prevMember = findById(nextId);
				return prevMember;
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> findAll(Date appointedDate) {
		return memberDao.findAll(appointedDate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> findAll(DateTime appointedDate) {
		return findAll(appointedDate.toDate());
	}
}
