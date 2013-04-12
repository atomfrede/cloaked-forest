package de.atomfrede.forest.alumni.application.wicket.degree.detail;


import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
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
import de.atomfrede.forest.alumni.application.wicket.degree.DegreePage;
import de.atomfrede.forest.alumni.application.wicket.degree.detail.DegreeDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.service.degree.DegreeService;

@SuppressWarnings("serial")
public class DegreeDetailForm extends BootstrapForm<Degree> {

	@SpringBean
	private DegreeService degreeService;

	private NotificationPanel feedbackPanel;

	private WebMarkupContainer shortWrapper, titleWrapper;

	private RequiredTextField<String> titleInput;

	private TextField<String> shortInput;
	
	private String _title, _short;

	private Type editType;

	public DegreeDetailForm(String id, Type editType,
			AbstractEntityModel<Degree> model) {
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
		
		BootstrapLink<Void> cancel = new BootstrapLink<Void>("btn-cancel", Buttons.Type.Default){

			@Override
			public void onClick() {
				setResponsePage(DegreePage.class);
			}
		};
		cancel.setLabel(Model.of(_("global.cancel")));
		
		add(cancel);

		initFormValues(model);

		setupInputs();
	}

	private void initFormValues(AbstractEntityModel<Degree> model) {
		if (editType == Type.Create) {
			_title = "";
			_short = "";
		} else if (model.getObject() != null) {
			_title = model.getObject().getTitle();
			_short = model.getObject().getShortForm();
		}
	}

	private void setupInputs() {
		titleWrapper = new WebMarkupContainer("degree.detail.title.wrapper");
		shortWrapper = new WebMarkupContainer("degree.detail.short.wrapper");
		titleWrapper.setOutputMarkupId(true);
		shortWrapper.setOutputMarkupId(true);

		add(titleWrapper);
		add(shortWrapper);

		titleInput = new RequiredTextField<>("degree.title",
				new PropertyModel<String>(this, "_title"));
		shortInput = new TextField<>("degree.short",
				new PropertyModel<String>(this, "_short"));

		titleWrapper.add(titleInput);
		shortWrapper.add(shortInput);
	}

	@Override
	protected void onError() {
		// Only on validation errors we make the feedbackpanel visible
		this.feedbackPanel.setVisible(true);
		this.feedbackPanel.hideAfter(Duration.seconds(10));
		if (!titleInput.isValid()) {
			titleWrapper.add(new AttributeAppender("class", " error"));
		} else {
			titleWrapper.add(new AttributeModifier("class", "control-group"));
		}
		if (!shortInput.isValid()) {
			shortWrapper.add(new AttributeAppender("class", " error"));
		} else {
			shortWrapper.add(new AttributeModifier("class", "control-group"));
		}

	}

	@Override
	public void onSubmit() {
		Degree degree = null;
		if (editType == Type.Create) {
			degree = degreeService.createDegree(_title, _short);
			editType = Type.Edit;
			setModel(new AbstractEntityModel<Degree>(degree));
		} else {
			degree = getModelObject();
			degree.setTitle(_title);
			degree.setShortForm(_short);
			
			degreeService.persist(degree);
		}
		
		NotificationMessage nf = new NotificationMessage(
				Model.of("Gespeichert"));
		nf.hideAfter(Duration.seconds(3));
		success(nf);
	}
}
