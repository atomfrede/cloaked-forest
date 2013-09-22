package de.atomfrede.forest.alumni.application.wicket.graph;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.digilabs.jqplot.chart.PieChart;
import br.com.digilabs.jqplot.data.item.LabeledItem;
import de.atomfrede.forest.alumni.application.wicket.jqplot.JQPlotChart;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.service.member.MemberService;

@SuppressWarnings("serial")
public class DegreeGraphPanel extends Panel {

	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(DegreeGraphPanel.class);

	@SpringBean
	MemberService memberService;

	public DegreeGraphPanel(String id) {
		super(id);
		setupGraph();
	}

	private void setupGraph() {
		PieChart<Number> pieChart = new PieChart<>(_("graph.degree.title")
				.getString());

		Map<Degree, Integer> values = memberService.getMembersPerDegree();

		for (Degree deg : values.keySet()) {
			LabeledItem<Number> item = new LabeledItem<Number>(
					deg.getShortForm(), values.get(deg));
			pieChart.addValue(item);
		}

		pieChart.getSeriesDefaults().getRendererOptions()
				.setShowDataLabels(true);
		add(new JQPlotChart("chart1", pieChart));
	}

}
