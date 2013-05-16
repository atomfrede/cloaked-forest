package de.atomfrede.forest.alumni.application.wicket.company.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;
import de.atomfrede.forest.alumni.application.wicket.util.StringCheckUtil;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

@SuppressWarnings("serial")
public class CompanyDetailPage extends BasePage<Void> {

	public static final String EDIT_TYPE = "type";
	public static final String COMPANY_ID = "companyID";
	public static final String FROM_PAGE = "fromPage";
	
	@SpringBean
	private CompanyDao companyDao;
	
	private Type mEditType;
	private Long mConpanyId;
	
	private Label header, subHeader;
	
	public CompanyDetailPage(PageParameters params){
		super();
		if(params.get(EDIT_TYPE) != null){
			mEditType = Type.valueOf(params.get(EDIT_TYPE).toString());
		}
		if(params.get(COMPANY_ID) != null){
			mConpanyId = Long.parseLong(params.get(COMPANY_ID).toString());
		}
		
		createHeader();
		
		add(new CompanyDetailPanel("details", mEditType, mConpanyId));
	}
	
	private void createHeader(){
		if(mEditType != null){
			switch (mEditType) {
			case Create:
				header = new Label("detail-header", _("legend.create.company"));
				subHeader = new Label("detail-sub-header", "");
				break;
			case Edit:
				Company cmp = companyDao.findById(mConpanyId);
				header = new Label("detail-header", _("legend.edit"));
				if(cmp != null && StringCheckUtil.isStringSet(cmp.getCompany())){
					subHeader = new Label("detail-sub-header", cmp.getCompany());
				}
				break;
				
			default:
				header = new Label("detail-header", _("legend.create.company"));
				subHeader = new Label("detail-sub-header", "");
				break;
			}
		} else{
			header = new Label("detail-header", _("legend.create.company"));
			subHeader = new Label("detail-sub-header", "");
		}
		
		add(header, subHeader);
	}
}
