package de.atomfrede.forest.alumni.application.wicket.department.detail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.application.wicket.query.MemberResultsPanel;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.filter.Filter;

@SuppressWarnings("serial")
public class DepartmentDetailPanel extends Panel {

	@SpringBean
	private DepartmentDao departmentDao;

	private Type mEditType;
	private Long mDepartmentId, mCompanyId;

	public DepartmentDetailPanel(String id, Type editType, Long departmentId){
		this(id, editType, departmentId, null);
	}
	
	public DepartmentDetailPanel(String id, Type editType, Long departmentId, Long companyId) {
		super(id);
		this.mEditType = editType;
		this.mDepartmentId = departmentId;

		add(new DepartmentDetailForm("department-form",
				new AbstractEntityModel<Department>(Department.class,
						mDepartmentId), mEditType));

		Query<Member> query = new Query<>(Member.class);
		Filter companyFilter = new Filter("department",
				departmentDao.findById(mDepartmentId), Filter.Type.EQ);
		query.addFilter(companyFilter);

		MemberResultsPanel members = new MemberResultsPanel("members", query);

		if (!(mDepartmentId != null && mDepartmentId != -1)) {
			members.setVisible(false);
		}
		add(members);

	}
}
