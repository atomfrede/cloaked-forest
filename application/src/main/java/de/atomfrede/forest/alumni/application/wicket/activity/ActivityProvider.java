package de.atomfrede.forest.alumni.application.wicket.activity;

import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.activity.ActivityDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;

@SuppressWarnings("serial")
public class ActivityProvider implements IDataProvider<Activity> {

	@SpringBean
	ActivityDao activityDao;
	
	public ActivityProvider(){
		Injector.get().inject(this);
	}
	
	@Override
	public void detach() {
		
		
	}

	@Override
	public Iterator<? extends Activity> iterator(long offset, long count) {
		return activityDao.list(offset, count).iterator();
	}

	@Override
	public long size() {
		return activityDao.count();
	}

	@Override
	public IModel<Activity> model(Activity object) {
		return new AbstractEntityModel<Activity>(object);
	}

}
