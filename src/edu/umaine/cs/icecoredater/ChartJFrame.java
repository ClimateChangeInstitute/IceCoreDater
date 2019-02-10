package edu.umaine.cs.icecoredater;

/*
 Copyright 2005 The Climate Change Institute At The University Of Maine.
 JFreeChart: http://www.jfree.org/jfreechart/ was created by David Gilbert
 and is being distributed under the terms of the GNU
 Lesser General Public License: http://www.object-refinery.com/lgpl.html
 This file is part of IceCoreDating software.
 IceCoreDating is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 IceCoreDating is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with IceCoreDating; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

/**
 * 
 * This class is responsible for creating the main chart screen, plotting the
 * data and allowing the user to dynamically manipulate the chart and date ice
 * cores
 * 
 * @author Bashar Abdul
 * @author Modified by Mark Royer
 * @version 1.1
 */
/**
 * @author mroyer
 * 
 */
public class ChartJFrame extends JFrame {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = -1661569671686108421L;

	/**
	 * A 2-D array that holds the values of the elements. First subscript is for the
	 * element itself and the second subscript is for the values of that element
	 */
	private Double[][] elementValuesDoubles = null;
	/**
	 * The chart object
	 */
	private static JFreeChart chart = null;
	/**
	 * The chart panel object
	 */
	private static ChartPanel chartPanel = null;
	/**
	 * A linked list that holds depth range values
	 */
	private List<Double> depthRange = new ArrayList<Double>();
	/**
	 * A linked list that holds years values
	 */
	private List<Integer> year = new ArrayList<Integer>();
	/**
	 * 2 variables used for panning
	 */
	private double[] primYMinMax = new double[2];
	private double[] secondYMinMax = new double[2];
	/**
	 * An array that holds x values as doubles
	 */
	private double[] xValuesDoublesPrimitives;
	/**
	 * An array that holds element values as doubles
	 */
	private double[][] elementValuesPrimitiveDoubles;
	/**
	 * Min Y and max Y
	 */
	private double minY, maxY;
	/**
	 * Subplot object
	 */
	private XYPlot subplot;
	/**
	 * Dataset object
	 */
	private XYSeriesCollection dataset;
	/**
	 * Series object
	 */
	private XYSeries series;
	/**
	 * Variable used for indexing
	 */
	private int index;
	/**
	 * Number of elements selected
	 */
	private int selectedElementsNumber;
	/**
	 * Combined domain plot object
	 */
	private CombinedDomainXYPlot plot, combinedPlot;
	/**
	 * List of subplots
	 */
	private java.util.List<XYPlot> subPlots;
	/**
	 * A variable that keeps tracks of number of clicks on the plot
	 */
	private int numberOfClicks = 0;
	private JComboBox<String> elementAxis;
	/**
	 * Data file read
	 */
	private File correctedFile = null;
	/**
	 * Report File
	 */
	private File reportFile = null;
	/**
	 * Top year of the core
	 */
	private int topYear = 0;
	/**
	 * A vector that represents selected elements
	 */
	private List<Checkbox> selectedElements = null;
	/**
	 * A string that represents the selected plotting method
	 */
	private String selectedMethod = "";
	/**
	 * A linked list that holds values of x Axis
	 */
	private List<Double> xValues = new ArrayList<Double>();
	/**
	 * Buffered Reader for reading from files
	 */
	private BufferedReader br = null;
	/**
	 * An array of lists. Each element has its own list that contains its values
	 */
	private List<Double>[] elementValues;
	/**
	 * An array of booleans that hold values of whether an element was selected for
	 * plotting or not
	 */
	private boolean[] selected;
	/**
	 * A linked list that contains names of elements
	 */
	private List<String> elementNames;
	/**
	 * Variable used to panning
	 */
	private Point2D panStartPoint;
	/**
	 * A boolean array that hold values of whether an element is in log mode or not
	 */
	private boolean[] logs;
	/**
	 * Coordinates of mouse clicks
	 */
	double xx, yy;
	/**
	 * index of the subplot the user clicked on
	 */
	int selectedPlot;
	/**
	 * Holds the user text comments
	 */
	private ArrayList<XYTextAnnotation> comments;
	/**
	 * Holds the plot index that has the comment
	 */
	private ArrayList<Integer> commentedPlot;
	/**
	 * Legends Collection
	 */
	private LegendItemCollection items;
	/**
	 * GUI Controls
	 */
	private JButton generateReport, importSession, mainMenu, setRangeAxis, viewReport;

	private JToggleButton deleteButton, recordButton, panButton, textButton, xzoom;
	private JMenuBar menuBar;
	private JCheckBox log;
	private JCheckBoxMenuItem tubesJCheckBox;
	private JFormattedTextField minYField;
	private JFormattedTextField maxYField;

	private ButtonGroup buttonGroup;

	/**
	 * The minimum and maximum domain value plus padding that was originally
	 * calculated by the plot.
	 */
	private double minXvalue, maxXvalue;

	/**
	 * Contains all of the interval markers for tubes.
	 */
	List<IntervalMarker> intervalList;

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * Default empty constructor. Simply calls run method
	 * 
	 * @throws IOException
	 * @throws DataFileException
	 * @throws FileNotFoundException
	 */
	public ChartJFrame() throws FileNotFoundException, DataFileException, IOException {
		buttonGroup = new ButtonGroup();
		init();
	}

	/**
	 * Constructor that is called when the user selects to start a new dating
	 * session
	 * 
	 * @param correctedFile    Ice sheet file to be dated (after being corrected)
	 * @param topYear          top year of the core
	 * @param numOfElements    total number of elements in the file
	 * @param selectedElements a vector that contains ALL the elements in the file
	 * @param selectedMethod   a string that represents the selected plotting method
	 * @throws IOException
	 * @throws DataFileException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ChartJFrame(File correctedFile, int topYear, int numOfElements, List<Checkbox> selectedElements,
			String selectedMethod) throws FileNotFoundException, DataFileException, IOException {
		buttonGroup = new ButtonGroup();
		this.correctedFile = correctedFile;

		this.topYear = topYear;

		// Can't create generic array List<Double>
		elementValues = new List[numOfElements];

		this.selectedElements = selectedElements;

		selected = new boolean[numOfElements];

		elementNames = new ArrayList<String>();

		// fill up elementNames string array with names of the selected elements
		for (int i = 0; i < selectedElements.size(); i++) {
			elementNames.add(selectedElements.get(i).getLabel());
		}

		this.selectedMethod = selectedMethod;

		init();
	}

	/**
	 * Constructor that is called when the user selects to continue an existing
	 * dating session
	 * 
	 * @param tempFile   Ice sheet file to be dated (after being corrected)
	 * @param reportFile report file
	 * @throws IOException
	 * @throws DataFileException
	 * @throws FileNotFoundException
	 */
	public ChartJFrame(File tempFile, File reportFile) throws FileNotFoundException, DataFileException, IOException {

		this.correctedFile = tempFile;
		this.reportFile = reportFile;
		init();
	}

	/**
	 * Main method that does the plotting. The method is called from the run method
	 * 
	 * @throws DataFileException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void plot() throws DataFileException, FileNotFoundException, IOException {
		/*
		 * check first to see if we have a report file (user selected to continue a
		 * session)
		 */
		if (reportFile != null) {
			readReportFile(reportFile);
		}

		int rowsNumber = loadCorrectedFile(correctedFile);

		/*
		 * The values in XValues list are stored as Double objects. However JFreeChart
		 * library require the values to be stored in an array as double primitives. The
		 * code below does that job
		 */
		if (xValues.size() > 0) {
			Double xValuesDoubles[] = xValues.toArray(new Double[0]);
			xValuesDoublesPrimitives = new double[xValuesDoubles.length];
			for (int i = 0; i < xValuesDoubles.length; i++) {
				xValuesDoublesPrimitives[i] = xValuesDoubles[i].doubleValue();
			}
		}
		selectedElementsNumber = 0;
		// this code calculates how many elements were actually selected for
		// plotting
		for (int i = 0; i < elementValues.length; i++) {
			if (elementValues[i] != null) {
				selectedElementsNumber++;
			}
		}
		// Again because JFreeChart requires values to be stored in an array as
		// primitives,
		// we have to convert element values stored as Double objects in a
		// linked list
		// to an array that contains double primitives
		elementValuesDoubles = new Double[selectedElementsNumber][rowsNumber];
		selectedElementsNumber = 0;
		for (int i = 0; i < elementValues.length; i++) {
			if (elementValues[i] != null) {
				elementValuesDoubles[selectedElementsNumber] = (Double[]) elementValues[i].toArray(new Double[0]);
				selectedElementsNumber++;
			}
		}
		elementValuesPrimitiveDoubles = new double[selectedElementsNumber][elementValuesDoubles[0].length];
		for (int i = 0; i < selectedElementsNumber; i++) {
			for (int j = 0; j < elementValuesDoubles[0].length; j++) {
				elementValuesPrimitiveDoubles[i][j] = elementValuesDoubles[i][j].doubleValue();
			}
		}
		/*
		 * Now we have our data ready! XValues are stored inside
		 * xValuesDoublesPrimitives[] array while Y values for elements are stored in a
		 * 2-D array elementValuesPrimitiveDoubles[][] where the first subscript is the
		 * element number and the 2nd subscript is the values associated with that
		 * element.
		 */

		// We are ready now to call our method that creates the chart
		chart = createChart();

		// Creates panel and attach the chart on it
		chartPanel = new ChartPanel(chart, true, true, true, true, true);
		// customize the panel

		chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
		chartPanel.setAutoscrolls(true);

		final JCheckBoxMenuItem pointShapes = new JCheckBoxMenuItem("Points Shapes");
		pointShapes.setState(false);
		pointShapes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (pointShapes.getState()) {
					for (int i = 0; i < subPlots.size(); i++) {
						setAllShapesVisibleOnPlot((XYLineAndShapeRenderer) subPlots.get(i).getRenderer(),
								subPlots.get(i), true);
					}
				} else {
					for (int i = 0; i < subPlots.size(); i++) {
						setAllShapesVisibleOnPlot((XYLineAndShapeRenderer) subPlots.get(i).getRenderer(),
								subPlots.get(i), false);
					}
				}
			}

			/**
			 * Sets all of the shapes visible for each renderer, of each dataset in the
			 * given plot.
			 * 
			 * @param renderer   The renderer for the plot
			 * @param subPlot    The subplot to have shapes set visible to
			 * @param showShapes Renderer the shapes if true
			 */
			private void setAllShapesVisibleOnPlot(XYLineAndShapeRenderer renderer, XYPlot subPlot,
					boolean showShapes) {
				int dataSetCount = subPlot.getDatasetCount();
				for (int n = 0; n < dataSetCount; n++) {

					int seriesCount = subPlot.getDataset(n).getSeriesCount();

					for (int k = 0; k < seriesCount; k++) {
						renderer.setSeriesShapesVisible(k, showShapes);
					}
				}
			}

		});
		chartPanel.getPopupMenu().addSeparator();
		chartPanel.getPopupMenu().add(pointShapes);

		tubesJCheckBox = new JCheckBoxMenuItem(new AbstractAction("Show Tubes") {

			/**
			 * For serializing
			 */
			private static final long serialVersionUID = -361339025532271614L;

			public void actionPerformed(ActionEvent arg0) {
				((IceCombinedDomainXYPlot) chart.getPlot()).setDrawIntervalMarkers(tubesJCheckBox.isSelected());
			}

		});

		chartPanel.getPopupMenu().add(tubesJCheckBox);

		// add a menu item to the right click context menu that allows you
		// save the image as a JPEG
		JMenuItem saveAsJPEG = new SaveJPEGMenuItem(chartPanel);

		chartPanel.getPopupMenu().addSeparator();
		chartPanel.getPopupMenu().add(saveAsJPEG);
		// add a menu Item to the right click context menu that allows you
		// to save the chart as an HTML

		JMenuItem saveAsHTML = new SaveHTMLMenuItem(chartPanel);

		chartPanel.getPopupMenu().addSeparator();
		chartPanel.getPopupMenu().add(saveAsHTML);
		// Create GUI Items

		// Main panel
		final JPanel main = new JPanel(new BorderLayout());
		main.setBorder(BorderFactory.createEtchedBorder());

		// Check JPanel
		final JPanel manipulatePanel = new JPanel();
		// add element Label and ComboBox
		JLabel elementAxisLabel = new JLabel("Element :");
		Vector<String> plottedElements = new Vector<String>();
		for (int i = 0; i < selected.length; i++) {
			if (selected[i]) {
				plottedElements.add(elementNames.get(i));
			}
		}
		elementAxis = new JComboBox<>(plottedElements);
		elementAxis.setFont(new java.awt.Font("Arial", 1, 11));

		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		format.setGroupingUsed(false);
		format.setDecimalSeparatorAlwaysShown(true);
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);

		// add minimum Y and Max Y text Fields
		JLabel minYLabel = new JLabel("Min Y");
		minYField = new JFormattedTextField(format);
		minYField.setColumns(6);
		JLabel maxYLabel = new JLabel("Max Y");
		maxYField = new JFormattedTextField(format);
		maxYField.setColumns(6);
		plot = (CombinedDomainXYPlot) chartPanel.getChart().getXYPlot();
		subPlots = (List<XYPlot>) plot.getSubplots();
		// display values in min Y and max Y field
		minYField.setValue(subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().getLowerBound());
		maxYField.setValue(subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().getUpperBound());

		log = new JCheckBox(new AbstractAction("Log values") {
			/**
			 * For serializing.
			 */
			private static final long serialVersionUID = -5987402549880408012L;

			public void actionPerformed(ActionEvent e) {
				setRangeAxis();
			}

		});

		logs = new boolean[selectedElementsNumber];

		/*
		 * add item listener to Element ComboBox (that changes values in Min y and max Y
		 * text fields).
		 */
		elementAxis.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				minYField.setValue(subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().getLowerBound());
				maxYField.setValue(subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().getUpperBound());
				log.setSelected(logs[elementAxis.getSelectedIndex()]);
			}
		});
		index = elementAxis.getSelectedIndex();
		// add "set Y Axis limits" button
		setRangeAxis = new SetRangeAxisJButton(this);

		// Adds what controls you have so far to a new JPanel called yAxis
		// JPanel
		final JPanel yAxisPanel = new JPanel();

		yAxisPanel.add(log);
		yAxisPanel.add(elementAxisLabel);
		yAxisPanel.add(elementAxis);
		yAxisPanel.add(minYLabel);
		yAxisPanel.add(minYField);
		yAxisPanel.add(maxYLabel);
		yAxisPanel.add(maxYField);
		yAxisPanel.add(setRangeAxis);
		// add Y Panel to the main Panel
		main.add(yAxisPanel, BorderLayout.SOUTH);

		// creates a check box for zooming
		xzoom = new JToggleButton("Zoom", new ImageIcon(getClass().getResource(IceCoreDating.imageDir + "/zoom.gif")));
		// if user checked the zoom check box, allow zooming on the chart and
		// create cross-hairs
		xzoom.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setMouseZoom(xzoom.isSelected());
				chartPanel.repaint();
			}
		});

		textButton = new TextJButton(this, chartPanel);

		// create the button that deletes lines
		deleteButton = new DeleteJButton(this, chartPanel);
		recordButton = new RecordJButton(this, chartPanel);

		// recording JPanel
		final JPanel recordPanel = new JPanel();
		recordPanel.add(recordButton);
		// create pan button
		panButton = new JToggleButton("Pan",
				new ImageIcon(getClass().getResource(IceCoreDating.imageDir + "/pan.gif")));
		panButton.setText("Pan");
		panButton.setToolTipText("Press to pan chart");
		panButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (panButton.isSelected()) {
					setPanMode(true);
					chartPanel.setMouseZoomable(false);
					chartPanel.setHorizontalAxisTrace(false);
					chartPanel.setVerticalAxisTrace(false);
					disableComponents(panButton, true);
				} else {
					setPanMode(false);
					chartPanel.setMouseZoomable(false);
					chartPanel.setHorizontalAxisTrace(false);
					chartPanel.setVerticalAxisTrace(false);
					disableComponents(panButton, false);
				}
			}
		});
		manipulatePanel.add(textButton);
		manipulatePanel.add(deleteButton);
		manipulatePanel.add(panButton);
		manipulatePanel.add(xzoom);

		// add Mouse listeners to the chart panel
		chartPanel.addMouseListener(new MouseAdapter() {
			// when a mouse is pressed
			public void mousePressed(final MouseEvent event) {
				try {
					if (panButton.isSelected()) {
						final Rectangle2D dataArea = chartPanel.getScreenDataArea();
						final Point2D point = event.getPoint();
						if (dataArea.contains(point)) {
							setPanMode(true);
							panStartPoint = point;
						}
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An Error had occurred:\n" + e.getMessage(), "Exception",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			// when a mouse is released
			public void mouseReleased(final MouseEvent event) {
				try {
					panStartPoint = null; // stop panning
					if (panButton.isSelected()) {
						// setPanMode(false);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An Error had occurred:\n" + e.getMessage(), "Exception",
							JOptionPane.ERROR_MESSAGE);
				}
				// fixes the bug where the chart panel is not refreshed
				// sometimes after zooming
				// I refreshed the panel after each mouse release. Not the best
				// solution but fixes the problem
				drawLines(-1, false);
			}
		});

		// draw any year lines (in case the session already had a report)
		drawLines(-1, false);

		// add Chart Mouse listeners to the chart panel
		chartPanel.addChartMouseListener(new ChartMouseListener() {
			// user clicked on chart
			public void chartMouseClicked(final ChartMouseEvent event) {
				if (textButton.isSelected()) {
					String text = JOptionPane.showInputDialog(null, "Enter text");
					if (comments == null) {
						comments = new ArrayList<XYTextAnnotation>();
					}
					if (commentedPlot == null) {
						commentedPlot = new ArrayList<Integer>();
					}
					if (text != null) {
						XYTextAnnotation annotation = new XYTextAnnotation(text, getXCoordinates(event),
								getYCoordinates(event));
						comments.add(annotation);
						commentedPlot.add(new Integer(selectedPlot));
						subPlots.get(selectedPlot).addAnnotation(annotation);
					}
				}

				addPoints(event);
			}

			// Adds points to the chart when the chart is clicked on and calls
			// draw lines method that draws lines and text labels
			public void addPoints(final ChartMouseEvent event) {
				xx = getXCoordinates(event);
				yy = getYCoordinates(event);
				// user is deleting lines
				if (deleteButton.isSelected() && !recordButton.isSelected()) {
					drawLines(xx, true);
					return;
				}
				// user is adding lines
				if (recordButton.isSelected() && !deleteButton.isSelected()) {
					ArrayList<Double> xxList = new ArrayList<Double>();
					xxList.add(new Double(xx));
					/**
					 * Added: 07/17/2005: Second condition in the if statement to solve the
					 * unexpected behavior if user had imported a report
					 */
					if (numberOfClicks == 0 && year.size() == 0) {
						depthRange.add(new Double(xx));
						year.add(new Integer(topYear));
						numberOfClicks++;
						importSession.getAction().setEnabled(false);
					}
					// adds lines to the chart. Handles updating the years if a
					// user adds
					// a line to the right or to the left of a year.
					else {
						numberOfClicks++;
						boolean inserted = false;
						int size = depthRange.size();
						for (int i = 0; i < size; i++) {
							if (xx == depthRange.get(i).doubleValue()) {
								inserted = true;
								break;
							} else if (xx < depthRange.get(i).doubleValue()) {
								depthRange.add(i, new Double(xx));
								year.add(i, new Integer((year.get(i).intValue())));
								for (int j = i + 1; j < depthRange.size(); j++) {
									if (topYear >= 0) {
										year.set(j, new Integer((year.get(j).intValue()) - 1));
									} else {
										year.set(j, new Integer((year.get(j).intValue()) + 1));
									}
								}
								inserted = true;
								break;
							}
						}
						if (!inserted) {
							depthRange.add(new Double(xx));
							int modifiedYear = 0;
							if (topYear >= 0) {
								modifiedYear = (year.get(year.size() - 1).intValue()) - 1;
							} else {
								modifiedYear = (year.get(year.size() - 1).intValue()) + 1;
							}
							year.add(new Integer(modifiedYear));
						}
					}
					// When values are written to year and depth range data
					// structures,
					// calls draw lines method that will read the values from
					// there and display them
					drawLines(xx, false);
				}
			}

			public void chartMouseMoved(final ChartMouseEvent event) {
				// ignore
			}
		});

		// add Mouse motion listeners to the chart panel
		chartPanel.addMouseMotionListener(new MouseMotionAdapter() {

			// user drags the mouse (pan)
			public void mouseDragged(final MouseEvent event) {
				try {
					if (panStartPoint != null) {
						final Rectangle2D scaledDataArea = chartPanel.getScreenDataArea();
						panStartPoint = ShapeUtilities.getPointInRectangle(panStartPoint.getX(), panStartPoint.getY(),
								scaledDataArea);
						Point2D panEndPoint = ShapeUtilities.getPointInRectangle(event.getX(), event.getY(),
								scaledDataArea);

						// horizontal pan
						final XYPlot plot = chart.getXYPlot();
						// final Plot plot = chartPanel.getChart().getPlot();
						if (plot instanceof XYPlot) {
							final XYPlot hvp = (XYPlot) plot;
							final ValueAxis xAxis = hvp.getDomainAxis();

							if (xAxis != null) {
								final double translatedStartPoint = xAxis.java2DToValue((float) panStartPoint.getX(),
										scaledDataArea, hvp.getDomainAxisEdge());
								final double translatedEndPoint = xAxis.java2DToValue((float) panEndPoint.getX(),
										scaledDataArea, hvp.getDomainAxisEdge());
								final double dX = translatedStartPoint - translatedEndPoint;
								final double oldMin = xAxis.getLowerBound();
								final double newMin = oldMin + dX;

								final double oldMax = xAxis.getUpperBound();
								final double newMax = oldMax + dX;

								// do not pan out of range
								if (newMin >= minXvalue && newMax <= maxXvalue) {
									xAxis.setLowerBound(newMin);
									xAxis.setUpperBound(newMax);
								}
							}
						}
						// vertical pan (1. Y-Axis)
						if (plot instanceof XYPlot) {
							final XYPlot vvp = (XYPlot) plot;
							final ValueAxis yAxis = vvp.getRangeAxis();

							if (yAxis != null) {
								final double translatedStartPoint = yAxis.java2DToValue((float) panStartPoint.getY(),
										scaledDataArea, vvp.getRangeAxisEdge());
								final double translatedEndPoint = yAxis.java2DToValue((float) panEndPoint.getY(),
										scaledDataArea, vvp.getRangeAxisEdge());
								final double dY = translatedStartPoint - translatedEndPoint;

								final double oldMin = yAxis.getLowerBound();
								final double newMin = oldMin + dY;

								final double oldMax = yAxis.getUpperBound();
								final double newMax = oldMax + dY;

								// do not pan out of range
								if (newMin >= primYMinMax[0] && newMax <= primYMinMax[1]) {
									yAxis.setLowerBound(newMin);
									yAxis.setUpperBound(newMax);
								}
							}
						}
						// vertical pan (2. Y-Axis)

						if (plot instanceof XYPlot) {
							final XYPlot xyPlot = (XYPlot) plot;
							final ValueAxis yAxis = xyPlot.getRangeAxis(1);

							if (yAxis != null) {
								final double translatedStartPoint = yAxis.java2DToValue((float) panStartPoint.getY(),
										scaledDataArea, xyPlot.getRangeAxisEdge(1));
								final double translatedEndPoint = yAxis.java2DToValue((float) panEndPoint.getY(),
										scaledDataArea, xyPlot.getRangeAxisEdge(1));
								final double dY = translatedStartPoint - translatedEndPoint;

								final double oldMin = yAxis.getLowerBound();
								final double newMin = oldMin + dY;

								final double oldMax = yAxis.getUpperBound();
								final double newMax = oldMax + dY;

								if (newMin >= secondYMinMax[0] && newMax <= secondYMinMax[1]) {
									yAxis.setLowerBound(newMin);
									yAxis.setUpperBound(newMax);
								}
							}
						}

						// for the next time
						panStartPoint = panEndPoint;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An Error had occurred:\n" + e.getMessage(), "Exception",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// view report button
		viewReport = new JButton("View Report");
		viewReport.setToolTipText("Click to view your report");
		viewReport.setAction(new ViewReportAction("View Report",
				new ImageIcon(getClass().getResource(IceCoreDating.imageDir + "/view.gif")), "View Report", null));
		viewReport.getAction().setEnabled(false);
		if (reportFile != null) {
			viewReport.setEnabled(true);
		}
		// import session button
		importSession = new JButton("Import an Existing Report");
		importSession.setToolTipText("Imports an existing report");
		importSession.setAction(new ImportSessionAction("Import an Existing Report",
				new ImageIcon(getClass().getResource(IceCoreDating.imageDir + "/import.gif")),
				"Import an Existing Report", null));
		// generate report button
		generateReport = new JButton("Generate Report");
		generateReport.setToolTipText("Click to generate your report");
		generateReport.setAction(new GenerateReportAction("Generate Report",
				new ImageIcon(getClass().getResource(IceCoreDating.imageDir + "/report.gif")), "Generate Report",
				null));

		// add buttons to the record panel
		recordPanel.add(generateReport);
		recordPanel.add(viewReport);
		recordPanel.add(importSession);

		// Main Menu button
		mainMenu = new JButton("Main Menu",
				new ImageIcon(getClass().getResource(IceCoreDating.imageDir + "/undo.gif")));
		mainMenu.setToolTipText("Return to the main menu");
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// user tried to exit without saving
				if (reportFile == null) {
					int answer = JOptionPane.showConfirmDialog(null,
							"Report file not generated yet. You sure you want to exit? ", "Exit", 2);
					if (answer == 0 && correctedFile != null) {
						dispose();
						new MainMenu(correctedFile);
						return;
					} else {
						// do nothing
					}
				} else {
					// user exits while file is saved
					if (correctedFile != null) {
						dispose();
						new MainMenu(correctedFile);
						return;
					}
				}
			}
		});

		// create top menu
		menuBar = new JMenuBar();
		JMenu chartMenu = new JMenu("Chart");
		JMenu FileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		// chartMenu.add(new JMenuItem(recordButton.getAction()));
		chartMenu.add(new JMenuItem(generateReport.getAction()));
		chartMenu.add(new JMenuItem(viewReport.getAction()));
		chartMenu.add(new JMenuItem(importSession.getAction()));

		FileMenu.add(new JMenuItem(new AbstractAction("Exit",
				new ImageIcon(getClass().getResource(IceCoreDating.imageDir + "/exit.gif"))) {
			/**
			 * For serializing.
			 */
			private static final long serialVersionUID = 6548243139871944750L;

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}));
		
		helpMenu.add(new AboutAction());
		
		menuBar.add(FileMenu);
		menuBar.add(chartMenu);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);
		minY = subPlots.get(index).getRangeAxis().getLowerBound();
		maxY = subPlots.get(index).getRangeAxis().getUpperBound();
		manipulatePanel.add(mainMenu);
		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.add(yAxisPanel, BorderLayout.NORTH);
		buttonsPanel.add(manipulatePanel, BorderLayout.SOUTH);
		buttonsPanel.add(recordPanel, BorderLayout.CENTER);
		main.add(buttonsPanel, BorderLayout.SOUTH);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(chartPanel, BorderLayout.CENTER);
		this.getContentPane().add(main, BorderLayout.SOUTH);
		this.setSize(700, 600);
		// show and center main frame
		RefineryUtilities.centerFrameOnScreen(this);
		this.setTitle(correctedFile.getName().substring(0, correctedFile.getName().indexOf("Corrected")));

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});

		for (AbstractButton jb : getToggleButtons()) {
			buttonGroup.add(jb);
		}

		for (IntervalMarker marker : intervalList) {
			((CombinedDomainXYPlot) chart.getPlot()).addDomainMarker(marker, Layer.BACKGROUND);
		}

		// Enable mouse zooming by default.
		setMouseZoom(true);

		NumberAxis domain = ((NumberAxis) ((CombinedDomainXYPlot) chart.getPlot()).getDomainAxis());

		domain.setAutoRangeIncludesZero(false);
		domain.setAutoRangeStickyZero(false);

		minXvalue = domain.getLowerBound();
		maxXvalue = domain.getUpperBound();

		this.validate();
		this.pack();
		this.setVisible(true);

	} // plot

	/**
	 * Read a corrected file and store the information in the corresponding
	 * structures.
	 * 
	 * @return The number of elements read from the file.
	 * @throws DataFileException Thrown if there is an error parsing the file.
	 */
	private int loadCorrectedFile(File correctedFile) throws DataFileException {

		/*
		 * SelectedElements is a List of check boxes. Those check boxes came from the
		 * previous main menu where the user selected what elements to plot. Notice that
		 * selectedElements contain ALL the elements in the file and if the user had
		 * selected any to plot then it's state will be set to true
		 */
		Checkbox[] elements = new Checkbox[selectedElements.size()];
		for (int i = 0; i < selectedElements.size(); i++) {
			// Array of Check boxes
			elements[i] = selectedElements.get(i);
		}

		int rowsNumber = 0;

		String line = "";
		String lastLine = "", currentTubeName = "", previousTubeName = "";
		double tubeStart = 0, tubeBottom = 0;
		boolean newTube = false;
		intervalList = new ArrayList<IntervalMarker>();
		boolean isC1 = true;

		try {

			/*
			 * Start reading from the corrected file. We store the element values in a list
			 * associated with each element.
			 */
			br = new BufferedReader(new FileReader(correctedFile));

			// skip the header line
			br.readLine();

			// Used as a sanity check
			// Data should be in ascending order
			double previousTop = -1, previousBottom = Double.MAX_VALUE;

			// blue and green colors.
			Color c1 = new Color(222, 222, 255, 148), c2 = new Color(222, 255, 222, 128);

			while ((line = br.readLine()) != null) {
				rowsNumber++;

				previousTubeName = currentTubeName;
				currentTubeName = line.substring(0, line.indexOf(","));
				if (rowsNumber == 1) {
					previousTubeName = currentTubeName;

					String t = line.substring(line.indexOf(",") + 1, line.length());

					tubeStart = Double.valueOf(t.substring(0, t.indexOf(",")));
				}

				// read element values first
				for (int i = 0; i < elements.length; i++) {
					/*
					 * We only want to read the values of the elements that have been selected for
					 * plotting, so we check their state first.
					 */
					if (elements[i].getState()) {
						if (elementValues[i] == null) {
							elementValues[i] = new ArrayList<Double>();
						}
						selected[i] = true;

						// Skip the tube column
						String temp = line.substring(line.indexOf(",") + 1, line.length());

						// Skip top and bottom and everything before
						for (int j = 0; j < i + 2; j++) {
							temp = temp.substring(temp.indexOf(",") + 1, temp.length());
						}

						// Extract the actual value
						if (i == elements.length - 1) {
							temp = temp.substring(0, temp.length());
						} else {
							temp = temp.substring(0, temp.indexOf(","));
						}

						/*
						 * Now we have reached the value we are interested in. Add it to the list
						 * associated with that element.
						 */
						elementValues[i].add(Double.valueOf(temp));
					}
				} // for

				// read x values now, depending on plotting method selected
				if (selectedMethod.equals("Top point")) {
					/*
					 * To make it easier how reading is done, open the ice sheet file and notice
					 * that we are interested in reading the 2nd column
					 */
					String temp = line.substring(line.indexOf(",") + 1, line.length());
					temp = temp.substring(0, temp.indexOf(","));
					// add the value we read to xValues linked List
					xValues.add(Double.valueOf(temp));
				} else if (selectedMethod.equals("Bottom point")) {
					String temp = line.substring(line.indexOf(",") + 1, line.length());
					temp = temp.substring(temp.indexOf(",") + 1, temp.length());
					temp = temp.substring(0, temp.indexOf(","));
					xValues.add(Double.valueOf(temp));
				} else if (selectedMethod.equals("Midpoint between top and bottom")) {
					String temp = line.substring(line.indexOf(",") + 1, line.length());
					double topDepth = Double.parseDouble(temp.substring(0, temp.indexOf(",")));
					temp = temp.substring(temp.indexOf(",") + 1, temp.length());
					double bottomDepth = Double.parseDouble(temp.substring(0, temp.indexOf(",")));
					double middleValue = (topDepth + bottomDepth) / 2;
					xValues.add(new Double(middleValue));
				}

				newTube = !currentTubeName.equals(previousTubeName);
				if (newTube) {

					String t = line.substring(line.indexOf(",") + 1, line.length());

					// This is the top of the next new tube
					tubeBottom = Double.valueOf(t.substring(0, t.indexOf(",")));

					if (isC1) {
						intervalList.add(new IntervalMarker(tubeStart, tubeBottom, c1));
						isC1 = false;
					} else {
						intervalList.add(new IntervalMarker(tubeStart, tubeBottom, c2));
						isC1 = true;
					}

					// New tube start
					tubeStart = tubeBottom;
				}

				lastLine = line;

				String ts = line.substring(line.indexOf(",") + 1, line.length());

				// top depth
				double top = Double.valueOf(ts.substring(0, ts.indexOf(",")));
				ts = ts.substring(ts.indexOf(",") + 1, ts.length());

				// bottom depth
				double bottom = Double.valueOf(ts.substring(0, ts.indexOf(",")));

				// Do some sanity checks with the data
				if (top > bottom) {
					throw new DataFileException(
							"The top value was greater than the " + "bottom value on the same row. "
									+ "Are the first three columns " + "tube, top depth, bottom depth?",
							line, rowsNumber);
				}
				if (top < previousTop) {
					throw new DataFileException("The top value was less than the " + "top value on the previous row. "
							+ "Make sure the rows are ordered " + "ascendingly by depth.", line, rowsNumber);
				}
				if (bottom < previousBottom && rowsNumber != 1) {
					throw new DataFileException(
							"The bottom value was less than the " + "bottom value on the previous row. "
									+ "Make sure the rows are ordered " + "ascendingly by depth.",
							line, rowsNumber);
				}

				previousTop = top;
				previousBottom = bottom;

			} // while
			br.close();

			String t = lastLine.substring(lastLine.indexOf(",") + 1, lastLine.length());

			t = t.substring(t.indexOf(",") + 1, t.length());

			tubeBottom = Double.valueOf(t.substring(0, t.indexOf(",")));

			if (isC1) {
				intervalList.add(new IntervalMarker(tubeStart, tubeBottom, c1));
			} else {
				intervalList.add(new IntervalMarker(tubeStart, tubeBottom, c2));
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new DataFileException("A value was found that was not a number.", line, rowsNumber);
		} catch (IOException ioe) {
			throw new DataFileException(
					"Unable to read the file. Is it currently being used by " + "another application?", line, 0);
		} catch (DataFileException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataFileException("An unknown error occurred while " + "trying to read the file.", line, 0);
		}
		return rowsNumber;
	}

	/**
	 * Reads in a report file and initializes the related fields in the ChartJFrame.
	 * 
	 * @param reportFile The report file to be read. (not null)
	 * @throws FileNotFoundException Thrown if the file is not found.
	 * @throws IOException           Thrown if there is a problem reading from the
	 *                               file.
	 */
	@SuppressWarnings("unchecked")
	private void readReportFile(File reportFile) throws FileNotFoundException, IOException {
		// read from the report file and fill up corresponding fields
		br = new BufferedReader(new FileReader(reportFile));
		// fill up top year
		topYear = Integer.parseInt(br.readLine());
		// fill up selected method
		selectedMethod = br.readLine();
		// fill up elements and whether they were selected for plotting or
		// not
		StringTokenizer tokenizer = new StringTokenizer(br.readLine(), ",");
		selectedElements = new ArrayList<Checkbox>();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			selectedElements.add(new Checkbox("element", (Boolean.valueOf(token.toLowerCase().trim())).booleanValue()));
		}
		elementValues = new ArrayList[selectedElements.size()];
		selected = new boolean[selectedElements.size()];
		elementNames = new ArrayList<String>();
		// fill up an array of strings with element names
		StringTokenizer tokenizer3 = new StringTokenizer(br.readLine(), ",");

		while (tokenizer3.hasMoreTokens()) {
			String token = tokenizer3.nextToken();
			elementNames.add(token.trim());
		}
		// fill up number of clicks user clicked on the chart
		numberOfClicks = Integer.parseInt(br.readLine().trim());
		br.readLine();
		String line = "";
		// fill up depth range and year linked lists
		while ((line = br.readLine()) != null) {
			StringTokenizer tokenizer2 = new StringTokenizer(line, ",");
			depthRange.add(new Double(Double.parseDouble(tokenizer2.nextToken().trim())));
			year.add(new Integer(Integer.parseInt(tokenizer2.nextToken().trim())));
		}
	}

	private AbstractButton[] getToggleButtons() {
		return new AbstractButton[] { deleteButton, xzoom, recordButton, panButton, generateReport, importSession,
				mainMenu, textButton };
	}

	private void setPanMode(final boolean val) {
		if (val) {
			chartPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			chartPanel.setCursor(Cursor.getDefaultCursor());
		}
	}

	/**
	 * 
	 * @param log  : True if user selects the log check box, false otherwise
	 * @param minY : Minimum Y value
	 * @param maxY : Maximum Y value
	 * @throws Exception This method is called every time you press "Set Y axis
	 *                   limits" Button
	 */
	@SuppressWarnings("unchecked")
	// Needed for dealing with raw library types
	private void updateChart(boolean log, double minY, double maxY) throws Exception {
		// return a reference to the big plot that has all the subplots
		CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) chartPanel.getChart().getXYPlot();
		// a list of all subplots in the big plot
		List<XYPlot> subPlots = (List<XYPlot>) combinedPlot.getSubplots();
		// return a reference of the plot in the selected element in the
		// elements ComboBox
		XYPlot plot = subPlots.get(elementAxis.getSelectedIndex());
		// ValueAxis normalAxis = plot.getRangeAxis();
		setAxis(log, minY, maxY, subPlots, plot);

		// draw the lines again
		// This appears to be necessary because the lines are not updated when the
		// y-axis on a subplot changes.
		drawLines(-1, false);

		// repaint the whole chart
		chartPanel.getChart().fireChartChanged();
	}

	private void setAxis(boolean log, double minY, double maxY, List<XYPlot> subPlots, XYPlot plot) {
		if (log) {
			// create a new log axis
			LogarithmicAxis logAxis = new LogarithmicAxis(elementAxis.getSelectedItem().toString());
			logs[elementAxis.getSelectedIndex()] = true;
			// customize logarithmic axis
			logAxis.setAllowNegativesFlag(true);
			logAxis.setAutoRange(true);
			// assign axis to plot
			plot.setRangeAxis(0, logAxis);
			// set lower and upper bounds of log axis to minimum and max y
			subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().setLowerBound(minY);
			subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().setUpperBound(maxY);

		} else {
			NumberAxis normalAxis = new NumberAxis(elementAxis.getSelectedItem().toString());
			logs[elementAxis.getSelectedIndex()] = false;

			normalAxis.setAutoRange(true);

			plot.setRangeAxis(0, normalAxis);
			subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().setLowerBound(minY);
			subPlots.get(elementAxis.getSelectedIndex()).getRangeAxis().setUpperBound(maxY);

		}
	}

	/**
	 * 
	 * @return Chart object
	 * @throws Exception This method creates the chart displayed on screen
	 */
	private JFreeChart createChart() {

		dataset = new org.jfree.data.xy.XYSeriesCollection();
		// Create the X Axis and customize it
		NumberAxis numberAxis = new NumberAxis("Depth Range");

		/*
		 * Our chart is an instance of CombinedDomainXYPlot, because it contains more
		 * than one chart (subplots) that share the same X axis but each has it's own Y
		 * axis
		 */
		combinedPlot = new IceCombinedDomainXYPlot(numberAxis);
		for (int i = 0; i < combinedPlot.getSubplots().size(); i++) {
			combinedPlot.remove((XYPlot) combinedPlot.getSubplots().get(i));
		}

		int elementNumber = 0;
		for (int i = 0; i < selected.length; i++) {
			if (selected[i]) {
				// create a new XYSeries for each element selected
				series = new XYSeries(elementNames.get(i));
				series.clear();

				for (int j = 0; j < xValuesDoublesPrimitives.length; j++) {
					// add to the series x values, and y values associated
					// with the element selected
					series.add(xValuesDoublesPrimitives[j], elementValuesPrimitiveDoubles[elementNumber][j]);
				}
				elementNumber++;
				// create a new XYSeriesCollection for the element series
				dataset = new XYSeriesCollection(series);
				// each element has its own range (Y) axis
				final NumberAxis rangeAxis = new NumberAxis(elementNames.get(i));

				subplot = new XYPlot(dataset, combinedPlot.getDomainAxis(), rangeAxis,
						new XYLineAndShapeRenderer(true, false));
				subplot.setBackgroundPaint(null);

				// customization
				subplot.setWeight(elementNumber);
				subplot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
				// now when you are done customizing the subPlot, add it to
				// your combined plot. Repeat
				// the process for each element (subPlot) you have
				combinedPlot.add(subplot, 1);
			}
		}
		// customize the plot
		combinedPlot.setFixedLegendItems(items);
		combinedPlot.setOrientation(PlotOrientation.VERTICAL);

		// create your chart from the one big combined plot
		final JFreeChart chart = new JFreeChart("IceCore Dating", JFreeChart.DEFAULT_TITLE_FONT, combinedPlot, true);

		// customize your chart
		chart.setBackgroundPaint(Color.white);

		// further customization of the plot
		final XYPlot plot = chart.getXYPlot();
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		if (chartPanel != null) {
			chartPanel.setChart(chart);
		}
		return chart;

	}

	/**
	 * 
	 * @param minY  minimum Y value
	 * @param maxY  maximum Y Value
	 * @param index index of the element to set Y limits for Sets Y axis values
	 */
	private void setRangeAxis(double minY, double maxY, int index) {
		subPlots.get(index).getRangeAxis().setLowerBound(minY);
		subPlots.get(index).getRangeAxis().setUpperBound(maxY);
	}

	/**
	 * Calculates x Screen coordinates of Mouse click
	 * 
	 * @param event ChartMouseEvent
	 * @return x coordinate
	 */
	private double getXCoordinates(final ChartMouseEvent event) {
		final int x = event.getTrigger().getX();
		final int y = event.getTrigger().getY();
		final Point2D p = chartPanel.translateScreenToJava2D(new Point(x, y));
		final Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getSubplotInfo(0).getDataArea();
		xx = plot.getDomainAxis().java2DToValue(p.getX(), dataArea, plot.getDomainAxisEdge());
		return xx;
	}

	/**
	 * Calculates y Screen coordinates of Mouse click
	 * 
	 * @param event ChartMouseEvent
	 * @return y coordinate
	 */

	private double getYCoordinates(final ChartMouseEvent event) {
		final int x = event.getTrigger().getX();
		final int y = event.getTrigger().getY();
		int i;
		final Point2D p = chartPanel.translateScreenToJava2D(new Point(x, y));
		for (i = 0; i < subPlots.size(); i++) {
			final Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getSubplotInfo(i)
					.getDataArea();
			yy = subPlots.get(i).getRangeAxis().java2DToValue(p.getY(), dataArea, subPlots.get(i).getRangeAxisEdge());
			// changed from 0 to 1 to fix the bug that always places text on top
			// plot it it's
			// using log scale(since it never drops below zero)
			if (yy > 1)
				break;
		}

		selectedPlot = i;
		return yy;
	}

	/**
	 * 
	 * @param x      X value (where to draw the line at)
	 * @param delete boolean: whether the user is deleting or adding a line. True is
	 *               for delete and false is for add * Draws the lines and text
	 *               labels on the chart and updates them
	 */
	private void drawLines(double x, boolean delete) {

		double tolerance = (plot.getDomainAxis().getUpperBound() - plot.getDomainAxis().getLowerBound()) * 0.01;
		// handles deleting a line
		if (delete) {
			for (int i = 0; i < depthRange.size(); i++) {
				if ((depthRange.get(i).doubleValue() < (x + tolerance))
						&& (depthRange.get(i).doubleValue() > (x - tolerance)))

				{
					depthRange.remove(i);
					year.remove(i);
					for (int k = i; k < depthRange.size(); k++) {
						if (topYear >= 0) {
							year.set(k, new Integer((year.get(k).intValue()) + 1));
						} else {
							year.set(k, new Integer((year.get(k).intValue()) - 1));
						}
					}
					drawLines(-1, false);
				}
			}
			// point where the user clicked to delete has no lines to delete
			return;

		}
		// clear annotations
		for (int i = 0; i < subPlots.size(); i++) {
			subPlots.get(i).clearAnnotations();
			// create text annotations again
			for (int j = 0; j < year.size(); j++) {
				XYTextAnnotation text = new XYTextAnnotation("\t\t\t\t           " + year.get(j),
						depthRange.get(j).doubleValue(), subPlots.get(0).getRangeAxis().getUpperBound());
				text.setRotationAngle(1.57);
				text.setPaint(Color.black);

				// add annotations
				subPlots.get(0).addAnnotation(text);
			}

			// redraw text comments
			if (comments != null && commentedPlot != null) {
				for (int k = 0; k < comments.size(); k++) {
					XYTextAnnotation comment = comments.get(k);
					(subPlots.get(commentedPlot.get(k).intValue())).addAnnotation(comment);
				}
			}
		}
		for (int i = 0; i < depthRange.size(); i++) {
			for (int j = 0; j < subPlots.size(); j++) {
				// redraw the lines
				XYLineAnnotation yearLine = new XYLineAnnotation(depthRange.get(i).doubleValue(),
						subPlots.get(j).getRangeAxis().getLowerBound(), depthRange.get(i).doubleValue(),
						subPlots.get(j).getRangeAxis().getUpperBound(), new BasicStroke(), Color.blue);
				subPlots.get(j).addAnnotation(yearLine);
			}
		}
	}

	/**
	 * @param ignore  Component to ignore
	 * @param disable Boolean (true to disable, false to enable)
	 */

	void disableComponents(JToggleButton ignore, boolean disable) {

		/**
		 * Added: 07/17/2005 Make sure Import Report button remains disabled if user had
		 * started adding lines to the chart
		 **/
		if (year.size() > 0) {
			importSession.setEnabled(false);
		}
	}

	public void init() throws FileNotFoundException, DataFileException, IOException {
		plot();
	}

	// Inner classes responsible for button actions

	// Import session Class
	private class ImportSessionAction extends AbstractAction {

		/**
		 * For serializing.
		 */
		private static final long serialVersionUID = -1025983045326375098L;

		public ImportSessionAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					BufferedReader br = new BufferedReader(new java.io.FileReader(chooser.getSelectedFile()));
					for (int i = 0; i < 6; i++) {
						br.readLine();
					}
					String line = "";
					while ((line = br.readLine()) != null) {
						StringTokenizer tokenizer2 = new StringTokenizer(line, ",");
						depthRange.add(new Double(Double.parseDouble(tokenizer2.nextToken().trim())));
						year.add(new Integer(Integer.parseInt(tokenizer2.nextToken().trim())));
					}
					br.close();
					// draw annotations
					drawLines(-1, false);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Report can't be imported", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	// View report Class
	private class ViewReportAction extends AbstractAction {

		/**
		 * For serializing.
		 */
		private static final long serialVersionUID = -6006797352681194187L;

		public ViewReportAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			new Editor(reportFile);
		}

	}

	// Generate Report Class
	private class GenerateReportAction extends AbstractAction {

		/**
		 * For serializing.
		 */
		private static final long serialVersionUID = 8613539189778151046L;

		File Path;

		public GenerateReportAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				Path = new File("Files" + File.separator + "Reports");
				if (!Path.exists()) {
					Path.mkdirs();
				}
				reportFile = new File(Path,
						correctedFile.getName().substring(0, correctedFile.getName().indexOf(".")) + "Report"
								+ correctedFile.getName().substring(correctedFile.getName().indexOf("."),
										correctedFile.getName().length()));
				if (!reportFile.exists()) {
					reportFile.createNewFile();
					createReport();
				} else {
					int answer = JOptionPane.showConfirmDialog(null,
							"Report file already exists, do you want to overwrite? ", "Exit",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (answer == 0) {
						createReport();
					} else if (answer == 1) {
						JFileChooser chooser = new JFileChooser();
						int returnVal = chooser.showSaveDialog(null);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							Path = chooser.getCurrentDirectory();
							reportFile = new File(Path, chooser.getSelectedFile().getName());
							createReport();
						}
					}
				}
			}

			catch (IOException ioe) {
				JOptionPane.showMessageDialog(null, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "An Error had occurred:\n" + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		private void createReport() {
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new java.io.FileWriter(reportFile)));
				out.write("" + topYear);
				out.println();
				out.write(selectedMethod);
				out.println();
				for (int i = 0; i < selected.length; i++) {
					if ((i == selected.length - 1)) {
						out.write("" + selected[i]);
					} else {
						out.write(selected[i] + ", ");
					}
				}
				out.println();
				for (int i = 0; i < selected.length; i++) {
					out.write(elementNames.get(i) + ", ");
				}
				out.println();
				out.write("" + numberOfClicks);
				out.println();
				out.write("Depth, Year");
				out.flush();
				if (depthRange != null) {
					out.println();
					for (int i = 0; i < depthRange.size(); i++) {
						double depthRangeDouble = depthRange.get(i).doubleValue();
						int yearInteger = year.get(i).intValue();
						out.write(depthRangeDouble + ", " + yearInteger);
						out.println();
						// out.flush();
					}
					out.flush();
					out.close();
				}
				JOptionPane.showMessageDialog(null, "Report generated successfully. Click on view report to display it",
						"Success", JOptionPane.PLAIN_MESSAGE);

				viewReport.getAction().setEnabled(true);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(ChartJFrame.this, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ChartJFrame.this, "An Error had occurred:\n" + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * Enable and disable the mouse zooming feature of the chart.
	 * 
	 * @param zoom Enables zooming if true
	 */
	private void setMouseZoom(boolean zoom) {
		xzoom.setSelected(zoom);
		chartPanel.setMouseZoomable(zoom, false);
		chartPanel.setHorizontalAxisTrace(zoom);
		chartPanel.setVerticalAxisTrace(zoom);
		disableComponents(xzoom, zoom);
	}

	/**
	 * Sets the range axis of the chart to the current values.
	 */
	public void setRangeAxis() {

		try {
			minY = Double.parseDouble(minYField.getText());
			maxY = Double.parseDouble(maxYField.getText());
			index = elementAxis.getSelectedIndex();
			// call method setRangeAxis when button is pressed
			setRangeAxis(minY, maxY, index);

			updateChart(log.isSelected(), minY, maxY);

		}
		// user entered an illegal value
		catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Please enter a valid number", "Invalid Number",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "An Error had occurred:\n" + ex.getMessage(), "Exception",
					JOptionPane.ERROR_MESSAGE);
		}

	}

} // Main class
