package de.atomfrede.forest.alumni.application.wicket.graph;

import org.apache.wicket.markup.html.panel.Panel;
import org.wicketstuff.jqplot.JqPlotChart;

import de.atomfrede.forest.alumni.application.wicket.jqplot.JQPlotChart;

import br.com.digilabs.jqplot.chart.LineChart;

@SuppressWarnings("serial")
public class MemberCountGraphPanel extends Panel{

	public MemberCountGraphPanel(String id) {
		super(id);
		setupGraph();
	}
	
	private void setupGraph(){
		LineChart<Integer> lineChart = new LineChart<Integer>();
		lineChart.addValues(1,2,3,4,5);
		add(new JQPlotChart("chart1", lineChart));
	}

}
