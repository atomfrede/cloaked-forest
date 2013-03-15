package de.atomfrede.forest.alumni.application.wicket.jqplot;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.wicketstuff.jqplot.behavior.JqPlotBehavior;

import br.com.digilabs.jqplot.Chart;
import br.com.digilabs.jqplot.JqPlotUtils;

@SuppressWarnings("serial")
public class JQPlotChart extends WebMarkupContainer{

	public JQPlotChart(String id, Chart<?> chart){
		super(id);
		setOutputMarkupId(true);
		add(new JQPlotBehaviour(chart, getMarkupId()));
	}
	

}
