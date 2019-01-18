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

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Modified by Mark Royer
 * 
 */
public class MainMenu extends JFrame {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = -1555470601012682214L;

	/**
	 * A csv file that has already been parsed and verified as correct data.
	 */
	private File correctedFile;

	/**
	 * All of the elements in the corrected file minus the first three columns,
	 * which are assumed to be tube, top depth, and bottom depth.
	 */
	private String[] headerElements;

	/**
	 * How to plot the elements on the chart.
	 */
	public final String[] plottingMethods = new String[] {
			"Midpoint between top and bottom", "Top point", "Bottom point" };

	/**
	 * The year the ice core was collected.
	 */
	private int topYear;

	/**
	 * The method selected for plotting on the chart.
	 * 
	 * @see plottingMethods
	 */
	private String selectedMethod;

	/**
	 * The JComboBox to display the plottingMethods.
	 * 
	 * @see plottingMethods
	 */
	private JComboBox<String> plottingMethodsJComboBox;

	/**
	 * Any element that can be selected for plotting.
	 */
	private Vector<Checkbox> selectedElements;

	/**
	 * Contains all of the possible elements that can be plotted.
	 */

	private JList<Checkbox> elementsToPlotJList;
	public MainMenu(File tempFile) {
		super("Main Menu");
		if (tempFile != null) {
			this.correctedFile = tempFile;
		}
		run("");
	}

	/**
	 * Read header from the given file. This method assumes the first three
	 * headers are the tube, top depth, and bottom depth. All headers after that
	 * are returned in the resulting array.
	 * 
	 * @param cFile
	 *            The corrected file containing the header
	 * @return All of the headers in the .csv file minus tube, top depth, and
	 *         bottom depth.
	 */
	public String[] readHeaderElementsFromFile(File cFile) {

		String[] headerElements = new String[0];

		try {
			BufferedReader br = new BufferedReader(new FileReader(cFile));
			String line = br.readLine();
			StringTokenizer tokenizer = null;
			if (cFile.getName().substring(cFile.getName().lastIndexOf("."),
					cFile.getName().length()).equals(".csv")) {
				tokenizer = new StringTokenizer(line, ",");
			} else {
				tokenizer = new StringTokenizer(line);
			}

			tokenizer.nextToken(); // Tube
			tokenizer.nextToken(); // Top Depth
			tokenizer.nextToken(); // Bottom Depth
			headerElements = new String[tokenizer.countTokens()];
			int i = 0;
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				headerElements[i] = token;
				i++;
			}

			br.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "IO Exception", "Exception",
					JOptionPane.ERROR_MESSAGE);
			headerElements = new String[0];
		}

		return headerElements;

	}

	public void run(String arg) {
		try {

			this.setResizable(false);
			this.getContentPane().setLayout(new BorderLayout());

			// Read in the elements from the corrected file.
			headerElements = readHeaderElementsFromFile(correctedFile);

			JPanel topPanel = new JPanel(new BorderLayout());
			JLabel selectElementsJLabel = new JLabel(
					"Select Elements to plot against:");
			selectElementsJLabel.setFont(new java.awt.Font("Serif", 1, 12));
//			selectElementsJLabel.setForeground(UIManager.getColor("menuText"));
			topPanel.add(selectElementsJLabel, BorderLayout.NORTH);
			JPanel checkBoxesPanel = new JPanel(new FlowLayout());
			checkBoxesPanel.setOpaque(true);

			selectedElements = new Vector<Checkbox>();

			for (int j = 0; j < headerElements.length; j++) {
				selectedElements.add(new Checkbox(headerElements[j], false));
			}

			elementsToPlotJList = new JList<>(selectedElements);
			elementsToPlotJList
					.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							for (int j = 0; j < selectedElements.size(); j++)
								selectedElements.get(j).setState(false);
							int[] values = elementsToPlotJList
									.getSelectedIndices();
							for (int i = 0; i < values.length; i++)
								((Checkbox) elementsToPlotJList.getModel()
										.getElementAt(values[i]))
										.setState(true);
						}
					});
			elementsToPlotJList.setCellRenderer(new MyCellRenderer());
			elementsToPlotJList.setSize(new Dimension(50, 50));
			elementsToPlotJList
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			elementsToPlotJList.setAutoscrolls(true);
			elementsToPlotJList
					.setToolTipText("Hold Ctrl to select multiple items");
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setSize(new Dimension(50, 50));
			scrollPane.getViewport().setView(elementsToPlotJList);
			scrollPane.setPreferredSize(new Dimension(300, 100));
			topPanel.add(scrollPane, BorderLayout.CENTER);
			this.getContentPane().add(topPanel, BorderLayout.NORTH);
			JPanel middlePanel = new JPanel(new BorderLayout());
			JPanel middlePanel1 = new JPanel(new FlowLayout());
			JLabel topYearLabel = new JLabel("Top Year of The core:");
			topYearLabel.setFont(new java.awt.Font("Serif", 1, 12));
//			topYearLabel.setForeground(UIManager.getColor("menuText"));
			final JTextField topYearJTextField = new JTextField();
			topYearJTextField.setToolTipText("Enter your top year here");
			String currentYear = Integer.valueOf(
					Calendar.getInstance().get(Calendar.YEAR)).toString();
			topYearJTextField.setColumns(currentYear.length());
			topYearJTextField.setText(currentYear);
			JButton okJButton = new JButton("OK");
			JButton cancelJButton = new JButton("Cancel");
			JPanel buttonsPanel = new JPanel(new FlowLayout());
			buttonsPanel.add(okJButton);
			buttonsPanel.add(cancelJButton);
			middlePanel1.add(topYearLabel);
			middlePanel1.add(topYearJTextField);
			middlePanel.add(middlePanel1, BorderLayout.CENTER);
			middlePanel.add(buttonsPanel, BorderLayout.SOUTH);
			this.getContentPane().add(middlePanel, BorderLayout.SOUTH);

			JPanel bottomPanel = new JPanel(new BorderLayout());
			plottingMethodsJComboBox = new JComboBox<>(plottingMethods);
			plottingMethodsJComboBox
					.setToolTipText("Select your plotting method");
			JLabel choicesLabel = new JLabel("Choose your plotting Method: ");
			choicesLabel.setFont(new java.awt.Font("Serif", 1, 12));
//			choicesLabel.setForeground(UIManager.getColor("menuText"));
			JPanel choicesPanel = new JPanel(new FlowLayout());
			choicesPanel.add(choicesLabel, FlowLayout.LEFT);
			choicesPanel.add(plottingMethodsJComboBox);
			bottomPanel.add(choicesPanel, BorderLayout.CENTER);
			this.getContentPane().add(bottomPanel, BorderLayout.CENTER);
			cancelJButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
					new StartMenu();
					return;
				}

			});

			okJButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						topYear = Integer.parseInt(topYearJTextField.getText());
						int topYearConfirm = JOptionPane.showConfirmDialog(
								null,
								"Please confirm that this is your top year: "
										+ topYear, "Top year",
								JOptionPane.YES_NO_OPTION);
						if (topYearConfirm == JOptionPane.YES_OPTION) {
							selectedMethod = plottingMethods[plottingMethodsJComboBox
									.getSelectedIndex()];
							if (check()) {
								dispose();
								try {
									new ChartJFrame(correctedFile, topYear,
											headerElements.length,
											selectedElements, selectedMethod);
								} catch (FileNotFoundException e1) {
									JOptionPane.showMessageDialog(MainMenu.this,
											"Unable to open file.", "File Error",
											JOptionPane.ERROR_MESSAGE);
									e1.printStackTrace();
								} catch (DataFileException e1) {
									JOptionPane.showMessageDialog(MainMenu.this,
											e1.toString(), "File Error",
											JOptionPane.ERROR_MESSAGE);
									e1.printStackTrace();
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(MainMenu.this,
											"Unable to open file.", "File Error",
											JOptionPane.ERROR_MESSAGE);
									e1.printStackTrace();
								}
							} else {
								JOptionPane.showMessageDialog(null,
										"Please select at least one "
												+ "element to plot against",
										"Error", JOptionPane.ERROR_MESSAGE);
								return;
							}

						} else {
							return;

						}

					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null,
								"Please enter a valid top year", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}

			});

			this.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				}
			});

			this.validate();
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			this.requestFocus();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error\n " + e.getCause());
		}
	}

	/**
	 * @return True iff an element to plot was selected.
	 */
	private boolean check() {
		for (int i = 0; i < selectedElements.size(); i++) {
			Checkbox element = selectedElements.get(i);
			if (element.getState()) {
				return true;
			}
		}
		return false;
	}

	private class MyCellRenderer extends JLabel implements ListCellRenderer<Checkbox> {

		/**
		 * For serializing.
		 */
		private static final long serialVersionUID = -7845258907793683406L;

		/**
		 * This is the only method defined by ListCellRenderer. We just
		 * reconfigure the JLabel each time we're called.
		 */
		public Component getListCellRendererComponent(JList<? extends Checkbox> list, Checkbox value, // value
				// to
				// display
				int index, // cell index
				boolean isSelected, // is the cell selected
				boolean cellHasFocus) // the list and the cell have the focus
		{
			String s = value.getLabel();
			setText(s);

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}

}
