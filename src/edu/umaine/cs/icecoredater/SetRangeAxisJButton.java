/**
 * 
 */
package edu.umaine.cs.icecoredater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A button for setting the range axis of the chart.
 * 
 * @author Mark Royer
 * 
 */
public class SetRangeAxisJButton extends JButton implements ActionListener {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = -4298201694522078954L;

	/**
	 * The chart panel that this button will set the range of.
	 */
	private ChartJFrame window;

	/**
	 * Create a button that can change the y axis limits.
	 * 
	 * @param window The window containing this button
	 */
	public SetRangeAxisJButton(ChartJFrame window) {
		super("Set Y Axis limits");
		this.window = window;
		setToolTipText("Change Y axis");
		this.addActionListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		window.setRangeAxis();
	}
}
