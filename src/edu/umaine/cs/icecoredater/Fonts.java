/**
 * 
 */
package edu.umaine.cs.icecoredater;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * Use this class to register fonts. Place fonts in the fonts directory.
 * 
 * @author Mark Royer
 *
 */
public class Fonts {

	public final static String FONTDIR = "fonts";

	static void registerFonts() {
		try {

			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();

			// Register fonts here for later use

			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
					Fonts.class.getResourceAsStream(
							FONTDIR + File.separator + "FreeSansBold.ttf")));

		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}

	}

	public static Font getFont(final String fontName) {
		Font result = Font.getFont(fontName);
		
		if (result == null) {
			registerFonts();// First time trying to get fonts
			result = Font.decode(fontName);
		}
		
		if (result == null) {
			result = Font.decode(Font.SANS_SERIF);
			System.err.printf("Unable to load font '%s'. Defaulting to '%s'%n", fontName, result);
		}
		
		return result;
	}
}
