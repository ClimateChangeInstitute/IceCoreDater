/**
 * 
 */
package edu.umaine.cs.icecoredater;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 * @author Mark Royer
 *
 */
public class AboutAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static ImageIcon icon = new ImageIcon(AboutAction.class
			.getResource(IceCoreDating.imageDir + "/about.gif"));

	public AboutAction() {
		super("About", icon);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		IceCoreDating splash = new IceCoreDating();
		splash.createSplashScreen(IceCoreDating.imageDir + "/splashScreen.jpg");
		splash.showSplashScreen();
	}

}
