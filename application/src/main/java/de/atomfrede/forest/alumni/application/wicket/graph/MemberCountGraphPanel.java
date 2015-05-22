package de.atomfrede.forest.alumni.application.wicket.graph;

import br.com.digilabs.jqplot.JqPlotResources;
import br.com.digilabs.jqplot.chart.LabeledLineChart;
import br.com.digilabs.jqplot.data.item.LabeledItem;
import br.com.digilabs.jqplot.elements.Highlighter;
import de.atomfrede.forest.alumni.application.wicket.Numbers;
import de.atomfrede.forest.alumni.application.wicket.jqplot.JQPlotChart;
import de.atomfrede.forest.alumni.service.member.MemberService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils.getText;

//import de.atomfrede.forest.alumni.application.wicket.jqplot.JQPlotChart;

@SuppressWarnings("serial")
public class MemberCountGraphPanel extends Panel {

	private final Log log = LogFactory.getLog(MemberCountGraphPanel.class);

	@SpringBean
	MemberService memberService;

	public MemberCountGraphPanel(String id) {
		super(id);
		setupGraph();
	}

	/**
	 * Build plot data.
	 */
	private void setupGraph() {
		try {
			DateTime dt = new DateTime(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String endYear = sdf.format(dt.toDate());
			dt = dt.year().addToCopy(Numbers.TEN * -1);
			String startYear = sdf.format(dt.toDate());

			Map<Date, Integer> values = memberService.getMemberCountPerYear(dt
					.toDate());

			LabeledLineChart<Integer> lineChart = new LabeledLineChart<Integer>(
                    getText("member.graph.heading", startYear, endYear).getString(),
                    getText("graph.year").getString(), getText("graph.members").getString());

			Date[] dates = values.keySet().toArray(new Date[] {});

			SimpleDateFormat sdfPlot = new SimpleDateFormat("yyyy-MM-dd");
			Arrays.sort(dates);
			for (Date key : dates) {
				LabeledItem<Integer> item = new LabeledItem<Integer>(
						sdfPlot.format(key), values.get(key));
				lineChart.addValue(item);

			}

			Highlighter hl = new Highlighter();
			hl.setShow(true);
			hl.setSizeAdjust(7.5);
			lineChart.getChartConfiguration().setHighlighter(hl);

			lineChart.getYAxis().setAutoScale(true);
			lineChart.getXAxis().getTickOptions().setFormatString("%b-%d-%Y");
			lineChart.getYAxis().setMin(Numbers.ZERO + "");
			lineChart.getXAxis().setAutoScale(true);
			lineChart.getXAxis().setRenderer(JqPlotResources.DateAxisRenderer);
			lineChart.getXAxis().setPad(0.0F);
			lineChart.getXAxis().setMin("'" + sdfPlot.format(dates[0]) + "'");
			add(new JQPlotChart("chart1", lineChart));
		} catch (Exception e) {
			log.error("Could not retrieve data values for plot.", e);
		}
	}

}
