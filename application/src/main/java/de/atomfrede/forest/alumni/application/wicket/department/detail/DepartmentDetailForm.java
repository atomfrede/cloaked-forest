package de.atomfrede.forest.alumni.application.wicket.department.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.markup.html.bootstrap.form.IDataSource;
import de.agilecoders.wicket.markup.html.bootstrap.form.Typeahead;
import de.agilecoders.wicket.markup.html.bootstrap.form.TypeaheadConfig;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.CompanyPage;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.service.company.CompanyService;

@SuppressWarnings("serial")
public class DepartmentDetailForm extends BootstrapForm<Department> {

	@SpringBean
	private CompanyService companyService;

	private Type mEditType;

	private NotificationPanel feedbackPanel;
	private WebMarkupContainer departmentWrapper, companyWrapper;

	private RequiredTextField<String> department;
	private Typeahead<String> typeahead;

	private TextField<String> street, number, addon, postcode, country, town,
			internet;

	private String _department, _company, _street, _number, _addon, _postcode,
			_country, _town, _internet;

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
				target.add(departmentWrapper);
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

		departmentWrapper = new WebMarkupContainer("department.wrapper");
		departmentWrapper.setOutputMarkupId(true);
		department = new RequiredTextField<String>("department",
				new PropertyModel<String>(this, "_department"));
		departmentWrapper.add(department);

		add(departmentWrapper);

		companyWrapper = new WebMarkupContainer("department.company.wrapper");
		companyWrapper.setOutputMarkupId(true);
		companyWrapper.add(addTypeahead("company"));

		add(companyWrapper);
		
		setupAdressFields();

	}

	private void setupAdressFields() {
		street = new TextField<>("personal.street", new PropertyModel<String>(this, "_street"));
		number = new TextField<>("personal.number", new PropertyModel<String>(this, "_number"));
		addon = new TextField<>("personal.addon", new PropertyModel<String>(this, "_addon"));
		town = new TextField<>("personal.town", new PropertyModel<String>(this, "_town"));
		postcode = new TextField<>("personal.postcode", new PropertyModel<String>(this, "_postcode"));
		internet = new TextField<>("personal.internet", new PropertyModel<String>(this, "_internet"));
		country = new TextField<>("personal.country", new PropertyModel<String>(this, "_country"));
		
		add(town, street, number, addon, postcode, country, internet);
	}

	private Component addTypeahead(String markupId) {
		final IDataSource<String> dataSource = new IDataSource<String>() {

			@Override
			public List<String> load() {
				return companyService.getTypeAheadCompanies();
			}
		};

		PropertyModel<String> model = new PropertyModel<>(this, "_company");
		typeahead = new Typeahead<String>(markupId, model, dataSource,
				new TypeaheadConfig().withNumberOfItems(20));
		typeahead.setRequired(true);

		return typeahead;
	}
}
