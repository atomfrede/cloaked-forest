package de.atomfrede.forest.alumni.application.wicket.company.detail;

import org.apache.wicket.markup.html.panel.Panel;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.department.DepartmentListPanel;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

@SuppressWarnings("serial")
public class CompanyDetailPanel extends Panel{

	private Type mEditType;
	private Long mCompanyId;
	
	public CompanyDetailPanel(String id, Type editType, Long companyId) {
		super(id);
		this.mEditType = editType;
		this.mCompanyId = companyId;
		
		add(new CompanyDetailForm("company-form", new AbstractEntityModel<Company>(Company.class,
				mCompanyId), mEditType));
		
		add(new DepartmentListPanel("departments", mCompanyId));
		
	}
	
}
