package de.atomfrede.forest.alumni.application.wicket.company;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.detail.CompanyDetailPage;

@SuppressWarnings("serial")
public class CompanyListActionPanel extends Panel {

	private BootstrapLink<Void> newCompany;
	private Long mSectorId;

	public CompanyListActionPanel(String id) {
		this(id, null);
	}

	public CompanyListActionPanel(String id, Long sectorId) {
		super(id);
		addNewCompany();
		;
		mSectorId = sectorId;
	}

	private void addNewCompany() {
		newCompany = new BootstrapLink<Void>("btn-new-company",
				Buttons.Type.Primary) {

			@Override
			public void onClick() {
				onNewCompany();
			}
		};

		newCompany.setIconType(IconType.plussign).setLabel(
				Model.of(_("company.action.new")));
		add(newCompany);
	}

	private void onNewCompany() {
		PageParameters params = new PageParameters();
		params.add(CompanyDetailPage.EDIT_TYPE, Type.Create);
		params.add(CompanyDetailPage.COMPANY_ID, "-1");
		params.add(CompanyDetailPage.SECTOR_ID, mSectorId + "");
		setResponsePage(CompanyDetailPage.class, params);
	}
}
