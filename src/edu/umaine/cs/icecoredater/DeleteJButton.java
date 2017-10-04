/**
 * 
 */
package edu.umaine.cs.icecoredater;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartPanel;

/**
 * A button that allows users to delete year markers on the chart.
 * 
 * @author Mark Royer
 * 
 */
public class DeleteJButton extends JToggleButton implements ActionListener {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = -5060619961264301399L;

	/**
	 * Window containing the chart and button.
	 */
	private ChartJFrame window;

	/**
	 * The chart to have year markers deleted on.
	 */
	private ChartPanel chartPanel;

	/**
	 * Create a new button capable of deleting year markers on the given chart.
	 * 
	 * @param w
	 *            The window containing the button.
	 * @param cp
	 *            The chart to delete year markers on.
	 */
	public DeleteJButton(ChartJFrame w, ChartPanel cp) {
		super("Delete", new ImageIcon(w.getClass().getResource(
				IceCoreDating.imageDir + "/delete.gif")));
		this.window = w;
		this.chartPanel = cp;
		setToolTipText("Delete lines");
		this.addActionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		if (isSelected()) {
			chartPanel
					.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			chartPanel.setVerticalAxisTrace(false);
			chartPanel.setHorizontalAxisTrace(false);
			chartPanel.setMouseZoomable(false);
			window.disableComponents(this, true);
		} else {
			chartPanel.setCursor(Cursor
					.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			window.disableComponents(this, false);
		}
	}

}
