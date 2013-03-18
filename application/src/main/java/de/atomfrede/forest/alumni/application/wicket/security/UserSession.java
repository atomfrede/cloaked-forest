package de.atomfrede.forest.alumni.application.wicket.security;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import de.atomfrede.forest.alumni.domain.entity.user.User;

public class UserSession<T extends UserAuthModel> extends WebSession implements
		IUserSession<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4564013929977304560L;

	// default user, ex. AnonymousUser
	protected T defaultUser;
	// current user
	protected T user;

	public UserSession(Request request) {
		this(request, (T) new UserAuthModel(User.class, -1L));
	}

	public UserSession(Request request, T defaultUser) {
		super(request);

		if (defaultUser == null) {
			throw new IllegalArgumentException("Default User can't be null.");
		}

		this.defaultUser = defaultUser;
		setUser(defaultUser);
	}

	@Override
	public T getUser() {
		return this.user;
	}

	@Override
	public void setUser(T user) {
		this.user = user;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		setUser(defaultUser);
	}

}
