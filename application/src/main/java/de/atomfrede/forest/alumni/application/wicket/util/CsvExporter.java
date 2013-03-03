package de.atomfrede.forest.alumni.application.wicket.util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@Component
public class CsvExporter {

	@Autowired
	MemberDao memberDao;
	
	public CsvExporter(){
	}
	
	public File generateCsvFile() {
		try{
			CSVWriter writer = null;
			
			try{
				File tempCsvFile = File.createTempFile("members", "csv");
				writer = new CSVWriter(new FileWriter(tempCsvFile));
				
				List<Member> members = memberDao.findAll();
				List<String[]> linesToWrite = new ArrayList<String[]>();
				
				for(Member member:members){
					String[] line = new String[6];
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
//					line[4] = member.getContactData().
					linesToWrite.add(line);
				}
				
				writer.writeAll(linesToWrite);
				return tempCsvFile;
			}catch (Exception e) {
			
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
