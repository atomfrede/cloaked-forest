package de.atomfrede.forest.alumni.application.wicket.sector.detail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.CompanyListPanel;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.application.wicket.query.MemberResultsPanel;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.filter.Filter;

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
		
		add(new CompanyListPanel("companies", mSectorId));
		
		Query<Member> query = new Query<>(Member.class);
		Filter sectorFilter = new Filter("sector", sectorDao.findById(sectorId), Filter.Type.EQ);
		query.addFilter(sectorFilter);
		
		add(new MemberResultsPanel("members", query));
		
	}

}
