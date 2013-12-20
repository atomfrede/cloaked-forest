package de.atomfrede.forest.alumni.application.wicket.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.QueryService;

@Component
public class CsvExporter {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private QueryService queryService;

	private static Log log = LogFactory.getLog(CsvExporter.class);

	public CsvExporter() {
	}

	private String[] createHeader() {
		String[] header = new String[22];

		header[0] = "Anrede";
		header[1] = "Abschluss/Titel";
		header[2] = "Vorname";
		header[3] = "Nachname";
		header[4] = "Abschlussjahr";
		header[5] = "Abschluss";
		header[6] = "Schwerpunkt";
		header[7] = "Tätigkeit";
		header[8] = "Straße/Hausnummer";
		header[9] = "PLZ";
		header[10] = "Ort";
		header[11] = "Mail (privat)";
		header[12] = "Firma/Arbeitgeber";
		header[13] = "Abteilung";
		header[14] = "Branche";

		header[15] = "Mail (dienstlich)";
		header[16] = "Telefon (privat)";
		header[17] = "Mobil (privat)";
		header[18] = "Mobile (dienstlich)";

		header[19] = "Straße/Hausnummer (dienstlich)";
		header[20] = "PLZ (dienstlich)";
		header[21] = "Ort (dienstlich)";

		return header;
	}

	private String[] createLine(Member member) {
		String[] line = new String[22];
		line[0] = member.getSalutation();
		if (member.getDegree() != null) {
			line[1] = member.getDegree().getShortForm();
		} else {
			line[1] = "-/-";
		}
		line[2] = member.getFirstname();
		line[3] = member.getLastname();

		line[4] = member.getYearOfGraduation();
		line[5] = member.getProfession();

		line[6] = member.getMainFocus();
		
		line[7] = "";
		int actCount = member.getActivities().size();
		int counter = 1;
		for (Activity act : member.getActivities()) {
			if (counter != actCount) {
				line[7] = line[7] + act.getActivity() + ", ";
			} else {
				line[7] = line[7] + act.getActivity();
			}

		}
		// Now the Adress and Contact Data
		ContactData cData = member.getContactData();
		line[8] = cData.getStreet() + " " + cData.getNumber();
		line[9] = cData.getPostCode();
		line[10] = cData.getTown();
		line[11] = cData.getEmail();

		if (member.getDepartment() != null) {
			line[12] = member.getDepartment().getCompany().getCompany();
			line[13] = member.getDepartment().getDepartment();
		} else {
			line[12] = "-/-";
			line[13] = "-/-";
		}

		if (member.getSector() != null) {
			line[14] = member.getSector().getSector();
		} else {
			line[14] = "-/-";
		}

		if (cData.getEmailD() != null && !cData.getEmailD().equals("NULL")) {
			line[15] = cData.getEmailD();
		}

		if (cData.getPhone() != null && !cData.getPhone().equals("NULL")) {
			line[16] = cData.getPhone();
		}

		if (cData.getMobile() != null && !cData.getMobile().equals("NULL")) {
			line[17] = cData.getMobile();
		}

		if (cData.getMobileD() != null && !cData.getMobileD().equals("NULL")) {
			line[18] = cData.getMobileD();
		}

		// Now the adress for work
		if (member.getDepartment() != null) {
			String streetWork = member.getDepartment().getStreet();
			String numberWork = member.getDepartment().getNumber();
			String postCodeWork = member.getDepartment().getPostCode();
			String townWork = member.getDepartment().getTown();

			line[19] = streetWork + " " + numberWork;
			line[20] = postCodeWork;
			line[21] = townWork;
		}

		return line;
	}

	public File generateCsvFile(Query query) {
		try {
			CSVWriter writer = null;
			try {
				File tempCsvFile = File.createTempFile("members-query", "csv");
				writer = new CSVWriter(new FileWriter(tempCsvFile));

				List<String[]> linesToWrite = new ArrayList<String[]>();

				String[] header = createHeader();

				linesToWrite.add(header);

				@SuppressWarnings("unchecked")
				List<Member> members = (List<Member>) queryService.queryDatabase(query);

				for (Object mem : members) {
					if (mem instanceof Member) {
						Member member = (Member) mem;
						String[] line = createLine(member);
						linesToWrite.add(line);
					}
				}
				writer.writeAll(linesToWrite);
				return tempCsvFile;
			} catch (IOException ioe) {
				log.error("Could not write CSV.", ioe);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}

		} catch (RuntimeException e) {
			throw e;
		} catch (IOException ioe) {
			log.error("Could not write CSV. CLosing stream failed.", ioe);
		}

		return null;
	}

	public File generateCsvFile() {
		try {
			CSVWriter writer = null;

			try {
				File tempCsvFile = File.createTempFile("members", "csv");
				writer = new CSVWriter(new FileWriter(tempCsvFile));

				List<Member> members = memberDao.findAll();
				List<String[]> linesToWrite = new ArrayList<String[]>();

				String[] header = createHeader();

				linesToWrite.add(header);

				for (Member member : members) {
					String[] line = createLine(member);
					linesToWrite.add(line);
				}

				writer.writeAll(linesToWrite);
				return tempCsvFile;
			} catch (IOException e) {
				log.error("Could not write CSV.", e);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (IOException ioe) {
			log.error("Could not write CSV. Closing stream failed.", ioe);
		}

		return null;

	}
}
