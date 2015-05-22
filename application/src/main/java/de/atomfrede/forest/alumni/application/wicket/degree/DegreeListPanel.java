package de.atomfrede.forest.alumni.application.wicket.degree;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.Numbers;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.degree.detail.DegreeDetailPage;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.service.degree.DegreeService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils.getText;

@SuppressWarnings("serial")
public class DegreeListPanel extends Panel {

	@SpringBean
	private DegreeService degreeService;

	private DegreeProvider degreeProvider;
	private DataView<Degree> degrees;

	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;

	public DegreeListPanel(String id) {
		super(id);

		add(new DegreeListActionPanel("degree-action"));

		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
	}

	private DegreeProvider getDegreeProvider() {
		if (degreeProvider == null) {
			degreeProvider = new DegreeProvider();
		}
		return degreeProvider;
	}

	private void setupModal() {
		modalWarning = new TextContentModal("modal-prompt",
				Model.of("Hallo Welt"));
        modalWarning.addCloseButton(Model.of(getText("modal.close", "").getString()));
        add(modalWarning);
	}

	private void populateItems() {

		degrees = new DataView<Degree>("degrees", getDegreeProvider()) {

			@Override
			protected void populateItem(Item<Degree> item) {
				final Degree degree = item.getModel().getObject();

				item.add(new Label("degree-shortform",
						new PropertyModel<String>(degree, "shortForm")));
				item.add(new Label("degree-title", new PropertyModel<String>(
						degree, "title")));

				final long degreeId = degree.getId();
				final String shortForm = degree.getShortForm();
				final String title = degree.getTitle();

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						editDegree(degreeId);

					}
				};
				editUser.setIconType(IconType.pencil)
						.setSize(Buttons.Size.Mini).setInverted(false);

				BootstrapLink<Void> deleteUser = new BootstrapLink<Void>(
						"action-delete", Buttons.Type.Danger) {

					@Override
					public void onClick() {
						deleteDegree(degreeId, shortForm, title);

					}

				};
				deleteUser.setIconType(IconType.remove).setSize(
						Buttons.Size.Mini);

				item.add(editUser);
				item.add(deleteUser);
			}

		};
		degrees.setItemsPerPage(Numbers.TEN + Numbers.FIVE);
		degrees.setOutputMarkupId(true);
		wmc.add(degrees);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", degrees));
		add(wmc);
	}

	private void editDegree(long id) {
		PageParameters params = new PageParameters();
		params.add(DegreeDetailPage.EDIT_TYPE, Type.Edit);
		params.add(DegreeDetailPage.DEGREE_ID, id);
		setResponsePage(DegreeDetailPage.class, params);
	}

	private void deleteDegree(final long id, String shortFrom, String title) {

		final TextContentModal modal = new TextContentModal("modal-prompt",
                Model.of(getText("modal.degree.text", title, shortFrom).getString()));
        modal.setOutputMarkupId(true);

        modal.addCloseButton(Model.of(getText("modal.close", "").getString()));
        modal.header(Model.of(getText("modal.degree.header", title, shortFrom)
                .getString()));

        AjaxLink<String> doDelete = new AjaxLink<String>("button", Model.of(getText(
                "modal.delete", "").getString())) {

			@Override
			protected void onConfigure() {
				super.onConfigure();

				setBody(getDefaultModel());
				add(new ButtonBehavior(Buttons.Type.Danger));
				// add(new IconBehavior(IconType.remove));
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				doDeleteDegree(id);
				target.appendJavaScript("$('.modal').modal('close');");
				setResponsePage(DegreePage.class);

			}
		};

		modal.addButton(doDelete);
		this.modalWarning.replaceWith(modal);
		this.modalWarning = modal;
		modalWarning.show(true);
	}

	private void doDeleteDegree(long id) {
		degreeService.deleteDegree(id);
	}
}
