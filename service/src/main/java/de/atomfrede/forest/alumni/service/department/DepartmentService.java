package de.atomfrede.forest.alumni.service.department;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.service.EntityService;

/**
 * Service methods interaction with {@link Department}s.
 * 
 * @author fred
 * 
 */
public interface DepartmentService extends EntityService<Department> {

	/**
	 * Returns a specific subset of all departments containing to a specific
	 * company.
	 * 
	 * The content is stating at the specific {@code offset} and at most
	 * {@code count} elements are retrieved.
	 * 
	 * @param offset
	 *            starting offset
	 * @param count
	 *            number of elements to load
	 * @param orderProperty
	 *            property to order
	 * @param desc
	 *            desc or asc order
	 * @param companyId
	 *            id of company departments should be retrieved for
	 * @return
	 */
	List<Department> list(long offset, long count, String orderProperty,
			boolean desc, Long companyId);

	/**
	 * Creates a new department with a given name.
	 * 
	 * @param department
	 * @return
	 */
	Department createDepartment(String department, String company)
			throws DepartmentAlreadyExistingException;

}
