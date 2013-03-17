package de.atomfrede.forest.alumni.service.member.profession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.member.MemberService;
import de.atomfrede.forest.alumni.service.member.professsion.ProfessionService;
import de.atomfrede.forest.alumni.service.query.QueryServiceImplTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "../../../../../../../domain-context.xml" })
public class ProfessionServiceImplTest {
	
	private final Log log = LogFactory.getLog(QueryServiceImplTest.class);
	
	@Autowired
	DegreeDao degreeDao;
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	ProfessionService professionService;
	
	Degree bachelor, master;
	Member fred, john, max;
	String profession1 = "Informatik";
	String profession2 = "BWL";
	
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
		
		fred.setProfession(profession1);
		john.setProfession(profession1);
		max.setProfession(profession2);
		
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
	public void listOfProfessions(){
		List<String> professions = professionService.getTypeaheadProfession();
		assertEquals(2, professions.size());
	}

}
