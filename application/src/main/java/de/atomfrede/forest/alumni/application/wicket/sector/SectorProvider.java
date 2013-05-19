package de.atomfrede.forest.alumni.application.wicket.sector;

import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@SuppressWarnings("serial")
public class SectorProvider implements IDataProvider<Sector> {

	@SpringBean
	private SectorDao sectorDao;

	public SectorProvider() {
		Injector.get().inject(this);
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<? extends Sector> iterator(long offset, long count) {
		return sectorDao.list(offset, count, "sector", false).iterator();
	}

	@Override
	public IModel<Sector> model(Sector object) {
		return new AbstractEntityModel<Sector>(object);
	}

	@Override
	public long size() {
		return sectorDao.count();
	}

}
