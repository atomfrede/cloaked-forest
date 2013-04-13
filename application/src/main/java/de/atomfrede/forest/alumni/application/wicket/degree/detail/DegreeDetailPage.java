package de.atomfrede.forest.alumni.application.wicket.degree.detail;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;

@SuppressWarnings("serial")
public class DegreeDetailPage extends BasePage<Void> {

	public static final String EDIT_TYPE = "type";
	public static final String DEGREE_ID = "degreeId";

	public enum Type {
		Edit, Create, Show
	}

	@SpringBean
	private DegreeDao degreeDao;

	private Type mEditType;
	private Long mDegreeId = null;

	public DegreeDetailPage(PageParameters params) {
		super();
		if (params.get(EDIT_TYPE) != null) {
			String editType = params.get(EDIT_TYPE).toString();
			mEditType = Type.valueOf(editType);
		}
		if (params.get(DEGREE_ID) != null) {
			String memberId = params.get(DEGREE_ID).toString();
			mDegreeId = Long.parseLong(memberId);
		}

		add(new DegreeDetailPanel("details", mEditType, mDegreeId));
	}
}
