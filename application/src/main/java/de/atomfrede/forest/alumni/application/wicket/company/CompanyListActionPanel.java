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
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage;
import de.atomfrede.forest.alumni.application.wicket.sector.detail.SectorDetailPage;

@SuppressWarnings("serial")
public class CompanyListActionPanel extends Panel{

	private BootstrapLink<Void> newCompany;
	
	public CompanyListActionPanel(String id) {
		super(id);
		addNewCompany();;
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
	
	private void onNewCompany(){
		PageParameters params = new PageParameters();
		params.add(CompanyDetailPage.EDIT_TYPE, Type.Create);
		params.add(CompanyDetailPage.COMPANY_ID, "-1");
		setResponsePage(SectorDetailPage.class, params);
	}
}
