package de.atomfrede.forest.alumni.service.member;


import static org.fest.assertions.Assertions.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.atomfrede.forest.alumni.domain.entity.member.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "../../../../../../domain-context.xml" })
public class MemberServiceImplTest {

	@Autowired
	private MemberService memberService;
	
	Member fred, john, max;
	
	@Before
	public void setup() {
		fred = memberService.createMember("Frederik", "Hahne", "fred@example.org");
		john = memberService.createMember("John","Doe", "john.doe@example.org");
		max = memberService.createMember("Max", "Mustermann", "m.mustermann@example.org");
		
		memberService.persist(fred);
		memberService.persist(john);
		memberService.persist(max);
	}
	
	@After
	public void afterEachTest() {
		memberService.deleteMember(fred);
		memberService.deleteMember(john);
		memberService.deleteMember(max);
	}
	
	@Test
	public void assertThatFindAllFindsAllMembers() {
		assertThat(memberService.findAll().size()).isEqualTo(3);
	}
	
	@Test
	public void assertThatFindAllWithDateFindsCorrectMembers() {
		DateTime date = new DateTime();
		date = date.monthOfYear().setCopy(DateTimeConstants.DECEMBER);
		date = date.dayOfMonth().setCopy(31);
		date = date.year().setCopy(2011);
		
		memberService.leaveMember(fred, date.toDate());
		
		assertThat(memberService.findAll(new DateTime().toDate()).size()).isEqualTo(2);
		
		DateTime appointedDate = new DateTime();
		appointedDate = appointedDate.monthOfYear().setCopy(DateTimeConstants.DECEMBER);
		appointedDate = appointedDate.dayOfMonth().setCopy(31);
		appointedDate = appointedDate.year().setCopy(2010);
		
		assertThat(memberService.findAll(appointedDate.toDate()).size()).isEqualTo(3);
	}
}
