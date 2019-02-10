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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Modified by Mark Royer
 * 
 */
public class StartMenu extends JFrame {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = 5357731128318727391L;

	JButton aboutButton = new JButton(new AboutAction());
	JPanel jPanel1 = new JPanel();
	JLabel newSessionLabel = new JLabel();
	JLabel continueSessionLabel = new JLabel();
	JButton newSessionButton = new JButton(new ImageIcon(getClass()
			.getResource(IceCoreDating.imageDir + "/new.gif")));
	JButton continueSessionButton = new JButton(new ImageIcon(getClass()
			.getResource(IceCoreDating.imageDir + "/open.gif")));
	JButton exitButton = new JButton(new ImageIcon(getClass().getResource(
			IceCoreDating.imageDir + "/exit.gif")));

	public StartMenu() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(null);
//		aboutButton.setText("About");
//		aboutButton
//				.addActionListener(new StartMenu_jButton4_actionAdapter(this));
		aboutButton.setBounds(new Rectangle(69, 218, 107, 27));
		aboutButton.setFont(new java.awt.Font("Arial", 1, 11));
		aboutButton.setToolTipText("About");
		this.setTitle("Start Menu");
		this.setResizable(false);
		jPanel1.setPreferredSize(new Dimension(756, 500));
		jPanel1.setBounds(new Rectangle(-2, 0, 415, 286));
		jPanel1.setLayout(null);
		newSessionLabel.setBounds(new Rectangle(116, 45, 176, 29));
		newSessionLabel.setEnabled(true);
		newSessionLabel.setFont(new java.awt.Font("Arial", 1, 11));
		newSessionLabel.setText("Start A New Dating Session");
		continueSessionLabel.setBounds(new Rectangle(113, 96, 201, 29));
		continueSessionLabel.setFont(new java.awt.Font("Arial", 1, 11));
		continueSessionLabel.setToolTipText("");
		continueSessionLabel.setText("Continue An Existing Dating Session");
		newSessionButton.setBounds(new Rectangle(61, 46, 42, 24));
		newSessionButton.addActionListener(new NewSessionAction(this));
		continueSessionButton.setBounds(new Rectangle(61, 99, 41, 24));
		continueSessionButton.setOpaque(false);
		continueSessionButton
				.addActionListener(new StartMenu_jButton2_actionAdapter(this));
		exitButton.setToolTipText("Exit");
		exitButton.setFont(new java.awt.Font("Arial", 1, 11));
		exitButton.setBounds(new Rectangle(222, 217, 107, 29));
		exitButton
				.addActionListener(new StartMenu_jButton5_actionAdapter(this));
		exitButton.setText("Exit");
		exitButton
				.addActionListener(new StartMenu_jButton5_actionAdapter(this));
		jPanel1.add(newSessionButton, null);
		jPanel1.add(newSessionLabel, null);
		jPanel1.add(continueSessionButton, null);
		jPanel1.add(continueSessionLabel, null);
		jPanel1.add(aboutButton, null);
		jPanel1.add(exitButton, null);
		this.getContentPane().add(jPanel1, null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.setSize(new Dimension(410, 285));
		this.setLocationRelativeTo(null);
		
		this.validate();		
		
		this.setVisible(true);
		this.toFront();
		this.requestFocus();
	}

	void jButton3_actionPerformed(ActionEvent e) {

	}

	void startNewSessionActionPerformed(ActionEvent e)
			throws FileNotFoundException, DataFileException, IOException {

		dispose();
		FileOpener opener = new FileOpener();
		opener.newSession = true;
		opener.open();

	}

	void jButton2_actionPerformed(ActionEvent e) throws FileNotFoundException,
			DataFileException, IOException {
		dispose();
		FileOpener opener = new FileOpener();
		opener.newSession = false;
		opener.open();
	}

	void jButton5_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	class StartMenu_jButton3_actionAdapter implements
			java.awt.event.ActionListener {
		StartMenu adaptee;

		StartMenu_jButton3_actionAdapter(StartMenu adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.jButton3_actionPerformed(e);
		}
	}

	class NewSessionAction implements ActionListener {
		StartMenu adaptee;

		NewSessionAction(StartMenu adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				adaptee.startNewSessionActionPerformed(e);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(StartMenu.this,
						"Unable to open file.", "File Error",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (DataFileException e1) {
				JOptionPane.showMessageDialog(StartMenu.this, e1.toString(),
						"File Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(StartMenu.this,
						"Unable to open file.", "File Error",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}

	class StartMenu_jButton2_actionAdapter implements ActionListener {
		StartMenu adaptee;

		StartMenu_jButton2_actionAdapter(StartMenu adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				adaptee.jButton2_actionPerformed(e);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(StartMenu.this,
						"Unable to open file.", "File Error",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (DataFileException e1) {
				JOptionPane.showMessageDialog(StartMenu.this, e1.toString(),
						"File Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(StartMenu.this,
						"Unable to open file.", "File Error",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}

	class StartMenu_jButton5_actionAdapter implements ActionListener {
		StartMenu adaptee;

		StartMenu_jButton5_actionAdapter(StartMenu adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.jButton5_actionPerformed(e);
		}
	}

}
