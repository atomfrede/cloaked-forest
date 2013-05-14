package de.atomfrede.forest.alumni.application.wicket.department;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.detail.CompanyDetailPage;

public class DepartmentListActionPanel extends Panel{

private BootstrapLink<Void> newDepartment;
	
	public DepartmentListActionPanel(String id) {
		super(id);
		addNewDepartment();;
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
	
	private void onNewDepartment(){
		PageParameters params = new PageParameters();
		params.add(CompanyDetailPage.EDIT_TYPE, Type.Create);
		params.add(CompanyDetailPage.COMPANY_ID, "-1");
		setResponsePage(CompanyDetailPage.class, params);
	}
}
