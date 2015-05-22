package de.atomfrede.forest.alumni.application.wicket.query;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.homepage.Homepage;
import de.atomfrede.forest.alumni.application.wicket.member.custom.BusinessCardModal;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.member.MemberService;
import de.atomfrede.forest.alumni.service.query.Query;
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
public class MemberResultsPanel extends Panel {

	@SpringBean
	private MemberDao memberDao;

	@SpringBean
	private MemberService memberService;

	private MemberQueryProvider memberProvider;
	private DataView<Member> members;
	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;
	private BusinessCardModal modalInfo;
	private WebMarkupContainer emptyResult;

	@SuppressWarnings("rawtypes")
	private Query memberQuery = null;

	public MemberResultsPanel(String id) {
		this(id, null);
	}

	@SuppressWarnings("rawtypes")
	public MemberResultsPanel(String id, Query query) {
		super(id);

		this.memberQuery = query;

		setOutputMarkupId(true);
		emptyResult = new WebMarkupContainer("empty-result");
		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
		setupModalInfo();
		add(emptyResult);

	}

	private void populateItems() {
		
		members = new DataView<Member>("members", getMemberProvider()) {

			@Override
			protected void populateItem(Item<Member> item) {
				final Member member = item.getModel().getObject();
				final Degree degree = member.getDegree();

				item.add(new Label("firstname", new PropertyModel<String>(
						member, "firstname")));
				item.add(new Label("lastname", new PropertyModel<String>(
						member, "lastname")));
				if (degree != null) {
					String degreeShort = degree.getShortForm();
					String graduationYear = member.getYearOfGraduation();
					String label = degreeShort + " (" + graduationYear + ")";
					Label degreeLbl = new Label("degree", Model.of(label));
					degreeLbl.add(new TooltipBehavior(
							new PropertyModel<String>(degree, "title")));
					item.add(degreeLbl);
				} else {
					item.add(new Label("degree", Model.of("-/-")));
				}
				item.add(new Label("profession", new PropertyModel<String>(
						member, "profession")));

				int size = member.getActivities().size();
				int cSize = 1;
				StringBuilder sb = new StringBuilder();
				for (Activity act : member.getActivities()) {
					sb.append(act.getActivity());
					if (cSize < size) {
						sb.append(", ");
					}
					cSize++;
				}
				item.add(new Label("activity", Model.of(sb.toString())));

				final long memberId = member.getId();
				final String firstname = member.getFirstname();
				final String lastname = member.getLastname();

				BootstrapLink<Void> infoUser = new BootstrapLink<Void>(
						"action-info", Buttons.Type.Default) {
					public void onClick() {
						infoMember(memberId);
					}
				};

				infoUser.setIconType(IconType.infosign)
						.setSize(Buttons.Size.Mini).setInverted(false);

				BootstrapLink<Void> editUser = new BootstrapLink<Void>(
						"action-edit", Buttons.Type.Default) {

					@Override
					public void onClick() {
						editMember(memberId);

					}
				};
				editUser.setIconType(IconType.pencil)
						.setSize(Buttons.Size.Mini).setInverted(false);

				BootstrapLink<Void> deleteUser = new BootstrapLink<Void>(
						"action-delete", Buttons.Type.Danger) {

					@Override
					public void onClick() {
						deleteMember(memberId, firstname, lastname);

					}

				};
				deleteUser.setIconType(IconType.remove).setSize(
						Buttons.Size.Mini);

				item.add(infoUser);
				item.add(editUser);
				item.add(deleteUser);
			}

		};
		members.setItemsPerPage(15);
		members.setOutputMarkupId(true);
		wmc.add(members);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", members));
		add(wmc);
		
		displayInfoOrTable();
	}

	protected void doFilter(Query query) {
		getMemberProvider().setQuery(query);
		displayInfoOrTable();
	}
	
	private void displayInfoOrTable() {
		if(getMemberProvider().size() == 0) {
			emptyResult.setVisible(true);
			wmc.setVisible(false);
		} else {
			emptyResult.setVisible(false);
			wmc.setVisible(true);
		}
	}

	private void setupModal() {
		modalWarning = new TextContentModal("modal-prompt",
				Model.of("Hallo Welt"));
        modalWarning.addCloseButton(Model.of(getText("modal.close", "").getString()));
        add(modalWarning);
	}

	private void setupModalInfo() {
		modalInfo = new BusinessCardModal("modal-info", null);
        modalInfo.addCloseButton(Model.of(getText("modal.close", "").getString()));
        add(modalInfo);
	}

	private MemberQueryProvider getMemberProvider() {
		if (memberProvider == null) {
			memberProvider = new MemberQueryProvider();
			if (memberQuery != null) {
				memberProvider.setQuery(memberQuery);
			}
		}
		return memberProvider;
	}

	private void infoMember(final long id) {
		Member mem = memberDao.findById(id);

		String header = "";
		if (mem.getDegree() != null && mem.getDegree().getShortForm() != null) {
			header = header + mem.getDegree().getShortForm();
		}

		header = header + " " + mem.getFirstname() + " " + mem.getLastname();
		String street = mem.getContactData().getStreet() + " "
				+ mem.getContactData().getNumber();
		String postTown = mem.getContactData().getPostCode() + " "
				+ mem.getContactData().getTown();
		String mailPrivate = mem.getContactData().getEmail();

        String content = getText("member.info.modal", mem.getSalutation(),
                mem.getFirstname(), mem.getLastname(), street, postTown,
                mailPrivate).getString();

		final BusinessCardModal modal = new BusinessCardModal("modal-info", id);
		modal.setOutputMarkupId(true);
        modal.addCloseButton(Model.of(getText("global.close").getString()));
        modal.header(Model.of(header));

		modal.setEscapeModelStrings(false);

		this.modalInfo.replaceWith(modal);
		this.modalInfo = modal;
		this.modalWarning.setVisible(false);
		modalInfo.setEscapeModelStrings(false);
		modalInfo.show(true);

	}

	private void deleteMember(final long id, String firstname, String lastname) {

		final TextContentModal modal = new TextContentModal("modal-prompt",
                Model.of(getText("modal.text", firstname, lastname).getString()));
        modal.setOutputMarkupId(true);

        modal.addCloseButton(Model.of(getText("modal.close", "").getString()));
        modal.header(Model.of(getText("modal.header", firstname, lastname)
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
				doDeleteMember(id);
				target.appendJavaScript("$('.modal').modal('close');");
				setResponsePage(Homepage.class);

			}
		};

		modal.addButton(doDelete);
		this.modalWarning.replaceWith(modal);
		this.modalWarning = modal;
		this.modalInfo.setVisible(false);
		modalWarning.show(true);
	}

	private void doDeleteMember(long id) {
		memberService.deleteMember(id);
	}

	private void editMember(long id) {
		PageParameters params = new PageParameters();
		params.add(MemberDetailPage.EDIT_TYPE, Type.Edit);
		params.add(MemberDetailPage.MEMBER_ID, id);
		setResponsePage(MemberDetailPage.class, params);
	}

}
