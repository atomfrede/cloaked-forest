package de.atomfrede.forest.alumni.application.wicket.member;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.yui.calendar.DateField;
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
import org.joda.time.format.DateTimeFormat;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage;
import de.atomfrede.forest.alumni.application.wicket.util.CsvExporter;
import de.atomfrede.forest.alumni.application.wicket.util.PdfExporter;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;

@SuppressWarnings("serial")
public class MemberListActionPanel extends Panel {

	private final Log log = LogFactory.getLog(MemberListActionPanel.class);

	@SpringBean
	private MemberDao memberDao;

	@SpringBean
	private CsvExporter csvExporter;

	@SpringBean
	private PdfExporter pdfExporter;

	private BootstrapLink<Void> newMember, csvDownload, pdfDownload;
	private TextField<String> nameFilter;
	private DateTextField appointedDate;
	private Form<String> filterForm;
	private String currentFilter = "";
	private Date _appointedDate;

	public MemberListActionPanel(String id) {
		super(id);
		filterForm = new Form<String>("filter-form");
		DateTextFieldConfig conf = new DateTextFieldConfig();
		conf.autoClose(true);
		String pattern = DateTimeFormat.patternForStyle("M-", getSession().getLocale());
		conf.withFormat(pattern);
		conf.withLanguage(getSession().getLocale().getLanguage());
		appointedDate = new DateTextField("appointedDate", new PropertyModel<Date>(
				this, "_appointedDate"), conf);
		
		appointedDate.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Just for updating the model object
			}
		});
		
		nameFilter = new TextField<String>("name-filter",
				new PropertyModel<String>(this, "currentFilter"));

		nameFilter.setOutputMarkupId(true);

		filterForm.add(nameFilter);
		filterForm.add(appointedDate);
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
				File file = null;
				if(_appointedDate == null) {
					file = pdfExporter.generatePdfFile();
				} else {
					file = pdfExporter.generatePdfFile(_appointedDate);
				}
				
				if (file != null) {
					try {
						IResource resource = new ByteArrayResource(
								"application/pdf",
								FileUtils.readFileToByteArray(file),
								"members.pdf");
						IRequestHandler handler = new ResourceRequestHandler(
								resource, null);
						getRequestCycle().scheduleRequestHandlerAfterCurrent(
								handler);

					} catch (IOException ioe) {
						log.error("Couldn't write PDF File.", ioe);
					}
				}

			}

		};

		// pdfDownload.add(load);

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
						log.error("Couldn't write CSV file.", ioe);
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

	public DateTextField getDateFilter() {
		return appointedDate;
	}
	
	public TextField<String> getNameFilter() {
		return nameFilter;
	}

	public Form<String> getFilterForm() {
		return filterForm;
	}

}
