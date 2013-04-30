package de.atomfrede.forest.alumni.application.wicket.department;

import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.entity.department.Department;

@SuppressWarnings("serial")
public class DepartmentProvider implements IDataProvider<Department>{

	@SpringBean
	private DepartmentDao departmentDao;

	public DepartmentProvider() {
		Injector.get().inject(this);
	}
	
	@Override
	public void detach() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<? extends Department> iterator(long offset, long count) {
		return departmentDao.list(offset, count, "department", false).iterator();
	}

	@Override
	public IModel<Department> model(Department object) {
		return new AbstractEntityModel<Department>(object);
	}

	@Override
	public long size() {
		return departmentDao.count();
	}

}
