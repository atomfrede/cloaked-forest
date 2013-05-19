package de.atomfrede.forest.alumni.application.wicket.user;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.Numbers;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.user.detail.UserDetailPage;
import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.user.UserService;

@SuppressWarnings("serial")
public class UserListPanel extends Panel {

	@SpringBean
	private UserService userService;

	private UserProvider userProvider;
	private DataView<User> users;

	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;

	public UserListPanel(String id) {
		super(id);

		add(new UserListActionPanel("user-action"));

		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
	}

	private UserProvider getUserProvider() {
		if (userProvider == null) {
			userProvider = new UserProvider();
		}
		return userProvider;
	}

	private void setupModal() {
		modalWarning = new TextContentModal("modal-prompt",
				Model.of("Hallo Welt"));
		modalWarning.addCloseButton(Model.of(_("modal.close", "").getString()));
		add(modalWarning);
	}

	private void populateItems() {

		users = new DataView<User>("users", getUserProvider()) {

			@Override
			protected void populateItem(Item<User> item) {
				final User user = item.getModel().getObject();

				item.add(new Label("user-firstname", new PropertyModel<String>(
						user, "firstname")));
				item.add(new Label("user-lastname", new PropertyModel<String>(
						user, "lastname")));
				item.add(new Label("user-username", new PropertyModel<String>(
						user, "username")));

				final long userId = user.getId();
				final String firstname = user.getFirstname();
				final String lastname = user.getLastname();
				final String username = user.getUsername();

				final String fullname = firstname + " " + lastname;
				// final String shortForm = degree.getShortForm();
				// final String title = degree.getTitle();

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						editUser(userId);

					}
				};
				editUser.setIconType(IconType.pencil)
						.setSize(Buttons.Size.Mini).setInverted(false);

				BootstrapLink<Void> deleteUser = new BootstrapLink<Void>(
						"action-delete", Buttons.Type.Danger) {

					@Override
					public void onClick() {
						deleteUser(userId, fullname, username);
					}

				};
				deleteUser.setIconType(IconType.remove).setSize(
						Buttons.Size.Mini);

				item.add(editUser);
				item.add(deleteUser);
			}

		};
		users.setItemsPerPage(Numbers.TEN + Numbers.FIVE);
		users.setOutputMarkupId(true);
		wmc.add(users);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", users));
		add(wmc);
	}

	private void editUser(long id) {
		PageParameters params = new PageParameters();
		params.add(UserDetailPage.EDIT_TYPE, Type.Edit);
		params.add(UserDetailPage.USER_ID, id);
		setResponsePage(UserDetailPage.class, params);
	}

	private void deleteUser(final long id, String username, String fullname) {

		final TextContentModal modal = new TextContentModal("modal-prompt",
				Model.of(_("modal.user.text", fullname, username).getString()));
		modal.setOutputMarkupId(true);

		modal.addCloseButton(Model.of(_("modal.close", "").getString()));
		modal.header(Model.of(_("modal.degree.header", fullname, username)
				.getString()));

		AjaxLink<String> doDelete = new AjaxLink<String>("button", Model.of(_(
				"modal.delete", "").getString())) {

			@Override
			protected void onConfigure() {
				super.onConfigure();

				setBody(getDefaultModel());
				add(new ButtonBehavior(Buttons.Type.Danger));
				// add(new IconBehavior(IconType.remove));
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				doDeleteUser(id);
				target.appendJavaScript("$('.modal').modal('close');");
				setResponsePage(UserPage.class);

			}
		};

		modal.addButton(doDelete);
		this.modalWarning.replaceWith(modal);
		this.modalWarning = modal;
		modalWarning.show(true);
	}

	private void doDeleteUser(long id) {
		userService.deleteUser(id);
	}
}
