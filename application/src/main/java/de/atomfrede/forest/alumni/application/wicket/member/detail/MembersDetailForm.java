package de.atomfrede.forest.alumni.application.wicket.member.detail;

import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextField;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import de.atomfrede.forest.alumni.application.wicket.activity.ActivityProvider;
import de.atomfrede.forest.alumni.application.wicket.custom.DegreeSelectOption;
import de.atomfrede.forest.alumni.application.wicket.custom.SectorSelectOption;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.activity.ActivityDao;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.member.MemberService;

@SuppressWarnings("serial")
public class MembersDetailForm extends Form<Void>{

	@SpringBean
	DegreeDao degreeDao;
	
	@SpringBean
	SectorDao sectorDao;
	
	@SpringBean
	CompanyDao companyDao;
	
	@SpringBean
	ActivityDao activityDao;
	
	@SpringBean
	DepartmentDao departmentDao;
	
	@SpringBean
	MemberService memberService;
	
	Type editType;
	Degree selectedDegree;
	Sector selectedSector;
	
	FeedbackPanel feedbackPanel;
	
	WebMarkupContainer firstnameWrapper, lastnameWrapper, personalMailWrapper;
	
	RequiredTextField<String> firstname, lastname, personalMail;
	TextField<String> personalAddon, profession, salutation, title, graduationYear, personalStreet, personalTown, personalPostcode, workMail, personalMobile, personalFax, personalPhone, personalInternet, workPhone, workMobile, workFax, workInternet;
	DateTextField entryDate;
	
	DataView<Activity> activities;
	
	//Now the data for this formfields. To reuse this panel for new (=non existing entities we don't use the model directly...)
	String _salutation, _title, _firstname, _lastname;
	Date _entryDate;
	String _personalStreet, _personalNumber, _personalAddon, _personalPostcode, _personalTown, _personalMail, _personalMobile, _personalPhone, _personalFax, _personalInternet;
	String _graduationYear, _profession;
	String _workMail, _workFax, _workMobile, _workInternet, _workPhone;
	
	public MembersDetailForm(String id, Type editType, AbstractEntityModel<Member> model) {
		super(id);
		this.editType = editType;
		
		feedbackPanel = new FeedbackPanel("feedbackPanel");
		add(feedbackPanel);
		feedbackPanel.setVisible(false);
		
		initFormValues(model);
		
		setupPersonalTab();
		setupWorkTab();
		setupDegreeTab();
		setupActivityTab();
	}
	
	private void initFormValues(AbstractEntityModel<Member> model){
		if(model.getObject() == null){
			_salutation = "";
			_title = "";
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
		}
	}
	/**
	 * Sets up the fourth ('activities') tab
	 */
	private void setupActivityTab(){
		populateItems();
	}
	
	/**
	 * Sets up the third ('work') tab
	 */
	private void setupWorkTab(){
		workMail = new TextField<String>("work.mail", new PropertyModel<String>(this, "_workMail"));
		workMail.add(EmailAddressValidator.getInstance());
		workFax = new TextField<String>("work.fax", new PropertyModel<String>(this, "_workFax"));
		workInternet = new TextField<String>("work.internet", new PropertyModel<String>(this, "_workInternet"));
		workMobile = new TextField<String>("work.mobile", new PropertyModel<String>(this, "_workMobile"));
		workPhone = new TextField<String>("work.phone", new PropertyModel<String>(this, "_workPhone"));
		
		add(workMail);
		add(workFax);
		add(workInternet);
		add(workMobile);
		add(workPhone);
		
		//Branche
		List<Sector> sectors = sectorDao.findAll();
				
		Select<Sector> sectorSelect = new Select<Sector>("sector-select", new PropertyModel<Sector>(this, "selectedSector"));
				
		sectorSelect.add(new ListView<Sector>("sector-options", sectors) {

			@Override
			protected void populateItem(ListItem<Sector> item) {
				item.add(new SectorSelectOption("sector-option", item.getModelObject()));
			}
		});
				
		add(sectorSelect);
	}
	
	/**
	 * Sets up the first ('personal') tab.
	 */
	private void setupPersonalTab(){
		firstnameWrapper = new WebMarkupContainer("member.detail.firstname.wrapper");
		lastnameWrapper = new WebMarkupContainer("member.detail.lastname.wrapper");
		personalMailWrapper = new WebMarkupContainer("member.detail.personal.mail.wrapper");
		
		add(firstnameWrapper);
		add(lastnameWrapper);
		add(personalMailWrapper);
		
		salutation = new TextField<String>("salutation", new PropertyModel<String>(this, "_salutation"));
		title = new TextField<String>("title", new PropertyModel<String>(this, "_title"));
		firstname = new RequiredTextField<String>("firstname", new PropertyModel<String>(this, "_firstname"));
		lastname = new RequiredTextField<String>("lastname", new PropertyModel<String>(this, "_lastname"));
		
		firstnameWrapper.add(firstname);
		lastnameWrapper.add(lastname);
		
		personalAddon = new TextField<String>("personal.addon", new PropertyModel<String>(this, "_personalAddon"));
		personalFax = new TextField<String>("personal.fax", new PropertyModel<String>(this, "_personalFax"));
		personalMobile = new TextField<String>("personal.mobile", new PropertyModel<String>(this, "_personalMobile"));
		personalPhone = new TextField<String>("personal.phone", new PropertyModel<String>(this, "_personalPhone"));
		personalInternet = new TextField<String>("personal.internet", new PropertyModel<String>(this, "_personalInternet"));
		personalPostcode = new TextField<String>("personal.postcode", new PropertyModel<String>(this, "_personalPostcode"));
		personalStreet = new TextField<String>("personal.street", new PropertyModel<String>(this, "_personalStreet"));
		personalTown = new TextField<String>("personal.town", new PropertyModel<String>(this, "_personalTown"));
		personalMail = new RequiredTextField<String>("personal.mail", new PropertyModel<String>(this, "_personalMail"));
		personalMail.add(EmailAddressValidator.getInstance());
		
		add(personalAddon);
		add(personalPostcode);
		add(personalStreet);
		add(personalTown);
		personalMailWrapper.add(personalMail);
		add(personalFax);
		add(personalMobile);
		add(personalPhone);
		add(personalInternet);
		
		add(salutation);
		add(title);
		
		DateTextFieldConfig conf = new DateTextFieldConfig();
		conf.withView(DateTextFieldConfig.View.Year);
		conf.autoClose(true);
		entryDate = new DateTextField("entrydate", new PropertyModel<Date>(this, "_entryDate"), conf);
		
		add(entryDate);
	}
	
	/**
	 * Sets up the second ('degree') tab
	 */
	private void setupDegreeTab(){
		profession = new TextField<String>("profession", new PropertyModel<String>(this, "_profession"));
		graduationYear = new TextField<String>("graduationyear", new PropertyModel<String>(this, "_graduationYear"));
		
		List<Degree> degrees = degreeDao.findAll();
		
		Select<Degree> degreeSelect = new Select<Degree>("degree-select", new PropertyModel<Degree>(this, "selectedDegree"));
		
		degreeSelect.add(new ListView<Degree>("degree-options", degrees) {

			@Override
			protected void populateItem(ListItem<Degree> item) {
				item.add(new DegreeSelectOption("degree-option", item.getModelObject()));
			}
		});
		
		add(profession);
		add(graduationYear);
		add(degreeSelect);
	}
	
	/**
	 * Populates the last Tab with checkboxes for possible activites.
	 */
	private void populateItems(){
		activities = new DataView<Activity>("activities", new ActivityProvider()) {

			@Override
			protected void populateItem(Item<Activity> item) {
				//TODO check which must be the default value...
				CheckBox checkBox =null;
				switch (editType) {
				case Create:
					checkBox = new CheckBox("activity", Model.of(Boolean.FALSE));
					break;
				case Edit:
					//TODO use the activities currently selected for this user
					break;
				case Show:
					//TODO use the activities currently selected for this user
					break;
				default:
					break;
				}
				
				checkBox.add(new AttributeModifier("data-label", item.getModelObject().getActivity()));
				item.add(checkBox);
			}
		};
		
		add(activities);
		
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript("$('.selectpicker').selectpicker();"));
		response.render(OnDomReadyHeaderItem.forScript("$('input.checkboxes-pretty').prettyCheckable({color: 'red'});"));
		
	}
	
	@Override
	protected void onError() {
		// Only on validation errors we make the feedbackpanel visible
		this.feedbackPanel.setVisible(true);
		if(!firstname.isValid()){
			firstnameWrapper.add(new AttributeAppender("class", " error"));
		}else{
			firstnameWrapper.add(new AttributeModifier("class", "control-group"));
		}
		if(!lastname.isValid()){
			lastnameWrapper.add(new AttributeAppender("class", " error"));
		}else{
			lastnameWrapper.add(new AttributeModifier("class", "control-group"));
		}
		if(!personalMail.isValid()){
			personalMailWrapper.add(new AttributeAppender("class", " error"));
		}else{
			personalMailWrapper.add(new AttributeModifier("class", "control-group"));
		}
	}
	
	@Override
	public void onSubmit() {
		memberService.createMember(_firstname, _lastname, _personalMail);
		
	}

}
