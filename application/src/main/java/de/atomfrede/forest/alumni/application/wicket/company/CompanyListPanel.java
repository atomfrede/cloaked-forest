package de.atomfrede.forest.alumni.application.wicket.company;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.degree.DegreePage;
import de.atomfrede.forest.alumni.application.wicket.degree.detail.DegreeDetailPage;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.service.company.CompanyService;

@SuppressWarnings("serial")
public class CompanyListPanel extends Panel{

	@SpringBean
	private CompanyService companyService;
	
	@SpringBean
	private DepartmentDao departmentDao;

	private CompanyProvider companyProvider;
	private DataView<Company> companies;

	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;
	
	public CompanyListPanel(String id) {
		super(id);

//		add(new DegreeListActionPanel("degree-action"));

		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
	}
	
	private CompanyProvider getCompanyProvider() {
		if (companyProvider == null) {
			companyProvider = new CompanyProvider();
		}
		return companyProvider;
	}

	private void setupModal() {
		modalWarning = new TextContentModal("modal-prompt",
				Model.of("Hallo Welt"));
		modalWarning.addCloseButton(Model.of(_("modal.close", "").getString()));
		add(modalWarning);
	}
	
	private void populateItems() {

		companies = new DataView<Company>("companies", getCompanyProvider()) {

			@Override
			protected void populateItem(Item<Company> item) {
				final Company company = item.getModel().getObject();

				item.add(new Label("company-name",
						new PropertyModel<String>(company, "company")));
				if(company.getSector() != null){
					item.add(new Label("company-sector", new PropertyModel<String>(
							company.getSector(), "sector")));
				}else{
					item.add(new Label("company-sector", Model.of("--")));
				}
				
				item.add(new Label("company-size", new PropertyModel<String>(company, "size")));
				
				List<Department> departments = departmentDao.findAllByProperty("company", company);
				
				if(departments != null){
					Link<Void> link = new Link<Void>("company-departments") {

						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							
						}
					};
					
					link.add(new Label("label", Model.of(departments.size()+"")));
					item.add(link);
				}else{
					Link<Void> link = new Link<Void>("company-departments") {

						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							
						}
					};
					
					link.add(new Label("label", Model.of(0+"")));
					link.setEnabled(false);
					item.add(link);
				}
				

				final long companyId = company.getId();
				final String title = company.getCompany();

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						editDegree(companyId);

					}
				};
				editUser.setIconType(IconType.pencil)
						.setSize(Buttons.Size.Mini).setInverted(false);

				BootstrapLink<Void> deleteUser = new BootstrapLink<Void>(
						"action-delete", Buttons.Type.Danger) {

					@Override
					public void onClick() {
//						deleteDegree(degreeId, shortForm, title);

					}

				};
				deleteUser.setIconType(IconType.remove).setSize(
						Buttons.Size.Mini);

				item.add(editUser);
				item.add(deleteUser);
			}

		};
		companies.setItemsPerPage(15);
		companies.setOutputMarkupId(true);
		wmc.add(companies);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", companies));
		add(wmc);
	}
	
	private void editDegree(long id) {
		PageParameters params = new PageParameters();
		params.add(DegreeDetailPage.EDIT_TYPE, Type.Edit);
		params.add(DegreeDetailPage.DEGREE_ID, id);
		setResponsePage(DegreeDetailPage.class, params);
	}

	private void deleteCompany(final long id, String title) {

		final TextContentModal modal = new TextContentModal("modal-prompt",
				Model.of(_("modal.company.text", title).getString()));
		modal.setOutputMarkupId(true);

		modal.addCloseButton(Model.of(_("modal.close", "").getString()));
		modal.header(Model.of(_("modal.company.header", title)
				.getString()));

		AjaxLink<String> doDelete = new AjaxLink<String>("button", Model.of(_(
				"modal.delete", "").getString())) {

			@Override
			protected void onConfigure() {
				super.onConfigure();

				setBody(getDefaultModel());
				add(new ButtonBehavior(Buttons.Type.Danger));
				// add(new IconBehavior(IconType.remove));
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				doDeleteCompany(id);
				target.appendJavaScript("$('.modal').modal('close');");
				setResponsePage(DegreePage.class);

			}
		};

		modal.addButton(doDelete);
		this.modalWarning.replaceWith(modal);
		this.modalWarning = modal;
		modalWarning.show(true);
	}

	private void doDeleteCompany(long id) {
//		degreeService.deleteDegree(id);
	}
}
