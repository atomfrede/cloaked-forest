package de.atomfrede.forest.alumni.application.wicket.department;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.util.StringCheckUtil;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.service.department.DepartmentService;

@SuppressWarnings("serial")
public class DepartmentListPanel extends Panel {

	@SpringBean
	private DepartmentService departmentService;
	
	@SpringBean
	private CompanyDao companyDao;

	private DepartmentProvider departmentProvider;
	private DataView<Department> departments;

	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;
	
	private Label companyInfo;
	
	private Long mCompanyId;

	public DepartmentListPanel(String id){
		this(id, null);
	}
	
	public DepartmentListPanel(String id, Long companyId) {
		super(id);
		
		add(new DepartmentListActionPanel("department-action"));
		
		this.mCompanyId = companyId;
		
		companyInfo = new Label("company-info");
		companyInfo.setVisible(false);
		
		if(mCompanyId != null) {
			companyInfo = new Label("company-info", Model.of(companyDao.findById(mCompanyId).getCompany()));
		}
		
		add(companyInfo);
		
		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
	}

	private DepartmentProvider getDepartmentProvider() {
		if (departmentProvider == null) {
			departmentProvider = new DepartmentProvider(this.mCompanyId);
		}
		return departmentProvider;
	}

	private void setupModal() {
		modalWarning = new TextContentModal("modal-prompt",
				Model.of("Hallo Welt"));
		modalWarning.addCloseButton(Model.of(_("modal.close", "").getString()));
		add(modalWarning);
	}

	private void populateItems() {

		departments = new DataView<Department>("departments",
				getDepartmentProvider()) {

			@Override
			protected void populateItem(Item<Department> item) {
				final Department department = item.getModel().getObject();
				final Company departmentCompany = department.getCompany();

				item.add(new Label("department-name",
						new PropertyModel<String>(department, "department")));

				item.add(new Label("department-company",
						new PropertyModel<String>(departmentCompany, "company")));

				Label noHomepage = new Label("no-homepage",
						Model.of(_("no.homepage")));
				String link = department.getInternet();
				if(!link.startsWith("http")){
					link = "http://"+link;
				}
				ExternalLink homepageLink = new ExternalLink("link",
						link, department.getInternet());
				
				if (StringCheckUtil.isStringSet(department.getInternet())) {
					homepageLink.setVisible(true);
					noHomepage.setVisible(false);
				} else {
					homepageLink.setVisible(false);
					noHomepage.setVisible(true);
				}
				item.add(homepageLink);
				item.add(noHomepage);

				final long departmentId = department.getId();
				final String title = department.getDepartment();

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						// editCompany(companyId);

					}
				};
				editUser.setIconType(IconType.pencil)
						.setSize(Buttons.Size.Mini).setInverted(false);

				item.add(editUser);
			}

		};
		departments.setItemsPerPage(15);
		departments.setOutputMarkupId(true);
		wmc.add(departments);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", departments));
		add(wmc);
	}
}
