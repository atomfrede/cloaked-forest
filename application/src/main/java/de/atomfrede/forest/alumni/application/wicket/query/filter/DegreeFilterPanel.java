package de.atomfrede.forest.alumni.application.wicket.query.filter;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.custom.DegreeSelectOption;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@SuppressWarnings("serial")
public class DegreeFilterPanel extends Panel{

	@SpringBean
	DegreeDao degreeDao;
	
	Degree selectedDegree;
	
	public DegreeFilterPanel(String id) {
		super(id);
		setupDegreeSelect();
	}
	
	public Degree getSelectedDegree(){
		return selectedDegree;
	}
	
	@SuppressWarnings("serial")
	private void setupDegreeSelect(){
		List<Degree> degrees = degreeDao.findAll();

		Degree noRestrictions = new Degree();
		noRestrictions.setTitle(_("query.no.restriction").getString());
		
		degrees.add(0, noRestrictions);
		
		Select<Degree> degreeSelect = new Select<Degree>("degree-select",
				new PropertyModel<Degree>(this, "selectedDegree"));

		degreeSelect.add(new ListView<Degree>("degree-options", degrees) {

			@Override
			protected void populateItem(ListItem<Degree> item) {
				item.add(new DegreeSelectOption("degree-option", item
						.getModelObject()));
			}
		});
		
		
		degreeSelect.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		add(degreeSelect);
	}
}
