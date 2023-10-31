package com.virtualkeyboard.client.screenshot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * 
 * @author Hassam
 *
 */
public class ScreenShot {

	/**
	 * take the screenshot of full screen
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage captureScreenShotofScreenSize() throws IOException {
		try {
			Robot robot = new Robot();
			System.out.println(Toolkit.getDefaultToolkit().getScreenSize());

			//find the size of screenshot
			Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

			BufferedImage image = robot.createScreenCapture(rectangle);
			//Cast into Image Obj
			//Image capturedImage = SwingFXUtils.toFXImage(image, null);

//			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//			GraphicsDevice[] gs = ge.getScreenDevices();
//			Rectangle screenBounds = new Rectangle();
//			for (GraphicsDevice gd : gs) {
//				DisplayMode dm = gd.getDisplayMode();
//				screenBounds.width += dm.getWidth();
//				screenBounds.height = Math.max(screenBounds.height, dm.getHeight());
//			}
//			System.out.println(screenBounds.getWidth() +", "+ screenBounds.getHeight());
//			BufferedImage image = robot.createScreenCapture(screenBounds);

			return image;
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * Use to capture the screen of define width and height
	 * @return
	 */
	public static BufferedImage captureScreenShotofGivenSize(int x1,int y1, int x2,int y2) {
		try {
			Robot robot = new Robot();
		
			Rectangle rectangle = new Rectangle(x1,y1,x2,y2);
			BufferedImage image = robot.createScreenCapture(rectangle);

			return image;
		} catch (AWTException ex) {

			ex.printStackTrace();
		}
		return null;
	}


}
