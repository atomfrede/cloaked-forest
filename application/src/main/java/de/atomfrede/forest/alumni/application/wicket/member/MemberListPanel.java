package de.atomfrede.forest.alumni.application.wicket.member;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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

import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonSize;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import de.agilecoders.wicket.markup.html.bootstrap.button.TypedLink;
import de.agilecoders.wicket.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.application.wicket.homepage.Homepage;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class MemberListPanel extends Panel{

	@SpringBean
	MemberDao memberDao;
	
	MemberListActionPanel actionPanel;
	MemberProvider memberProvider;
	DataView<Member> members;
	WebMarkupContainer wmc;
	TextContentModal modal;
	
	public MemberListPanel(String id) {
		super(id);
		
		actionPanel = new MemberListActionPanel("members-action");
		add(actionPanel);
		addFilter();
		setOutputMarkupId(true);
		wmc = new WebMarkupContainer("table-wrapper");
		wmc.setOutputMarkupId(true);
		populateItems();
		setupModal();
		
	}
	
	private void addFilter(){
		actionPanel.nameFilter.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String input = actionPanel.nameFilter.getConvertedInput();
				doFilter(input);
				target.add(wmc);
			}
		});
	}
	
	protected void doFilter(String input){
		getMemberProvider().nameFilter = input;
	}
	
	private void populateItems(){
		
		
		members = new DataView<Member>("members", getMemberProvider()){

			@Override
			protected void populateItem(Item<Member> item) {
				final Member member = item.getModel().getObject();
				final Degree degree = member.getDegree();
				
				item.add(new Label("firstname", new PropertyModel<String>(member, "firstname")));
				item.add(new Label("lastname", new PropertyModel<String>(member, "lastname")));
				if(degree != null){
					String degreeShort = degree.getShortForm();
					String graduationYear = member.getYearOfGraduation();
					String label = degreeShort+ " ("+graduationYear+")";
					Label degreeLbl = new Label("degree", Model.of(label));
					degreeLbl.add(new TooltipBehavior(new PropertyModel<String>(degree, "title")));
					item.add(degreeLbl);
				}else{
					item.add(new Label("degree", Model.of("-/-")));
				}
				item.add(new Label("profession", new PropertyModel<String>(member, "profession")));
				
				int size = member.getActivities().size();
				int cSize = 1;
				StringBuilder sb = new StringBuilder();
				for(Activity act:member.getActivities()){
					sb.append(act.getActivity());
					if(cSize < size){
						sb.append(", ");
					}
					cSize++;
				}
				item.add(new Label("activity", Model.of(sb.toString())));
				
				final long memberId = member.getId();
				final String firstname = member.getFirstname();
				final String lastname = member.getLastname();
				TypedLink<Void> editUser = new TypedLink<Void>("action-edit", ButtonType.Default) {

					@Override
					public void onClick() {
						editMember(memberId);
						
					}
				};
				editUser.setIconType(IconType.pencil).setSize(ButtonSize.Mini).setInverted(false);
				
				TypedLink<Void> deleteUser = new TypedLink<Void>("action-delete", ButtonType.Danger) {

					@Override
					public void onClick() {
						deleteMember(memberId, firstname, lastname);
						
					}
				};
				deleteUser.setIconType(IconType.remove).setSize(ButtonSize.Mini);
				
				item.add(editUser);
				item.add(deleteUser);
			}
			
		};
		members.setItemsPerPage(15);
		members.setOutputMarkupId(true);
		wmc.add(members);
		wmc.add(new BootstrapAjaxPagingNavigator("pager", members));
		add(wmc);
	}
	
	void setupModal(){
		modal = new TextContentModal("modal-prompt", Model.of("Hallo Welt"));
		modal.addCloseButton(Model.of(_("modal.close", "").getString()));
		add(modal);
	}
	
	public MemberProvider getMemberProvider(){
		if(memberProvider == null){
			memberProvider = new MemberProvider();
		}
		return memberProvider;
	}
	
	private void deleteMember(final long id, String firstname, String lastname){
		
		final TextContentModal modal = new TextContentModal("modal-prompt", Model.of(_("modal.text", firstname, lastname).getString()));
		modal.setOutputMarkupId(true);
		
		modal.addCloseButton(Model.of(_("modal.close", "").getString()));
		modal.header(Model.of(_("modal.header", firstname, lastname).getString()));
		
		AjaxLink<String> doDelete = new AjaxLink<String>("button", Model.of(_("modal.delete", "").getString())) {

			@Override
		    protected void onConfigure() {
		        super.onConfigure();

		        setBody(getDefaultModel());
		        add(new ButtonBehavior(ButtonType.Danger));
//		        add(new IconBehavior(IconType.remove));
		    }
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				doDeleteMember(id);
				target.appendJavaScript("$('.modal').modal('close');");
				setResponsePage(Homepage.class);
				
			}
		};
		
		
		modal.addButton(doDelete);
		this.modal.replaceWith(modal);
		modal.show(true);
		this.modal = modal;
	}
	
	private void doDeleteMember(long id){
		memberDao.remove(id);
	}
	
	private void editMember(long id){
		PageParameters params = new PageParameters();
		params.add(MemberDetailPage.EDIT_TYPE, Type.Edit);
		params.add(MemberDetailPage.MEMBER_ID, id);
		setResponsePage(MemberDetailPage.class, params);
	}

}
