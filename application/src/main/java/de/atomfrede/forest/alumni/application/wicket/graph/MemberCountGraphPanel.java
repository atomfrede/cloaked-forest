package de.atomfrede.forest.alumni.application.wicket.graph;

import org.apache.wicket.markup.html.panel.Panel;

import br.com.digilabs.jqplot.chart.LineChart;
import de.atomfrede.forest.alumni.application.wicket.jqplot.JQPlotChart;

@SuppressWarnings("serial")
public class MemberCountGraphPanel extends Panel{

	public MemberCountGraphPanel(String id) {
		super(id);
		setupGraph();
	}
	
	private void setupGraph(){
		LineChart<Integer> lineChart = new LineChart<Integer>("Mitgliederentwicklung");
		lineChart.addValues(1,2,3,4,5);
		add(new JQPlotChart("chart1", lineChart));
	}

}
