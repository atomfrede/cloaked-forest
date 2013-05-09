package de.atomfrede.forest.alumni.application.wicket.sector.detail;

import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;

@SuppressWarnings("serial")
public class SectorDetailPage extends BasePage<Void> {

	public static final String EDIT_TYPE = "type";
	public static final String SECTOR_ID = "sectorID";
	public static final String FROM_PAGE = "fromPage";
	
	public enum Type {
		Edit, Create
	}
	
	@SpringBean
	private SectorDao sectorDao;
}
