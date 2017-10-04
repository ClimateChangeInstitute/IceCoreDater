/**
 * 
 */
package edu.umaine.cs.icecoredater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.ui.ExtensionFileFilter;

/**
 * A menu item that can save a chart as html.
 * 
 * @author Mark Royer
 * 
 */
public class SaveHTMLMenuItem extends JMenuItem implements ActionListener {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = -8265178434135001149L;

	/**
	 * The chart panel that this menu item is associated with.
	 */
	private ChartPanel chartPanel;

	/**
	 * Create a new menu item for saving the chart as html.
	 * 
	 * @param cp
	 */
	public SaveHTMLMenuItem(ChartPanel cp) {
		super("Save as HTML...");
		this.chartPanel = cp;
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
			ExtensionFileFilter filter = new ExtensionFileFilter("HTML",
					".htm,html");
			fileChooser.addChoosableFileFilter(filter);
			int option = fileChooser.showSaveDialog(null);
			if (option == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().getName();
				String filePath = fileChooser.getSelectedFile().getParent();
				String test = filePath.substring(filePath.length() - 1);
				if (test != "/") {
					fileName = filePath + "/" + fileName;
				} else {
					fileName = filePath + fileName;
				}
				File file = new File(fileName + ".jpg");
				ChartUtilities.saveChartAsJPEG(file, chartPanel.getChart(),
						chartPanel.getWidth(), chartPanel.getHeight());
				final ChartRenderingInfo info = new ChartRenderingInfo(
						new StandardEntityCollection());
				final File file2 = new File(fileName + ".html");
				final OutputStream out = new BufferedOutputStream(
						new FileOutputStream(file2));
				final PrintWriter writer = new PrintWriter(out);
				writer.println("<HTML>");
				writer.println("<HEAD><TITLE>Ice Core Dating</TITLE></HEAD>");
				writer.println("<BODY>");
				ChartUtilities.writeImageMap(writer, "chart", info, true);
				writer
						.println("<IMG SRC=\""
								+ file.getName()
								+ "\"WIDTH=\"600\" HEIGHT=\"400\" BORDER=\"0\" USEMAP=\"#chart\">");
				writer.println("</BODY>");
				writer.println("</HTML>");
				writer.close();
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "IO Exception", "Exception",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
