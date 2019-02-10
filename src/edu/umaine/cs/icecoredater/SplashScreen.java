package edu.umaine.cs.icecoredater;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

public class SplashScreen {

	public static BufferedImage getSplashScreenImage(final String path)
			throws IOException {

		BufferedImage bimg = ImageIO.read(SplashScreen.class.getResource(path));

		Graphics2D g2 = (Graphics2D) bimg.createGraphics();

		Font font = Fonts.getFont("FreeSansBold").deriveFont(Font.BOLD, 16f);

		g2.setFont(font);
		g2.setColor(new Color(20, 79, 135, 255));

		final int padding = 10;

		FontMetrics fm = g2.getFontMetrics();
		Properties props = new Properties();

		props.load(SplashScreen.class
				.getResourceAsStream("icecoredating.properties"));

		final String VERSION = "Version"
				+ props.getProperty("version").replaceAll("-SNAPSHOT", "");

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2.drawString(VERSION,
				(float) bimg.getWidth() - (fm.stringWidth(VERSION) + padding),
				(float) bimg.getHeight() - padding);
		
		return bimg;
	}

}
