package de.atomfrede.forest.alumni.application.wicket.user;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.user.detail.UserDetailPage;

@SuppressWarnings("serial")
public class UserListActionPanel extends Panel {

	private BootstrapLink<Void> newUser;

	public UserListActionPanel(String id) {
		super(id);
		addNewUser();
	}

	private void addNewUser() {
		newUser = new BootstrapLink<Void>("btn-new-degree",
				Buttons.Type.Primary) {

			@Override
			public void onClick() {
				onNewUser();
			}
		};

		newUser.setIconType(IconType.plussign).setLabel(
				Model.of(_("user.action.new")));
		add(newUser);
	}

	private void onNewUser() {
		PageParameters params = new PageParameters();
		params.add(UserDetailPage.EDIT_TYPE, Type.Create);
		params.add(UserDetailPage.USER_ID, "-1");
		setResponsePage(UserDetailPage.class, params);
	}
}
