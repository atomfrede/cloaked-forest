package de.atomfrede.forest.alumni.service.company;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.company.Company;

public interface CompanyService {

	List<String> getTypeAheadCompanies();
	
	Company createCompany(String company);
}
