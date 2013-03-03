package de.atomfrede.forest.alumni.application.wicket.member.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
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
	
	Label header;
	
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
		
		createHeader();
		add(new MemberDetailPanel("details", mEditType, mMemberId));
	}
	
	private void createHeader(){
		if(mEditType != null){
			switch (mEditType) {
			case Create:
				header = new Label("detail-header", _("legend.create", "Create"));
				break;
			case Edit:
				header= new Label("detail-header", _("legend.edit", "Edit"));
				break;
			case Show:
				header = new Label("detail-header", _("legend.show", "Show"));
				break;
			default:
				header = new Label("detail-header", _("legend.show", "Show"));
				break;
			}
		}else{
			header = new Label("detail-header", _("legend.show", "Show"));
		}
		
		add(header);
	}
	
}
