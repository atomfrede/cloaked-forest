package de.atomfrede.forest.alumni.application.wicket.user.detail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.application.wicket.user.detail.UserDetailPage.Type;
import de.atomfrede.forest.alumni.domain.dao.user.UserDao;
import de.atomfrede.forest.alumni.domain.entity.user.User;

@SuppressWarnings("serial")
public class UserDetailPanel extends Panel{

	@SpringBean
	private UserDao userDao;

	private Type editType;
	private Long userId;

	public UserDetailPanel(String id, Type editType, Long userId) {
		super(id);
		this.editType = editType;
		this.userId = userId;

		UserDetailForm form = new UserDetailForm("detail-form",
				this.editType, new AbstractEntityModel<User>(User.class,
						userId));
		add(form);
	}
}
