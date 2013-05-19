package de.atomfrede.forest.alumni.application.wicket.user.detail;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class UserDetailPage extends BasePage<Void> {

	public static final String EDIT_TYPE = "type";
	public static final String USER_ID = "userID";

	public enum Type {
		Edit, Create, Show
	}

	private Type mEditType;
	private Long mUserId = null;

	public UserDetailPage(PageParameters params) {
		super();
		if (params.get(EDIT_TYPE) != null) {
			String editType = params.get(EDIT_TYPE).toString();
			mEditType = Type.valueOf(editType);
		}
		if (params.get(USER_ID) != null) {
			String memberId = params.get(USER_ID).toString();
			mUserId = Long.parseLong(memberId);
		}

		add(new UserDetailPanel("details", mEditType, mUserId));
	}
}
