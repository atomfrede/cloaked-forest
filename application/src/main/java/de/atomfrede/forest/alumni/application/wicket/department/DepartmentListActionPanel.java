package de.atomfrede.forest.alumni.application.wicket.department;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.department.detail.DepartmentDetailPage;

@SuppressWarnings("serial")
public class DepartmentListActionPanel extends Panel {

	private BootstrapLink<Void> newDepartment;

	private Long mCompanyId;

	public DepartmentListActionPanel(String id) {
		this(id, null);
	}

	public DepartmentListActionPanel(String id, Long companyId) {
		super(id);
		this.mCompanyId = companyId;
		addNewDepartment();
	}

	private void addNewDepartment() {
		newDepartment = new BootstrapLink<Void>("btn-new-department",
				Buttons.Type.Primary) {

			@Override
			public void onClick() {
				onNewDepartment();
			}
		};

		newDepartment.setIconType(IconType.plussign).setLabel(
				Model.of(_("department.action.new")));
		add(newDepartment);
	}

	private void onNewDepartment() {
		PageParameters params = new PageParameters();
		params.add(DepartmentDetailPage.EDIT_TYPE, Type.Create);
		params.add(DepartmentDetailPage.DEPARTMENT_ID, "-1");
		params.add(DepartmentDetailPage.COMPANY_ID, mCompanyId+"");
		setResponsePage(DepartmentDetailPage.class, params);
	}
}
