package de.atomfrede.forest.alumni.application.wicket.member.detail;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.custom.CustomSelectOption;
import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class MembersDetailForm extends Form<Void>{

	@SpringBean
	DegreeDao degreeDao;
	
	Type editType;
	
	Label legendLabel;
	RequiredTextField<String> firstname, lastname;
	
	public MembersDetailForm(String id, Type editType, AbstractEntityModel<Member> model) {
		super(id);
		this.editType = editType;
		
		switch (this.editType) {
		case Create:
			legendLabel = new Label("member-detail-legend", Model.of(_("legend.create", "Create")));
			break;
		case Edit:
			legendLabel = new Label("member-detail-legend", Model.of(_("legend.edit", "Edit")));
			break;
		case Show:
			legendLabel = new Label("member-detail-legend", Model.of(_("legend.show", "Show")));
			break;
		default:
			legendLabel = new Label("member-detail-legend", Model.of(_("legend.show", "Show")));
			break;
		}
		
		add(legendLabel);
		
		firstname = new RequiredTextField<String>("firstname");
		lastname = new RequiredTextField<String>("lastname");
		add(firstname);
		add(lastname);
		
		List<Degree> degrees = degreeDao.findAll();
		
		Select<Degree> degreeSelect = new Select<Degree>("degree-select");
		
		degreeSelect.add(new ListView<Degree>("degree-options", degrees) {

			@Override
			protected void populateItem(ListItem<Degree> item) {
				item.add(new CustomSelectOption("degree-option", item.getModelObject().getTitle()));
			}
		});
		
		add(degreeSelect);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript("$('.selectpicker').selectpicker();"));
		
	}

}
