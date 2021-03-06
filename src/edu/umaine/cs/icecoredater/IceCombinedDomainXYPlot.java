/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -------------------------
 * CombinedDomainXYPlot.java
 * -------------------------
 * (C) Copyright 2001-2007, by Bill Kelemen and Contributors.
 *
 * Original Author:  Bill Kelemen;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *                   Anthony Boulestreau;
 *                   David Basten;
 *                   Kevin Frechette (for ISTI);
 *                   Nicolas Brodu;
 *                   Petr Kubanek (bug 1606205);
 *
 *
 */

package edu.umaine.cs.icecoredater;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;

/**
 * A CombinedDomainXYPlot that is capable of displaying interval markers across
 * all of the subplots.
 * 
 * @author Modified by Mark Royer
 * 
 */
public class IceCombinedDomainXYPlot extends CombinedDomainXYPlot implements
		Cloneable, PublicCloneable, Serializable, PlotChangeListener {

	/** For serialization. */
	private static final long serialVersionUID = -7765545541261907383L;

	/** Storage for the subplot references. */
	private List<XYPlot> subplots;

	/** The gap between subplots. */
	private double gap = 5.0;

	/** Temporary storage for the subplot areas. */
	private transient Rectangle2D[] subplotAreas;

	private boolean drawIntervalMarkers = false;

	/**
	 * Default constructor.
	 */
	public IceCombinedDomainXYPlot() {
		this(new NumberAxis());
	}

	/**
	 * Creates a new combined plot that shares a domain axis among multiple
	 * subplots.
	 * 
	 * @param domainAxis
	 *            the shared axis.
	 */
	public IceCombinedDomainXYPlot(ValueAxis domainAxis) {
		super(domainAxis);
		this.subplots = new java.util.ArrayList<XYPlot>();

		this.gap = 0.0;

		// Needed to render interval markers
		this.setRenderer(new XYLineAndShapeRenderer());
		
	}

	/**
	 * Returns a string describing the type of plot.
	 * 
	 * @return The type of plot.
	 */
	public String getPlotType() {
		return "Combined_Domain_XYPlot";
	}

	/**
	 * Sets the orientation for the plot (also changes the orientation for all
	 * the subplots to match).
	 * 
	 * @param orientation
	 *            the orientation (<code>null</code> not allowed).
	 */
	public void setOrientation(PlotOrientation orientation) {

		super.setOrientation(orientation);
		Iterator<XYPlot> iterator = this.subplots.iterator();
		while (iterator.hasNext()) {
			XYPlot plot = iterator.next();
			plot.setOrientation(orientation);
		}

	}

	/**
	 * Returns a range representing the extent of the data values in this plot
	 * (obtained from the subplots) that will be rendered against the specified
	 * axis. NOTE: This method is intended for internal JFreeChart use, and is
	 * public only so that code in the axis classes can call it. Since only the
	 * domain axis is shared between subplots, the JFreeChart code will only
	 * call this method for the domain values (although this is not
	 * checked/enforced).
	 * 
	 * @param axis
	 *            the axis.
	 * 
	 * @return The range (possibly <code>null</code>).
	 */
	public Range getDataRange(ValueAxis axis) {
		Range result = null;
		if (this.subplots != null) {
			Iterator<XYPlot> iterator = this.subplots.iterator();
			while (iterator.hasNext()) {
				XYPlot subplot = iterator.next();
				result = Range.combine(result, subplot.getDataRange(axis));
			}
		}
		return result;
	}

	/**
	 * Returns the gap between subplots, measured in Java2D units.
	 * 
	 * @return The gap (in Java2D units).
	 */
	public double getGap() {
		return this.gap;
	}

	/**
	 * Sets the amount of space between subplots and sends a
	 * {@link PlotChangeEvent} to all registered listeners.
	 * 
	 * @param gap
	 *            the gap between subplots (in Java2D units).
	 */
	public void setGap(double gap) {
		this.gap = gap;
		fireChangeEvent();
	}

	/**
	 * Adds a subplot (with a default 'weight' of 1) and sends a
	 * {@link PlotChangeEvent} to all registered listeners.
	 * <P>
	 * The domain axis for the subplot will be set to <code>null</code>. You
	 * must ensure that the subplot has a non-null range axis.
	 * 
	 * @param subplot
	 *            the subplot (<code>null</code> not permitted).
	 */
	public void add(XYPlot subplot) {
		// defer argument checking
		add(subplot, 1);
	}

	/**
	 * Adds a subplot with the specified weight and sends a
	 * {@link PlotChangeEvent} to all registered listeners. The weight
	 * determines how much space is allocated to the subplot relative to all the
	 * other subplots.
	 * <P>
	 * The domain axis for the subplot will be set to <code>null</code>. You
	 * must ensure that the subplot has a non-null range axis.
	 * 
	 * @param subplot
	 *            the subplot (<code>null</code> not permitted).
	 * @param weight
	 *            the weight (must be >= 1).
	 */
	public void add(XYPlot subplot, int weight) {

		if (subplot == null) {
			throw new IllegalArgumentException("Null 'subplot' argument.");
		}
		if (weight <= 0) {
			throw new IllegalArgumentException("Require weight >= 1.");
		}

		// store the plot and its weight
		subplot.setParent(this);
		subplot.setWeight(weight);
		subplot.setInsets(new RectangleInsets(0.0, 0.0, 0.0, 0.0), false);
		subplot.setDomainAxis(null);
		subplot.addChangeListener(this);
		this.subplots.add(subplot);

		ValueAxis axis = getDomainAxis();
		if (axis != null) {
			axis.configure();
		}
		fireChangeEvent();
	}

	/**
	 * Removes a subplot from the combined chart and sends a
	 * {@link PlotChangeEvent} to all registered listeners.
	 * 
	 * @param subplot
	 *            the subplot (<code>null</code> not permitted).
	 */
	public void remove(XYPlot subplot) {
		if (subplot == null) {
			throw new IllegalArgumentException(" Null 'subplot' argument.");
		}
		int position = -1;
		int size = this.subplots.size();
		int i = 0;
		while (position == -1 && i < size) {
			if (this.subplots.get(i) == subplot) {
				position = i;
			}
			i++;
		}
		if (position != -1) {
			this.subplots.remove(position);
			subplot.setParent(null);
			subplot.removeChangeListener(this);
			ValueAxis domain = getDomainAxis();
			if (domain != null) {
				domain.configure();
			}
			fireChangeEvent();
		}
	}

	/**
	 * Returns the list of subplots. The returned list may be empty, but is
	 * never <code>null</code>.
	 * 
	 * @return An unmodifiable list of subplots.
	 */
	@SuppressWarnings("unchecked")
	public List<XYPlot> getSubplots() {
		if (this.subplots != null) {
			return Collections.unmodifiableList(this.subplots);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Calculates the axis space required.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param plotArea
	 *            the plot area.
	 * 
	 * @return The space.
	 */
	protected AxisSpace calculateAxisSpace(Graphics2D g2, Rectangle2D plotArea) {

		AxisSpace space = new AxisSpace();
		PlotOrientation orientation = getOrientation();

		// work out the space required by the domain axis...
		AxisSpace fixed = getFixedDomainAxisSpace();
		if (fixed != null) {
			if (orientation == PlotOrientation.HORIZONTAL) {
				space.setLeft(fixed.getLeft());
				space.setRight(fixed.getRight());
			} else if (orientation == PlotOrientation.VERTICAL) {
				space.setTop(fixed.getTop());
				space.setBottom(fixed.getBottom());
			}
		} else {
			ValueAxis xAxis = getDomainAxis();
			RectangleEdge xEdge = Plot.resolveDomainAxisLocation(
					getDomainAxisLocation(), orientation);
			if (xAxis != null) {
				space = xAxis.reserveSpace(g2, this, plotArea, xEdge, space);
			}
		}

		Rectangle2D adjustedPlotArea = space.shrink(plotArea, null);

		// work out the maximum height or width of the non-shared axes...
		int n = this.subplots.size();
		int totalWeight = 0;
		for (int i = 0; i < n; i++) {
			XYPlot sub = this.subplots.get(i);
			totalWeight += sub.getWeight();
		}
		this.subplotAreas = new Rectangle2D[n];
		double x = adjustedPlotArea.getX();
		double y = adjustedPlotArea.getY();
		double usableSize = 0.0;
		if (orientation == PlotOrientation.HORIZONTAL) {
			usableSize = adjustedPlotArea.getWidth() - this.gap * (n - 1);
		} else if (orientation == PlotOrientation.VERTICAL) {
			usableSize = adjustedPlotArea.getHeight() - this.gap * (n - 1);
		}

		for (int i = 0; i < n; i++) {
			XYPlot plot = this.subplots.get(i);

			// calculate sub-plot area
			if (orientation == PlotOrientation.HORIZONTAL) {
				double w = usableSize * plot.getWeight() / totalWeight;
				this.subplotAreas[i] = new Rectangle2D.Double(x, y, w,
						adjustedPlotArea.getHeight());
				x = x + w + this.gap;
			} else if (orientation == PlotOrientation.VERTICAL) {
				double h = usableSize * plot.getWeight() / totalWeight;
				this.subplotAreas[i] = new Rectangle2D.Double(x, y,
						adjustedPlotArea.getWidth(), h);
				y = y + h + this.gap;
			}

			AxisSpace subSpace = calculateRangeAxisSpace(g2,
					this.subplotAreas[i], null, plot);
			space.ensureAtLeast(subSpace);

		}

		return space;
	}

	/**
	 * Calculates the space required for the range axis/axes.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param plotArea
	 *            the plot area.
	 * @param space
	 *            a carrier for the result (<code>null</code> permitted).
	 * 
	 * @return The required space.
	 */
	protected AxisSpace calculateRangeAxisSpace(Graphics2D g2,
			Rectangle2D plotArea, AxisSpace space, XYPlot plot) {

		if (space == null) {
			space = new AxisSpace();
		}

		// reserve some space for the range axis...
		if (plot.getFixedRangeAxisSpace() != null) {
			if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
				space.ensureAtLeast(plot.getFixedRangeAxisSpace().getTop(),
						RectangleEdge.TOP);
				space.ensureAtLeast(plot.getFixedRangeAxisSpace().getBottom(),
						RectangleEdge.BOTTOM);
			} else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
				space.ensureAtLeast(plot.getFixedRangeAxisSpace().getLeft(),
						RectangleEdge.LEFT);
				space.ensureAtLeast(plot.getFixedRangeAxisSpace().getRight(),
						RectangleEdge.RIGHT);
			}
		} else {
			// reserve space for the range axes...
			for (int i = 0; i < plot.getRangeAxisCount(); i++) {
				Axis axis = (Axis) plot.getRangeAxis(i);
				if (axis != null) {
					RectangleEdge edge = getRangeAxisEdge(i);
					space = axis.reserveSpace(g2, plot, plotArea, edge, space);
				}
			}
		}
		return space;

	}

	/**
	 * Draws the plot within the specified area on a graphics device.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param area
	 *            the plot area (in Java2D space).
	 * @param anchor
	 *            an anchor point in Java2D space (<code>null</code> permitted).
	 * @param parentState
	 *            the state from the parent plot, if there is one (
	 *            <code>null</code> permitted).
	 * @param info
	 *            collects chart drawing information (<code>null</code>
	 *            permitted).
	 */
	@SuppressWarnings("unchecked")
	public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
			PlotState parentState, PlotRenderingInfo info) {

		// set up info collection...
		if (info != null) {
			info.setPlotArea(area);
		}

		// adjust the drawing area for plot insets (if any)...
		RectangleInsets insets = getInsets();
		insets.trim(area);

		setFixedRangeAxisSpaceForSubplots(null);
		AxisSpace space = calculateAxisSpace(g2, area);
		Rectangle2D dataArea = space.shrink(area, null);

		// set the width and height of non-shared axis of all sub-plots
		setFixedRangeAxisSpaceForSubplots(space);

		// draw the shared axis
		ValueAxis axis = getDomainAxis();
		RectangleEdge edge = getDomainAxisEdge();
		double cursor = RectangleEdge.coordinate(dataArea, edge);
		AxisState axisState = axis.draw(g2, cursor, area, dataArea, edge, info);
		if (parentState == null) {
			parentState = new PlotState();
		}
		parentState.getSharedAxisStates().put(axis, axisState);

		// Added so that the tube markers would span all the subplots.
		if (this.drawIntervalMarkers) {
			Collection<IntervalMarker> markers = this
					.getDomainMarkers(Layer.BACKGROUND);
			if (markers != null) {
				for (int i = 0; i < markers.size(); i++) {
					drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);
				}
			}
		}

		// draw all the subplots
		for (int i = 0; i < this.subplots.size(); i++) {
			XYPlot plot = this.subplots.get(i);
			PlotRenderingInfo subplotInfo = null;
			if (info != null) {
				subplotInfo = new PlotRenderingInfo(info.getOwner());
				info.addSubplotInfo(subplotInfo);
			}
			plot.draw(g2, this.subplotAreas[i], anchor, parentState,
					subplotInfo);
		}

		if (info != null) {
			info.setDataArea(dataArea);
		}

	}

	/**
	 * Returns a collection of legend items for the plot.
	 * 
	 * @return The legend items.
	 */
	public LegendItemCollection getLegendItems() {
		LegendItemCollection result = getFixedLegendItems();
		if (result == null) {
			result = new LegendItemCollection();
			if (this.subplots != null) {
				Iterator<XYPlot> iterator = this.subplots.iterator();
				while (iterator.hasNext()) {
					XYPlot plot = iterator.next();
					LegendItemCollection more = plot.getLegendItems();
					result.addAll(more);
				}
			}
		}
		return result;
	}

	/**
	 * Multiplies the range on the range axis/axes by the specified factor.
	 * 
	 * @param factor
	 *            the zoom factor.
	 * @param info
	 *            the plot rendering info (<code>null</code> not permitted).
	 * @param source
	 *            the source point (<code>null</code> not permitted).
	 */
	public void zoomRangeAxes(double factor, PlotRenderingInfo info,
			Point2D source) {
		zoomRangeAxes(factor, info, source, false);
	}

	/**
	 * Multiplies the range on the range axis/axes by the specified factor.
	 * 
	 * @param factor
	 *            the zoom factor.
	 * @param state
	 *            the plot state.
	 * @param source
	 *            the source point (in Java2D coordinates).
	 * @param useAnchor
	 *            use source point as zoom anchor?
	 */
	public void zoomRangeAxes(double factor, PlotRenderingInfo state,
			Point2D source, boolean useAnchor) {
		// delegate 'state' and 'source' argument checks...
		XYPlot subplot = findSubplot(state, source);
		if (subplot != null) {
			subplot.zoomRangeAxes(factor, state, source, useAnchor);
		} else {
			// if the source point doesn't fall within a subplot, we do the
			// zoom on all subplots...
			Iterator<XYPlot> iterator = getSubplots().iterator();
			while (iterator.hasNext()) {
				subplot = iterator.next();
				subplot.zoomRangeAxes(factor, state, source, useAnchor);
			}
		}
	}

	/**
	 * Zooms in on the range axes.
	 * 
	 * @param lowerPercent
	 *            the lower bound.
	 * @param upperPercent
	 *            the upper bound.
	 * @param info
	 *            the plot rendering info (<code>null</code> not permitted).
	 * @param source
	 *            the source point (<code>null</code> not permitted).
	 */
	public void zoomRangeAxes(double lowerPercent, double upperPercent,
			PlotRenderingInfo info, Point2D source) {
		// delegate 'info' and 'source' argument checks...
		XYPlot subplot = findSubplot(info, source);
		if (subplot != null) {
			subplot.zoomRangeAxes(lowerPercent, upperPercent, info, source);
		} else {
			// if the source point doesn't fall within a subplot, we do the
			// zoom on all subplots...
			Iterator<XYPlot> iterator = getSubplots().iterator();
			while (iterator.hasNext()) {
				subplot = iterator.next();
				subplot.zoomRangeAxes(lowerPercent, upperPercent, info, source);
			}
		}
	}

	/**
	 * Returns the subplot (if any) that contains the (x, y) point (specified in
	 * Java2D space).
	 * 
	 * @param info
	 *            the chart rendering info (<code>null</code> not permitted).
	 * @param source
	 *            the source point (<code>null</code> not permitted).
	 * 
	 * @return A subplot (possibly <code>null</code>).
	 */
	public XYPlot findSubplot(PlotRenderingInfo info, Point2D source) {
		if (info == null) {
			throw new IllegalArgumentException("Null 'info' argument.");
		}
		if (source == null) {
			throw new IllegalArgumentException("Null 'source' argument.");
		}
		XYPlot result = null;
		int subplotIndex = info.getSubplotIndex(source);
		if (subplotIndex >= 0) {
			result = this.subplots.get(subplotIndex);
		}
		return result;
	}

	/**
	 * Sets the item renderer FOR ALL SUBPLOTS. Registered listeners are
	 * notified that the plot has been modified.
	 * <P>
	 * Note: usually you will want to set the renderer independently for each
	 * subplot, which is NOT what this method does.
	 * 
	 * @param renderer
	 *            the new renderer.
	 */
	public void setRenderer(XYItemRenderer renderer) {

		super.setRenderer(renderer); // not strictly necessary, since the
		// renderer set for the
		// parent plot is not used

		Iterator<XYPlot> iterator = this.subplots.iterator();
		while (iterator.hasNext()) {
			XYPlot plot = iterator.next();
			plot.setRenderer(renderer);
		}

	}

	/**
	 * Sets the fixed range axis space and sends a {@link PlotChangeEvent} to
	 * all registered listeners.
	 * 
	 * @param space
	 *            the space (<code>null</code> permitted).
	 */
	public void setFixedRangeAxisSpace(AxisSpace space) {
		super.setFixedRangeAxisSpace(space);
		setFixedRangeAxisSpaceForSubplots(space);
		fireChangeEvent();
	}

	/**
	 * Sets the size (width or height, depending on the orientation of the plot)
	 * for the domain axis of each subplot.
	 * 
	 * @param space
	 *            the space.
	 */
	protected void setFixedRangeAxisSpaceForSubplots(AxisSpace space) {
		Iterator<XYPlot> iterator = this.subplots.iterator();
		while (iterator.hasNext()) {
			XYPlot plot = iterator.next();
			plot.setFixedRangeAxisSpace(space, false);
		}
	}

	/**
	 * Handles a 'click' on the plot by updating the anchor values.
	 * 
	 * @param x
	 *            x-coordinate, where the click occurred.
	 * @param y
	 *            y-coordinate, where the click occurred.
	 * @param info
	 *            object containing information about the plot dimensions.
	 */
	public void handleClick(int x, int y, PlotRenderingInfo info) {
		Rectangle2D dataArea = info.getDataArea();
		if (dataArea.contains(x, y)) {
			for (int i = 0; i < this.subplots.size(); i++) {
				XYPlot subplot = this.subplots.get(i);
				PlotRenderingInfo subplotInfo = info.getSubplotInfo(i);
				subplot.handleClick(x, y, subplotInfo);
			}
		}
	}

	/**
	 * Receives a {@link PlotChangeEvent} and responds by notifying all
	 * listeners.
	 * 
	 * @param event
	 *            the event.
	 */
	public void plotChanged(PlotChangeEvent event) {
		notifyListeners(event);
	}

	/**
	 * Tests this plot for equality with another object.
	 * 
	 * @param obj
	 *            the other object.
	 * 
	 * @return <code>true</code> or <code>false</code>.
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof CombinedDomainXYPlot)) {
			return false;
		}
		CombinedDomainXYPlot that = (CombinedDomainXYPlot) obj;
		if (this.gap != that.getGap()) {
			return false;
		}
		if (!ObjectUtilities.equal(this.subplots, that.getSubplots())) {
			return false;
		}
		return super.equals(obj);
	}

	/**
	 * Returns a clone of the annotation.
	 * 
	 * @return A clone.
	 * 
	 * @throws CloneNotSupportedException
	 *             this class will not throw this exception, but subclasses (if
	 *             any) might.
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * @param drawIntervalMarkers
	 *            draws the interval markers iff true
	 */
	public void setDrawIntervalMarkers(boolean drawIntervalMarkers) {
		this.drawIntervalMarkers = drawIntervalMarkers;
		this.fireChangeEvent();
	}
}
