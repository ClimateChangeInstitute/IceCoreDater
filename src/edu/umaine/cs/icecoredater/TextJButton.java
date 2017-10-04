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
 * A button that allows users to add text to the chart.
 * 
 * @author Mark Royer
 * 
 */
public class TextJButton extends JToggleButton implements ActionListener {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = -3122486327810145723L;

	private ChartJFrame window;

	private ChartPanel chartPanel;

	public TextJButton(ChartJFrame w, ChartPanel cp) {
		super("Text", new ImageIcon(w.getClass().getResource(
				IceCoreDating.imageDir + "/text.gif")));
		this.window = w;
		this.chartPanel = cp;
		setToolTipText("Enter text");
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (isSelected()) {
			chartPanel
					.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			window.disableComponents(this, true);
		} else {
			chartPanel.setCursor(Cursor
					.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			window.disableComponents(this, false);
		}
	}

}
