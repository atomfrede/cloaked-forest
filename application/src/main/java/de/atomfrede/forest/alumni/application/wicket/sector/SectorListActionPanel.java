package de.atomfrede.forest.alumni.application.wicket.sector;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.sector.detail.SectorDetailPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils.getText;

@SuppressWarnings("serial")
public class SectorListActionPanel extends Panel {

	private BootstrapLink<Void> newSector;

	public SectorListActionPanel(String id) {
		super(id);
		addNewSector();
	}

	private void addNewSector() {
		newSector = new BootstrapLink<Void>("btn-new-sector",
				Buttons.Type.Primary) {

			@Override
			public void onClick() {
				onNewSector();
			}
		};

		newSector.setIconType(IconType.plussign).setLabel(
                Model.of(getText("sector.action.new")));
        add(newSector);
	}

	private void onNewSector() {
		PageParameters params = new PageParameters();
		params.add(SectorDetailPage.EDIT_TYPE, Type.Create);
		params.add(SectorDetailPage.SECTOR_ID, "-1");
		setResponsePage(SectorDetailPage.class, params);
	}
}
