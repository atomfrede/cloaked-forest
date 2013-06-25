package de.atomfrede.forest.alumni.application.wicket.department.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.IDataSource;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.Typeahead;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.TypeaheadConfig;
import de.atomfrede.forest.alumni.application.wicket.Numbers;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.CompanyPage;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.service.company.CompanyAlreadyExistingException;
import de.atomfrede.forest.alumni.service.company.CompanyService;
import de.atomfrede.forest.alumni.service.department.DepartmentAlreadyExistingException;
import de.atomfrede.forest.alumni.service.department.DepartmentService;

@SuppressWarnings("serial")
public class DepartmentDetailForm extends BootstrapForm<Department> {

	@SpringBean
	private CompanyService companyService;

	@SpringBean
	private CompanyDao companyDao;

	@SpringBean
	private DepartmentService departmentService;

	@SpringBean
	private DepartmentDao departmentDao;

	private Type mEditType;
	private Long mCompanyId;

	private NotificationPanel feedbackPanel;
	private WebMarkupContainer departmentWrapper, companyWrapper;

	private RequiredTextField<String> department;
	private Typeahead<String> company;

	private TextField<String> street, number, addon, postcode, country, town,
			internet;

	private String _department, _company, _street, _number, _addon, _postcode,
			_country, _town, _internet;

	public DepartmentDetailForm(String componentId, IModel<Department> model,
			Type editType, Long companyId) {
		super(componentId, model);

		this.mEditType = editType;
		this.mCompanyId = companyId;

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

		initFormValues();

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
		street = new TextField<>("personal.street", new PropertyModel<String>(
				this, "_street"));
		number = new TextField<>("personal.number", new PropertyModel<String>(
				this, "_number"));
		addon = new TextField<>("personal.addon", new PropertyModel<String>(
				this, "_addon"));
		town = new TextField<>("personal.town", new PropertyModel<String>(this,
				"_town"));
		postcode = new TextField<>("personal.postcode",
				new PropertyModel<String>(this, "_postcode"));
		internet = new TextField<>("personal.internet",
				new PropertyModel<String>(this, "_internet"));
		country = new TextField<>("personal.country",
				new PropertyModel<String>(this, "_country"));

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
		company = new Typeahead<String>(markupId, model, dataSource,
				new TypeaheadConfig().withNumberOfItems(Numbers.TEN * 2));
		company.setRequired(true);

		return company;
	}

	private void initFormValues() {
		switch (mEditType) {
		case Create:
			_department = "";
			_company = "";
			if (mCompanyId != null) {
				Company cmp = companyDao.findById(mCompanyId);
				if (cmp != null) {
					_company = cmp.getCompany();
				}
			}
			_street = "";
			_number = "";
			_addon = "";
			_postcode = "";
			_town = "";
			_country = "";
			break;
		case Edit:
			_department = getModelObject().getDepartment();
			if (getModelObject().getCompany() != null) {
				_company = getModelObject().getCompany().getCompany();
			} else {
				_company = "";
			}
			_street = getModelObject().getStreet();
			_number = getModelObject().getNumber();
			_addon = getModelObject().getAddon();
			_postcode = getModelObject().getPostCode();
			_town = getModelObject().getTown();
			_country = getModelObject().getCountry();
			_internet = getModelObject().getInternet();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onError() {
		this.feedbackPanel.setVisible(true);
		this.feedbackPanel.hideAfter(Duration.seconds(Numbers.TEN));
		if (!department.isValid()) {
			departmentWrapper.add(new AttributeAppender("class", " error"));
		} else {
			departmentWrapper.add(new AttributeModifier("class",
					"control-group"));
		}
		if (!company.isValid()) {
			companyWrapper.add(new AttributeAppender("class", " error"));
		} else {
			companyWrapper.add(new AttributeModifier("class", "control-group"));
		}
	}

	@Override
	public void onSubmit() {
		Department dep = null;
		try {
			if (mEditType == Type.Create) {
				if (companyService.departmentAlreadyExisting(_company,
						_department)) {
					throw new DepartmentAlreadyExistingException(_department);
				}
				mEditType = Type.Edit;
				dep = departmentService.createDepartment(_department, _company);
				Company cmp = companyDao.findByProperty("company", _company);

				if (cmp != null) {
					dep.setCompany(cmp);
				} else {
					cmp = companyService.createCompany(_company);
					dep.setCompany(cmp);
				}
				dep.setStreet(_street);
				dep.setAddon(_addon);
				dep.setCountry(_country);
				dep.setInternet(_internet);
				dep.setNumber(_number);
				dep.setPostCode(_postcode);
				dep.setTown(_town);
				departmentDao.persist(dep);
				setModel(new AbstractEntityModel<Department>(dep));
			} else {
				if (companyService.departmentAlreadyExisting(_company,
						_department)) {
					throw new DepartmentAlreadyExistingException(_department);
				}
				Company cmp = companyDao.findByProperty("company", _company);
				if (cmp != null) {
					getModelObject().setCompany(cmp);
				} else {
					cmp = companyService.createCompany(_company);
					getModelObject().setCompany(cmp);
				}
				getModelObject().setDepartment(_department);
				getModelObject().setStreet(_street);
				getModelObject().setAddon(_addon);
				getModelObject().setCountry(_country);
				getModelObject().setInternet(_internet);
				getModelObject().setNumber(_number);
				getModelObject().setPostCode(_postcode);
				getModelObject().setTown(_town);
				departmentDao.persist(getModelObject());
			}

			// It Was succesfull, so display a notifications about this
			NotificationMessage nf = new NotificationMessage(Model.of(_(
					"success.saved").getString()));
			nf.hideAfter(Duration.seconds(Numbers.FIVE));
			success(nf);
		} catch (DepartmentAlreadyExistingException caee) {
			onDepartmentAlreadyExisting(_department);
		} catch (CompanyAlreadyExistingException coaee) {
			onCompanyAlreadyExisting(_company);
		}
	}

	private void onDepartmentAlreadyExisting(String departmentName) {
		NotificationMessage nf = new NotificationMessage(Model.of(_(
				"error.department.existing", departmentName).getString()));
		nf.hideAfter(Duration.seconds(Numbers.TEN));
		error(nf);
	}
	
	private void onCompanyAlreadyExisting(String companyName) {
		NotificationMessage nf = new NotificationMessage(Model.of(_(
				"error.company.existing", companyName).getString()));
		nf.hideAfter(Duration.seconds(Numbers.TEN));
		error(nf);
	}

}
