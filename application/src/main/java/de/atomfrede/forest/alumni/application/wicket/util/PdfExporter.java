package de.atomfrede.forest.alumni.application.wicket.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Component
public class PdfExporter {

	public static final String PDF = "<html><h1>Hello World</h1></html>";

	@Autowired
	private MemberDao memberDao;

	public PdfExporter() {
	}

	private String getHeaderAndStyles() {
		StringBuilder headerAndStyles = new StringBuilder(
				"<!DOCTYPE html><html><head>");
		
		headerAndStyles.append("<style type=\"text/css\">@page {@bottom-right { content:\"Page \" counter(page) \" of \" counter(pages); }}</style>  ");
		headerAndStyles.append("</head>");

		return headerAndStyles.toString();
	}

	private String getEntryForMember(Member member) {
		StringBuilder sb = new StringBuilder();

		// First the header div..
		sb.append("<div class=\"header\">");

		sb.append("<span class=\"name\">");
		sb.append(member.getLastname() + ", " + member.getFirstname() + " ");
		sb.append("</span>");

		sb.append("<span class=\"degree\">");
		if (member.getDegree() != null) {
			sb.append(member.getDegree().getShortForm() + " ");
		}
		if (member.getProfession() != null
				&& StringCheckUtil.isStringSet(member.getProfession())) {
			// TODO encode as HTML
			// sb.append(member.getProfession()+" ");
		}
		if (member.getYearOfGraduation() != null
				&& StringCheckUtil.isStringSet(member.getYearOfGraduation())) {
			sb.append("(" + member.getYearOfGraduation() + ")");
		}

		sb.append("</span>");
		sb.append("</div>");

		sb.append(getSectorAndActivity(member));

		sb.append("<div class=\"addresses\">");

		sb.append("<div class=\"address\">");
		sb.append("<span class=\"address-header\">Privatadresse</span>");
		sb.append(getPrivateAddress(member));
		sb.append("</div>");

		sb.append("<div class=\"work-address\">");
		sb.append("<span class=\"work-address-header\">Dienstadresse</span>");
		// TODO add work address here.
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	private String getPrivateAddress(Member member) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"address-left\">");
		
		if(member.getContactData() != null){
			ContactData cData = member.getContactData();
			/*
			 * Herr
Michael Altgen
Kaakweg 1c
37077 Göttingen
michael_altgen@web.de
+49 (0)176 640 230 66
			 */
			if(member.getSalutation() != null && StringCheckUtil.isStringSet(member.getSalutation())){
				sb.append(member.getSalutation());
			}
			
			sb.append("<br/>");
			
			if(member.getDegree() != null && StringCheckUtil.isStringSet(member.getDegree().getShortForm())){
				sb.append(member.getDegree().getShortForm()+" ");
				sb.append(member.getFirstname()+" "+member.getLastname()+" ");
			}
			
			sb.append("<br/>");
			if(cData.getStreet() != null && StringCheckUtil.isStringSet(cData.getStreet())){
				sb.append(cData.getStreet()+" ");
				if(cData.getNumber() != null && StringCheckUtil.isStringSet(cData.getNumber())){
					sb.append(cData.getNumber());
				}
			}
			
			sb.append("<br/>");
			
			if(cData.getPostCode() != null && StringCheckUtil.isStringSet(cData.getPostCode())){
				sb.append(cData.getPostCode()+" ");
				if(cData.getTown() != null && StringCheckUtil.isStringSet(cData.getTown())){
					sb.append(cData.getTown());
				}
			}
			
			sb.append("<br/>");
			
			if(cData.getCountry() != null && StringCheckUtil.isStringSet(cData.getCountry())){
				sb.append(cData.getCountry());
			}
			sb.append("<br/>");
			
			if(cData.getEmail() != null && StringCheckUtil.isStringSet(cData.getEmail())){
				sb.append(cData.getEmail());
				sb.append("<br/>");
			}
			
			if(cData.getMobile() != null && StringCheckUtil.isStringSet(cData.getMobile())){
				sb.append(cData.getMobile());
				sb.append("<br/>");
			}
			
			if(cData.getPhone() != null && StringCheckUtil.isStringSet(cData.getPhone())){
				sb.append(cData.getPhone());
				sb.append("<br/>");
			}
			
		}
		
		sb.append("</div>");
		
		sb.append("<div class=\"address-right\">");
		//TODO add phone and other contacts here
		sb.append("</div>");
		return sb.toString();
	}

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
					// sb.append(act+" ");
				}
			}

			sb.append("</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");

		sb.append("</div>");
		return sb.toString();
	}

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

			content.append("</html>");

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
