package de.atomfrede.forest.alumni.application.wicket.member;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.text.DateFormat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.PopoverBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.PopoverConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.Numbers;
import de.atomfrede.forest.alumni.application.wicket.base.BasePage.Type;
import de.atomfrede.forest.alumni.application.wicket.homepage.Homepage;
import de.atomfrede.forest.alumni.application.wicket.member.custom.BusinessCardModal;
import de.atomfrede.forest.alumni.application.wicket.member.custom.LeaveMemberModal;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.application.wicket.util.StringCheckUtil;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.member.MemberService;

@SuppressWarnings("serial")
public class MemberListPanel extends Panel {

	@SpringBean
	private MemberDao memberDao;

	@SpringBean
	private MemberService memberService;

	private MemberListActionPanel actionPanel;
	private MemberProvider memberProvider;
	private DataView<Member> members;
	private WebMarkupContainer wmc;
	private TextContentModal modalWarning;
	private BusinessCardModal modalInfo;
	private LeaveMemberModal leaveModal;

	private Long currentlyDisplayedId = null;
	private Integer currentlyDisplayPosition = null;
	
	private DateFormat df;

	public MemberListPanel(String id) {
		super(id);

		df = DateFormat.getDateInstance(DateFormat.SHORT, getSession().getLocale());
		
		actionPanel = new MemberListActionPanel("members-action");
		add(actionPanel);
		addFilter();
		setOutputMarkupId(true);
		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
		setupModalInfo();
		setupModalLeave();

	}

	private void addFilter() {

		actionPanel.getNameFilter().add(
				new AjaxFormComponentUpdatingBehavior("keyUp") {
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(wmc);
						String input = actionPanel.getNameFilter()
								.getConvertedInput();
						doFilter(input);
					}
				});
	}

	/**
	 * Filtering the displayed results by setting a filter value into the
	 * provier.
	 * 
	 * @param input
	 */
	protected void doFilter(String input) {
		getMemberProvider().setNameFilter(input);
	}

	/**
	 * Populates the list view repeater with content.
	 */
	private void populateItems() {

		members = new DataView<Member>("members", getMemberProvider()) {

			@Override
			protected void populateItem(Item<Member> item) {
				final int index = item.getIndex();
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
				Label actLabel = new Label("activity", Model.of(sb.toString()));
				String companyName = null;
				String departmentName = null;
				if (member.getCompany() != null) {
					companyName = member.getCompany().getCompany();
				}
				if (member.getDepartment() != null) {
					departmentName = member.getDepartment().getDepartment();
				}

				if (StringCheckUtil.isStringSet(companyName)
						&& StringCheckUtil.isStringSet(departmentName)) {
					PopoverConfig popConfig = new PopoverConfig()
							.withAnimation(true).withHoverTrigger();

					PopoverBehavior popOver = new PopoverBehavior(
							Model.of(companyName), Model.of(departmentName),
							popConfig);

					actLabel.add(popOver);
				} else {
					if (StringCheckUtil.isStringSet(companyName)) {
						actLabel.add(new TooltipBehavior(Model.of(companyName)));
					} else {
						actLabel.add(new TooltipBehavior(Model
								.of(departmentName)));
					}
				}
				item.add(actLabel);

				String leaveDateText = "--";
				if(member.getLeaveDate() != null) {
					leaveDateText = df.format(member.getLeaveDate());
				}
				Label leaveDate = new Label("leavedate", Model.of(leaveDateText));
				item.add(leaveDate);
				
				final long memberId = member.getId();
				final String firstname = member.getFirstname();
				final String lastname = member.getLastname();

				BootstrapLink<Void> infoUser = new BootstrapLink<Void>(
						"action-info", Buttons.Type.Default) {
					public void onClick() {
						infoMember(memberId, index);
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

				BootstrapLink<Void> leaveUser = new BootstrapLink<Void>(
						"action-leave", Buttons.Type.Warning) {

					@Override
					public void onClick() {
						leaveMember(memberId, firstname, lastname);
					}

				};
				leaveUser.setIconType(IconType.bancircle).setSize(
						Buttons.Size.Mini);

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
				item.add(leaveUser);
				item.add(deleteUser);
			}

		};
		members.setItemsPerPage(Numbers.TEN + Numbers.FIVE);
		members.setOutputMarkupId(true);
		wmc.add(members);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", members));
		add(wmc);
	}

	private void setupModalInfo() {
		modalInfo = new BusinessCardModal("modal-info", null);
		modalInfo.addCloseButton(Model.of(_("modal.close", "").getString()));
		add(modalInfo);
	}
	
	private void setupModalLeave() {
		leaveModal = new LeaveMemberModal("modal-leave", null);
		leaveModal.addCloseButton(Model.of(_("modal.close").getString()));
		add(leaveModal);
	}

	private void setupModal() {
		modalWarning = new TextContentModal("modal-prompt",
				Model.of("Hallo Welt"));
		modalWarning.addCloseButton(Model.of(_("modal.close", "").getString()));
		add(modalWarning);
	}

	private MemberProvider getMemberProvider() {
		if (memberProvider == null) {
			memberProvider = new MemberProvider();
		}
		return memberProvider;
	}

	private BootstrapLink<String> getNextButton() {
		final int ending = (int) Math.min(members.getItemsPerPage() - 1,
				members.getItemCount());

		BootstrapLink<String> nextButton = new BootstrapLink<String>("button",
				Model.of("Weiter"), Buttons.Type.Default) {

			@Override
			public void onClick() {
				// We want only next and work on the current page
				if (currentlyDisplayPosition < ending - 1) {
					@SuppressWarnings("unchecked")
					Long newId = ((AbstractEntityModel<Member>) members.get(
							currentlyDisplayPosition + 1).getInnermostModel())
							.getObject().getId();
					Integer newPosition = currentlyDisplayPosition + 1;
					infoMember(newId, newPosition);
				}
			}
		};

		if (currentlyDisplayPosition == ending - 1) {
			nextButton.add(new AttributeAppender("class", " disabled"));
		}

		nextButton.setLabel(Model.of(_("next")))
				.setIconType(IconType.arrowright).setInverted(false);

		return nextButton;
	}

	private BootstrapLink<String> getPrevButton() {
		BootstrapLink<String> prevButton = new BootstrapLink<String>("button",
				Model.of("Back"), Buttons.Type.Default) {

			@Override
			public void onClick() {
				if (currentlyDisplayPosition > 0) {
					@SuppressWarnings("unchecked")
					Long newId = ((AbstractEntityModel<Member>) members.get(
							currentlyDisplayPosition - 1).getInnermostModel())
							.getObject().getId();
					Integer newPosition = currentlyDisplayPosition - 1;
					infoMember(newId, newPosition);
				}

			}
		};

		if (currentlyDisplayPosition == 0) {
			prevButton.add(new AttributeAppender("class", " disabled"));
		}

		prevButton.setLabel(Model.of(_("back")))
				.setIconType(IconType.arrowleft).setInverted(false);

		return prevButton;
	}

	private BootstrapLink<String> getEditButton() {
		BootstrapLink<String> editButton = new BootstrapLink<String>("button",
				Model.of("legend.edit"), Buttons.Type.Info) {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				modalInfo.setVisible(false);
				modalInfo.show(false);
				editMember(currentlyDisplayedId);

			}
		};

		editButton.setLabel(Model.of(_("legend.edit")))
				.setIconType(IconType.edit).setInverted(true);
		return editButton;
	}

	private LeaveMemberModal createModalLeave(final long id) {
		Member mem = memberService.findById(id);
		
		String header = "";
		if (mem.getDegree() != null && mem.getDegree().getShortForm() != null) {
			header = header + mem.getDegree().getShortForm();
		}

		header = header + " " + mem.getFirstname() + " " + mem.getLastname();
		
		LeaveMemberModal modal = new LeaveMemberModal("modal-leave", id);
		
		modal.setOutputMarkupId(true);

		modal.addCloseButton(Model.of(_("global.close").getString()));
		modal.header(Model.of(header));
		
		return modal;
		
	}
	
	private BusinessCardModal createModalInfoContent(final long id) {
		Member mem = memberDao.findById(id);

		String header = "";
		if (mem.getDegree() != null && mem.getDegree().getShortForm() != null) {
			header = header + mem.getDegree().getShortForm();
		}

		header = header + " " + mem.getFirstname() + " " + mem.getLastname();

		BusinessCardModal modal = new BusinessCardModal("modal-info", id);

		modal.setOutputMarkupId(true);

		modal.addButton(getPrevButton());
		modal.addButton(getEditButton());
		modal.addButton(getNextButton());

		modal.addCloseButton(Model.of(_("global.close").getString()));
		modal.header(Model.of(header));

		modal.setEscapeModelStrings(false);
		modal.setOutputMarkupId(true);

		return modal;
	}

	private void infoMember(final long id, final int index) {
		currentlyDisplayedId = id;
		currentlyDisplayPosition = index;

		final BusinessCardModal modal = createModalInfoContent(id);

		this.modalInfo.replaceWith(modal);
		this.modalInfo = modal;
		this.modalWarning.setVisible(false);
		this.leaveModal.setVisible(false);

		modalInfo.setEscapeModelStrings(false);
		modalInfo.setOutputMarkupId(true);
		modalInfo.show(true);
	}

	private void leaveMember(final long id, String firstname, String lastname) {
		final LeaveMemberModal modal = createModalLeave(id);
		
		AjaxLink<String> doLeave = new AjaxLink<String>("button", Model.of(_("modal.leave").getString())) {

			@Override
			protected void onConfigure() {
				super.onConfigure();

				setBody(getDefaultModel());
				add(new ButtonBehavior(Buttons.Type.Warning));
				// add(new IconBehavior(IconType.remove));
			}
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				System.out.println(modal.getEnteredDate());
				memberService.leaveMember(id, modal.getEnteredDate());
				target.appendJavaScript("$('.modal').modal('close');");
				setResponsePage(Homepage.class);
//				setResponsePage(Homepage.class);
				
			}
		};
		
		modal.addButton(doLeave);

		this.leaveModal.replaceWith(modal);
		this.leaveModal = modal;
		this.modalWarning.setVisible(false);
		this.modalInfo.setVisible(false);
		
		
		leaveModal.setEscapeModelStrings(false);
		leaveModal.setOutputMarkupId(true);
		leaveModal.show(true);
	}
	
	private void deleteMember(final long id, String firstname, String lastname) {

		final TextContentModal modal = new TextContentModal("modal-prompt",
				Model.of(_("modal.text", firstname, lastname).getString()));
		modal.setOutputMarkupId(true);

		modal.addCloseButton(Model.of(_("modal.close", "").getString()));
		modal.header(Model.of(_("modal.header", firstname, lastname)
				.getString()));

		AjaxLink<String> doDelete = new AjaxLink<String>("button", Model.of(_(
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
		this.leaveModal.setVisible(false);
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
