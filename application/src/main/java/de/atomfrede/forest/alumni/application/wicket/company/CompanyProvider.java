package de.atomfrede.forest.alumni.application.wicket.company;

import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

@SuppressWarnings("serial")
public class CompanyProvider implements IDataProvider<Company>{

	@SpringBean
	private CompanyDao companyDao;

	public CompanyProvider() {
		Injector.get().inject(this);
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<? extends Company> iterator(long offset, long count) {
		return companyDao.list(offset, count, "company", false).iterator();
	}

	@Override
	public IModel<Company> model(Company object) {
		return new AbstractEntityModel<Company>(object);
	}

	@Override
	public long size() {
		return companyDao.count();
	}
}
