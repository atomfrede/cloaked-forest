package de.atomfrede.forest.alumni.application.wicket.member.detail;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextField;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import de.atomfrede.forest.alumni.application.wicket.activity.ActivityProvider;
import de.atomfrede.forest.alumni.application.wicket.custom.CustomSelectOption;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@SuppressWarnings("serial")
public class MembersDetailForm extends Form<Void>{

	@SpringBean
	DegreeDao degreeDao;
	
	@SpringBean
	SectorDao sectorDao;
	
	@SpringBean
	CompanyDao companyDao;
	
	@SpringBean
	DepartmentDao departmentDao;
	
	Type editType;
	Degree selectedDegree;
	Sector selectedSector;
	
	RequiredTextField<String> firstname, lastname, personalMail;
	TextField<String> personalAddon, profession, salutation, title, graduationYear, personalStreet, personalTown, personalPostcode, workMail, personalMobile, personalFax, personalPhone, personalInternet, workPhone, workMobile, workFax, workInternet;
	DateTextField entryDate;
	
	DataView<Activity> activities;
	
	public MembersDetailForm(String id, Type editType, AbstractEntityModel<Member> model) {
		super(id);
		this.editType = editType;
		
		
		salutation = new TextField<String>("salutation");
		title = new TextField<String>("title");
		firstname = new RequiredTextField<String>("firstname");
		lastname = new RequiredTextField<String>("lastname");
		profession = new TextField<String>("profession");
		
		DateTextFieldConfig conf = new DateTextFieldConfig();
//		conf.withFormat("yyyy");
		conf.withView(DateTextFieldConfig.View.Year);
		conf.autoClose(true);
		entryDate = new DateTextField("entrydate", conf);
		
		graduationYear = new TextField<String>("graduationyear");
		
		add(salutation);
		add(title);
		add(firstname);
		add(lastname);
		add(profession);
		add(entryDate);
		add(graduationYear);
		
		List<Degree> degrees = degreeDao.findAll();
		
		Select<Degree> degreeSelect = new Select<Degree>("degree-select", new PropertyModel<Degree>(this, "selectedDegree"));
		
		degreeSelect.add(new ListView<Degree>("degree-options", degrees) {

			@Override
			protected void populateItem(ListItem<Degree> item) {
				item.add(new CustomSelectOption("degree-option", item.getModelObject().getTitle()));
			}
		});
		
		add(degreeSelect);
		
		personalAddon = new TextField<String>("personal.addon");
		personalFax = new TextField<String>("personal.fax");
		personalMobile = new TextField<String>("personal.mobile");
		personalPhone = new TextField<String>("personal.phone");
		personalInternet = new TextField<String>("personal.internet");
		personalPostcode = new TextField<String>("personal.postcode");
		personalStreet = new TextField<String>("personal.street");
		personalTown = new TextField<String>("personal.town");
		personalMail = new RequiredTextField<String>("personal.mail");
		personalMail.add(EmailAddressValidator.getInstance());
		workMail = new TextField<String>("work.mail");
		workMail.add(EmailAddressValidator.getInstance());
		workFax = new TextField<String>("work.fax");
		workInternet = new TextField<String>("work.internet");
		workMobile = new TextField<String>("work.mobile");
		workPhone = new TextField<String>("work.phone");
		
		
		add(workFax);
		add(workInternet);
		add(workMobile);
		add(workPhone);
		add(personalAddon);
		add(workMail);
		add(personalPostcode);
		add(personalStreet);
		add(personalTown);
		add(personalMail);
		add(personalFax);
		add(personalMobile);
		add(personalPhone);
		add(personalInternet);
		
		//Branche
		List<Sector> sectors = sectorDao.findAll();
		
		Select<Sector> sectorSelect = new Select<Sector>("sector-select", new PropertyModel<Sector>(this, "selectedSector"));
		
		sectorSelect.add(new ListView<Sector>("sector-options", sectors) {

			@Override
			protected void populateItem(ListItem<Sector> item) {
				item.add(new CustomSelectOption("sector-option", item.getModelObject().getSector()));
			}
		});
		
		add(sectorSelect);
		
		populateItems();
	}
	
	
	private void populateItems(){
		activities = new DataView<Activity>("activities", new ActivityProvider()) {

			@Override
			protected void populateItem(Item<Activity> item) {
				CheckBox checkBox = new CheckBox("activity");
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

}
