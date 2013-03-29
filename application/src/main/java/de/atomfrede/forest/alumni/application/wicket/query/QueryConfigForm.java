package de.atomfrede.forest.alumni.application.wicket.query;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.query.filter.ActivityFilterPanel;
import de.atomfrede.forest.alumni.application.wicket.query.filter.CompanyFilterPanel;
import de.atomfrede.forest.alumni.application.wicket.query.filter.DegreeFilterPanel;
import de.atomfrede.forest.alumni.application.wicket.query.filter.ProfessionFilterPanel;
import de.atomfrede.forest.alumni.application.wicket.util.CsvExporter;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.filter.Filter;
import de.atomfrede.forest.alumni.service.query.filter.Filter.Type;

@SuppressWarnings("serial")
public class QueryConfigForm extends BootstrapForm<Void> {

	@SpringBean
	private CsvExporter csvExporter;

	private ProfessionFilterPanel professionFilterPanel;
	private DegreeFilterPanel degreeFilter;
	private CompanyFilterPanel companyFilterPanel;
	private ActivityFilterPanel activityFilterPanel;

	private BootstrapLink<Void> csvDownload;

	public QueryConfigForm(String componentId) {
		super(componentId);
		setupProfessionFilter();
		setupDegreeFilter();
		setupCompanyFilter();
		setupActivityFilter();

		addCsvDownload();

		AjaxButton submitBtn = new AjaxButton("submit-btn", this) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// repaint the feedback panel so that it is hidden
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		};

		add(submitBtn);
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

		return query;

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

					}
				}
			}
		};

		csvDownload.setIconType(IconType.file)
				.setLabel(Model.of(_("member.action.csv"))).setInverted(false);

		add(csvDownload);
	}

}
