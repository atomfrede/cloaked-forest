package de.atomfrede.forest.alumni.application.wicket.sector;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.sector.SectorService;

@SuppressWarnings("serial")
public class SectorListPanel extends Panel{

	@SpringBean
	private SectorService sectorService;
	
	private SectorProvider sectorProvider;
	private DataView<Sector> sectors;

	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;
	
	public SectorListPanel(String id) {
		super(id);
		
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
				final String title = sector.getSector();

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						//editCompany(companyId);

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
}
