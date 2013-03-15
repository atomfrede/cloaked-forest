package de.atomfrede.forest.alumni.application.wicket.jqplot;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
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

	private static final ResourceReference JQPLOT_BAR = new JavaScriptResourceReference(
			JQPlotBehaviour.class, "jqplot.barRenderer.min.js");

	private static final ResourceReference JQPLOT_BEZIER = new JavaScriptResourceReference(
			JQPlotBehaviour.class, "jqplot.BezierCurveRenderer.min.js");

	private static final ResourceReference JQPLOT_BLOCK = new JavaScriptResourceReference(
			JQPlotBehaviour.class, "jqplot.blockRenderer.min.js");

	private static final ResourceReference JQPLOT_BUBBLE = new JavaScriptResourceReference(
			JQPlotBehaviour.class, "jqplot.bubbleRenderer.min.js");

	private static final ResourceReference JQPLOT_JSON2 = new JavaScriptResourceReference(
			JQPlotBehaviour.class, "jqplot.json2.min.js");
	private static final ResourceReference JQPLOT_CSS = new CssResourceReference(
			JQPlotBehaviour.class, "jquery.jqplot.min.css");
	
	private static final ResourceReference JQUERY = new JavaScriptResourceReference(JQPlotBehaviour.class, "jqeury.min.js");

	private Chart<?> chart;
	private String divId;

	public JQPlotBehaviour(Chart<?> chart, String divId) {
		this.chart = chart;
		this.divId = divId;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(JQPLOT_JS));
		response.render(CssHeaderItem.forReference(JQPLOT_CSS));
		response.render(JavaScriptHeaderItem.forReference(JQPLOT_BAR));
		response.render(JavaScriptHeaderItem.forReference(JQPLOT_BEZIER));
		response.render(JavaScriptHeaderItem.forReference(JQPLOT_BLOCK));
		response.render(JavaScriptHeaderItem.forReference(JQPLOT_JSON2));
		
		String json = JqPlotUtils.createJquery(chart, divId);
		response.render(JavaScriptHeaderItem.forScript(json, null));
		// response.renderJavaScript(json, null);

	}
}
