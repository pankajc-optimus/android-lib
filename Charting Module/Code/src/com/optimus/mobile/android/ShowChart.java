package com.optimus.mobile.android;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * class name : ShowChart.java This class is responsible for generating charts
 * on the basis of the values passed by the user
 * 
 * @author Pooja
 * 
 */
public class ShowChart extends Activity {
	private int series1[] = new int[3];
	private int series2[] = new int[3];
	private String chart;
	boolean bar;
	boolean line;
	boolean scatter;
	boolean pie;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		/**
		 * get the value of 1st series array from the previous activity
		 */
		series1 = getIntent().getExtras().getIntArray("year1");

		/**
		 * get the value of 2nd series from the previous activity
		 */
		series2 = getIntent().getExtras().getIntArray("year2");

		/**
		 * get the string of the the button pressed from the previous activity.
		 * The button pressed from the previous activity represents the type of
		 * chart that needs to be created. Four types of charts are supported
		 * here : line chart, bar chart, scatter chart, pie chart
		 */
		chart = getIntent().getExtras().getString("chart");

		bar = (chart.equals("bar"));
		line = (chart.equals("line"));
		pie = (chart.equals("pie"));
		scatter = (chart.equals("scatter"));

		/**
		 * XYMultipleSeriesRenderer is the class from the library(jar file)
		 */
		XYMultipleSeriesRenderer renderer = getDemoRenderer();
		if (line) {

			/**
			 * calling the intent from the ChartFactory of the library to create
			 * line chart
			 */
			intent = ChartFactory.getLineChartIntent(this, getDemoDataset(),
					getDemoRenderer());
		}
		if (bar) {

			/**
			 * set the general chart settings
			 */
			setChartSettings(renderer);

			/**
			 * calling the intent from the ChartFactory to create Bar chart
			 */
			intent = ChartFactory.getBarChartIntent(this, getDemoDataset(),
					renderer, Type.DEFAULT);
		}
		if (scatter) {

			/**
			 * calling the intent from the ChartFactory to create Scatter chart
			 */
			intent = ChartFactory.getScatterChartIntent(this, getDemoDataset(),
					getDemoRenderer());
		}
		if (pie) {

			/**
			 * calling the intent from the ChartFactory to create Pie chart
			 */
			intent = ChartFactory.getPieChartIntent(this, getPieDataset(),
					getPieRenderer(), "Pie Chart example");
		}

		startActivity(intent);

	}

	/**
	 * function name : getPieDataSet
	 * 
	 * responsible for getting the data for creating the pie chart
	 * 
	 * here we are creating pie chart of only the 1st series
	 * 
	 * @return : series of type CategorySeries. CategorySeries is a library
	 *         class : org.achartengine.model.CategorySeries
	 */
	public CategorySeries getPieDataset() {
		CategorySeries series = new CategorySeries("pie chart demo example");
		for (int k = 0; k < 3; k++) {
			series.add(series1[k]);
		}
		return series;

	}

	/**
	 * function name : getPieRenderer
	 * 
	 * Responsible for setting chart settings and adding colours for each of the
	 * three value in the pie chart
	 * 
	 * @return : DefaultRenderer instance. DefaultRenderer is also the library
	 *         class :org.achartengine.renderer.DefaultRenderer
	 */
	public DefaultRenderer getPieRenderer() {

		/**
		 * creating an instance of the DefaultRenderer
		 */
		DefaultRenderer renderer = new DefaultRenderer();

		/**
		 * set the chart title
		 */
		renderer.setChartTitle("budget");

		/**
		 * set the margin for the chart
		 */
		renderer.setMargins(new int[] { 20, 30, 15, 0 });

		/**
		 * text size of the labels
		 */
		renderer.setLabelsTextSize(15);

		/**
		 * size of the chart title text
		 */
		renderer.setChartTitleTextSize(20);

		/**
		 * create an instance of SimpleSeriesRenderer for the 1st value
		 * 
		 * (org.achartengine.renderer.SimpleSeriesRenderer)
		 */
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();

		/**
		 * set colour of for the first value
		 */
		r.setColor(Color.CYAN);

		/**
		 * add the above renderer r to the defaultRenderer instance
		 */
		renderer.addSeriesRenderer(r);

		/**
		 * create another SimpleSeriesRenderer for the 2nd value
		 */
		r = new SimpleSeriesRenderer();

		/**
		 * set colour for the 2nd value
		 */
		r.setColor(Color.GREEN);

		/**
		 * add the renderer r to the DefaultRenderer instance
		 */
		renderer.addSeriesRenderer(r);

		/**
		 * create 3rd instance of SimpleSeriesRenderer for the 3rd value
		 */
		r = new SimpleSeriesRenderer();

		/**
		 * set the colour for the 3rd value
		 */
		r.setColor(Color.LTGRAY);

		/**
		 * add the renderer r to the DefaultRenderer instance
		 */
		renderer.addSeriesRenderer(r);
		return renderer;

	}

	/**
	 * function name : getDemoRenderer()
	 * 
	 * responsible for the chart settings and setting axis and value colours for
	 * the values in bar chart, line chart, and scatter chart
	 * 
	 * @return : instance of XYMultipleSeriesRenderer
	 *         (org.achartengine.renderer.XYMultipleSeriesRenderer)
	 */
	public XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });

		/**
		 * if bar chart has to be created then an instance of
		 * SimpleSeriesRenderer has to be created
		 */
		if (bar) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(Color.BLUE);
			renderer.addSeriesRenderer(r);
			r = new SimpleSeriesRenderer();
			r.setColor(Color.GREEN);
			renderer.addSeriesRenderer(r);

		}

		/**
		 * In case of line chart, or scatter chart. XYSeriesRenderer has to be
		 * created
		 */
		if (line || scatter) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(Color.BLUE);
			r.setPointStyle(PointStyle.SQUARE);
			r.setFillBelowLine(true);
			r.setFillBelowLineColor(Color.WHITE);
			r.setFillPoints(true);
			renderer.addSeriesRenderer(r);
			r = new XYSeriesRenderer();
			r.setPointStyle(PointStyle.CIRCLE);
			r.setColor(Color.GREEN);
			r.setFillPoints(true);
			renderer.addSeriesRenderer(r);
			renderer.setAxesColor(Color.DKGRAY);
			renderer.setLabelsColor(Color.LTGRAY);

		}
		return renderer;
	}

	/**
	 * this function is responsible for setting the general chart settings.
	 * 
	 * @param renderer
	 *            : XYMultipleSeriesRenderer type.
	 */
	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Chart demo");
		renderer.setXTitle("x values");
		renderer.setYTitle("y values");
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(10.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(210);
	}

	/**
	 * function name: getDemoDataset
	 * 
	 * responsible for getting data to create bar chart, line chart, and pie
	 * chart
	 * 
	 * @return : dataset of the type XYMultipleSeriesDataset
	 */
	private XYMultipleSeriesDataset getDemoDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int value[] = new int[3];

		/**
		 * since there are two series, loop has to iterate rwo times
		 */
		for (int i = 0; i < 2; i++) {

			/**
			 * for the first series, data will be the 1st array
			 */
			if (i == 0) {
				value = series1;

			}
			/**
			 * for the 2nd series, data will be the 2nd array
			 */
			if (i == 1) {
				value = series2;
			}

			/**
			 * in case of bar chart creation, data has to be added in the form
			 * of CategorySeries class of the library
			 */
			if (bar) {
				CategorySeries series = new CategorySeries("Demo series "
						+ (i + 1));

				/**
				 * loop iterates for 3 times since there are 3 values in each of
				 * the series
				 */
				for (int k = 0; k < 3; k++) {

					/**
					 * add the value to the series
					 */
					series.add(value[k]);
				}

				/**
				 * add the series to the dataset
				 */
				dataset.addSeries(series.toXYSeries());
			}

			/**
			 * in case of line or scatter chart, data has to be added in the
			 * form of XYSeries class of the library
			 */
			if (line || scatter) {
				XYSeries series = new XYSeries("Demo series " + (i + 1));
				for (int k = 0; k < 3; k++) {
					series.add(k, value[k]);
				}
				dataset.addSeries(series);
			}

		}
		/**
		 * return the dataset.
		 */
		return dataset;
	}

}
