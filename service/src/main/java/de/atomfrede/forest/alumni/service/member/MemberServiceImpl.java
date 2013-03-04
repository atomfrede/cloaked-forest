package de.atomfrede.forest.alumni.service.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.atomfrede.forest.alumni.domain.dao.contact.ContactDataDao;
import de.atomfrede.forest.alumni.domain.dao.filter.FilterElement;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Service(value = "memberService")
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDao memberDao;

	@Autowired
	ContactDataDao contactDao;

	@Override
	public List<Member> list(long offset, long count) {
		return memberDao.list(offset, count);
	}

	@Override
	public List<Member> findAll() {
		return memberDao.findAll();
	}

	@Override
	public Member findById(Long id) {
		return memberDao.findById(id);
	}

	@Override
	public Member findByProperty(String propertyName, Object propertyValue) {
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
	public List<Member> list(long offset, long count, FilterElement... elements) {
		return memberDao.list(offset, count, elements);
	}

	@Override
	public List<Member> list(long offset, long count, String orderProperty,
			boolean desc) {
		return memberDao.list(offset, count, orderProperty, desc);
	}

	@Override
	public List<Member> findAllByProperty(String propertName,
			Object propertyValue) {
		return memberDao.findAllByProperty(propertName, propertyValue);
	}

	@Override
	public void persistAll(List<Member> entities) {
		memberDao.persistAll(entities);
	}

	@Override
	public Member createMember(String firstname, String lastname,
			String personalMail) {
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
}
