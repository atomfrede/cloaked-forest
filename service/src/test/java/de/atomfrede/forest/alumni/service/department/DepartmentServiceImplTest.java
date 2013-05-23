package de.atomfrede.forest.alumni.service.department;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.service.company.CompanyAlreadyExistingException;
import de.atomfrede.forest.alumni.service.company.CompanyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "../../../../../../domain-context.xml" })
public class DepartmentServiceImplTest {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private CompanyDao companyDao;
	
	private String company_1 = "Intel";
	
	@Before
	public void setup() {
		try {
			companyService.createCompany(company_1);
		} catch (CompanyAlreadyExistingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void cleanup() {
		List<Company> companies = companyDao.findAll();
		for (Company cmp : companies) {
			companyDao.remove(cmp);
		}
	}
	
	@Test
	public void newDepartment() throws DepartmentAlreadyExistingException{
		departmentService.createDepartment("Neue Abteilung", company_1);
	}
}
