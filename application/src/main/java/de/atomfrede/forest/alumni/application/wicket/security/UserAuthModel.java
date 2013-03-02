package de.atomfrede.forest.alumni.application.wicket.security;

import java.io.Serializable;

import javax.persistence.EntityNotFoundException;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.entity.user.AnonymousUser;
import de.atomfrede.forest.alumni.domain.entity.user.User;

public class UserAuthModel extends AbstractEntityModel<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5019674040250627202L;

	boolean isAnonymous = true;

	public UserAuthModel(Class<? extends User> clazz, Serializable id) {
		super(clazz, id);
		if (id != null && !id.equals(-1L)) {
			isAnonymous = false;
		} else {
			isAnonymous = true;
		}
	}

	@Override
	public User getObject() {
		try {
			//FIXME CHeck why this throws an exception for newly created/registered users
			// saying no hibernate session is available 
			entity.getId();
		} catch (Exception e) {
			entity = null;
		}
		if (entity == null) {
			if (id != null) {
				entity = load(id);
				if (entity == null && !isAnonymous) {
					throw new EntityNotFoundException("Entity of type " + clazz
							+ " with id " + id + " could not be found.");
				} else if (entity == null) {
					entity = new AnonymousUser();
				}
			}
		}
		return entity;

	}

	protected User load(Serializable id) {
		if (!isAnonymous) {
			return (User) mEntityLoader.load(clazz, id);
		} else {
			return new AnonymousUser();
		}
	}

}
