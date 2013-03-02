package de.atomfrede.forest.alumni.application.wicket.member.detail;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import de.agilecoders.wicket.Bootstrap;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class MemberDetailPage extends BasePage<Void>{

	public static final String EDIT_TYPE = "type";
	public static final String MEMBER_ID = "memberId";
	
	public enum Type {Edit, Create, Show}
	
	Type mEditType;
	Long mMemberId = null;
	
	public MemberDetailPage(PageParameters params){
		super();
		if(params.get(EDIT_TYPE) != null){
			String editType = params.get(EDIT_TYPE).toString();
			mEditType = Type.valueOf(editType);
		}
		if(params.get(MEMBER_ID) != null){
			String memberId = params.get(MEMBER_ID).toString();
			mMemberId = Long.parseLong(memberId);
		}
		
		add(new MemberDetailPanel("details", mEditType, mMemberId));
	}
	
}
