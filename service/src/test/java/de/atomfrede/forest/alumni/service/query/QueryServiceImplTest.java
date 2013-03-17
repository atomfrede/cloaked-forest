package de.atomfrede.forest.alumni.service.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.member.MemberService;
import de.atomfrede.forest.alumni.service.query.filter.Filter;
import de.atomfrede.forest.alumni.service.query.filter.Filter.Type;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "../../../../../../domain-context.xml" })
public class QueryServiceImplTest {
	
	private final Log log = LogFactory.getLog(QueryServiceImplTest.class);
	
	@Autowired
	DegreeDao degreeDao;
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	QueryService queryService;
	
	@Autowired
	MemberService memberService;
	
	Degree bachelor, master;
	Member fred, john, max;
	
	@Before
	public void setup(){
		//Creating some dummy members to be able to test different queries.
		fred = memberService.createMember("Frederik", "Hahne", "fred@example.org");
		john = memberService.createMember("John","Doe", "john.doe@example.org");
		max = memberService.createMember("Max", "Mustermann", "m.mustermann@example.org");
		
		//Create some degrees to use a more complicated query
		bachelor = new Degree();
		bachelor.setTitle("Bachelor of Science");
		bachelor.setId(System.currentTimeMillis());
		
		degreeDao.persist(bachelor);
		
		master = new Degree();
		master.setTitle("Master of Science");
		master.setId(System.currentTimeMillis());
		
		degreeDao.persist(master);
		
		fred.setDegree(bachelor);
		max.setDegree(bachelor);
		john.setDegree(master);
		
		memberDao.persist(fred);
		memberDao.persist(john);
		memberDao.persist(max);
	}
	
	@After
	public void cleanup(){
		memberService.deleteMember(fred);
		memberService.deleteMember(john);
		memberService.deleteMember(max);
	}
	
	@Test
	public void querySimpleProperty(){
		Query<Member> memberQuery = new Query<>(Member.class);
		Filter filter = new Filter("lastname", "Hahne", Type.LIKE);
		memberQuery.addFilter(filter);
		
		List<AbstractEntity> result = queryService.queryDatabase(memberQuery);
		
		assertEquals(1, result.size());
	}
	
	@Test
	public void queryComplexProperty(){
		Query<Member> memberQuery = new Query<>(Member.class);
		Filter filter = new Filter("degree", bachelor, Type.EQ);
		memberQuery.addFilter(filter);
		
		List<AbstractEntity> result = queryService.queryDatabase(memberQuery);
		
		assertEquals(2, result.size());
	}
	
	@Test
	public void queryCombination(){
		Query<Member> memberQuery = new Query<>(Member.class);
		Filter filter = new Filter("degree", bachelor, Type.EQ);
		Filter filter2 = new Filter("lastname", "Hahne%", Type.LIKE);
		memberQuery.addFilter(filter);
		memberQuery.addFilter(filter2);
		
		List<AbstractEntity> result = queryService.queryDatabase(memberQuery);
		
		assertEquals(1, result.size());
	}
	
	@Test
	public void queryOr(){
		Query<Member> memberQuery = new Query<>(Member.class);
		Filter filter = new Filter("degree", bachelor, Type.EQ);
		Filter filter2 = new Filter("degree", master, Type.EQ);
		memberQuery.addOr(filter);
		memberQuery.addOr(filter2);
		
		List<AbstractEntity> result = queryService.queryDatabase(memberQuery);
		
		assertEquals(3, result.size());
	}

}
