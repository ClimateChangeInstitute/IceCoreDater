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
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @author Modified by Mark Royer
 * 
 */
public class IceCoreDating extends Frame {

	/**
	 * For serializing.
	 */
	private static final long serialVersionUID = 561598821319472163L;

	public static final String imageDir = "images";

	final int splashTime = 5000;
	
	private JWindow splashScreen;

	private JLabel splashLabel;

	/**
	 * Flag to indicate the program should start. While false, the program will
	 * continue to show the splash screen.
	 */
	private boolean start;

	public static void main(String args[]) {
		IceCoreDating icd = new IceCoreDating();
		icd.run();
	}

	public void createSplashScreen(String path) {
		ImageIcon img = new ImageIcon(getClass().getResource(path));
		splashLabel = new JLabel(img);
		splashScreen = new JWindow(this);
		splashScreen.getContentPane().add(splashLabel, BorderLayout.CENTER);
		splashScreen.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = splashLabel.getPreferredSize();
		splashScreen.setLocation(screenSize.width / 2 - (labelSize.width / 2),
				screenSize.height / 2 - (labelSize.height / 2));

		splashScreen.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				splashScreen.setVisible(false);
				splashScreen.dispose();
				IceCoreDating.this.setStart(true);
			}
		});

	}

	public void showSplashScreen() {
		splashScreen.setVisible(true);
	}

	public void hideSplash() {
		splashScreen.setVisible(false);
		splashScreen = null;
		splashLabel = null;
	}

	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unknown Error "
					+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		createSplashScreen(imageDir + "/splashScreen.jpg");
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					showSplashScreen();
				}
			});
		} catch (Exception ex) {
		}

		try {
			int count = 0;
			int inc = 50;
			while (!isStart()) {
				if (count > splashTime) {
					this.setStart(true);
				} else {
					count += inc;
				}
				
				Thread.sleep(inc);
			}

		} catch (InterruptedException e) {
			System.out.println(e);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				hideSplash();
			}
		});

		new StartMenu();

	}

	public synchronized boolean isStart() {
		return start;
	}

	public synchronized void setStart(boolean start) {
		this.start = start;
	}
}
