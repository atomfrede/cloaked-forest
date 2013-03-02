package de.atomfrede.forest.alumni.application.wicket.member;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class MemberListPanel extends Panel{

	@SpringBean
	MemberDao memberDao;
	
	MemberListActionPanel actionPanel;
	
	public MemberListPanel(String id) {
		super(id);
		
		actionPanel = new MemberListActionPanel("members-action");
		add(actionPanel);
		
		setOutputMarkupId(true);
		populateItems();
		
	}
	
	private void populateItems(){
		DataView<Member> members = new DataView<Member>("members", new MemberProvider()){

			@Override
			protected void populateItem(Item<Member> item) {
				final Member member = item.getModel().getObject();
				final Degree degree = member.getDegree();
				
				item.add(new Label("firstname", new PropertyModel<String>(member, "firstname")));
				item.add(new Label("lastname", new PropertyModel<String>(member, "lastname")));
				if(degree != null){
					Label degreeLbl = new Label("degree", new PropertyModel<String>(degree, "shortForm"));
					degreeLbl.add(new TooltipBehavior(new PropertyModel<String>(degree, "title")));
					item.add(degreeLbl);
				}else{
					item.add(new Label("degree", Model.of("-/-")));
				}
				item.add(new Label("graduation-year", new PropertyModel<String>(member, "yearOfGraduation")));
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
			}
			
		};
		
		members.setItemsPerPage(15);
		add(members);
		add(new BootstrapAjaxPagingNavigator("pager", members));
	}

}
