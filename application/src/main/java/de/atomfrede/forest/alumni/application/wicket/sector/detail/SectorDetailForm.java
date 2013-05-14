package de.atomfrede.forest.alumni.application.wicket.sector.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.markup.html.bootstrap.form.BootstrapForm;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.application.wicket.sector.SectorPage;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.sector.SectorService;

@SuppressWarnings("serial")
public class SectorDetailForm extends BootstrapForm<Sector> {

	@SpringBean
	private SectorService sectorService;

	@SpringBean
	private SectorDao sectorDao;

	private Type mEditType;
	private String _sector;

	private NotificationPanel feedbackPanel;
	private RequiredTextField<String> sector;
	private WebMarkupContainer sectorWrapper;

	public SectorDetailForm(String componentId, IModel<Sector> model,
			Type editType) {
		super(componentId, model);
		this.mEditType = editType;

		feedbackPanel = new NotificationPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		AjaxButton submitBtn = new AjaxButton("submit-btn", this) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// repaint the feedback panel so that it is hidden
				target.add(feedbackPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
				target.add(sectorWrapper);
			}
		};

		add(submitBtn);

		BootstrapLink<Void> cancel = new BootstrapLink<Void>("btn-cancel",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				setResponsePage(SectorPage.class);
			}
		};

		add(cancel);

		cancel.setLabel(Model.of(_("global.cancel")));

		initFormValues();

		sectorWrapper = new WebMarkupContainer("sector.wrapper");
		sectorWrapper.setOutputMarkupId(true);
		sector = new RequiredTextField<String>("sector", new PropertyModel<String>(this, "_sector"));
		sectorWrapper.add(sector);

		add(sectorWrapper);
	}

	private void initFormValues() {
		switch (mEditType) {
		case Create:
			_sector = "";
			break;
		case Edit:
			_sector = getModelObject().getSector();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onError() {
		this.feedbackPanel.setVisible(true);
		this.feedbackPanel.hideAfter(Duration.seconds(10));
		if (!sector.isValid()) {
			sectorWrapper.add(new AttributeAppender("class", " error"));
		} else {
			sectorWrapper.add(new AttributeModifier("class", "control-group"));
		}
	}

	private void onSectorAlreadyExisting(String sectorName) {
		NotificationMessage nf = new NotificationMessage(Model.of(_(
				"error.sector.existing", sectorName).getString()));
		nf.hideAfter(Duration.seconds(10));
		error(nf);
	}

	@Override
	public void onSubmit() {
		Sector sector = null;
		try {
			if (mEditType == Type.Create) {
				if (sectorService.alreadyExisting(_sector)) {
					throw new SectorAlreadyExistingException(_sector);
				}
				mEditType = Type.Edit;
				sector = sectorService.createSector(_sector);
				setModel(new AbstractEntityModel<Sector>(sector));
			} else {
				if (sectorService.alreadyExisting(_sector)) {
					throw new SectorAlreadyExistingException(_sector);
				}
				getModelObject().setSector(_sector);
				sectorDao.persist(getModelObject());
			}

			// It Was succesfull, so display a notifications about this
			NotificationMessage nf = new NotificationMessage(Model.of(_(
					"success.saved").getString()));
			nf.hideAfter(Duration.seconds(3));
			success(nf);
		} catch (SectorAlreadyExistingException e) {
			onSectorAlreadyExisting(_sector);
		}
	}
	
	private class SectorAlreadyExistingException extends Exception {
		
		public SectorAlreadyExistingException(String sector){
			super("Sector with name "+sector+" already exists.");
		}
	}
}
