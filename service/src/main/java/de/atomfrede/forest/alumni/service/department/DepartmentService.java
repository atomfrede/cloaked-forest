package de.atomfrede.forest.alumni.service.department;

import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.department.Department;

public interface DepartmentService {

	List<Department> list(long offset, long count, String orderProperty, boolean desc, Long companyId);
	
	Department createDepartment(String department);
}
