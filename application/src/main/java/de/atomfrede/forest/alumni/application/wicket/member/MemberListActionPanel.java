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

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage;
import de.atomfrede.forest.alumni.application.wicket.util.CsvExporter;
import de.atomfrede.forest.alumni.application.wicket.util.PdfExporter;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;

@SuppressWarnings("serial")
public class MemberListActionPanel extends Panel {

	@SpringBean
	private MemberDao memberDao;

	@SpringBean
	private CsvExporter csvExporter;
	
	@SpringBean
	private PdfExporter pdfExporter;

	private BootstrapLink<Void> newMember;
	private BootstrapLink<Void> csvDownload;
	private BootstrapLink<Void> pdfDownload;
	private TextField<String> nameFilter;
	private Form<String> filterForm;
	private String currentFilter = "";

	public MemberListActionPanel(String id) {
		super(id);
		filterForm = new Form<String>("filter-form");
		nameFilter = new TextField<String>("name-filter",
				new PropertyModel<String>(this, "currentFilter"));

		nameFilter.setOutputMarkupId(true);

		filterForm.add(nameFilter);
		filterForm.setOutputMarkupId(true);
		add(filterForm);

		addNewMember();
		addCsvDownload();
		addPdfDownload();
		setOutputMarkupId(true);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(
				MemberListActionPanel.class, "member.css")));
	}

	private void addNewMember() {
		newMember = new BootstrapLink<Void>("btn-new-member",
				Buttons.Type.Primary) {

			@Override
			public void onClick() {
				onNewMember();
			}
		};

		newMember.setIconType(IconType.plussign).setLabel(
				Model.of(_("member.action.new")));
		add(newMember);
	}

	private void addPdfDownload() {
		pdfDownload = new BootstrapLink<Void>("btn-pdf-download",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				File file = pdfExporter.generatePdfFile();
				if(file != null){
					try{
						IResource resource = new ByteArrayResource("application/pdf",
								FileUtils.readFileToByteArray(file),
								"members.pdf");
						IRequestHandler handler = new ResourceRequestHandler(
								resource, null);
						getRequestCycle().scheduleRequestHandlerAfterCurrent(
								handler);
					}catch (IOException ioe){
						
					}
				}

			}

		};
		
		pdfDownload.setIconType(IconType.file)
		.setLabel(Model.of(_("member.action.pdf"))).setInverted(false);
		
		add(pdfDownload);
	}

	private void addCsvDownload() {
		csvDownload = new BootstrapLink<Void>("btn-csv-download",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				File file = csvExporter.generateCsvFile();
				if (file != null) {
					try {
						IResource resource = new ByteArrayResource("text/csv",
								FileUtils.readFileToByteArray(file),
								"members.csv");
						IRequestHandler handler = new ResourceRequestHandler(
								resource, null);
						getRequestCycle().scheduleRequestHandlerAfterCurrent(
								handler);
					} catch (IOException ioe) {

					}
				}

			}
		};

		csvDownload.setIconType(IconType.file)
				.setLabel(Model.of(_("member.action.csv"))).setInverted(false);

		add(csvDownload);
	}

	private void onNewMember() {
		PageParameters params = new PageParameters();
		params.add(MemberDetailPage.EDIT_TYPE, Type.Create);
		params.add(MemberDetailPage.MEMBER_ID, "-1");
		setResponsePage(MemberDetailPage.class, params);
	}

	public TextField<String> getNameFilter() {
		return nameFilter;
	}

}
