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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 * @author Modified by Mark Royer
 *
 */
public class Editor extends JFrame {
	
	/**
	 * For serializing. 
	 */
	private static final long serialVersionUID = -7802864343171230009L;
	
	JEditorPane editor = new JEditorPane();
	JScrollPane jScrollPane1 = new JScrollPane();

	public Editor(File reportFile) {
		try {
			editor.read(new BufferedReader(new java.io.FileReader(reportFile)),
					null);
			jbInit();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "IO Exception", "Exception",
					JOptionPane.ERROR_MESSAGE);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() throws Exception {
		jScrollPane1.setBounds(new Rectangle(16, 330, 150, 299));
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(editor, null);
		this.setSize(new Dimension(410, 285));
		editor.setEditable(false);
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = this.getBounds();
		this.setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
		this.setVisible(true);
		this.requestFocus();

	}

}