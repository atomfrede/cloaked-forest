package de.atomfrede.forest.alumni.application.wicket.company;

import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.service.company.CompanyService;

@SuppressWarnings("serial")
public class CompanyProvider implements IDataProvider<Company> {

	@SpringBean
	private CompanyDao companyDao;

	@SpringBean
	private CompanyService companyService;

	private Long sectorId;

	public CompanyProvider() {
		this(null);
	}

	public CompanyProvider(Long sectorId) {
		Injector.get().inject(this);
		this.sectorId = sectorId;
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<? extends Company> iterator(long offset, long count) {
		if (sectorId != null) {
			return companyService.list(offset, count, "company", false,
					sectorId).iterator();
		} else {
			return companyDao.list(offset, count, "company", false).iterator();
		}
	}

	@Override
	public IModel<Company> model(Company object) {
		return new AbstractEntityModel<Company>(object);
	}

	@Override
	public long size() {
		if (sectorId != null) {
			return companyDao.findAllByProperty("sector.id", sectorId).size();
		} else {
			return companyDao.count();
		}
	}
}
