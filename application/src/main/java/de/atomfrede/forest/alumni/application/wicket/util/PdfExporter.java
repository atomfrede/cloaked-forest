package de.atomfrede.forest.alumni.application.wicket.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.QueryService;

@Component
public class PdfExporter {

	/**
	 * Web address of the logo image
	 */
	static final String IMAGE = "https://raw.github.com/atomfrede/cloaked-forest/master/application/src/main/webapp/img/application.png";

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private QueryService queryService;

	public PdfExporter() {
	}

	/**
	 * Returns header and required CSS styles.
	 * 
	 * @return
	 */
	private String getHeaderAndStyles() {

		StringBuilder headerAndStyles = new StringBuilder(
				"<!DOCTYPE html><html><head>");

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
		String date = dateFormatter.format(new Date());
		headerAndStyles
				.append("<style type=\"text/css\">@page {@bottom-right { content:\"Seite \" counter(page) \" von \" counter(pages); }}  ");
		headerAndStyles.append("@page {@bottom-center {content:\"Abrufdatum: "
				+ date + "\"}}");

		headerAndStyles.append("@page{margin-bottom: 1.5in;}");
		headerAndStyles.append("@page {" + "@bottom-left {"
				+ "content: element(pageHeader);" + "}" + "}" + "#pageHeader{"
				+ "position: running(pageHeader);" + "}");

		headerAndStyles
				.append("logo-image{ margin-bottom: 50px; padding-bottom: 50px;"
						+ "}");
		headerAndStyles
				.append(".member-header{width: 100%; padding: 5px; font-weight: bold; background-color: #DDDDDD;}");

		headerAndStyles.append(".address{width: 100%;  overflow: auto;}");
		headerAndStyles
				.append(".address-header{ width: 100%; padding-top: 5px; padding-bottom: 5px; border-bottom: 1px solid #DDDDDD; font-weight: bold;: bold;}");
		headerAndStyles
				.append(".work-address-header{ width: 100%; padding-top: 5px; padding-bottom: 5px; border-bottom: 1px solid #DDDDDD; font-weight: bold;: bold;}");
		headerAndStyles
				.append("body { font-family: 'Ubuntu',Tahoma,sans-serif; height: 100%;}");
		headerAndStyles.append(".address-left {   float: left; width: 50%;}");

		// headerAndStyles.append(".address-right { margin: 0 0 0 50%;}");
		headerAndStyles.append(".address-right { float:right; width: 48%;}");

		headerAndStyles.append(".member-entry { page-break-inside: avoid;}");

		headerAndStyles
				.append(".degree {float: right; text-align: right; font-weight: normal; background-color: #DDDDDD;}");

		headerAndStyles.append(".name {background-color: #DDDDDD;}");

		headerAndStyles.append(".addresses {padding: 10px;}");
		headerAndStyles
				.append(".addresses-inner {width: 100%; height: 150px;}");

		/*
		 * table td{ border:1px solid #000000; word-wrap:break-word;
		 * white-space:normal; overflow:hidden; text-align:left;
		 * line-height:16px; height:16px;
		 * 
		 * }
		 */
		headerAndStyles.append("table {table-layout: fixed;}");
		headerAndStyles
				.append("table td {word-wrap: break-word; text-align: left;}");
		headerAndStyles.append(".table-left {width: 25%;}");

		headerAndStyles.append("</style>");
		headerAndStyles.append("</head>");
		headerAndStyles.append("<body>");

		return headerAndStyles.toString();
	}

	private String getEntryForMember(Member member) {
		StringBuilder sb = new StringBuilder();

		// First the header div..

		sb.append("<div class=\"member-entry\">");

		sb.append("<div class=\"member-header\">");

		sb.append("<span class=\"name \">");
		if (StringCheckUtil.isStringSet(member.getTitle())) {
			sb.append(member.getLastname() + ", " + member.getTitle() + " "
					+ member.getFirstname() + " ");
		} else {
			sb.append(member.getLastname() + ", " + member.getFirstname() + " ");
		}

		sb.append("</span>");

		sb.append("<span class=\"degree \">");
		if (member.getDegree() != null
				&& !StringCheckUtil.isStringSet(member.getTitle())) {
			sb.append(member.getDegree().getShortForm() + " ");
		}
		if (member.getProfession() != null
				&& StringCheckUtil.isStringSet(member.getProfession())) {
			sb.append(member.getProfession().replaceAll("&", "&amp;") + " ");
		}
		if (member.getYearOfGraduation() != null
				&& StringCheckUtil.isStringSet(member.getYearOfGraduation())) {
			sb.append(member.getYearOfGraduation());
		}

		sb.append("</span>");
		sb.append("</div>");

		sb.append(getSectorAndActivity(member));

		sb.append("<div class=\"addresses\">");

		sb.append("<div class=\"address\">");
		sb.append("<div class=\"address-header\">Privatadresse</div>");
		sb.append(getPrivateAddress(member));
		sb.append("</div>");

		if (isWorkAdressAvailable(member)) {
			sb.append("<div class=\"work-address\">");
			sb.append("<div class=\"work-address-header\">Dienstadresse</div>");
			sb.append(getWorkAddress(member));
			sb.append("</div>");

		}
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	/**
	 * Returns if a valid workadress is available and should be display inside
	 * the generated PDF.
	 * 
	 * @param member
	 * @return
	 */
	private boolean isWorkAdressAvailable(Member member) {
		boolean isValid = false;
		if (member.getDepartment() != null) {
			if (member.getCompany() != null
					|| member.getDepartment().getCompany() != null) {
				isValid = true;
			}
		}

		if (!isValid) {
			ContactData cData = member.getContactData();
			if (cData.getPhoneD() != null
					&& StringCheckUtil.isStringSet(cData.getPhoneD())) {
				return true;
			}
			if (cData.getFaxD() != null
					&& StringCheckUtil.isStringSet(cData.getFaxD())) {
				return true;
			}
			if (cData.getMobileD() != null
					&& StringCheckUtil.isStringSet(cData.getMobileD())) {
				return true;
			}
			if (cData.getEmailD() != null
					&& StringCheckUtil.isStringSet(cData.getEmailD())) {
				return true;
			}
			if (cData.getInternetD() != null
					&& StringCheckUtil.isStringSet(cData.getInternetD())) {
				return true;
			}
		}

		return isValid;
	}

	private String getWorkAddress(Member member) {
		StringBuilder sb = new StringBuilder();
		StringBuilder leftBuilder = new StringBuilder();
		StringBuilder rightBuilder = new StringBuilder();

		Department dep = null;

		if (member.getDepartment() != null) {
			dep = member.getDepartment();
			if (member.getCompany() != null) {
				leftBuilder.append("<b>" + member.getCompany().getCompany()
						+ "</b>");
				leftBuilder.append("<br/>");
			} else if (dep.getCompany() != null) {
				leftBuilder.append("<b>" + dep.getCompany().getCompany()
						+ "</b>");
				leftBuilder.append("<br/>");
			}
			if (StringCheckUtil.isStringSet(dep.getDepartment())) {
				leftBuilder.append(dep.getDepartment() + "<br/>");
			}

			leftBuilder.append(dep.getStreet() + " " + dep.getNumber()
					+ "<br/>");
			if (StringCheckUtil.isStringSet(dep.getAddon())) {
				leftBuilder.append(dep.getAddon() + "<br/>");
			}
			leftBuilder.append(dep.getPostCode() + " " + dep.getTown()
					+ "<br/>");
			leftBuilder.append(dep.getCountry());

		}

		{
			ContactData cData = member.getContactData();

			rightBuilder.append("<table style=\"width:100%;\">");

			rightBuilder.append("<tr>");
			rightBuilder.append("<td class=\"table-left\">Tel: </td>");
			rightBuilder.append("<td>");
			if (cData.getPhoneD() != null
					&& StringCheckUtil.isStringSet(cData.getPhoneD())) {
				rightBuilder.append(cData.getPhoneD());
			}
			rightBuilder.append("</td>");
			rightBuilder.append("</tr>");

			rightBuilder.append("<tr>");
			rightBuilder.append("<td class=\"table-left\">Fax: </td>");
			rightBuilder.append("<td>");
			if (cData.getFaxD() != null
					&& StringCheckUtil.isStringSet(cData.getFaxD())) {
				rightBuilder.append(cData.getFaxD());
			}
			rightBuilder.append("</td>");
			rightBuilder.append("</tr>");

			rightBuilder.append("<tr>");
			rightBuilder.append("<td class=\"table-left\">Mobil: </td>");
			rightBuilder.append("<td>");
			if (cData.getMobileD() != null
					&& StringCheckUtil.isStringSet(cData.getMobileD())) {
				rightBuilder.append(cData.getMobileD());
			}
			rightBuilder.append("</td>");
			rightBuilder.append("</tr>");

			rightBuilder.append("<tr>");
			rightBuilder.append("<td class=\"table-left\">eMail: </td>");
			rightBuilder.append("<td>");
			if (cData.getEmailD() != null
					&& StringCheckUtil.isStringSet(cData.getEmailD())) {
				rightBuilder.append(cData.getEmailD());
			}
			rightBuilder.append("</td>");
			rightBuilder.append("</tr>");

			rightBuilder.append("<tr>");
			rightBuilder.append("<td class=\"table-left\">Internet: </td>");
			rightBuilder.append("<td>");
			if (cData.getInternetD() != null
					&& StringCheckUtil.isStringSet(cData.getInternetD())) {
				rightBuilder.append(cData.getInternetD());
			}
			rightBuilder.append("</td>");
			rightBuilder.append("</tr>");

			rightBuilder.append("</table>");
		}

		sb.append("<div class=\"addresses-inner\">");
		sb.append("<div class=\"address-left\">");
		sb.append(leftBuilder.toString());
		sb.append("</div>");

		sb.append("<div class=\"address-right\">");
		sb.append(rightBuilder.toString());
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	private String getPrivateAddress(Member member) {
		StringBuilder sb = new StringBuilder();

		sb.append("<div class=\"addresses-inner\">");
		sb.append("<div class=\"address-left\">");

		if (member.getContactData() != null) {
			ContactData cData = member.getContactData();

			if (member.getSalutation() != null
					&& StringCheckUtil.isStringSet(member.getSalutation())) {
				sb.append(member.getSalutation());
			}

			sb.append("<br/>");

			if (member.getDegree() != null
					&& StringCheckUtil.isStringSet(member.getDegree()
							.getShortForm())) {
				sb.append(member.getDegree().getShortForm() + " ");
				sb.append(member.getFirstname() + " " + member.getLastname()
						+ " ");
			} else {
				sb.append(member.getFirstname() + " " + member.getLastname()
						+ " ");
			}

			sb.append("<br/>");
			if (cData.getStreet() != null
					&& StringCheckUtil.isStringSet(cData.getStreet())) {
				sb.append(cData.getStreet() + " ");
				if (cData.getNumber() != null
						&& StringCheckUtil.isStringSet(cData.getNumber())) {
					sb.append(cData.getNumber());
				}
			}

			sb.append("<br/>");

			if (cData.getPostCode() != null
					&& StringCheckUtil.isStringSet(cData.getPostCode())) {
				sb.append(cData.getPostCode() + " ");
				if (cData.getTown() != null
						&& StringCheckUtil.isStringSet(cData.getTown())) {
					sb.append(cData.getTown());
				}
			}

			sb.append("<br/>");

			if (cData.getCountry() != null
					&& StringCheckUtil.isStringSet(cData.getCountry())) {
				sb.append(cData.getCountry());
			}
			sb.append("<br/>");

			sb.append("</div>");

			sb.append("<div class=\"address-right\">");

			sb.append("<table style=\"width: 100%;\">");

			sb.append("<tr>");
			sb.append("<td class=\"table-left \">Tel: </td>");
			sb.append("<td>");
			if (cData.getPhone() != null
					&& StringCheckUtil.isStringSet(cData.getPhone())) {
				sb.append(cData.getPhone());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td class=\"table-left \">Fax: </td>");
			sb.append("<td>");
			if (cData.getFax() != null
					&& StringCheckUtil.isStringSet(cData.getFax())) {
				sb.append(cData.getFax());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td class=\"table-left \">Mobil: </td>");
			sb.append("<td>");
			if (cData.getMobile() != null
					&& StringCheckUtil.isStringSet(cData.getMobile())) {
				sb.append(cData.getMobile());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td class=\"table-left\">eMail: </td>");
			sb.append("<td>");
			if (cData.getEmail() != null
					&& StringCheckUtil.isStringSet(cData.getEmail())) {
				sb.append(cData.getEmail());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td class=\"table-left\">Internet: </td>");
			sb.append("<td>");
			if (cData.getInternet() != null
					&& StringCheckUtil.isStringSet(cData.getInternet())) {
				sb.append(cData.getInternet());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("</table>");
		}
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * Returns the (X)HTML code for sector and activity of the given member.
	 * 
	 * @param member
	 * @return
	 */
	private String getSectorAndActivity(Member member) {
		StringBuilder sb = new StringBuilder();

		sb.append("<div class=\"sector\">");

		sb.append("<table>");
		sb.append("<tr>");
		if (member.getSector() != null
				&& StringCheckUtil.isStringSet(member.getSector().getSector())) {
			sb.append("<td>");
			sb.append("Branche: ");
			sb.append("</td><td>");
			sb.append(member.getSector());
			sb.append("</td>");

			sb.append("<td>");
			sb.append("Tätigkeit: ");
			sb.append("</td>");

			sb.append("<td>");
			if (member.getActivities() != null
					&& !member.getActivities().isEmpty()) {
				for (Activity act : member.getActivities()) {
					sb.append(act.getActivity().replaceAll("&", "&amp;") + " ");
				}
			}

			sb.append("</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");

		sb.append("</div>");
		return sb.toString();
	}

	private File createPdf(List<Member> members) {
		try {
			File tempPdfFile = File.createTempFile("members", "pdf");

			StringBuilder header = new StringBuilder();
			header.append(getHeaderAndStyles());

			StringBuilder content = new StringBuilder();

			content.append(header.toString());

			content.append("<div class=\"pageHeader\" id=\"pageHeader\"><img class=\"logo-image\" src=\""
					+ IMAGE + "\" style=\" height: 60px;\"/></div>");

			for (Member mem : members) {
				// Replacing all & with the htmnl entity...
				content.append(getEntryForMember(mem).replaceAll("&", "&amp;"));
			}

			content.append("</body></html>");

			ITextRenderer renderer = new ITextRenderer();
			renderer.getSharedContext().setReplacedElementFactory(
					new MediaReplacedElementFactory(renderer.getSharedContext()
							.getReplacedElementFactory()));
			renderer.setDocumentFromString(content.toString());
			renderer.layout();
			OutputStream outputStream = new FileOutputStream(tempPdfFile);
			renderer.createPDF(outputStream);

			// Finishing up
			renderer.finishPDF();

			return tempPdfFile;
		} catch (Exception e) {

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public File generatePdfFile(@SuppressWarnings("rawtypes") Query query) {
		return createPdf((List<Member>) queryService.queryDatabase(query));
	}

	/**
	 * Generate a PDF of all Members.
	 * 
	 * @return
	 */
	public File generatePdfFile() {
		return createPdf(memberDao.findAll());
	}
}
