package de.atomfrede.forest.alumni.service.company;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.service.EntityService;

/**
 * Encapsules all relevant operations regarding {@link Company}s
 * 
 * @author fred
 *
 */
public interface CompanyService extends EntityService<Company>{

	List<String> getTypeAheadCompanies();
	
	/**
	 * Creates a new company with the given name.
	 * 
	 * Throws a {@link CompanyAlreadyExistingException} if a company with the same name already exists.
	 * 
	 * @param company
	 * @return the newly created company.
	 * @throws CompanyAlreadyExistingException 
	 */
	Company createCompany(String company) throws CompanyAlreadyExistingException;
	
	/**
	 * Checks if a company with the given name already exists.
	 * 
	 * @param company name to check.
	 * @return false if no company with the giveb name can be found.
	 */
	boolean alreadyExisting(String company);
	
	/**
	 * Checks if for the company with the given name a department with the given name already exists.
	 * 
	 * @param company name of the company.
	 * @param department name of the department.
	 * @return true iff the company has already a department with the given name.
	 */
	boolean departmentAlreadyExisting(String company, String department);
	
	/**
	 * Retrieves a subset of companies, ordered by the given order property that belong to the given sector.
	 * 
	 * @param offset
	 * @param count
	 * @param orderProperty property the result should be ordered by, e.g. the company name.
	 * @param desc flag indicating the order direction.
	 * @param sectorId the ID of the sector the retrieved companies should belong to.
	 * @return
	 */
	List<Company> list(long offset, long count, String orderProperty, boolean desc, Long sectorId);
}
