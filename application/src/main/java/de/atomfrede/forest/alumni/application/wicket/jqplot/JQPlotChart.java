package de.atomfrede.forest.alumni.application.wicket.jqplot;

import org.apache.wicket.markup.html.WebMarkupContainer;

import br.com.digilabs.jqplot.Chart;

@SuppressWarnings("serial")
public class JQPlotChart extends WebMarkupContainer{

	public JQPlotChart(String id, Chart<?> chart){
		super(id);
		setOutputMarkupId(true);
		add(new JQPlotBehaviour(chart, getMarkupId()));
	}
}
