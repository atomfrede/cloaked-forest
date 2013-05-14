package de.atomfrede.forest.alumni.application.wicket.company.detail;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class CompanyDetailPage extends BasePage<Void> {

	public static final String EDIT_TYPE = "type";
	public static final String COMPANY_ID = "companyID";
	public static final String FROM_PAGE = "fromPage";
	
	private Type mEditType;
	private Long mConpanyId;
	
	public CompanyDetailPage(PageParameters params){
		super();
		if(params.get(EDIT_TYPE) != null){
			mEditType = Type.valueOf(params.get(EDIT_TYPE).toString());
		}
		if(params.get(COMPANY_ID) != null){
			mConpanyId = Long.parseLong(params.get(COMPANY_ID).toString());
		}
		
		add(new CompanyDetailPanel("details", mEditType, mConpanyId));
	}
}
