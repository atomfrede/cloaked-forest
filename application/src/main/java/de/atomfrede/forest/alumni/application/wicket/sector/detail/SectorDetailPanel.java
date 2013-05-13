package de.atomfrede.forest.alumni.application.wicket.sector.detail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@SuppressWarnings("serial")
public class SectorDetailPanel extends Panel{

	@SpringBean
	private SectorDao sectorDao;

	private Type mEditType;
	private Long mSectorId;
	
	public SectorDetailPanel(String id, Type editType, Long sectorId) {
		super(id);
		this.mEditType = editType;
		this.mSectorId = sectorId;
		
		add(new SectorDetailForm("sector-form", new AbstractEntityModel<Sector>(Sector.class,
				mSectorId), mEditType));
		
	}

}
