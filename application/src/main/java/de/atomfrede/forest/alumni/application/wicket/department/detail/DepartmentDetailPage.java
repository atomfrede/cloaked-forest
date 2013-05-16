package de.atomfrede.forest.alumni.application.wicket.department.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.detail.CompanyDetailPanel;
import de.atomfrede.forest.alumni.application.wicket.util.StringCheckUtil;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.dao.department.DepartmentDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.department.Department;

@SuppressWarnings("serial")
public class DepartmentDetailPage extends BasePage<Void>{

	public static final String EDIT_TYPE = "type";
	public static final String DEPARTMENT_ID = "departmentID";
	public static final String FROM_PAGE = "fromPage";
	
	@SpringBean
	private DepartmentDao departmentDao;
	
	private Type mEditType;
	private Long mDepartmentId;
	
	private Label header, subHeader;
	
	public DepartmentDetailPage(PageParameters params){
		super();
		if(params.get(EDIT_TYPE) != null){
			mEditType = Type.valueOf(params.get(EDIT_TYPE).toString());
		}
		if(params.get(DEPARTMENT_ID) != null){
			mDepartmentId = Long.parseLong(params.get(DEPARTMENT_ID).toString());
		}
		
		createHeader();
		
		add(new DepartmentDetailPanel("details", mEditType, mDepartmentId));
	}
	
	private void createHeader(){
		if(mEditType != null){
			switch (mEditType) {
			case Create:
				header = new Label("detail-header", _("legend.create.department"));
				subHeader = new Label("detail-sub-header", "");
				break;
			case Edit:
				Department dmp = departmentDao.findById(mDepartmentId);
				header = new Label("detail-header", _("legend.edit"));
				if(dmp != null && StringCheckUtil.isStringSet(dmp.getDepartment())){
					subHeader = new Label("detail-sub-header", dmp.getCompany());
				}
				break;
				
			default:
				header = new Label("detail-header", _("legend.create.department"));
				subHeader = new Label("detail-sub-header", "");
				break;
			}
		} else{
			header = new Label("detail-header", _("legend.create.department"));
			subHeader = new Label("detail-sub-header", "");
		}
		
		add(header, subHeader);
	}
}
