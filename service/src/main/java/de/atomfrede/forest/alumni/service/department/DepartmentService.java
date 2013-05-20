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
public interface DepartmentService extends EntityService<Department>{

	List<Department> list(long offset, long count, String orderProperty, boolean desc, Long companyId);
	
	/**
	 * Creates a new department with a given name.
	 * 
	 * @param department
	 * @return
	 */
	Department createDepartment(String department, String company) throws DepartmentAlreadyExistingException;
	
}
