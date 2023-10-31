package com.virtualkeyboard.client.onscreenkeyboard;



import com.virtualkeyboard.client.models.CustomException;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;


/**
 * 
 * @author Hassam
 *
 */
public class NativeAsciiRobotHandler  {
	
	

   
    private final int controlKeyEvent;

    public NativeAsciiRobotHandler() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac")) {
            controlKeyEvent = VK_META;
        } else {
            controlKeyEvent = VK_CONTROL;
        }
    }

  
 
    /**
     * 
     * @param ch
     * @param ctrl
     * Function for handling and inputing character through java.awt
     */
   public  void sendToComponent(char ch, boolean ctrl) throws CustomException, AWTException, IOException, UnsupportedFlavorException {

    	Robot robot;
        try {
            robot = new Robot();
            
        } catch (AWTException e) {
            throw e;

        }

        if (ctrl) {
            switch (ch) {
                case VK_ENTER:
                case VK_BACK_SPACE:
                case VK_DELETE:
                case VK_ESCAPE:
                case VK_SPACE:
                case VK_TAB:
                case VK_UP:
                case VK_DOWN:
                case VK_LEFT:
                case VK_RIGHT:
                case VK_UNDO:
                case VK_AGAIN:
                case VK_HOME:
                case VK_END:
                case VK_PAGE_UP:
                case VK_PAGE_DOWN:
                case VK_HELP:
                case VK_PRINTSCREEN:
                case VK_F1:
                case VK_F2:
                case VK_F3:
                case VK_F4:
                case VK_F5:
                case VK_F6:
                case VK_F7:
                case VK_F8:
                case VK_F9:
                case VK_F10:
                case VK_F11:
                case VK_F12:
                    robot.keyPress(ch);
                    robot.keyRelease(ch);
                    return;
            }

            int upperCase = Character.toUpperCase(ch);
            switch (upperCase) {
                case VK_A:
                case VK_X:
                case VK_C:
                case VK_V:
                case VK_Z:
                case VK_Y:
                    robot.keyPress(controlKeyEvent);
                    robot.keyPress(upperCase);
                    robot.keyRelease(upperCase);
                    robot.keyRelease(controlKeyEvent);
                    return;
            }

        }

        int keyCode = getExtendedKeyCodeForChar(ch);
        if (Character.isWhitespace(ch)) {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            return;
        }
        if (VK_UNDEFINED == keyCode || keyCode > 10000) {
            clipboardTransfer(robot, ch);
            return;
        }

        boolean isUpperCase = Character.isUpperCase(ch);

        if (isUpperCase) {
            robot.keyPress(VK_SHIFT);
        }
        try {
          robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        } catch (IllegalArgumentException e) {
          if (isUpperCase) {
                robot.keyRelease(VK_SHIFT);
            }
             clipboardTransfer(robot, ch);
            return;

        }
        if (isUpperCase) {
            robot.keyRelease(VK_SHIFT);
        }

    }

    /**
     * does not work for extended codepage signs
     */

    private void clipboardTransfer(Robot robot, char ch) throws IOException, UnsupportedFlavorException {
        try {
        	System.out.println(ch);
            StringSelection stringSelection = new StringSelection(Character.toString(ch));
            java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            Object recover = clpbrd.isDataFlavorAvailable(DataFlavor.stringFlavor) ? clpbrd.getData(DataFlavor.stringFlavor) : null;
            clpbrd.setContents(stringSelection, null);

            robot.keyPress(controlKeyEvent);
            robot.keyPress(VK_V);
            robot.keyRelease(VK_V);
            robot.keyRelease(controlKeyEvent);
            robot.delay(50);

            StringSelection recoverSelection = new StringSelection(recover != null ? recover.toString() : "");
            clpbrd.setContents(recoverSelection, null);
        } catch (Exception e) {
            e.printStackTrace();
            return;

        }
    }

}
