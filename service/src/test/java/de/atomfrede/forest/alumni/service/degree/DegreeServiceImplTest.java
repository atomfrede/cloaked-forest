package de.atomfrede.forest.alumni.service.degree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "../../../../../../domain-context.xml" })
public class DegreeServiceImplTest {

	@Autowired
	private DegreeService degreeService;
	
	@Test
	public void createDegree(){
		Degree master = degreeService.createDegree("Master of Science", "M. Sc.");
		assertNotNull(master);
		assertNotNull(master.getId());
		assertEquals("M. Sc.", master.getShortForm());
		
	}
}
