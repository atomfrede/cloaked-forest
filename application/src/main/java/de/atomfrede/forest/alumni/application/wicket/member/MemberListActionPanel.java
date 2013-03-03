package de.atomfrede.forest.alumni.application.wicket.member;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import de.agilecoders.wicket.markup.html.bootstrap.button.TypedLink;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.util.CsvExporter;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;

@SuppressWarnings("serial")
public class MemberListActionPanel extends Panel {

	@SpringBean
	MemberDao memberDao;
	
	@SpringBean
	CsvExporter csvExporter;
	
	TypedLink<Void> newMember;
	TypedLink<Void> csvDownload;
	TextField<String> nameFilter;
	Form<String> filterForm;
	String currentFilter = "";
	
	public MemberListActionPanel(String id) {
		super(id);
		filterForm = new Form<String>("filter-form");
		nameFilter = new TextField<String>("name-filter", new PropertyModel<String>(this, "currentFilter"));
		
		filterForm.add(nameFilter);
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
		addNewMember();
		addCsvDownload();
		setOutputMarkupId(true);
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
		PageParameters params = new PageParameters();
		params.add(MemberDetailPage.EDIT_TYPE, Type.Create);
		params.add(MemberDetailPage.MEMBER_ID, "-1");
		setResponsePage(MemberDetailPage.class, params);
	}
	
	

}
