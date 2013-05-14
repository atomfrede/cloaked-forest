package de.atomfrede.forest.alumni.application.wicket.company.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
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
import de.agilecoders.wicket.markup.html.bootstrap.form.IDataSource;
import de.agilecoders.wicket.markup.html.bootstrap.form.Typeahead;
import de.agilecoders.wicket.markup.html.bootstrap.form.TypeaheadConfig;
import de.agilecoders.wicket.markup.html.bootstrap.layout.SpanType;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.company.CompanyPage;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.company.CompanyDao;
import de.atomfrede.forest.alumni.domain.dao.sector.SectorDao;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;
import de.atomfrede.forest.alumni.service.company.CompanyService;
import de.atomfrede.forest.alumni.service.sector.SectorService;

@SuppressWarnings("serial")
public class CompanyDetailForm extends BootstrapForm<Company> {

	@SpringBean
	private CompanyService companyService;

	@SpringBean
	private SectorService sectorService;

	@SpringBean
	private SectorDao sectorDao;

	@SpringBean
	private CompanyDao companyDao;

	private Type mEditType;
	private String _company, _size, _sector;
	private Sector sector;

	private RequiredTextField<String> company;
	private TextField<String> size;
	private Typeahead<String> typeahead;

	private NotificationPanel feedbackPanel;
	private WebMarkupContainer companyWrapper;

	public CompanyDetailForm(String componentId, IModel<Company> model,
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
				target.add(companyWrapper);
			}
		};

		add(submitBtn);

		BootstrapLink<Void> cancel = new BootstrapLink<Void>("btn-cancel",
				Buttons.Type.Default) {

			@Override
			public void onClick() {
				setResponsePage(CompanyPage.class);
			}
		};

		add(cancel);

		cancel.setLabel(Model.of(_("global.cancel")));

		initFormValues();

		companyWrapper = new WebMarkupContainer("company.wrapper");
		companyWrapper.setOutputMarkupId(true);
		company = new RequiredTextField<String>("company",
				new PropertyModel<String>(this, "_company"));
		companyWrapper.add(company);

		add(companyWrapper);

		size = new TextField<String>("size", new PropertyModel<String>(this,
				"_size"));
		add(size);

		add(addTypeahead("sector"));

	}

	private void initFormValues() {
		switch (mEditType) {
		case Create:
			_company = "";
			_sector = "";
			_size = "";
			break;
		case Edit:
			_company = getModelObject().getCompany();
			_sector = getModelObject().getSector().getSector();
			_size = getModelObject().getSize();
			sector = getModelObject().getSector();
			break;
		default:
			break;
		}
	}

	private Component addTypeahead(String markupId) {
		final IDataSource<String> dataSource = new IDataSource<String>() {

			@Override
			public List<String> load() {
				return sectorService.getTypeAheadSectors();
			}
		};

		PropertyModel<String> model = new PropertyModel<>(this, "_sector");
		typeahead = new Typeahead<String>(markupId, model, dataSource,
				new TypeaheadConfig().withNumberOfItems(15));
		typeahead.size(SpanType.SPAN5);
		typeahead.setRequired(true);

		return typeahead;
	}

	@Override
	protected void onError() {
		this.feedbackPanel.setVisible(true);
		this.feedbackPanel.hideAfter(Duration.seconds(10));
		if (!company.isValid()) {
			companyWrapper.add(new AttributeAppender("class", " error"));
		} else {
			companyWrapper.add(new AttributeModifier("class", "control-group"));
		}
	}

	@Override
	public void onSubmit() {
		Company company = null;
		try {
			if (mEditType == Type.Create) {
				if (companyService.alreadyExisting(_company)) {
					throw new CompanyAlreadyExistingException(_company);
				}
				mEditType = Type.Edit;
				company = companyService.createCompany(_company);
				company.setSize(_size);
				Sector possibleSector = sectorDao.findByProperty("sector", _sector);
				if(possibleSector != null){
					company.setSector(possibleSector);
				}else{
					//TODO create new sector by name
				}
				companyDao.persist(company);
				setModel(new AbstractEntityModel<Company>(company));
			} else {
				getModelObject().setCompany(_company);
				getModelObject().setSize(_size);
				Sector possibleSector = sectorDao.findByProperty("sector", _sector);
				if(possibleSector != null){
					getModelObject().setSector(possibleSector);
				}else{
					//TODO create new sector by name
				}
				companyDao.persist(getModelObject());
			}

			// It Was succesfull, so display a notifications about this
			NotificationMessage nf = new NotificationMessage(Model.of(_(
					"success.saved").getString()));
			nf.hideAfter(Duration.seconds(3));
			success(nf);
		} catch (CompanyAlreadyExistingException caee) {
			onCompanyAlreadyExisting(_company);
		}
	}

	private void onCompanyAlreadyExisting(String companyName) {
		NotificationMessage nf = new NotificationMessage(Model.of(_(
				"error.company.existing", companyName).getString()));
		nf.hideAfter(Duration.seconds(10));
		error(nf);
	}

	private class CompanyAlreadyExistingException extends Exception {

		public CompanyAlreadyExistingException(String companyName) {
			super();
		}
	}
}
