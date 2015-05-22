package de.atomfrede.forest.alumni.application.wicket.graph;

import br.com.digilabs.jqplot.JqPlotResources;
import br.com.digilabs.jqplot.chart.BarChart;
import br.com.digilabs.jqplot.elements.TickOptions;
import de.atomfrede.forest.alumni.application.wicket.jqplot.JQPlotChart;
import de.atomfrede.forest.alumni.service.member.MemberService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Map;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils.getText;

@SuppressWarnings("serial")
public class SectorGraphPanel extends Panel {

	private static final int ANGLE = -45;
    private final Log log = LogFactory.getLog(SectorGraphPanel.class);
    @SpringBean
	MemberService memberService;

	public SectorGraphPanel(String id) {
		super(id);
		setupGraph();
	}

	private void setupGraph() {
        BarChart<Integer> barchart = new BarChart<>(getText(
                "graph.member.sector.header").getString());

		Map<String, Integer> values = memberService.getMembersPerSector();

		String[] ticks = values.keySet().toArray(new String[] {});
		Integer[] intValues = new Integer[values.size()];
		int count = 0;
		for (String key : values.keySet()) {
			intValues[count] = values.get(key);
			count++;
		}
		barchart.addValues(intValues);
		barchart.getXAxis().setTicks(ticks);
		TickOptions tickOptions = new TickOptions();
		tickOptions.setAngle(ANGLE);

		barchart.getAxesDefaults().setTickRenderer(
				JqPlotResources.CanvasAxisTickRenderer);
		barchart.getAxesDefaults().setTickOptions(tickOptions);
		
		// RendererOptions ro = new RendererOptions();
		// ro.setBarDirection("horizontal");
		// barchart.getYAxis().setRenderer(JqPlotResources.CategoryAxisRenderer);
		// barchart.getYAxis().setTicks(ticks);
		// barchart.getSeriesDefaults().setRendererOptions(ro);

		add(new JQPlotChart("chart1", barchart));
	}
}
