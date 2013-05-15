package de.atomfrede.forest.alumni.service.company;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.company.Company;

public interface CompanyService {

	List<String> getTypeAheadCompanies();
	
	Company createCompany(String company);
	
	boolean alreadyExisting(String company);
	
	List<Company> list(long offset, long count, String orderProperty, boolean desc, Long sectorId);
}
