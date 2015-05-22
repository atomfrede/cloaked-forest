package de.atomfrede.forest.alumni.application.wicket.query;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.query.filter.*;
import de.atomfrede.forest.alumni.application.wicket.util.CsvExporter;
import de.atomfrede.forest.alumni.application.wicket.util.PdfExporter;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.filter.Filter;
import de.atomfrede.forest.alumni.service.query.filter.Filter.Type;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils.getText;

@SuppressWarnings("serial")
public class QueryConfigForm extends BootstrapForm<Void> {

	private final Log log = LogFactory.getLog(QueryConfigForm.class);

	@SpringBean
	private CsvExporter csvExporter;

	@SpringBean
	private PdfExporter pdfExporter;

	private ProfessionFilterPanel professionFilterPanel;
	private DegreeFilterPanel degreeFilter;
	private CompanyFilterPanel companyFilterPanel;
	private ActivityFilterPanel activityFilterPanel;
	private SectorFilterPanel sectorFilterPanel;
	private AppointedDateFilterPanel dateFilterPanel;

	private MemberResultsPanel memberResultPanel;

	private BootstrapLink<Void> csvDownload;
	private BootstrapLink<Void> pdfDownload;

	public QueryConfigForm(String componentId) {
		super(componentId);
		setupProfessionFilter();
		setupDegreeFilter();
		setupCompanyFilter();
		setupActivityFilter();
		setupSectorFilter();
		setupDateFilter();

		addCsvDownload();
		addPdfDownload();

		AjaxButton submitBtn = new AjaxButton("submit-btn", this) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// repaint the feedback panel so that it is hidden
				Query<Member> query = setupQuery();
				memberResultPanel.doFilter(query);
				target.add(memberResultPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		};

		add(submitBtn);

		memberResultPanel = new MemberResultsPanel("member-results");
		add(memberResultPanel);
	}

	private void setupDegreeFilter() {
		degreeFilter = new DegreeFilterPanel("degree-filter");
		add(degreeFilter);
	}

	private void setupProfessionFilter() {
		professionFilterPanel = new ProfessionFilterPanel("profession-filter");
		add(professionFilterPanel);
	}

	private void setupCompanyFilter() {
		companyFilterPanel = new CompanyFilterPanel("company-filter");
		add(companyFilterPanel);
	}

	private void setupActivityFilter() {
		activityFilterPanel = new ActivityFilterPanel("activity-filter");
		add(activityFilterPanel);
		activityFilterPanel.setVisible(false);
	}

	private void setupSectorFilter() {
		sectorFilterPanel = new SectorFilterPanel("sector-filter");
		add(sectorFilterPanel);
	}
	
	private void setupDateFilter() {
		dateFilterPanel = new AppointedDateFilterPanel("date-filter");
		add(dateFilterPanel);
	}

	private Query<Member> setupQuery() {
		Query<Member> query = new Query<>(Member.class);

		Degree degree = degreeFilter.getSelectedDegree();
		if (degree != null && degree.getId() != null) {
			Filter degFilter = new Filter("degree", degree, Type.EQ);
			query.addFilter(degFilter);
		}

		String profession = professionFilterPanel.getValue();
		if (profession != null) {
			Filter profFilter = new Filter("profession", profession, Type.EQ);
			query.addFilter(profFilter);
		}

		Company company = companyFilterPanel.getValue();
		if (company != null) {
			Filter compFilter = new Filter("company", company, Type.EQ);
			query.addFilter(compFilter);
		}

		Sector sector = sectorFilterPanel.getValue();
		if (sector != null) {
			Filter sectorFilter = new Filter("sector", sector, Type.EQ);
			query.addFilter(sectorFilter);
		}
		
		Date date = dateFilterPanel.getValue();
		if(date != null) {
			Filter leaveDateFilter = new Filter("leaveDate", date, Type.GE);
			query.addFilter(leaveDateFilter);
		} else {
			Filter leaveDateFilter = new Filter("leaveDate", new Date(), Type.GE);
			query.addFilter(leaveDateFilter);
		}

		return query;

	}

	private void addPdfDownload() {
		pdfDownload = new BootstrapLink<Void>("btn-pdf-download",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				File file = pdfExporter.generatePdfFile(setupQuery());
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
						log.error("Couldn't write PDF file.", ioe);
					}
				}

			}

		};

		pdfDownload.setIconType(IconType.file)
                .setLabel(Model.of(getText("member.action.pdf"))).setInverted(false);

		add(pdfDownload);
	}

	private void addCsvDownload() {
		csvDownload = new BootstrapLink<Void>("btn-csv-download",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				File file = csvExporter.generateCsvFile(setupQuery());
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
                .setLabel(Model.of(getText("member.action.csv"))).setInverted(false);

		add(csvDownload);
	}

}
