package de.atomfrede.forest.alumni.application.wicket.degree;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.degree.detail.DegreeDetailPage;

@SuppressWarnings("serial")
public class DegreeListActionPanel extends Panel {

	private BootstrapLink<Void> newDegree;

	public DegreeListActionPanel(String id) {
		super(id);
		addNewDegree();
	}

	private void addNewDegree() {
		newDegree = new BootstrapLink<Void>("btn-new-degree",
				Buttons.Type.Primary) {

			@Override
			public void onClick() {
				onNewDegree();
			}
		};

		newDegree.setIconType(IconType.plussign).setLabel(
				Model.of(_("degree.action.new")));
		add(newDegree);
	}

	private void onNewDegree() {
		PageParameters params = new PageParameters();
		params.add(DegreeDetailPage.EDIT_TYPE, Type.Create);
		params.add(DegreeDetailPage.DEGREE_ID, "-1");
		setResponsePage(DegreeDetailPage.class, params);
	}
}
