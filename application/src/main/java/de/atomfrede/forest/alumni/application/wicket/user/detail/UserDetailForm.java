package de.atomfrede.forest.alumni.application.wicket.user.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.markup.html.bootstrap.form.BootstrapForm;
import de.atomfrede.forest.alumni.application.wicket.Numbers;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.application.wicket.user.UserPage;
import de.atomfrede.forest.alumni.application.wicket.user.detail.UserDetailPage.Type;
import de.atomfrede.forest.alumni.domain.entity.user.User;
import de.atomfrede.forest.alumni.service.user.UserService;
import de.atomfrede.forest.alumni.service.user.UsernameAlreadyTakenException;

@SuppressWarnings("serial")
public class UserDetailForm extends BootstrapForm<User> {

	@SpringBean
	private UserService userService;

	private NotificationPanel feedbackPanel;

	private WebMarkupContainer usernameWrapper, passwordWrapper;

	private RequiredTextField<String> username;

	private PasswordTextField password;

	private TextField<String> firstname, lastname;

	private String _firstname, _lastname, _username, _password;

	private Type editType;

	public UserDetailForm(String id, Type editType,
			AbstractEntityModel<User> model) {
		super(id, model);
		this.editType = editType;

		feedbackPanel = new NotificationPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		AjaxButton submitBtn = new AjaxButton("submit-btn", this) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// repaint the feedback panel so that it is hidden
				target.add(feedbackPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};

		add(submitBtn);

		BootstrapLink<Void> cancel = new BootstrapLink<Void>("btn-cancel",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				setResponsePage(UserPage.class);
			}
		};
		cancel.setLabel(Model.of(_("global.cancel")));

		add(cancel);

		setupInputs();

		initFormValues(model);

	}

	private void initFormValues(AbstractEntityModel<User> model) {
		if (editType == Type.Create) {
			_firstname = "";
			_lastname = "";
			_username = "";
			_password = "";
		} else if (model.getObject() != null) {
			_firstname = model.getObject().getFirstname();
			_lastname = model.getObject().getLastname();
			_username = model.getObject().getUsername();
			// _password = model.getObject().getPassword();
			password.setRequired(false);
		}
	}

	private void setupInputs() {
		usernameWrapper = new WebMarkupContainer("user.detail.username.wrapper");
		passwordWrapper = new WebMarkupContainer("user.detail.password.wrapper");
		usernameWrapper.setOutputMarkupId(true);
		passwordWrapper.setOutputMarkupId(true);

		add(passwordWrapper);
		add(usernameWrapper);

		username = new RequiredTextField<>("user.username",
				new PropertyModel<String>(this, "_username"));
		password = new PasswordTextField("user.password",
				new PropertyModel<String>(this, "_password"));

		passwordWrapper.add(password);
		usernameWrapper.add(username);

		firstname = new TextField<>("user.firstname",
				new PropertyModel<String>(this, "_firstname"));
		lastname = new TextField<>("user.lastname", new PropertyModel<String>(
				this, "_lastname"));

		add(firstname);
		add(lastname);
	}

	@Override
	protected void onError() {
		// Only on validation errors we make the feedbackpanel visible
		this.feedbackPanel.setVisible(true);
		this.feedbackPanel.hideAfter(Duration.seconds(Numbers.TEN));
		if (!username.isValid()) {
			usernameWrapper.add(new AttributeAppender("class", " error"));
		} else {
			usernameWrapper
					.add(new AttributeModifier("class", "control-group"));
		}
		if (!password.isValid()) {
			passwordWrapper.add(new AttributeAppender("class", " error"));
		} else {
			passwordWrapper
					.add(new AttributeModifier("class", "control-group"));
		}

	}

	@Override
	public void onSubmit() {
		User user = null;
		try {
			if (editType == Type.Create) {
				user = userService.createUser(_username, _firstname, _lastname,
						_password);
				editType = Type.Edit;
				setModel(new AbstractEntityModel<User>(user));
			} else {
				user = getModelObject();
				user.setFirstname(_firstname);
				user.setLastname(_lastname);
				if (_password != null && !_password.trim().equals("")) {
					user.setPassword(_password);
				}
				if (_username != null
						&& !_username.trim().equals(user.getUsername())) {
					// Only check if username is already taken if the username
					// is changed...
					if (!userService.canCreateUser(_username)) {
						throw new UsernameAlreadyTakenException();
					}
				} else if (_username != null && !_username.trim().equals("")) {
					user.setUsername(_username);
				}

				userService.persist(user);
			}

			NotificationMessage nf = new NotificationMessage(
					Model.of("Gespeichert"));
			nf.hideAfter(Duration.seconds(Numbers.FIVE));
			success(nf);
		} catch (UsernameAlreadyTakenException e) {
			NotificationMessage nf = new NotificationMessage(
					Model.of("Benutzername bereits vergeben"));
			nf.hideAfter(Duration.seconds(Numbers.FIVE));
			error(nf);
		}
	}
}
