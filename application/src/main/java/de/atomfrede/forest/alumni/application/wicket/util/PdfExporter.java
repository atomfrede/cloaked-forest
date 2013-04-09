package de.atomfrede.forest.alumni.application.wicket.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Component
public class PdfExporter {


	@Autowired
	private MemberDao memberDao;

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
		headerAndStyles.append(".header{width: 100%; padding: 5px;}");
		headerAndStyles
				.append(".name{width: 40%; align:left; text-align: left;}");
		headerAndStyles
				.append(".degree{width: 40%; align:right; text-align: right;}");
		headerAndStyles.append(".address{width: 100%; }");
		headerAndStyles
				.append(".address-header{ width: 100%; padding: 5px; border-bottom: 1px solid #DDDDDD; font-weight: bold;: bold;}");
		headerAndStyles
				.append(".work-address-header{ width: 100%; padding: 5px; border-bottom: 1px solid #DDDDDD; font-weight: bold;: bold;}");
		headerAndStyles
				.append("body { font-family: 'Ubuntu',Tahoma,sans-serif;}");
		headerAndStyles.append("</style>");
		headerAndStyles.append("</head>");
		headerAndStyles.append("<body>");

		return headerAndStyles.toString();
	}

	private String getEntryForMember(Member member) {
		StringBuilder sb = new StringBuilder();

		// First the header div..
		sb.append("<div class=\"header\">");

		sb.append("<div class=\"name\">");
		sb.append(member.getLastname() + ", " + member.getFirstname() + " ");
		sb.append("</div>");

		sb.append("<div class=\"degree\">");
		if (member.getDegree() != null) {
			sb.append(member.getDegree().getShortForm() + " ");
		}
		if (member.getProfession() != null
				&& StringCheckUtil.isStringSet(member.getProfession())) {
			sb.append(member.getProfession().replaceAll("&", "&amp;") + " ");
		}
		if (member.getYearOfGraduation() != null
				&& StringCheckUtil.isStringSet(member.getYearOfGraduation())) {
			sb.append("(" + member.getYearOfGraduation() + ")");
		}

		sb.append("</div>");
		sb.append("</div>");

		sb.append(getSectorAndActivity(member));

		sb.append("<div class=\"addresses\">");

		sb.append("<div class=\"address\">");
		sb.append("<div class=\"address-header\">Privatadresse</div>");
		sb.append(getPrivateAddress(member));
		sb.append("</div>");

		sb.append("<div class=\"work-address\">");
		sb.append("<div class=\"work-address-header\">Dienstadresse</div>");
		// TODO add work address here.
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	private String getPrivateAddress(Member member) {
		StringBuilder sb = new StringBuilder();

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

			sb.append("<table>");

			sb.append("<tr>");
			sb.append("<td>Tel: </td>");
			sb.append("<td>");
			if (cData.getPhone() != null
					&& StringCheckUtil.isStringSet(cData.getPhone())) {
				sb.append(cData.getPhone());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>Fax: </td>");
			sb.append("<td>");
			if (cData.getFax() != null
					&& StringCheckUtil.isStringSet(cData.getFax())) {
				sb.append(cData.getFax());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>Mobil: </td>");
			sb.append("<td>");
			if (cData.getMobile() != null
					&& StringCheckUtil.isStringSet(cData.getMobile())) {
				sb.append(cData.getMobile());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>eMail: </td>");
			sb.append("<td>");
			if (cData.getEmail() != null
					&& StringCheckUtil.isStringSet(cData.getEmail())) {
				sb.append(cData.getEmail());
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>Internet: </td>");
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

	/**
	 * Generate a PDF of all Members.
	 * 
	 * @return
	 */
	public File generatePdfFile() {
		try {
			File tempPdfFile = File.createTempFile("members", "pdf");

			StringBuilder header = new StringBuilder();
			header.append(getHeaderAndStyles());

			StringBuilder content = new StringBuilder();

			content.append(header.toString());

			for (Member mem : memberDao.findAll()) {
				content.append(getEntryForMember(mem));
			}

			content.append("</body></html>");

			// String escapedHtml =
			// StringEscapeUtils.escapeHtml4(content.toString());
			// String escapedHtml = HtmlUtils.htmlEscape(content.toString());
			ITextRenderer renderer = new ITextRenderer();
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
}
