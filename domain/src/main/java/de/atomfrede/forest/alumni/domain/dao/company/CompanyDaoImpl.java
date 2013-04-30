package de.atomfrede.forest.alumni.domain.dao.company;

import org.springframework.stereotype.Repository;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

@Repository(value = "companyDao")
public class CompanyDaoImpl extends AbstractDAO<Company> implements CompanyDao {

	public CompanyDaoImpl() {
		super(Company.class);
	}

}
