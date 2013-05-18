package de.atomfrede.forest.alumni.application.wicket.sector.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;
import de.atomfrede.forest.alumni.application.wicket.util.StringCheckUtil;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

@SuppressWarnings("serial")
public class SectorDetailPage extends BasePage<Void> {

	public static final String EDIT_TYPE = "type";
	public static final String SECTOR_ID = "sectorID";
	public static final String FROM_PAGE = "fromPage";

	@SpringBean
	private SectorDao sectorDao;

	private Type mEditType;
	private Long mSectorId;

	private Label header, subHeader;

	public SectorDetailPage(PageParameters params) {
		super();
		if (params.get(EDIT_TYPE) != null) {
			mEditType = Type.valueOf(params.get(EDIT_TYPE).toString());
		}
		if (params.get(SECTOR_ID) != null) {
			mSectorId = Long.parseLong(params.get(SECTOR_ID).toString());
		}

		createHeader();

		add(new SectorDetailPanel("details", mEditType, mSectorId));
	}

	private void createHeader() {
		if (mEditType != null) {
			switch (mEditType) {
			case Create:
				header = new Label("detail-header", _("legend.create.company"));
				subHeader = new Label("detail-sub-header", "");
				break;
			case Edit:
				Sector sec = sectorDao.findById(mSectorId);
				header = new Label("detail-header", _("legend.edit"));
				if (sec != null && StringCheckUtil.isStringSet(sec.getSector())) {
					subHeader = new Label("detail-sub-header", sec.getSector());
				}
				break;

			default:
				header = new Label("detail-header", _("legend.create.sector"));
				subHeader = new Label("detail-sub-header", "");
				break;
			}
		} else {
			header = new Label("detail-header", _("legend.create.sector"));
			subHeader = new Label("detail-sub-header", "");
		}

		add(header, subHeader);
	}
}
