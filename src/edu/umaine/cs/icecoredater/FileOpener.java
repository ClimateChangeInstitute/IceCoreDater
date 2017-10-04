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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * @author Modified by Mark Royer
 * 
 */
public class FileOpener extends JFrame {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = 7291522985763820823L;

	private File file;

	boolean newSession = false;

	public void open() throws FileNotFoundException, DataFileException, IOException {

		JFileChooser chooser = new JFileChooser();
		BasicFilter filter = new BasicFilter();
		filter.addExtension("txt");
		filter.addExtension("csv");
		filter.setDescription("TXT & CSV Files");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			String fileType = file.getName().substring(
					file.getName().lastIndexOf("."), file.getName().length());
			if (fileType.equals(".txt") || fileType.equals(".csv")) {
				File report = null, correctedFile = null;

				if (fileType.equals(".txt")) {
					report = new File("Files"
							+ File.separator
							+ "Reports"
							+ File.separator
							+ file.getName().substring(0,
									file.getName().lastIndexOf("."))
							+ "CorrectedReport.txt");
					correctedFile = new File("Files"
							+ File.separator
							+ "Corrected"
							+ File.separator
							+ file.getName().substring(0,
									file.getName().lastIndexOf("."))
							+ "Corrected.txt");

				}

				else if (fileType.equals(".csv")) {
					report = new File("Files"
							+ File.separator
							+ "Reports"
							+ File.separator
							+ file.getName().substring(0,
									file.getName().lastIndexOf("."))
							+ "CorrectedReport.csv");
					correctedFile = new File("Files"
							+ File.separator
							+ "Corrected"
							+ File.separator
							+ file.getName().substring(0,
									file.getName().lastIndexOf("."))
							+ "Corrected.csv");
				}
				if (report.exists() && correctedFile.exists() && !newSession) {
					new ChartJFrame(correctedFile, report);
					return;
				} else if ((!report.exists() || !correctedFile.exists())
						&& !newSession) {
					int answer = JOptionPane
							.showConfirmDialog(
									null,
									"This file has no session associated"
											+ " with it, do you want to start a new one?",
									"Session Doesn't Exist",
									JOptionPane.YES_NO_OPTION);
					if (answer != JOptionPane.YES_OPTION) {
						new StartMenu();
						return;
					}

				} else if (report.exists() && correctedFile.exists()
						&& newSession) {
					int answer = JOptionPane
							.showConfirmDialog(
									null,
									"This file already has a session associated"
											+ " with it, do you want to start it over?",
									"Session Already Exists",
									JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						report.delete();
						correctedFile.delete();
						// go on normally
					} else {
						new StartMenu();
						return;
					}

				}
				CSVFileReader fr = new CSVFileReader(this);
				File tempFile = fr.read(file);
				if (tempFile != null) {
					new MainMenu(tempFile);
				}

			}

			else {
				String msg = "File is not in text format, or it was not found.";

				msg += " \n  \n   " + file.getParent() + "\\" + file.getName();
				JOptionPane.showMessageDialog(null, msg,
						"File can't be opened", JOptionPane.ERROR_MESSAGE);
				open();

			}

		} else {
			new StartMenu();
			return;
		}
	}
}

class BasicFilter extends FileFilter {
	private Hashtable<String, BasicFilter> filters = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	public BasicFilter() {
		this.filters = new Hashtable<String, BasicFilter>();
	}

	public BasicFilter(String extension, String description) {
		this();
		if (extension != null)
			addExtension(extension);
		if (description != null)
			setDescription(description);
	}

	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && filters.get(getExtension(f)) != null) {
				return true;
			}
			;
		}
		return false;
	}

	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
			;
		}
		return null;
	}

	public void addExtension(String extension) {
		if (filters == null) {
			filters = new Hashtable<String, BasicFilter>(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}

	public String getDescription() {
		if (fullDescription == null) {
			if (description == null || isExtensionListInDescription()) {
				fullDescription = description == null ? "(" : description
						+ " (";
				// build the description from the extension list
				Enumeration<String> extensions = filters.keys();
				if (extensions != null) {
					fullDescription += "." + (String) extensions.nextElement();
					while (extensions.hasMoreElements()) {
						fullDescription += ", ."
								+ (String) extensions.nextElement();
					}
				}
				fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}

}
