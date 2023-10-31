package com.virtualkeyboard.client.process;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.List;


import com.virtualkeyboard.client.models.CustomException;
import com.virtualkeyboard.client.models.Response;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Point;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualkeyboard.client.comparing.image.ImageDetection;
import com.virtualkeyboard.client.models.EventProcess;
import com.virtualkeyboard.client.mouse.movement.MouseControl;
import com.virtualkeyboard.client.onscreenkeyboard.InputText;
import com.virtualkeyboard.client.screenshot.ScreenShot;
import com.virtualkeyboard.client.util.Constant;
import com.virtualkeyboard.client.util.Util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 * @author Hassam
 */
public class ProcessExecution {


    /**
     * implementation for executing the process with the help of implemented
     * components.
     *
     * @return
     * @throws IOException
     * @throws AWTException
     * @throws InterruptedException
     */
    public List<EventProcess> executeProcess( List<EventProcess> eventProcesses) throws Exception {
        String response="";
        ExcelProcessService executionProcess = new ExcelProcessService();
        boolean toSkipped=false;
        for (EventProcess enEventProcess : eventProcesses) {

            if (enEventProcess.getActive() && (!toSkipped || enEventProcess.getEventsHead() )) {
                toSkipped=false;
                response=eventExcution(enEventProcess);
                if(response.equals(Constant.Status.FAIL)){
                    enEventProcess.setStatus(Constant.Status.FAIL);
                    if(enEventProcess.getMandatory()){
                        toSkipped=true;
                        // break;
                    }
                }else{
                    enEventProcess.setStatus(Constant.Status.SUCCESS);
                }
                if (enEventProcess.getWaitTime() != null)
                    Thread.sleep(enEventProcess.getWaitTime().longValue());
            }else if(enEventProcess.getActive()){
                enEventProcess.setStatus(Constant.Status.FAIL);
            }
        }
    return eventProcesses;
    }

    /**
     * implementation for executing the process with the help of implemented
     * components.
     *
     * @throws Exception
     */
    public Response executeChatProcess(String path, String sheet,Image image) throws Exception {
      Response response=new Response();
        try {
            ExcelProcessService executionProcess = new ExcelProcessService();
            List<EventProcess> eventProcesses = executionProcess.readProcessFlow(path, sheet);
            //Point point=getPointForClick("chatsearch.png");
            Point point = ImageDetection.returnLocationOfContentInImage("searchin", ScreenShot.captureScreenShotofScreenSize());
            Point pointForContent=null;
            if (point == null) {
                response.setMessage("Unable to find search text field for email insertion");
                response.setStatus(Constant.Status.FAIL);
                return response;
            }
            String strReponse="";
            MouseControl.singleClick((int) point.x, (int) point.y);
            Boolean isContentTextPoint=false;
            for (EventProcess eventProcess : eventProcesses) {
                eventExcution(new EventProcess(Constant.Events.INSERT_TEXT, eventProcess.getEmail().trim(), true, null, null, null));
                if (!confirmTextExist("matching people or spaces",false)) {
                    MouseControl.singleClick((int) point.x, (int) point.y + 50);
                    MouseControl.moveCursor(0,0);
                    if(!isContentTextPoint) {
                        Util.definedSleep(3000);
                        EventProcess eventProcess1=new EventProcess();
                        eventProcess1.setIconImage(image);
                        pointForContent = ImageDetection.returnLocationOfIconInScreenShot(ScreenShot.captureScreenShotofScreenSize(),eventProcess1);
                        if(pointForContent==null){
                            //pointForContent = ImageDetection.returnLocationOfContentInImage("History is on", ScreenShot.captureScreenShotofScreenSize());
                                response.setStatus(Constant.Status.FAIL);
                                response.setMessage("Unable to find HISTORY IS ON in Screen shot");
                                return response;

                        }
                        System.out.println("X " + pointForContent.x + "Y "+ pointForContent.y);
                        MouseControl.singleClick((int) pointForContent.x, (int) pointForContent.y);
                        InputText.removeAllContent();
                        isContentTextPoint=true;
                    }else{
                        Util.definedSleep(200);
                        MouseControl.singleClick((int) pointForContent.x, (int) pointForContent.y);
                        InputText.removeAllContent();
                    }
                    strReponse=  eventExcution(new EventProcess(Constant.Events.INSERT_TEXT, eventProcess.getText(), true, null, null, null));

                 if(strReponse.equals(Constant.Status.FAIL)){


                     eventProcess.setStatus("Message Failed");
                 } else{
                    InputText.insertCommand('\n');
                    eventProcess.setStatus("Message Sent");
                 }
                } else {
                    eventProcess.setStatus("Email Not Found");
                }

                MouseControl.singleClick((int) point.x, (int) point.y);
                InputText.removeAllContent();
                Util.postSleep();

            }
            executionProcess.updateResponseInExcel(eventProcesses, path, sheet);
            response.setStatus(Constant.Status.SUCCESS);
            response.setMessage("Process Completed Successfully");
            return  response;

        }catch (Exception ex){

            ex.printStackTrace();
            throw ex;


        }

    }

    /**
     * implementation for executing the process with the help of implemented
     * components.
     *
     * @throws Exception
     */
    public Response executeTranscriptProcess() throws Exception {
        Response response=new Response();
        try {
            response.setStatus(Constant.Status.SUCCESS);
            response.setMessage("Process Completed Successfully");
            return  response;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }

    }

    /**
     * event code
     * @param eventProcess
     * @return
     * @throws Exception
     */
    private String eventExcution(EventProcess eventProcess) throws Exception {

        if (eventProcess.getEvent().equals(Constant.Events.SINGLE_CLICK)) {
            Point point=null;
            if(eventProcess.getIconImage()!=null)
                point = getPointForClick(eventProcess);
            if(point==null && !Util.returnEmptyIfNull(eventProcess.getTargetText()).isEmpty())
                point=ImageDetection.returnLocationOfContentInImage(eventProcess.getTargetText(),ScreenShot.captureScreenShotofScreenSize());
            if (point == null) {
                return Constant.Status.FAIL;
            }
        MouseControl.singleClick((int) point.x, (int) point.y);
            return Constant.Status.SUCCESS;
        }
        if (eventProcess.getEvent().equals(Constant.Events.VERIFICATION)) {
            Point point=null;
            if(eventProcess.getIconImage()!=null)
                point = getPointForClick(eventProcess);
            if(point==null && !Util.returnEmptyIfNull(eventProcess.getTargetText()).isEmpty())
                point=ImageDetection.returnLocationOfContentInImage(eventProcess.getTargetText(),ScreenShot.captureScreenShotofScreenSize());
            if (point == null) {
                return Constant.Status.FAIL;
            }
            //MouseControl.singleClick((int) point.x, (int) point.y);
            return Constant.Status.SUCCESS;
        }
        if (eventProcess.getEvent().equals(Constant.Events.COUNT)) {
            System.out.println(conditionEvaluater(eventProcess));

            eventProcess.setText(conditionEvaluater(eventProcess));

            return Constant.Status.SUCCESS;
        }
        else if (eventProcess.getEvent().equals(Constant.Events.RESUME_AFTER)) {

            if(eventProcess.getIconImage()!=null)
                return ifDisappear(eventProcess);

             return Constant.Status.SUCCESS;
        }
        else if (eventProcess.getEvent().equals(Constant.Events.INSERT_TEXT)) {
            InputText.insertText(eventProcess.getText());
            return Constant.Status.SUCCESS;
//            if(!confirmTextExist(eventProcess.getText(),true)){
//
//                return Constant.Status.FAIL;
//            }
        } else if (eventProcess.getEvent().equals(Constant.Events.ENTER)) {
            InputText.insertCommand('\n');
            return Constant.Status.SUCCESS;
        } else if (eventProcess.getEvent().equals(Constant.Events.SCROLL)) {
            MouseControl.scrollWheel(100);
            return Constant.Status.SUCCESS;
        }
        else if (eventProcess.getEvent().equals(Constant.Events.SCREENSHOT)) {
           BufferedImage bufferedImage= ScreenShot.captureScreenShotofScreenSize();
          eventProcess.setIconImage(SwingFXUtils.toFXImage(bufferedImage, null));
            return Constant.Status.SUCCESS;
        }
        else if (eventProcess.getEvent().equals(Constant.Events.DOUBLE_CLICK)) {
            Point point=null;
            if(eventProcess.getIconImage()!=null)
                point = getPointForClick(eventProcess);
            if(point==null && !Util.returnEmptyIfNull(eventProcess.getTargetText()).isEmpty())
                point=ImageDetection.returnLocationOfContentInImage(eventProcess.getTargetText(),ScreenShot.captureScreenShotofScreenSize());
            if (point == null) {
                return Constant.Status.FAIL;
            }
            MouseControl.doubleClick((int) point.x, (int) point.y);
            return Constant.Status.SUCCESS;
        } else if (eventProcess.getEvent().equals(Constant.Events.MINIMIZE_ALL_TABS)) {
            MouseControl.singleClick((int) Toolkit.getDefaultToolkit().getScreenSize().width, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        }else if (eventProcess.getEvent().equals(Constant.Events.RIGHT_CLICK)) {
            Point point=null;
            if(eventProcess.getIconImage()!=null)
                point = getPointForClick(eventProcess);
            if(point==null && !Util.returnEmptyIfNull(eventProcess.getTargetText()).isEmpty())
                point=ImageDetection.returnLocationOfContentInImage(eventProcess.getTargetText(),ScreenShot.captureScreenShotofScreenSize());
            if (point == null) {
                return Constant.Status.FAIL;
            }
            MouseControl.rightClick((int)point.x,(int)point.y);
            return Constant.Status.SUCCESS;
        }else if (eventProcess.getEvent().equals(Constant.Events.OPEN_FILE)) {

            Thread thread = new Thread(() -> {
                try {
                    openFile(eventProcess.getText());
                } catch (Exception e) {
                    // Handle any exceptions thrown by openFile()
                    e.printStackTrace();
                }
            });
            thread.start();

            try {
                thread.join();  // Wait for the thread to finish
                System.out.println("Thread has finished executing.");
            } catch (InterruptedException e) {
                // Handle any InterruptedException that may occur
                e.printStackTrace();
            }
            return Constant.Status.SUCCESS;


        }

        return Constant.Status.SUCCESS;
    }

    /**
     * check if text in the image or not
     * @param textToSearch
     * @param isWordBasedSearch
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private Boolean confirmTextExist(String textToSearch, Boolean isWordBasedSearch) throws IOException, InterruptedException {
        int i = 1;
        while (i < 3) {

            String screenShottext = ImageDetection.textFromImage(ScreenShot.captureScreenShotofScreenSize());
            System.out.println("--- ScreenShot Text " + screenShottext);
            System.out.println("--- Inputy Text " + textToSearch);
            // System.out.println(screenShottext);
            if (!isWordBasedSearch && Util.textExistInImageText(screenShottext, textToSearch)) {
                System.out.println("Content  exist " + i);
               return true;
            }
            if (isWordBasedSearch && Util.anyWordExistInImageText(screenShottext, textToSearch)) {
                System.out.println("Content  exist " + i);
                return true;
            }

            else {
                i++;
                Util.definedSleep(2000);
                System.out.println("Content Doesnt exist " + i );
                //return false;
            }

        }
        return false;
    }

    /**
     * get location where to click with help of image
     * @param eventProcess
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws AWTException
     */
    private Point getPointForClick(EventProcess eventProcess) throws IOException, InterruptedException, AWTException {

        MouseControl.moveCursor(0,0);
        Point point = ImageDetection.returnLocationOfIconInScreenShot(ScreenShot.captureScreenShotofScreenSize(),
                 eventProcess);
        int i = 0;
        while (point == null && i < 3) {
            Thread.sleep(Constant.RETRY);
            point = ImageDetection.returnLocationOfIconInScreenShot(ScreenShot.captureScreenShotofScreenSize(),
                     eventProcess);
            if (point != null) {
                break;
            }
            i++;
        }

        return point;

    }

    /**
     * if any icon disappear so it notify back
     * @param eventProcess
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws AWTException
     */
    private String ifDisappear(EventProcess eventProcess) throws IOException, InterruptedException, AWTException {
        MouseControl.moveCursor(0,0);
        Util.definedSleep(2000);
        Point point = ImageDetection.returnLocationOfIconInScreenShot(ScreenShot.captureScreenShotofScreenSize(),
                eventProcess);
        if(point==null) return Constant.Status.SUCCESS;
        int i=0;
        int sleep=2500;
        while (point != null && i < 20) {
            point = ImageDetection.returnLocationOfIconInScreenShot(ScreenShot.captureScreenShotofScreenSize(),
                    eventProcess);
            if (point != null) {
                Thread.sleep(sleep);
                if(i<4) sleep=2*sleep;
            }else{
                return Constant.Status.SUCCESS;
            }
            i++;
        }
        return Constant.Status.FAIL;
    }


    /**
     * open the file directly by providing the path
     * @param path
     * @throws Exception
     */
    public void openFile(String path) throws Exception {
        File folder = new File(path);

        // check if Desktop is supported on the current platform
        if (Desktop.isDesktopSupported()) {
            // get the Desktop instance
            Desktop desktop = Desktop.getDesktop();

            // check if the folder exists
            if (folder.exists()) {
                // open the file explorer and navigate to the folder


                desktop.open(folder);

            } else {
                // handle the case where the folder does not exist

                CustomException customException=new CustomException();
                customException.setMessage("Path mentioned doesnt exit");

                throw customException;
            }
        } else {
            // handle the case where Desktop is not supported

            CustomException customException=new CustomException();
            customException.setMessage("Desktop is not supported on this platform!");
            throw customException;
        }

    }

    /**
     * count of icon exist in image
     * @param eventProcess
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    private String conditionEvaluater(EventProcess eventProcess) throws ScriptException, IOException {
       int count= ImageDetection.getCountofIconinImage(ScreenShot.captureScreenShotofScreenSize(),eventProcess);

        // Update the result output field
       return String.valueOf(count);


    }
}
