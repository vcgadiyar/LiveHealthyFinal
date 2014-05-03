/*
 * Copyright 2012 AndroidPlot.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jsp.lh;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DBLayout.FoodRecord;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.androidplot.LineRegion;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.TextOrientationType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

/**
 * The simplest possible example of using AndroidPlot to plot some data.
 */
public class CaloriesChartActivity extends Activity {

	private static final String NO_SELECTION_TXT = "Touch bar to select.";
	private XYPlot plot;

	// private CheckBox series1CheckBox;
	private Spinner spWidthStyle;
	private SeekBar sbFixedWidth, sbVariableWidth;

	LinkedHashMap<String, Integer> entries;

	private XYSeries series1;
	FoodRecord fr;

	// Create a couple arrays of y-values to plot:
	Number[] series1Numbers10 = { 2, null, 5, 2, 7, 4, 3, 7, 4, 5 };
	Number[] series1Numbers20 = { 2, null, 5, 2, 7, 4, 3, 7, 4, 5, 7, 4, 5, 8,
			5, 3, 6, 3, 9, 3 };
	Number[] series1Numbers60 = { 2, null, 5, 2, 7, 4, 3, 7, 4, 5, 7, 4, 5, 8,
			5, 3, 6, 3, 9, 3, 2, null, 5, 2, 7, 4, 3, 7, 4, 5, 7, 4, 5, 8, 5,
			3, 6, 3, 9, 3, 2, null, 5, 2, 7, 4, 3, 7, 4, 5, 7, 4, 5, 8, 5, 3,
			6, 3, 9, 3 };
	Number[] series1Numbers = series1Numbers20;

	private MyBarFormatter formatter1;

	private MyBarFormatter selectionFormatter;

	private TextLabelWidget selectionWidget;

	private Pair<Integer, XYSeries> selection;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calories_chart);

		fr = new FoodRecord(CaloriesChartActivity.this);
		fr.open();

		entries = fr.getRecordsPerDay();

		Log.d("anavgire", "graph calorie count\n");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// Date startDate = new Date(System.currentTimeMillis() - 7L * 24 * 3600
		// * 1000 );
		Date startDate, endDate;
		Calendar start = Calendar.getInstance(), end = Calendar.getInstance();

		try {
			startDate = dateFormat
					.parse((String) entries.keySet().toArray()[0]);

			// start = Calendar.getInstance();
			// end = Calendar.getInstance();

			endDate = new Date(System.currentTimeMillis());

			Date minStartDate = new Date(System.currentTimeMillis() - 7L * 24
					* 3600 * 1000);
			if (startDate.after(minStartDate)) {
				startDate = minStartDate;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			startDate = new Date(System.currentTimeMillis() - 7L * 24 * 3600
					* 1000);
			endDate = new Date(System.currentTimeMillis());

		}

		start.setTime(startDate);
		end.setTime(endDate);

		Log.d("anavgire", "Start date = " + dateFormat.format(startDate));
		Log.d("anavgire", "End date = " + dateFormat.format(endDate));

		for (Date date = start.getTime(); !start.after(end); start.add(
				Calendar.DATE, 1), date = start.getTime()) {
			// Do your job here with `date`.
			String dateStr = dateFormat.format(date);
			if (!entries.containsKey(dateStr)) {
				Log.d("anavgire", "Entry does not exist for " + dateStr);
				entries.put(dateStr, 0);
			}
		}

		Map<String, Integer> sorted = sortByKeys(entries);

		ArrayList<Number> my_values = new ArrayList<Number>();
		for (String key : sorted.keySet()) {
			Log.d("anavgire", "key = " + key + " value = " + entries.get(key)
					+ "\n");
			my_values.add(entries.get(key));
		}

		series1Numbers = my_values.toArray(new Number[0]);
		fr.close();

		// initialize our XYPlot reference:
		plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

		formatter1 = new MyBarFormatter(Color.argb(200, 100, 150, 100),
				Color.LTGRAY);
		selectionFormatter = new MyBarFormatter(Color.YELLOW, Color.WHITE);

		selectionWidget = new TextLabelWidget(plot.getLayoutManager(),
				NO_SELECTION_TXT, new SizeMetrics(PixelUtils.dpToPix(100),
						SizeLayoutType.ABSOLUTE, PixelUtils.dpToPix(100),
						SizeLayoutType.ABSOLUTE),
				TextOrientationType.HORIZONTAL);

		selectionWidget.getLabelPaint().setTextSize(PixelUtils.dpToPix(16));

		// add a dark, semi-transparent background to the selection label
		// widget:
		Paint p = new Paint();
		p.setARGB(100, 0, 0, 0);
		selectionWidget.setBackgroundPaint(p);

		selectionWidget.position(0, XLayoutStyle.RELATIVE_TO_CENTER,
				PixelUtils.dpToPix(45), YLayoutStyle.ABSOLUTE_FROM_TOP,
				AnchorPosition.TOP_MIDDLE);
		selectionWidget.pack();

		// reduce the number of range labels
		plot.setTicksPerRangeLabel(3);
		plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
		plot.getGraphWidget().setGridPadding(30, 10, 30, 0);

		plot.setTicksPerDomainLabel(2);

		plot.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					onPlotClicked(new PointF(motionEvent.getX(), motionEvent
							.getY()));
				}
				return true;
			}
		});

		spWidthStyle = (Spinner) findViewById(R.id.spWidthStyle);
		ArrayAdapter<BarRenderer.BarWidthStyle> adapter1 = new ArrayAdapter<BarRenderer.BarWidthStyle>(
				this, android.R.layout.simple_spinner_item,
				BarRenderer.BarWidthStyle.values());
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spWidthStyle.setAdapter(adapter1);
		spWidthStyle.setSelection(BarRenderer.BarWidthStyle.VARIABLE_WIDTH
				.ordinal());
		spWidthStyle.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (BarRenderer.BarWidthStyle.VARIABLE_WIDTH
						.equals(spWidthStyle.getSelectedItem())) {
					sbFixedWidth.setVisibility(View.VISIBLE);
					sbVariableWidth.setVisibility(View.INVISIBLE);
				} else {
					sbFixedWidth.setVisibility(View.INVISIBLE);
					sbVariableWidth.setVisibility(View.VISIBLE);
				}
				updatePlot();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sbFixedWidth = (SeekBar) findViewById(R.id.sbFixed);
		sbFixedWidth.setProgress(50);
		sbFixedWidth
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int i,
							boolean b) {
						updatePlot();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		sbVariableWidth = (SeekBar) findViewById(R.id.sbVariable);
		sbVariableWidth.setProgress(1);
		sbVariableWidth.setVisibility(View.INVISIBLE);
		sbVariableWidth
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int i,
							boolean b) {
						updatePlot();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		plot.setDomainValueFormat(new NumberFormat() {
			@Override
			public StringBuffer format(double value, StringBuffer buffer,
					FieldPosition field) {
				String key = (String) entries.keySet().toArray()[(int) value];

				return new StringBuffer(key);
				// int year = (int) (value + 0.5d) / 12;
				// int month = (int) ((value + 0.5d) % 12);
				// return new
				// StringBuffer(DateFormatSymbols.getInstance().getShortMonths()[month]
				// + " '0" + year);
			}

			@Override
			public StringBuffer format(long value, StringBuffer buffer,
					FieldPosition field) {
				throw new UnsupportedOperationException("Not yet implemented.");
			}

			@Override
			public Number parse(String string, ParsePosition position) {
				throw new UnsupportedOperationException("Not yet implemented.");
			}
		});
		updatePlot();

	}

	private void updatePlot() {

		// Remove all current series from each plot
		Iterator<XYSeries> iterator1 = plot.getSeriesSet().iterator();
		while (iterator1.hasNext()) {
			XYSeries setElement = iterator1.next();
			plot.removeSeries(setElement);
		}

		// Setup our Series with the selected number of elements
		series1 = new SimpleXYSeries(Arrays.asList(series1Numbers),
				SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Calories");

		// add a new series' to the xyplot:
		plot.addSeries(series1, formatter1);

		// Setup the BarRenderer with our selected options
		MyBarRenderer renderer = ((MyBarRenderer) plot
				.getRenderer(MyBarRenderer.class));
		renderer.setBarRenderStyle(BarRenderer.BarRenderStyle.OVERLAID);
		renderer.setBarWidthStyle((BarRenderer.BarWidthStyle) spWidthStyle
				.getSelectedItem());
		renderer.setBarWidth(sbFixedWidth.getProgress());
		renderer.setBarGap(sbVariableWidth.getProgress());

		if (BarRenderer.BarRenderStyle.STACKED
				.equals(BarRenderer.BarRenderStyle.OVERLAID)) {
			plot.setRangeTopMin(1000);
		} else {
			plot.setRangeTopMin(1000);
		}

		plot.redraw();

	}

	private void onPlotClicked(PointF point) {

		// make sure the point lies within the graph area. we use gridrect
		// because it accounts for margins and padding as well.
		if (plot.getGraphWidget().getGridRect().contains(point.x, point.y)) {
			Number x = plot.getXVal(point);
			Number y = plot.getYVal(point);

			selection = null;
			double xDistance = 0;
			double yDistance = 0;

			// find the closest value to the selection:
			for (XYSeries series : plot.getSeriesSet()) {
				for (int i = 0; i < series.size(); i++) {
					Number thisX = series.getX(i);
					Number thisY = series.getY(i);
					if (thisX != null && thisY != null) {
						double thisXDistance = LineRegion.measure(x, thisX)
								.doubleValue();
						double thisYDistance = LineRegion.measure(y, thisY)
								.doubleValue();
						if (selection == null) {
							selection = new Pair<Integer, XYSeries>(i, series);
							xDistance = thisXDistance;
							yDistance = thisYDistance;
						} else if (thisXDistance < xDistance) {
							selection = new Pair<Integer, XYSeries>(i, series);
							xDistance = thisXDistance;
							yDistance = thisYDistance;
						} else if (thisXDistance == xDistance
								&& thisYDistance < yDistance
								&& thisY.doubleValue() >= y.doubleValue()) {
							selection = new Pair<Integer, XYSeries>(i, series);
							xDistance = thisXDistance;
							yDistance = thisYDistance;
						}
					}
				}
			}

		} else {
			// if the press was outside the graph area, deselect:
			selection = null;
		}

		if (selection == null) {
			selectionWidget.setText(NO_SELECTION_TXT);
		} else {
			selectionWidget.setText(" " + selection.second.getTitle()
					+ " Value: " + selection.second.getY(selection.first));
		}
		plot.redraw();
	}

	class MyBarFormatter extends BarFormatter {
		public MyBarFormatter(int fillColor, int borderColor) {
			super(fillColor, borderColor);
		}

		@Override
		public Class<? extends SeriesRenderer> getRendererClass() {
			return MyBarRenderer.class;
		}

		@Override
		public SeriesRenderer getRendererInstance(XYPlot plot) {
			return new MyBarRenderer(plot);
		}
	}

	class MyBarRenderer extends BarRenderer<MyBarFormatter> {

		public MyBarRenderer(XYPlot plot) {
			super(plot);
		}

		/**
		 * Implementing this method to allow us to inject our special selection
		 * formatter.
		 * 
		 * @param index
		 *            index of the point being rendered.
		 * @param series
		 *            XYSeries to which the point being rendered belongs.
		 * @return
		 */
		// @Override
		public MyBarFormatter getFormatter(int index, XYSeries series) {
			if (selection != null && selection.second == series
					&& selection.first == index) {
				return selectionFormatter;
			} else {
				return getFormatter(series);
			}
		}
	}

	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByKeys(
			Map<K, V> map) {
		List<K> keys = new LinkedList<K>(map.keySet());
		Collections.sort(keys);

		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (K key : keys) {
			sortedMap.put(key, map.get(key));
		}

		return sortedMap;
	}

}