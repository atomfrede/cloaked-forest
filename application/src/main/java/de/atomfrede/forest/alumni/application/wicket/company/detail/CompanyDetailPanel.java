package de.atomfrede.forest.alumni.application.wicket.company.detail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.department.DepartmentListPanel;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.application.wicket.query.MemberResultsPanel;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.filter.Filter;

@SuppressWarnings("serial")
public class CompanyDetailPanel extends Panel{

	@SpringBean
	private CompanyDao companyDao;
	
	private Type mEditType;
	private Long mCompanyId;
	
	public CompanyDetailPanel(String id, Type editType, Long companyId) {
		super(id);
		this.mEditType = editType;
		this.mCompanyId = companyId;
		
		add(new CompanyDetailForm("company-form", new AbstractEntityModel<Company>(Company.class,
				mCompanyId), mEditType));
		
		add(new DepartmentListPanel("departments", mCompanyId));
		
		Query<Member> query = new Query<>(Member.class);
		Filter companyFilter = new Filter("company", companyDao.findById(mCompanyId), Filter.Type.EQ);
		query.addFilter(companyFilter);
		
		add(new MemberResultsPanel("members", query));
		
	}
	
}
