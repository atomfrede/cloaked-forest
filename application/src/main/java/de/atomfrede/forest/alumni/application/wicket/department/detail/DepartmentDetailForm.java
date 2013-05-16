package de.atomfrede.forest.alumni.application.wicket.department.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.markup.html.bootstrap.form.BootstrapForm;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.CompanyPage;
import de.atomfrede.forest.alumni.domain.entity.department.Department;

@SuppressWarnings("serial")
public class DepartmentDetailForm extends BootstrapForm<Department>{

	private Type mEditType;
	
	private NotificationPanel feedbackPanel;
	
	public DepartmentDetailForm(String componentId, IModel<Department> model,
			Type editType) {
		super(componentId, model);
		
		this.mEditType = editType;

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
//				target.add(companyWrapper);
			}
		};

		add(submitBtn);
		
		BootstrapLink<Void> cancel = new BootstrapLink<Void>("btn-cancel",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				setResponsePage(CompanyPage.class);
			}
		};

		add(cancel);

		cancel.setLabel(Model.of(_("global.cancel")));
		
	}
}
