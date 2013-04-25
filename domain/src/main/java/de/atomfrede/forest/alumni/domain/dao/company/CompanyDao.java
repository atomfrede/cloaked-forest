package de.atomfrede.forest.alumni.domain.dao.company;

import java.util.List;

import de.atomfrede.forest.alumni.domain.dao.DAO;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

public interface CompanyDao extends DAO<Company> {

	List<Company> list(long offset, long count, String orderProperty, boolean desc);
}
