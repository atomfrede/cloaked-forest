package de.atomfrede.forest.alumni.application.wicket.user;

import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.user.UserDao;
import de.atomfrede.forest.alumni.domain.entity.user.User;

@SuppressWarnings("serial")
public class UserProvider implements IDataProvider<User>{

	@SpringBean
	private UserDao userDao;

	public UserProvider() {
		Injector.get().inject(this);
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<? extends User> iterator(long offset, long count) {
		return userDao.list(offset, count).iterator();
	}

	@Override
	public IModel<User> model(User object) {
		return new AbstractEntityModel<User>(object);
	}

	@Override
	public long size() {
		return userDao.count();
	}


}
