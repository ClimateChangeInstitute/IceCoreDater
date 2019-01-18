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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

/**
 * @author Modified by Mark Royer
 * 
 */
public class CSVFileReader {
	boolean valid = true;
	File tempFile1 = null;
	FileOpener opener;
	String message = "";
	boolean negatives = false;
	List<String> header;
	List<String> negativeElements;

	public CSVFileReader(FileOpener opener) {
		this.opener = opener;
	}

	/**
	 * Parse the given file and create a corrected version in the Files
	 * directory. For example, if the file name is iceData.csv. When the file is
	 * successfully parsed, a file name iceDataCorrected.csv will be created in
	 * the Files directory. The corrected file is then returned. If we were
	 * unable to parse the file, null is returned.
	 * 
	 * Note that -99 values in the parsed file will be replaced with 0 values.
	 * 
	 * @param csvFile
	 *            The file to parse.
	 * @return The corrected version of the parsed file or null if there was an
	 *         error parsing the given csvFile.
	 */
	public File read(File csvFile) {

		negativeElements = new ArrayList<String>();
		int lineNumber = 0;
		String line = "";
		int numberOfZeroes = 0;

		boolean minus99 = false;
		boolean emptyValues = false;

		File path = new File("Files" + File.separator + "Corrected");

		if (!path.exists())
			path.mkdirs();

		tempFile1 = new File(path, csvFile.getName().substring(0,
				csvFile.getName().indexOf("."))
				+ "Corrected"
				+ csvFile.getName().substring(csvFile.getName().indexOf("."),
						csvFile.getName().length()));

		BufferedReader br = null;
		PrintWriter out = null;
		try {

			br = new BufferedReader(new FileReader(csvFile));

			if (!tempFile1.exists())
				tempFile1.createNewFile();
			out = new PrintWriter(new BufferedWriter(
					new FileWriter(tempFile1)));

			double previousTop = -1, previousBottom = Double.MAX_VALUE;

			/*
			 * Read the file line by line
			 */
			while ((line = br.readLine()) != null) {

				valid = true;
				lineNumber++;
				// header line
				if (lineNumber == 1) {
					readAndWriteHeaderRow(line, out);
				} else {

					StringTokenizer st = new StringTokenizer(line, ",\t\n\r\f");
					int tokenCount = st.countTokens();
					int tokenNumber = 0;
					while (st.hasMoreTokens()) {
						String token = "";
						token = st.nextToken();
						tokenNumber++;

						if (token.trim().equals("")) {
							token = "0.00";
							emptyValues = true;
						} else if (Double.parseDouble(token) == -99) {
							minus99 = true;
							token = "0.00";
						}

						if (Double.parseDouble(token) == 0) {
							numberOfZeroes++;
						} else if (Double.parseDouble(token) < 0
								&& Double.parseDouble(token) != -99) {
							negatives = true;
							negativeElements.add(header.get(tokenNumber - 1));

						}

						if (tokenNumber < tokenCount)
							out.write(token + ",");
						else
							out.write(token);
					}
					out.flush();
					out.println();

					String ts = line.substring(line.indexOf(",") + 1, line
							.length());

					// top depth
					double top = Double.valueOf(ts.substring(0, ts
							.indexOf(",")));
					ts = ts.substring(ts.indexOf(",") + 1, ts.length());

					// bottom depth
					double bottom = Double.valueOf(ts.substring(0, ts
							.indexOf(",")));

					// Do some sanity checks with the data
					if (top > bottom) {
						throw new DataFileException(
								"The top value was greater than the "
										+ "bottom value on the same row. "
										+ DataFileException.lineSeparator
										+ "Are the first three columns "
										+ "tube, top depth, bottom depth?",
								line, lineNumber);
					}
					if (top < previousTop) {
						throw new DataFileException(
								"The top value was less than the "
										+ "top value on the previous row. "
										+ DataFileException.lineSeparator
										+ "Make sure the rows are ordered "
										+ "ascendingly by depth.", line,
								lineNumber);
					}
					if (bottom < previousBottom && lineNumber != 2) {
						throw new DataFileException(
								"The bottom value was less than the "
										+ "bottom value on the previous row. "
										+ DataFileException.lineSeparator
										+ "Make sure the rows are ordered "
										+ "ascendingly by depth.", line,
								lineNumber);
					}

					previousTop = top;
					previousBottom = bottom;

				}
			}

			if (negatives) {
				message += "\n The following elements contain negative values, "
						+ "do you want to proceed ?\n";
				for (int i = 0; i < negativeElements.size(); i++) {
					if (message.indexOf((String) negativeElements.get(i)) != -1)
						continue;
					message += "- " + (String) negativeElements.get(i) + "\n";
				}
				int answer = JOptionPane.showConfirmDialog(null, message,
						"Negative Values Found", JOptionPane.YES_NO_OPTION);
				if (answer != JOptionPane.YES_OPTION) {
					tempFile1.delete();
					opener.open();
					return null;
				}
			} else if (numberOfZeroes > 0) {
				if (emptyValues && !minus99) {
					message += "\n- Your file contains empty values, they have been replaced with zeros";
				} else if (!emptyValues && minus99) {
					message += "\n- Your file contains the -99 values. The have been replaced with zeros";
				} else if (emptyValues && minus99) {
					message += "\n- Your file contains empty values and the value -99. They have been replaced with zeroes";
				}

				message += "\n- Your file contains " + numberOfZeroes
						+ " zeros";
			}

			if (negatives)
				return tempFile1;
			if (valid && !negatives) {
				message += "\n The file was valid, and it has been successfully loaded.";
				JOptionPane.showMessageDialog(null, message, "Valid",
						JOptionPane.PLAIN_MESSAGE);
				return tempFile1;
			} else {
				message += "\n The file format is invalid.  "
						+ "The loading process will be aborted.";
				JOptionPane.showMessageDialog(null, message, "Invalid",
						JOptionPane.ERROR_MESSAGE);
				tempFile1.delete();
				FileOpener fo = new FileOpener();
				fo.open();
				return null;
			}

		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "IO Error!", "IO Exception",
					JOptionPane.ERROR_MESSAGE);
			try {
				opener.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(opener, e, "Error",
					JOptionPane.ERROR_MESSAGE);
			tempFile1.delete();
			try {
				opener.open();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null)
				out.close();
				
		}

	}

	/**
	 *Read headers and write to the given output stream. Example headers are
	 * "tube","top","bottom","length","Na","NH4","K","Mg","Ca","Cl","NO3","SO4".
	 * 
	 * @param line
	 *            The row to parse
	 * @param out
	 *            The output stream to write to
	 */
	private void readAndWriteHeaderRow(String line, PrintWriter out) {

		StringTokenizer st = new StringTokenizer(line, ",");
		int tokenCount = st.countTokens();
		int tokenNumber = 0;
		int i = 0;
		header = new ArrayList<String>();
		while (st.hasMoreTokens()) {

			String token = st.nextToken();
			header.add(token);
			tokenNumber++;
			/*
			 * if (!token.trim().equalsIgnoreCase(headers[i])){ valid = false;
			 * break; }
			 */
			if (tokenNumber < tokenCount)
				out.write(token + ",");
			else
				out.write(token);
			i++;
		}
		out.flush();
		out.println();
	}
}
