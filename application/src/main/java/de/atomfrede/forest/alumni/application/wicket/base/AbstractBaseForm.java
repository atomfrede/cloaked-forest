package de.atomfrede.forest.alumni.application.wicket.base;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import de.atomfrede.forest.alumni.application.wicket.WicketApplication;
import de.atomfrede.forest.alumni.application.wicket.security.UserAuthModel;
import de.atomfrede.forest.alumni.application.wicket.security.UserSession;
import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;

public abstract class AbstractBaseForm<T extends AbstractEntity> extends
		Form<T> {

	public AbstractBaseForm(String id, IModel<T> model) {
		super(id, model);
	}

	/**
	 * Get application specific UserSession
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserSession<UserAuthModel> getSession() {
		return (UserSession<UserAuthModel>) Session.get();
	}

	/**
	 * Get application specific WebApplication
	 */
	public WicketApplication getApp() {
		return (WicketApplication) getApplication();
	}

}
