package de.atomfrede.forest.alumni.application.wicket.degree;

import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@SuppressWarnings("serial")
public class DegreeProvider implements IDataProvider<Degree> {

	@SpringBean
	private DegreeDao degreeDao;

	public DegreeProvider() {
		Injector.get().inject(this);
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<? extends Degree> iterator(long offset, long count) {
		return degreeDao.list(offset, count, "title", false).iterator();
	}

	@Override
	public IModel<Degree> model(Degree object) {
		return new AbstractEntityModel<Degree>(object);
	}

	@Override
	public long size() {
		return degreeDao.count();
	}

}
