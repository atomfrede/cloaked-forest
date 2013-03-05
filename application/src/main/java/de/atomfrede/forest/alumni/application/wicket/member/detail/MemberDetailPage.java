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
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.Bootstrap;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage;
import de.atomfrede.forest.alumni.application.wicket.member.MemberDetailPageListener;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;

@SuppressWarnings("serial")
public class MemberDetailPage extends BasePage<Void> implements
		MemberDetailPageListener {

	public static final String EDIT_TYPE = "type";
	public static final String MEMBER_ID = "memberId";

	public enum Type {
		Edit, Create, Show
	}

	@SpringBean
	MemberDao memberDao;

	Label header, subHeader;

	Type mEditType;
	Long mMemberId = null;

	public MemberDetailPage(PageParameters params) {
		super();
		if (params.get(EDIT_TYPE) != null) {
			String editType = params.get(EDIT_TYPE).toString();
			mEditType = Type.valueOf(editType);
		}
		if (params.get(MEMBER_ID) != null) {
			String memberId = params.get(MEMBER_ID).toString();
			mMemberId = Long.parseLong(memberId);
		}

		createHeader();
		add(new MemberDetailPanel("details", mEditType, mMemberId));
	}

	private void createHeader() {
		String firstname = "";
		String lastname = "";
		if (mEditType != null) {
			switch (mEditType) {
			case Create:
				header = new Label("detail-header",
						_("legend.create", "Create"));
				subHeader = new Label("detail-sub-header", "");
				break;
			case Edit:
				header = new Label("detail-header", _("legend.edit", "Edit"));
				firstname = memberDao.findById(mMemberId).getFirstname();
				lastname = memberDao.findById(mMemberId).getLastname();

				subHeader = new Label("detail-sub-header", firstname + " "
						+ lastname);
				break;
			case Show:
				header = new Label("detail-header", _("legend.show", "Show"));
				firstname = memberDao.findById(mMemberId).getFirstname();
				lastname = memberDao.findById(mMemberId).getLastname();

				subHeader = new Label("detail-sub-header", firstname + " "
						+ lastname);
				break;
			default:
				header = new Label("detail-header", _("legend.show", "Show"));
				subHeader = new Label("detail-sub-header", "");
				break;
			}
		} else {
			header = new Label("detail-header", _("legend.show", "Show"));
			subHeader = new Label("detail-sub-header", "");
		}

		add(header);
		add(subHeader);
	}

	@Override
	public void editTypeChanged(Type newEditType) {
		mEditType = newEditType;
	}

}
