package de.atomfrede.forest.alumni.service.company;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.service.EntityService;

public interface CompanyService extends EntityService<Company>{

	List<String> getTypeAheadCompanies();
	
	Company createCompany(String company) throws CompanyAlreadyExistingException;
	
	boolean alreadyExisting(String company);
	
	boolean departmentAlreadyExisting(String company, String department);
	
	List<Company> list(long offset, long count, String orderProperty, boolean desc, Long sectorId);
}
