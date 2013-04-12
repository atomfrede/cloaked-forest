package de.atomfrede.forest.alumni.application.wicket.member.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextField;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import de.agilecoders.wicket.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.markup.html.bootstrap.form.IDataSource;
import de.agilecoders.wicket.markup.html.bootstrap.form.Typeahead;
import de.agilecoders.wicket.markup.html.bootstrap.form.TypeaheadConfig;
import de.agilecoders.wicket.markup.html.bootstrap.layout.SpanType;
import de.atomfrede.forest.alumni.application.wicket.activity.ActivityProvider;
import de.atomfrede.forest.alumni.application.wicket.custom.CompanySelectOption;
import de.atomfrede.forest.alumni.application.wicket.custom.DegreeSelectOption;
import de.atomfrede.forest.alumni.application.wicket.custom.DepartmentSelectOption;
import de.atomfrede.forest.alumni.application.wicket.custom.SectorSelectOption;
import de.atomfrede.forest.alumni.application.wicket.member.MemberDetailPageListener;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.activity.ActivityDao;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.member.MemberService;
import de.atomfrede.forest.alumni.service.member.professsion.ProfessionService;

@SuppressWarnings("serial")
public class MembersDetailForm extends BootstrapForm<Member> {

	@SpringBean
	private DegreeDao degreeDao;

	@SpringBean
	private SectorDao sectorDao;

	@SpringBean
	private CompanyDao companyDao;

	@SpringBean
	private ActivityDao activityDao;

	@SpringBean
	private DepartmentDao departmentDao;

	@SpringBean
	private MemberService memberService;
	
	@SpringBean
	private ProfessionService professionService;

	private List<Company> companies;
	private List<Department> departments;

	private Sector emptySector;
	private Company emptyCompany;
	private Department emptyDepartment;

	private Type editType;
	private Degree selectedDegree;
	private Sector selectedSector;
	private Company selectedCompany;
	private Department selectedDepartment;

	private Select<Sector> sectorSelect;
	private Select<Company> companySelect;
	private Select<Department> departmentSelect;

	private Select<String> salutationSelect;

	private NotificationPanel feedbackPanel;

	private WebMarkupContainer firstnameWrapper, lastnameWrapper,
			personalMailWrapper, selectWrapper;

	private RequiredTextField<String> firstname, lastname;
	private TextField<String> personalMail, personalAddon, graduationYear,
			personalStreet, personalTown, personalPostcode, workMail,
			personalMobile, personalFax, personalPhone, personalInternet,
			workPhone, workMobile, workFax, workInternet, personalNumber;
	private Typeahead<String> profession;
	private DateTextField entryDate;

	DataView<Activity> activities;

	// Now the data for this formfields. To reuse this panel for new (=non
	// existing entities we don't use the model directly...)
	private String _salutation, _firstname, _lastname;
	private Date _entryDate;
	private String _personalStreet, _personalNumber, _personalAddon,
			_personalPostcode, _personalTown, _personalMail, _personalMobile,
			_personalPhone, _personalFax, _personalInternet;
	private String _graduationYear, _profession;
	private String _workMail, _workFax, _workMobile, _workInternet, _workPhone;

	private Map<String, Boolean> activityName_checked = new HashMap<>();

	public MembersDetailForm(String id, Type editType,
			AbstractEntityModel<Member> model) {
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
				target.add(firstnameWrapper);
				target.add(lastnameWrapper);
				target.add(personalMailWrapper);
			}
		};

		add(submitBtn);

		emptySector = new Sector();
		emptySector.setSector(_("model.empty").getString());

		emptyCompany = new Company();
		emptyCompany.setCompany(_("model.empty", "---").getString());

		emptyDepartment = new Department();
		emptyDepartment.setDepartment(_("model.empty", "---").getString());

		initFormValues(model);

		setupPersonalTab();
		setupWorkTab();
		setupDegreeTab();
		setupActivityTab();
	}

	private void initFormValues(AbstractEntityModel<Member> model) {
		if (editType == Type.Create) {
			_salutation = "";
			_firstname = "";
			_lastname = "";
			_personalMail = "";
			_personalAddon = "";
			_personalFax = "";
			_personalInternet = "";
			_personalMobile = "";
			_personalNumber = "";
			_personalPostcode = "";
			_personalStreet = "";
			_personalTown = "";

			_entryDate = new Date();

		} else if (model.getObject() != null) {
			Member mem = model.getObject();
			_graduationYear = mem.getYearOfGraduation();
			_salutation = mem.getSalutation();
			_firstname = mem.getFirstname();
			_lastname = mem.getLastname();
			_personalMail = mem.getContactData().getEmail();
			_personalAddon = mem.getContactData().getAddon();
			_personalFax = mem.getContactData().getFax();
			_personalInternet = mem.getContactData().getInternet();
			_personalMobile = mem.getContactData().getMobile();
			_personalNumber = mem.getContactData().getNumber();
			_personalPostcode = mem.getContactData().getPostCode();
			_personalStreet = mem.getContactData().getStreet();
			_personalTown = mem.getContactData().getTown();
			_personalPhone = mem.getContactData().getPhone();
			_workFax = mem.getContactData().getFaxD();
			_workInternet = mem.getContactData().getInternetD();
			_workMail = mem.getContactData().getEmailD();
			_workMobile = mem.getContactData().getMobileD();
			_workPhone = mem.getContactData().getPhoneD();

			_entryDate = mem.getEntryDate();
			_profession = mem.getProfession();

			selectedDegree = mem.getDegree();
			selectedSector = mem.getSector();
			selectedCompany = mem.getCompany();
			selectedDepartment = mem.getDepartment();

			for (Activity act : activityDao.findAll()) {
				if (mem.getActivities().contains(act)) {
					activityName_checked.put(act.getActivity(), Boolean.TRUE);
				} else {
					activityName_checked.put(act.getActivity(), Boolean.FALSE);
				}
			}
		}
	}

	/**
	 * Sets up the fourth ('activities') tab
	 */
	private void setupActivityTab() {
		populateItems();
	}

	/**
	 * Sets up the third ('work') tab
	 */
	private void setupWorkTab() {

		selectWrapper = new WebMarkupContainer("select-wrapper");

		workMail = new TextField<String>("work.mail",
				new PropertyModel<String>(this, "_workMail"));
		workMail.add(EmailAddressValidator.getInstance());
		workFax = new TextField<String>("work.fax", new PropertyModel<String>(
				this, "_workFax"));
		workInternet = new TextField<String>("work.internet",
				new PropertyModel<String>(this, "_workInternet"));
		workMobile = new TextField<String>("work.mobile",
				new PropertyModel<String>(this, "_workMobile"));
		workPhone = new TextField<String>("work.phone",
				new PropertyModel<String>(this, "_workPhone"));

		add(workMail);
		add(workFax);
		add(workInternet);
		add(workMobile);
		add(workPhone);

		setupWorkSelectors();

	}

	private void setupWorkSelectors() {
		// Branche
		List<Sector> sectors = sectorDao.findAll();

		sectors.add(0, emptySector);

		sectorSelect = new Select<Sector>("sector-select",
				new PropertyModel<Sector>(this, "selectedSector"));

		sectorSelect.add(new ListView<Sector>("sector-options", sectors) {

			@Override
			protected void populateItem(ListItem<Sector> item) {
				item.add(new SectorSelectOption("sector-option", item
						.getModelObject()));
			}
		});

		sectorSelect.setOutputMarkupId(true);

		sectorSelect.add(new OnChangeAjaxBehavior() {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateSelectors();
				target.add(selectWrapper);
				target.appendJavaScript("$('.selectpicker').selectpicker();");
			}
		});

		selectWrapper.add(sectorSelect);

		// Firma
		companies = companyDao.findAllByProperty("sector", selectedSector);

		companies.add(0, emptyCompany);

		companySelect = new Select<Company>("company-select",
				new PropertyModel<Company>(this, "selectedCompany"));

		companySelect.add(new ListView<Company>("company-options", companies) {
			@Override
			protected void populateItem(ListItem<Company> item) {
				item.add(new CompanySelectOption("company-option", item
						.getModelObject()));
			}
		});

		companySelect.setOutputMarkupId(true);

		companySelect.add(new OnChangeAjaxBehavior() {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				onCompanySelected();
				target.add(selectWrapper);
				target.appendJavaScript("$('.selectpicker').selectpicker();");
			}
		});

		selectWrapper.add(companySelect);

		// Abteilung
		departments = new ArrayList<>();

		if (selectedCompany != null) {
			departments = departmentDao.findAllByProperty("company",
					selectedCompany);
		}
		departments.add(0, emptyDepartment);
		departmentSelect = new Select<Department>("department-select",
				new PropertyModel<Department>(this, "selectedDepartment"));

		departmentSelect.add(new ListView<Department>("department-options",
				departments) {
			@Override
			protected void populateItem(ListItem<Department> item) {
				item.add(new DepartmentSelectOption("department-option", item
						.getModelObject()));
			}
		});

		departmentSelect.setOutputMarkupId(true);
		selectWrapper.add(departmentSelect);

		selectWrapper.setOutputMarkupId(true);

		add(selectWrapper);
	}

	private void updateSelectors() {
		if (selectedSector.getId() != null) {
			companies = companyDao.findAllByProperty("sector", selectedSector);
		} else {
			companies = new ArrayList<>();
			selectedCompany = emptyCompany;
		}
		companies.add(0, emptyCompany);

		companySelect.addOrReplace(new ListView<Company>("company-options",
				companies) {
			@Override
			protected void populateItem(ListItem<Company> item) {
				item.add(new CompanySelectOption("company-option", item
						.getModelObject()));
			}
		});

		onCompanySelected();
	}

	private void onCompanySelected() {
		if (selectedCompany != null && selectedCompany.getId() != null) {
			departments = departmentDao.findAllByProperty("company",
					selectedCompany);
			departments.add(0, emptyDepartment);

		} else {
			departments = new ArrayList<>();
			departments.add(0, emptyDepartment);
		}

		departmentSelect.addOrReplace(new ListView<Department>(
				"department-options", departments) {
			@Override
			protected void populateItem(ListItem<Department> item) {
				item.add(new DepartmentSelectOption("department-option", item
						.getModelObject()));
			}
		});
	}

	/**
	 * Sets up the first ('personal') tab.
	 */
	private void setupPersonalTab() {
		firstnameWrapper = new WebMarkupContainer(
				"member.detail.firstname.wrapper");
		lastnameWrapper = new WebMarkupContainer(
				"member.detail.lastname.wrapper");
		personalMailWrapper = new WebMarkupContainer(
				"member.detail.personal.mail.wrapper");

		firstnameWrapper.setOutputMarkupId(true);
		lastnameWrapper.setOutputMarkupId(true);
		personalMailWrapper.setOutputMarkupId(true);

		add(firstnameWrapper);
		add(lastnameWrapper);
		add(personalMailWrapper);

		/*
		 * Select<Degree> degreeSelect = new Select<Degree>("degree-select", new
		 * PropertyModel<Degree>(this, "selectedDegree"));
		 * 
		 * degreeSelect.add(new ListView<Degree>("degree-options", degrees) {
		 * 
		 * @Override protected void populateItem(ListItem<Degree> item) {
		 * item.add(new DegreeSelectOption("degree-option", item
		 * .getModelObject())); } });
		 */
		salutationSelect = new Select<String>("salutation-select",
				new PropertyModel<String>(this, "_salutation"));

		salutationSelect.add(new SelectOption<String>("male-select", Model
				.of(_("salutation.male").getString())));
		salutationSelect.add(new SelectOption<String>("female-select", Model.of(_(
				"salutation.male").getString())));

		firstname = new RequiredTextField<String>("firstname",
				new PropertyModel<String>(this, "_firstname"));
		lastname = new RequiredTextField<String>("lastname",
				new PropertyModel<String>(this, "_lastname"));

		firstnameWrapper.add(firstname);
		lastnameWrapper.add(lastname);

		personalNumber = new TextField<String>("personal.number",
				new PropertyModel<String>(this, "_personalNumber"));
		personalAddon = new TextField<String>("personal.addon",
				new PropertyModel<String>(this, "_personalAddon"));
		personalFax = new TextField<String>("personal.fax",
				new PropertyModel<String>(this, "_personalFax"));
		personalMobile = new TextField<String>("personal.mobile",
				new PropertyModel<String>(this, "_personalMobile"));
		personalPhone = new TextField<String>("personal.phone",
				new PropertyModel<String>(this, "_personalPhone"));
		personalInternet = new TextField<String>("personal.internet",
				new PropertyModel<String>(this, "_personalInternet"));
		personalPostcode = new TextField<String>("personal.postcode",
				new PropertyModel<String>(this, "_personalPostcode"));
		personalStreet = new TextField<String>("personal.street",
				new PropertyModel<String>(this, "_personalStreet"));
		personalTown = new TextField<String>("personal.town",
				new PropertyModel<String>(this, "_personalTown"));
		personalMail = new TextField<String>("personal.mail",
				new PropertyModel<String>(this, "_personalMail"));
		personalMail.add(EmailAddressValidator.getInstance());

		add(personalAddon);
		add(personalNumber);
		add(personalPostcode);
		add(personalStreet);
		add(personalTown);
		personalMailWrapper.add(personalMail);
		add(personalFax);
		add(personalMobile);
		add(personalPhone);
		add(personalInternet);

		add(salutationSelect);

		DateTextFieldConfig conf = new DateTextFieldConfig();
		conf.withView(DateTextFieldConfig.View.Year);
		conf.autoClose(true);
		entryDate = new DateTextField("entrydate", new PropertyModel<Date>(
				this, "_entryDate"), conf);

		add(entryDate);
	}

	/**
	 * Sets up the second ('degree') tab
	 */
	private void setupDegreeTab() {
		
		
		 final IDataSource<String> dataSource = new IDataSource<String>() {

			@Override
			public List<String> load() {
				return professionService.getTypeaheadProfession();
			}
		};

		PropertyModel<String> model = new PropertyModel<>(this, "_profession");
		profession = new Typeahead<String>("profession", model, dataSource,
				new TypeaheadConfig().withNumberOfItems(15));
		profession.size(SpanType.SPAN5);
		
		graduationYear = new TextField<String>("graduationyear",
				new PropertyModel<String>(this, "_graduationYear"));

		List<Degree> degrees = degreeDao.findAll();

		Select<Degree> degreeSelect = new Select<Degree>("degree-select",
				new PropertyModel<Degree>(this, "selectedDegree"));

		degreeSelect.add(new ListView<Degree>("degree-options", degrees) {

			@Override
			protected void populateItem(ListItem<Degree> item) {
				item.add(new DegreeSelectOption("degree-option", item
						.getModelObject()));
			}
		});

		add(profession);
		add(graduationYear);
		add(degreeSelect);
	}

	/**
	 * Populates the last Tab with checkboxes for possible activites.
	 */
	private void populateItems() {
		activities = new DataView<Activity>("activities",
				new ActivityProvider()) {

			@Override
			protected void populateItem(Item<Activity> item) {
				// TODO check which must be the default value...
				CheckBox checkBox = null;
				boolean checked = false;
				switch (editType) {
				case Create:
					checkBox = new CheckBox("activity", Model.of(Boolean.FALSE));
					break;
				case Edit:
					checked = activityName_checked.get(item.getModelObject()
							.getActivity());
					checkBox = new CheckBox("activity", Model.of(checked));
					break;
				case Show:
					checked = activityName_checked.get(item.getModelObject()
							.getActivity());
					checkBox = new CheckBox("activity", Model.of(checked));
					break;
				default:
					break;
				}

				checkBox.add(new AttributeModifier("data-label", item
						.getModelObject().getActivity()));
				item.add(checkBox);
			}
		};

		add(activities);

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem
				.forScript("$('.selectpicker').selectpicker();"));
		response.render(OnDomReadyHeaderItem
				.forScript("$('input.checkboxes-pretty').prettyCheckable({color: 'red'});"));
	}

	@Override
	protected void onError() {
		// Only on validation errors we make the feedbackpanel visible
		this.feedbackPanel.setVisible(true);
		this.feedbackPanel.hideAfter(Duration.seconds(10));
		if (!firstname.isValid()) {
			firstnameWrapper.add(new AttributeAppender("class", " error"));
		} else {
			firstnameWrapper
					.add(new AttributeModifier("class", "control-group"));
		}
		if (!lastname.isValid()) {
			lastnameWrapper.add(new AttributeAppender("class", " error"));
		} else {
			lastnameWrapper
					.add(new AttributeModifier("class", "control-group"));
		}
		if (!personalMail.isValid()) {
			personalMailWrapper.add(new AttributeAppender("class", " error"));
		} else {
			personalMailWrapper.add(new AttributeModifier("class",
					"control-group"));
		}
	}

	@Override
	public void onSubmit() {
		Member member = null;
		if (editType == Type.Create) {
			member = memberService.createMember(_firstname, _lastname,
					_personalMail);
			editType = Type.Edit;
			if (getPage() instanceof MemberDetailPageListener) {
				((MemberDetailPageListener) getPage())
						.editTypeChanged(editType);
			}
		} else {
			member = getModelObject();
			member.setFirstname(_firstname);
			member.setLastname(_lastname);
		}

		// Here we have now definitly a member, so set all fields
		member.setSalutation(_salutation);

		if (selectedDegree != null) {
			member.setTitle(selectedDegree.getShortForm());
		} else {
			member.setTitle(null);
		}

		member.setEntryDate(_entryDate);
		member.setDegree(selectedDegree);
		member.setProfession(_profession);
		member.setYearOfGraduation(_graduationYear);
		if (selectedDepartment.getId() != null) {
			member.setDepartment(selectedDepartment);
		} else {
			member.setDepartment(null);
		}
		if (selectedCompany.getId() != null) {
			member.setCompany(selectedCompany);
		} else {
			member.setCompany(null);
		}
		if (selectedSector.getId() != null) {
			member.setSector(selectedSector);
		} else {
			member.setSector(null);
		}

		ContactData cData = member.getContactData();

		cData.setStreet(_personalStreet);
		cData.setNumber(_personalNumber);
		cData.setAddon(_personalAddon);
		cData.setPostCode(_personalPostcode);
		cData.setTown(_personalTown);
		cData.setEmail(_personalMail);
		cData.setMobile(_personalMobile);
		cData.setPhone(_personalPhone);
		cData.setFax(_personalFax);
		cData.setInternet(_personalInternet);

		cData.setEmailD(_workMail);
		cData.setPhoneD(_workPhone);
		cData.setMobileD(_workMobile);
		cData.setFaxD(_workFax);
		cData.setInternetD(_workInternet);

		if (selectedDepartment.getId() == null) {
			cData.setDepartment(null);
		} else {
			cData.setDepartment(selectedDepartment);
		}

		// Now the tricky part, how to save the activities...
		Iterator<Item<Activity>> ite = activities.getItems();

		member.clearActivities();
		while (ite.hasNext()) {
			Item<Activity> cItem = ite.next();
			if (Boolean.parseBoolean(cItem.get(0)
					.getDefaultModelObjectAsString())) {
				member.addActivity(cItem.getModelObject());
			}

		}

		memberService.persist(member);

		// It Was succesfull, so display a notifications about this
		NotificationMessage nf = new NotificationMessage(
				Model.of("Gespeichert"));
		nf.hideAfter(Duration.seconds(3));
		success(nf);

	}

}
