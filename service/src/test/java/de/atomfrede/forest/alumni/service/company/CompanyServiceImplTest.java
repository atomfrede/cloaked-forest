package de.atomfrede.forest.alumni.service.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "../../../../../../domain-context.xml" })
public class CompanyServiceImplTest {

	@Autowired
	private CompanyDao companyDao;
	
	@Autowired
	private CompanyService companyService;

	private String company_1 = "Intel";
	private String company_2 = "Lenovo";
	private String company_3 = "Google";
	private String company_4 = "Apple";

	@Before
	public void setup() {
		companyService.createCompany(company_1);
		companyService.createCompany(company_2);
		companyService.createCompany(company_3);
		companyService.createCompany(company_4);
	}
	
	@Test
	public void testTypeahead(){
		List<String> typeahed = companyService.getTypeAheadCompanies();
		
		assertNotNull(typeahed);
		assertEquals(4, typeahed.size());
	}
}
