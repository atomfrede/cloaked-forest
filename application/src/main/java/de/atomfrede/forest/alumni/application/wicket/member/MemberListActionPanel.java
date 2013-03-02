package de.atomfrede.forest.alumni.application.wicket.member;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.com.bytecode.opencsv.CSVWriter;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import de.agilecoders.wicket.markup.html.bootstrap.button.TypedLink;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.util.CsvExporter;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class MemberListActionPanel extends Panel {

	@SpringBean
	MemberDao memberDao;
	
	@SpringBean
	CsvExporter csvExporter;
	
	TypedLink<Void> newMember;
	TypedLink<Void> csvDownload;
	
	public MemberListActionPanel(String id) {
		super(id);
		addNewMember();
		addCsvDownload();
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				MemberListActionPanel.class, "member.css")));
	}
	
	private void addNewMember(){
		newMember = new TypedLink<Void>("btn-new-member", ButtonType.Primary) {

			@Override
			public void onClick() {
				onNewMember();
			}
		};
		
		newMember.setIconType(IconType.plussign).setLabel(Model.of(_("member.action.new")));
		add(newMember);
	}
	
	private void addCsvDownload(){
		csvDownload = new TypedLink<Void>("btn-csv-download", ButtonType.Default) {

			@Override
			public void onClick() {
				File file = csvExporter.generateCsvFile();
				if(file != null){
					try{
						IResource resource = new ByteArrayResource("text/csv", FileUtils.readFileToByteArray(file), "members.csv");
						IRequestHandler handler = new ResourceRequestHandler(resource, null);
						getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
					}catch(IOException ioe){
						
					}
				}
				
			}
		};
		
		csvDownload.setIconType(IconType.file).setLabel(Model.of(_("member.action.csv"))).setInverted(false);
		
		add(csvDownload);
	}
	
	private void onNewMember(){
		
	}
	
	

}
