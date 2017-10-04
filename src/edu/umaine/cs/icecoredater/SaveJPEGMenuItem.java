/**
 * 
 */
package edu.umaine.cs.icecoredater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.ui.ExtensionFileFilter;

/**
 * Menu Item that can save a chart.
 * 
 * @author Mark Royer
 * 
 */
public class SaveJPEGMenuItem extends JMenuItem implements ActionListener {

	/**
	 * For Serializing.
	 */
	private static final long serialVersionUID = -5577704602065445576L;

	private ChartPanel chartPanel;

	public SaveJPEGMenuItem(ChartPanel cp) {
		super("Save as JPEG..");
		chartPanel = cp;
		this.addActionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			ExtensionFileFilter filter = new ExtensionFileFilter("JPEG", ".jpg");
			fileChooser.addChoosableFileFilter(filter);
			int option = fileChooser.showSaveDialog(null);
			if (option == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().getName();
				String filePath = fileChooser.getSelectedFile().getParent();
				String test = filePath.substring(filePath.length() - 1);
				if (test != "/") {
					fileName = filePath + "/" + fileName + ".jpg";
				} else {
					fileName = filePath + fileName + ".jpg";
				}
				File file = new File(fileName);

				ChartUtilities.saveChartAsJPEG(file, chartPanel.getChart(),
						chartPanel.getWidth(), chartPanel.getHeight());
			}

		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "IO Exception", "Exception",
					JOptionPane.ERROR_MESSAGE);
		}

	}

}
