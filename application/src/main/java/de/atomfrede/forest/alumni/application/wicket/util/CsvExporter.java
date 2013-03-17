package de.atomfrede.forest.alumni.application.wicket.util;

import java.io.File;
import java.io.FileWriter;
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
	MemberDao memberDao;
	
	@Autowired
	QueryService queryService;
	
	private static Log log = LogFactory.getLog(CsvExporter.class);
	public CsvExporter(){
	}
	
	private String[] createHeader(){
		String[] header = new String[14];
		
		header[0] = "Anrede";
		header[1] = "Abschluss/Titel";
		header[2] = "Vorname";
		header[3] = "Nachname";
		header[4] = "Abschlussjahr";
		header[5] = "Abschluss";
		header[6] = "Tätigkeit";
		header[7] = "Straße/Hausnummer";
		header[8] = "PLZ";
		header[9] = "Ort";
		header[10] = "Mail (privat)";
		header[11] = "Firma/Arbeitgeber";
		header[12] = "Abteilung";
		header[13] = "Branche";
		
		return header;
	}
	
	private String[] createLine(Member member){
		String[] line = new String[14];
		line[0] = member.getSalutation();
		if(member.getDegree() != null){
			line[1] = member.getDegree().getShortForm();
		}else{
			line[1] = "-/-";
		}
		line[2] = member.getFirstname();
		line[3] = member.getLastname();
		
		line[4] = member.getYearOfGraduation();
		line[5] = member.getProfession();
		
		line[6] = "";
		int actCount = member.getActivities().size();
		int counter = 1;
		for(Activity act:member.getActivities()){
			if(counter != actCount){
				line[6] = line[6] + act.getActivity()+", ";
			}else{
				line[6] = line[6] + act.getActivity();
			}
			
		}
		//Now the Adress and Contact Data
		ContactData cData = member.getContactData();
		line[7] = cData.getStreet() + " " +cData.getNumber();
		line[8] = cData.getPostCode();
		line[9] = cData.getTown();
		line[10] = cData.getEmail();
		
		if(member.getDepartment() != null){
			line[11] = member.getDepartment().getCompany().getCompany();
			line[12] = member.getDepartment().getDepartment();
		}else{
			line[11] = "-/-";
			line[12] = "-/-";
		}
		
		if(member.getSector() != null){
			line[13] = member.getSector().getSector();
		}else{
			line[13] = "-/-";
		}
		return line;
	}
	
	public File generateCsvFile(Query query){
		try{
			CSVWriter writer = null;
			try{
				File tempCsvFile = File.createTempFile("members-query", "csv");
				writer = new CSVWriter(new FileWriter(tempCsvFile));
				
				List<String[]> linesToWrite = new ArrayList<String[]>();
				
				String[] header = createHeader();
				
				linesToWrite.add(header);
				
				List members = queryService.queryDatabase(query);
				
				for(Object mem:members){
					if(mem instanceof Member){
						Member member = (Member)mem;
						String[] line = createLine(member);
						linesToWrite.add(line);
					}
				}
				writer.writeAll(linesToWrite);
				return tempCsvFile;
			}catch (Exception e) {
				log.error("Could not write CSV.", e);
			}finally{
				if(writer != null){
					writer.close();
				}
			}
			
		}catch(Exception e){
			
		}
		
		return null;
	}
	
	public File generateCsvFile() {
		try{
			CSVWriter writer = null;
			
			try{
				File tempCsvFile = File.createTempFile("members", "csv");
				writer = new CSVWriter(new FileWriter(tempCsvFile));
				
				List<Member> members = memberDao.findAll();
				List<String[]> linesToWrite = new ArrayList<String[]>();
				
				String[] header = createHeader();
				
				linesToWrite.add(header);
				
				for(Member member:members){
					String[] line = createLine(member);
					linesToWrite.add(line);
				}
				
				writer.writeAll(linesToWrite);
				return tempCsvFile;
			}catch (Exception e) {
				log.error("Could not write CSV.", e);
			}finally{
				if(writer != null){
					writer.close();
				}
			}
		}catch(Exception e){
			
		}
		
		return null;
		
	}
}
