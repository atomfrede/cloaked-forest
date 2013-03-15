package de.atomfrede.forest.alumni.application.wicket.jqplot;

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.wicketstuff.jqplot.behavior.JqPlotBehavior;

import br.com.digilabs.jqplot.Chart;
import br.com.digilabs.jqplot.JqPlotUtils;

@SuppressWarnings("serial")
public class JQPlotBehaviour extends Behavior {

	private static final ResourceReference JQPLOT_JS = new JavaScriptResourceReference(
			JQPlotBehaviour.class, "jquery.jqplot.min.js");

	private static final ResourceReference JQPLOT_CSS = new CssResourceReference(
			JQPlotBehaviour.class, "jquery.jqplot.min.css");


	private Chart<?> chart;
	private String divId;

	public JQPlotBehaviour(Chart<?> chart, String divId) {
		this.chart = chart;
		this.divId = divId;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
		
		response.render(JavaScriptHeaderItem.forReference(JQPLOT_JS));
		response.render(CssHeaderItem.forReference(JQPLOT_CSS));

		List<String> resources = JqPlotUtils.retriveJavaScriptResources(chart);
		for (String resource : resources) {
			response.render(JavaScriptHeaderItem
					.forReference(new JavaScriptResourceReference(
							JQPlotBehaviour.class, resource)));
		}
		String json = createJquery();
		response.render(OnDomReadyHeaderItem.forScript(json));
	}

	private String createJquery() {
		StringBuilder builder = new StringBuilder();
		builder.append("$.jqplot('").append(divId).append("', ");
		builder.append(chart.getChartData().toJsonString());
		builder.append(", ");
		builder.append(JqPlotUtils.jqPlotToJson(chart.getChartConfiguration()));
		builder.append(");\r\n");
		return builder.toString();
	}
}
