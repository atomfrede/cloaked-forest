package de.atomfrede.forest.alumni.application.wicket.degree.detail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.degree.detail.DegreeDetailPage.Type;
import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.degree.DegreeDao;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;

@SuppressWarnings("serial")
public class DegreeDetailPanel extends Panel{

	@SpringBean
	private DegreeDao degreeDao;

	private Type editType;
	private Long degreeId;

	public DegreeDetailPanel(String id, Type editType, Long degreeId) {
		super(id);
		this.editType = editType;
		this.degreeId = degreeId;

		DegreeDetailForm form = new DegreeDetailForm("detail-form",
				this.editType, new AbstractEntityModel<Degree>(Degree.class,
						degreeId));
		add(form);
	}
}
