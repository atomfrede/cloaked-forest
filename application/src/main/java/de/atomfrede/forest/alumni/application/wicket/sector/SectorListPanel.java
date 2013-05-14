package de.atomfrede.forest.alumni.application.wicket.sector;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.detail.CompanyDetailPage;
import de.atomfrede.forest.alumni.application.wicket.degree.detail.DegreeDetailPage;
import de.atomfrede.forest.alumni.application.wicket.member.MemberListActionPanel;
import de.atomfrede.forest.alumni.application.wicket.sector.detail.SectorDetailPage;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.sector.SectorService;

@SuppressWarnings("serial")
public class SectorListPanel extends Panel{

	@SpringBean
	private SectorService sectorService;
	
	@SpringBean
	private CompanyDao companyDao;
	
	private SectorListActionPanel actionPanel;
	
	private SectorProvider sectorProvider;
	private DataView<Sector> sectors;

	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;
	
	public SectorListPanel(String id) {
		super(id);
		
		actionPanel = new SectorListActionPanel("sector-action");
		add(actionPanel);
		
		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
	}
	
	private SectorProvider getSectorProvider() {
		if (sectorProvider == null) {
			sectorProvider = new SectorProvider();
		}
		return sectorProvider;
	}

	private void setupModal() {
		modalWarning = new TextContentModal("modal-prompt",
				Model.of("Hallo Welt"));
		modalWarning.addCloseButton(Model.of(_("modal.close", "").getString()));
		add(modalWarning);
	}
	
	private void populateItems() {

		sectors = new DataView<Sector>("sectors", getSectorProvider()) {

			@Override
			protected void populateItem(Item<Sector> item) {
				final Sector sector = item.getModel().getObject();

				item.add(new Label("sector-name",
						new PropertyModel<String>(sector, "sector")));

				final long sectorId = sector.getId();
				
				int companyCount = companyDao.findAllByProperty("sector.id", sectorId).size();
				
				Link<Void> link = new Link<Void>("sector-companies"){

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						
					}
				};
				
				link.add(new Label("label", Model.of(companyCount+"")));
				item.add(link);

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						editSector(sectorId);

					}
				};
				editUser.setIconType(IconType.pencil)
						.setSize(Buttons.Size.Mini).setInverted(false);


				item.add(editUser);
			}

		};
		sectors.setItemsPerPage(15);
		sectors.setOutputMarkupId(true);
		wmc.add(sectors);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", sectors));
		add(wmc);
	}
	
	private void editSector(Long sectorId){
		PageParameters params = new PageParameters();
		params.add(SectorDetailPage.EDIT_TYPE, Type.Edit);
		params.add(SectorDetailPage.SECTOR_ID, sectorId);
		setResponsePage(SectorDetailPage.class, params);
	}
}
