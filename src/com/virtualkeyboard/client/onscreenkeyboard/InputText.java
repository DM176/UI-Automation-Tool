package com.virtualkeyboard.client.onscreenkeyboard;

import com.virtualkeyboard.client.models.CustomException;
import com.virtualkeyboard.client.util.Util;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * 
 * @author Hassam
 *
 */
public class InputText {

	/**
	 * 
	 * @param text Function for entering text
	 */
	public static void insertText(String text) throws CustomException, IOException, UnsupportedFlavorException, AWTException {

		if (text == null || text.isEmpty()) {
			return;

		}
		NativeAsciiRobotHandler nativeAsciiRobotHandler = new NativeAsciiRobotHandler();
	   Util.definedSleep(200);
		for (int i = 0; i < text.length(); i++) {
			nativeAsciiRobotHandler.sendToComponent(text.charAt(i), false);
			Util.definedSleep(20);
		}
		Util.definedSleep(300);
	}

	public static void insertCommand(char chr) throws CustomException, IOException, UnsupportedFlavorException, AWTException {
		NativeAsciiRobotHandler nativeAsciiRobotHandler = new NativeAsciiRobotHandler();

		nativeAsciiRobotHandler.sendToComponent(chr, false);
		Util.definedSleep(300);
	}
	
	public static void removeAllContent() throws CustomException, IOException, UnsupportedFlavorException, AWTException {
		NativeAsciiRobotHandler nativeAsciiRobotHandler = new NativeAsciiRobotHandler();

		nativeAsciiRobotHandler.sendToComponent('A', true);
		nativeAsciiRobotHandler.sendToComponent('', true);
		Util.postSleep();

	}

	public static void capslock() throws CustomException, IOException, UnsupportedFlavorException, AWTException {
		NativeAsciiRobotHandler nativeAsciiRobotHandler = new NativeAsciiRobotHandler();
		char cap=0x14;
		nativeAsciiRobotHandler.sendToComponent('', true);

		Util.postSleep();

	}
	

}
