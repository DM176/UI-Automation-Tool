package com.virtualkeyboard.client.mouse.movement;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import com.virtualkeyboard.client.util.Util;

/**
 * 
 * @author Hassam
 *
 */
public class MouseControl {

	
	/**
	 * 
	 * @param x
	 * @param y
	 * to click on define x and y location
	 * @throws AWTException
	 */
	public static void singleClick(int x, int y) throws AWTException{
	    Robot bot = new Robot();
	    bot.mouseMove(x, y);

	    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		Util.definedSleep(100);
	    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    Util.postSleep();
	}

	public static void rightClick(int x, int y) throws AWTException{
		Robot bot = new Robot();
		bot.mouseMove(x, y);


		bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		Util.definedSleep(200);
		bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		Util.postSleep();
	}

	public static void moveCursor(int x, int y) throws AWTException{
		Robot bot = new Robot();
		bot.mouseMove(x, y);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @throws AWTException
	 * @throws InterruptedException 
	 */
	public static void doubleClick(int x, int y) throws AWTException, InterruptedException{
	    Robot bot = new Robot();
	    bot.mouseMove(x, y);    
	    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    Util.postSleep();
	    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	   
	}
	
	
	
	/**
	 * 
	 * @param scrollValue
	 * @throws AWTException
	 */
	public static void scrollWheel(int scrollValue) throws AWTException{
	    Robot bot = new Robot();
	    System.out.println("scroll");
	    bot.mouseWheel(scrollValue);   
	 
	    //bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    //bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

	}
	
	
}
