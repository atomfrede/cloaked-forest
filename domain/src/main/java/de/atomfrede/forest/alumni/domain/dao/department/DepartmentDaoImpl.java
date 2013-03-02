package de.atomfrede.forest.alumni.domain.dao.department;

import org.springframework.stereotype.Repository;

import de.atomfrede.forest.alumni.domain.dao.AbstractDAO;
import de.atomfrede.forest.alumni.domain.entity.department.Department;

@Repository(value = "departmentDao")
public class DepartmentDaoImpl extends AbstractDAO<Department> implements
		DepartmentDao {

	public DepartmentDaoImpl() {
		super(Department.class);
	}
}
