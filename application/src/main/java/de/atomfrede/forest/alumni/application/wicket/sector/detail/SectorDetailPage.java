package de.atomfrede.forest.alumni.application.wicket.sector.detail;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;

@SuppressWarnings("serial")
public class SectorDetailPage extends BasePage<Void> {

	public static final String EDIT_TYPE = "type";
	public static final String SECTOR_ID = "sectorID";
	public static final String FROM_PAGE = "fromPage";
	
	
	private Type mEditType;
	private Long mSectorId;
	
	public SectorDetailPage(PageParameters params){
		super();
		if(params.get(EDIT_TYPE) != null){
			mEditType = Type.valueOf(params.get(EDIT_TYPE).toString());
		}
		if(params.get(SECTOR_ID) != null){
			mSectorId = Long.parseLong(params.get(SECTOR_ID).toString());
		}
		
		add(new SectorDetailPanel("details", mEditType, mSectorId));
		
	}
}
