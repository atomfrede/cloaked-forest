package de.atomfrede.forest.alumni.application.wicket.company;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
@MountPath(value = "/companies")
public class CompanyPage extends BasePage<Void> {

	public static String SECTOR_ID = "sectorId";

	private Long mSectorId = null;
	
	public CompanyPage(PageParameters params){
		super();
		if (params.get(SECTOR_ID) != null) {
			try {
				mSectorId = Long.parseLong(params.get(SECTOR_ID).toString());
			} catch (NumberFormatException nfe) {
				mSectorId = null;
			}

		}
		add(new CompanyListPanel("companies", mSectorId));
	}
}
