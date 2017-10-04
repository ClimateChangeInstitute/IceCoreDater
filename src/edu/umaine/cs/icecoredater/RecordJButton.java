/**
 * 
 */
package edu.umaine.cs.icecoredater;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartPanel;

/**
 * A button that allows users to delete year markers on the chart.
 * 
 * @author Mark Royer
 * 
 */
public class RecordJButton extends JToggleButton {

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
	 * This is only because Java 5.0 doesn't have clearSelection for button
	 * groups.
	 */
	private JToggleButton hiddenButton;

	/**
	 * Create a new button capable of deleting year markers on the given chart.
	 * 
	 * @param w
	 *            The window containing the button.
	 * @param cp
	 *            The chart to delete year markers on.
	 */
	public RecordJButton(ChartJFrame w, ChartPanel cp) {
		super("Start Recording", new ImageIcon(w.getClass().getResource(
				IceCoreDating.imageDir + "/record.gif")));
		this.window = w;
		this.chartPanel = cp;
		setToolTipText("Press to start recording annual layers");
		this.addActionListener(getAction());
		
		// Never seen by the user.
		hiddenButton = new JToggleButton();
		window.getButtonGroup().add(hiddenButton);
	}

	public RecordButtonAction getAction() {
		return new RecordButtonAction(this.getText(), this.getIcon(), this
				.getText());
	}

	/**
	 * Record action
	 * 
	 * @author Mark Royer
	 * 
	 */
	private class RecordButtonAction extends AbstractAction {

		/**
		 * For serializing.
		 */
		private static final long serialVersionUID = 8283730498328425991L;

		private boolean isRecording;

		public RecordButtonAction(String text, Icon icon, String desc) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, null);
		}

		public void actionPerformed(ActionEvent e) {

			if (!isRecording) {
				setIcon(new ImageIcon(getClass().getResource(
						IceCoreDating.imageDir + "/recording.gif")));
				setText("Stop Recording");
				chartPanel.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
				chartPanel.setVerticalAxisTrace(true);
				chartPanel.setHorizontalAxisTrace(true);
				chartPanel.setMouseZoomable(false);
				window.disableComponents(RecordJButton.this, true);
				isRecording = true;
			} else {
				setIcon(new ImageIcon(getClass().getResource(
						IceCoreDating.imageDir + "/record.gif")));
				setText("Start Recording");
				chartPanel.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				chartPanel.setVerticalAxisTrace(false);
				chartPanel.setHorizontalAxisTrace(false);
				window.disableComponents(RecordJButton.this, false);
				isRecording = false;
				hiddenButton.setSelected(true);
			}
		}
	}
}
