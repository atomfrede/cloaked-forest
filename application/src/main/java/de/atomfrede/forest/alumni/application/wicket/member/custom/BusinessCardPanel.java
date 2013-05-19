package de.atomfrede.forest.alumni.application.wicket.member.custom;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.util.StringCheckUtil;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class BusinessCardPanel extends Panel {

	@SpringBean
	private MemberDao memberDao;

	public BusinessCardPanel(String id, Long memberId) {
		super(id);

		Member mem = null;
		ContactData cData = null;

		if (memberId != null) {
			mem = memberDao.findById(memberId);
			cData = mem.getContactData();
		}

		Label profession = new Label("profession");
		Label sector = new Label("sector");
		Label company = new Label("company");
		Label degree = new Label("degree");
		Label department = new Label("department");

		profession.setVisible(false);
		sector.setVisible(false);
		company.setVisible(false);
		degree.setVisible(false);
		department.setVisible(false);

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
			if (mem.getProfession() != null) {
				if (isStringSet(mem.getProfession())) {
					profession = new Label("profession", Model.of(mem
							.getProfession()));
				}
			}

			if (mem.getSector() != null) {
				sector = new Label("sector", Model.of(mem.getSector()
						.getSector()));
			}
			if (mem.getCompany() != null) {
				company = new Label("company", Model.of(mem.getCompany()
						.getCompany()));
			}
			if (mem.getDepartment() != null) {
				if (StringCheckUtil.isStringSet(mem.getDepartment()
						.getDepartment())) {
					department = new Label("department", Model.of(mem
							.getDepartment().getDepartment()));
				}
			}
		}

		StringBuilder personalBuilder = new StringBuilder();
		StringBuilder workBuilder = new StringBuilder();

		if (mem != null) {
			personalBuilder.append(mem.getSalutation() + "<br/>");
			personalBuilder.append(mem.getFirstname() + " " + mem.getLastname()
					+ "<br/>");
			personalBuilder.append(cData.getStreet() + " " + cData.getNumber()
					+ "<br/>");
			if (isStringSet(cData.getAddon())) {
				personalBuilder.append(cData.getAddon() + "<br/>");
			}
			if (isStringSet(cData.getPostCode())
					&& isStringSet(cData.getTown())) {
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

		MultiLineLabel personal = new MultiLineLabel(
				"contact-personal-content",
				Model.of(personalBuilder.toString()));

		personal.setEscapeModelStrings(false);

		MultiLineLabel work = new MultiLineLabel("contact-work-content",
				Model.of(workBuilder.toString()));

		work.setEscapeModelStrings(false);

		add(personal);
		add(work);

		add(profession);
		add(sector);
		add(company);
		add(degree);
		add(department);
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
