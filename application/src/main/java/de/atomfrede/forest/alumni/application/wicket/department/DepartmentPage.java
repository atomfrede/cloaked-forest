package de.atomfrede.forest.alumni.application.wicket.department;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/departments")
public class DepartmentPage extends BasePage<Void> {

	public static String COMPANY_ID = "companyId";

	private Long mCompanyId = null;

	public DepartmentPage(PageParameters params) {
		super();
		if (params.get(COMPANY_ID) != null) {
			try {
				mCompanyId = Long.parseLong(params.get(COMPANY_ID).toString());
			} catch (NumberFormatException nfe) {
				mCompanyId = null;
			}

		}
		add(new DepartmentListPanel("departments", mCompanyId));
	}
}
