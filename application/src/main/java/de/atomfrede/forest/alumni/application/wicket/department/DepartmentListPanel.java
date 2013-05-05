package de.atomfrede.forest.alumni.application.wicket.department;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
import de.atomfrede.forest.alumni.application.wicket.sector.SectorProvider;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.department.DepartmentService;
import de.atomfrede.forest.alumni.service.sector.SectorService;

@SuppressWarnings("serial")
public class DepartmentListPanel extends Panel{

	@SpringBean
	private DepartmentService departmentService;
	
	private DepartmentProvider departmentProvider;
	private DataView<Department> departments;

	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;
	
	public DepartmentListPanel(String id) {
		super(id);
		
		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
	}
	
	private DepartmentProvider getDepartmentProvider() {
		if (departmentProvider == null) {
			departmentProvider = new DepartmentProvider();
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

		departments = new DataView<Department>("departments", getDepartmentProvider()) {

			@Override
			protected void populateItem(Item<Department> item) {
				final Department department = item.getModel().getObject();

				item.add(new Label("department-name",
						new PropertyModel<String>(department, "department")));

				final long departmentId = department.getId();
				final String title = department.getDepartment();

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						//editCompany(companyId);

					}
				};
				editUser.setIconType(IconType.pencil)
						.setSize(Buttons.Size.Mini).setInverted(false);

				BootstrapLink<Void> deleteUser = new BootstrapLink<Void>(
						"action-delete", Buttons.Type.Danger) {

					@Override
					public void onClick() {
						//deleteCompany(companyId, title);

					}

				};
				deleteUser.setIconType(IconType.remove).setSize(
						Buttons.Size.Mini);

				item.add(editUser);
				item.add(deleteUser);
			}

		};
		departments.setItemsPerPage(15);
		departments.setOutputMarkupId(true);
		wmc.add(departments);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", departments));
		add(wmc);
	}
}