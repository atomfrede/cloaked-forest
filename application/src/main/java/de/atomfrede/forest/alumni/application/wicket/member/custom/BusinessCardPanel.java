package de.atomfrede.forest.alumni.application.wicket.member.custom;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class BusinessCardPanel extends Panel {

	@SpringBean
	private MemberDao memberDao;

	private Label profession;
	private Label sector;
	private Label company;
	private Label degree;
	private Label department;
	private Label mainFocus;
	
	private MultiLineLabel personal;
	private MultiLineLabel work;
	
	public BusinessCardPanel(String id, Long memberId) {
		super(id);

		Member mem = null;

		if (memberId != null) {
			mem = memberDao.findById(memberId);
		}

		initLabels();
		fillLabels(mem);
		
		String personalInformation = generatePersonalInformation(mem);
		String companyInformation = generateCompanyInformation(mem);
		
		fillPersonalInformation(personalInformation.toString());
		fillWorkInformation(companyInformation.toString());

		addMultilineLabels();
		addLabels();
	}

	private void initLabels() {
		profession = new Label("profession");
		sector = new Label("sector");
		company = new Label("company");
		degree = new Label("degree");
		department = new Label("department");
		mainFocus = new Label("mainfocus");

		profession.setVisible(false);
		sector.setVisible(false);
		company.setVisible(false);
		degree.setVisible(false);
		department.setVisible(false);
		mainFocus.setVisible(false);
	}
	
	private void fillPersonalInformation(String content) {
		personal = new MultiLineLabel("contact-personal-content", Model.of(content));
		personal.setEscapeModelStrings(false);
	}
	
	private void fillWorkInformation(String content) {
		work = new MultiLineLabel("contact-work-content", Model.of(content));
		work.setEscapeModelStrings(false);
	}
	
	private void addLabels() {
		add(profession);
		add(sector);
		add(company);
		add(degree);
		add(department);
		add(mainFocus);
	}
	
	private void addMultilineLabels() {
		add(personal);
		add(work);
	}
	
	private String generatePersonalInformation(Member mem) {
		StringBuilder personalBuilder = new StringBuilder();
		
		if(mem != null) {
			ContactData cData = mem.getContactData();
			personalBuilder.append(mem.getSalutation() + "<br/>");
			personalBuilder.append(mem.getFirstname() + " " + mem.getLastname()
					+ "<br/>");
			personalBuilder.append(cData.getStreet() + " " + cData.getNumber()
					+ "<br/>");
			if (isStringSet(cData.getAddon())) {
				personalBuilder.append(cData.getAddon() + "<br/>");
			}
			if (isStringSet(cData.getPostCode()) && isStringSet(cData.getTown())) {
				personalBuilder.append(cData.getPostCode() + " "
						+ cData.getTown() + "<br/>");
			}

			if (isStringSet(cData.getEmail())) {
				personalBuilder.append(cData.getEmail() + "<br/>");
			}

			if (isStringSet(cData.getPhone())) {
				personalBuilder.append(cData.getPhone() + "<br/>");
			}

			if (isStringSet(cData.getMobile())) {
				personalBuilder.append(cData.getMobile());
			}
		}
		
		return personalBuilder.toString();
	}
	
	private String generateCompanyInformation(Member mem) {
		StringBuilder workBuilder = new StringBuilder();
		
		if(mem != null) {
			ContactData cData = mem.getContactData();
			
			if (mem.getCompany() != null) {
				workBuilder.append(mem.getCompany().getCompany() + "<br/>");
			}
			workBuilder.append(mem.getSalutation() + "<br/>");
			workBuilder.append(mem.getFirstname() + " " + mem.getLastname()
					+ "<br/>");
			if (cData.getDepartment() != null) {
				if (isStringSet(cData.getDepartment().getStreet())
						&& isStringSet(cData.getDepartment().getNumber())) {
					workBuilder.append(cData.getDepartment().getStreet() + " "
							+ cData.getDepartment().getNumber() + "<br/>");
					workBuilder.append(cData.getDepartment().getPostCode()
							+ " " + cData.getDepartment().getTown() + "<br/>");
				}
			}

			if (isStringSet(cData.getEmailD())) {
				workBuilder.append(cData.getEmailD() + "<br/>");
			}

			if (isStringSet(cData.getMobileD())) {
				workBuilder.append(cData.getMobileD() + "<br/>");
			}

			if (isStringSet(cData.getPhoneD())) {
				workBuilder.append(cData.getPhoneD());
			}
		}
		
		return workBuilder.toString();
	}
	
	private void fillLabels(Member mem) {
		if (mem != null) {
			if (mem.getDegree() != null) {
				String graduationYear = mem.getYearOfGraduation();
				String labelString = "";
				if (isStringSet(mem.getDegree().getTitle())) {
					labelString = labelString + mem.getDegree().getTitle();
					if (isStringSet(graduationYear)) {
						labelString = labelString + "(" + graduationYear + ")";
					}

					degree = new Label("degree", Model.of(labelString));
				}

			}
			if (mem.getProfession() != null && StringUtils.isNotBlank(mem.getProfession()) && StringUtils.isNotEmpty(mem.getProfession())) {
					profession = new Label("profession", Model.of(mem.getProfession()));
			}

			if (mem.getSector() != null) {
				sector = new Label("sector", Model.of(mem.getSector().getSector()));
			}
			
			if (mem.getCompany() != null) {
				company = new Label("company", Model.of(mem.getCompany().getCompany()));
			}
			
			if (mem.getDepartment() != null && StringUtils.isNotBlank(mem.getDepartment().getDepartment()) && StringUtils.isNotEmpty(mem.getDepartment().getDepartment())) {
					department = new Label("department", Model.of(mem.getDepartment().getDepartment()));
			}
			
			if (mem.getMainFocus() != null && StringUtils.isNotBlank(mem.getMainFocus()) && StringUtils.isEmpty(mem.getMainFocus())) {
				mainFocus = new Label("mainfocus", Model.of(mem.getMainFocus()));
			}
		}
	}
	
	/**
	 * Checks if a string is correctly set. In particular this methods return
	 * true iff toCheck is not null and the content is not equal to the string
	 * NULL
	 * 
	 * @param toCheck
	 * @return
	 */
	private boolean isStringSet(String toCheck) {
		return toCheck != null && !toCheck.trim().equals("NULL");
	}

}
