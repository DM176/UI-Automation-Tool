package com.virtualkeyboard.client.comparing.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualkeyboard.client.models.EventProcess;
import com.virtualkeyboard.client.util.Constant;
import net.sourceforge.tess4j.*;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.virtualkeyboard.client.util.Util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import nu.pattern.OpenCV;

import javax.imageio.ImageIO;

/**
 * @author Hassam
 */
public class ImageDetection {

    /**
     * @return
     * @throws IOException
     */
    public static WritableImage imageDetectionFromScreenShot() throws IOException {
        OpenCV.loadLocally();
        // Mat inputImage = Util.bufferedImageToMat(screenShot);
        // Imgproc.resize(inputImage, inputImage, new Size(1920, 1080));
        System.out.println(Constant.path + "Full.png");
        Mat inputImage = Imgcodecs.imread("D:\\Upwork\\Intellij Projects\\ScriptedVirtualKeyboard\\EventFlow\\full.png");
        Mat icon = Imgcodecs.imread("D:\\Upwork\\Intellij Projects\\ScriptedVirtualKeyboard\\EventFlow\\reference.png");

        Mat result = new Mat();
        Imgproc.matchTemplate(inputImage, icon, result, Imgproc.TM_CCOEFF_NORMED);


        // Set a threshold for the correlation value to determine if the icon is present

        Mat mask = new Mat();
        double threshold = 0.8;
        Core.compare(result, new Scalar(threshold), mask, Core.CMP_GE);
        // Find the location of the icon in the input image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Loop through the contours and draw a rectangle around each one
        List<Point> points = new ArrayList<>();
        System.out.println(contours.size());
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));

            Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
            double x = (rect.x + p.x) / 2;
            double y = (rect.y + p.y) / 2;
            System.out.println("Point added");
            // points.add(Util.getScaledPoint(new Point(x, y)));

            Imgproc.rectangle(inputImage, rect.tl(), p, new Scalar(0, 0, 255), 2);
        }


        return Util.Mat2WritableImage1(inputImage);


    }


    public static Point returnLocationOfIconInScreenShot(BufferedImage screenShot, EventProcess eventProcess) throws IOException {

        // loading a library of open cv

        OpenCV.loadLocally();
        Point point = null;
        List<Point> referencePoints = null;
        OpenCV.loadLocally();
        double[] threshold = {0.8, 0.81, 0.85, 0.9, 1.0};
        if (eventProcess.getReferenceImage() != null) {
            referencePoints = iconlocation(screenShot, eventProcess.getReferenceImage(), 0.8);
            System.out.println("Outside " + referencePoints.size());
        }
        List<Point> firstPoints = null;
        List<Point> previousPoints = null;
        if (eventProcess.getReferenceAlign()==null || eventProcess.getReferenceAlign().isEmpty()) {
            eventProcess.setReferenceAlign(Constant.BOTH);
        }
        for (int i = 0; i < threshold.length; i++) {

            previousPoints = firstPoints;

            firstPoints = findLocationsOfIconInScreenShotWithAxis(screenShot, eventProcess.getIconImage(), threshold[i], referencePoints, eventProcess.getReferenceAlign());
            System.out.println( "Count of Icon " + firstPoints.size());
            if (firstPoints == null) {
                return null;
            } else if (!firstPoints.isEmpty() && firstPoints.size() == 1) {
                return firstPoints.get(0);
            }
        }

        if (!previousPoints.isEmpty() && previousPoints.size() > 0) {
            return previousPoints.get(0);
        } else {
            return null;
        }

    }

    public static int getCountofIconinImage(BufferedImage screenShot, EventProcess eventProcess) throws IOException {

        // loading a library of open cv

        OpenCV.loadLocally();
        Point point = null;
        List<Point> referencePoints = null;

        OpenCV.loadLocally();
        double[] threshold = {0.8};

        if (eventProcess.getReferenceImage() != null) {
            referencePoints = iconlocation(screenShot, eventProcess.getReferenceImage(), 0.8);
            System.out.println("Outside " + referencePoints.size());
            if (eventProcess.getReferenceAlign().isEmpty()) {
                eventProcess.setReferenceAlign("both");
            }
        }
        List<Point> firstPoints = null;
        System.out.println(eventProcess.getReferenceAlign());

        for (int i = 0; i < threshold.length; i++) {
            //   firstPoints = findLocationsOfIconInScreenShotWithXaxis(screenShot, eventProcess.getIconImage(), threshold[i],referencePoints,eventProcess.getReferenceAlign());
            firstPoints = findLocationsOfIconInScreenShotWithAxis(screenShot, eventProcess.getIconImage(), threshold[i], referencePoints, eventProcess.getReferenceAlign());
        }

        return firstPoints != null && !firstPoints.isEmpty() ? firstPoints.size() : 0;

    }

    /**
     * @param screenShot
     * @param image
     * @param threshold
     * @return
     * @throws IOException Will check if need to remove
     */
    public static List<Point> findLocationsOfIconInScreenShot(BufferedImage screenShot, Image image, double threshold, List<Point> referencePoints) throws IOException {
        List<Point> points = new ArrayList<>();
        Mat inputImage = Util.bufferedImageToMat(screenShot);

        Imgproc.resize(inputImage, inputImage, new Size(Constant.RESOLUTION_WIDTH, Constant.RESOLUTION_HEIGHT));

        //Mat icon = Imgcodecs.imread(image);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        Mat icon = Util.bufferedImageToMat(bufferedImage);


        Mat result = new Mat();
        Imgproc.matchTemplate(inputImage, icon, result, Imgproc.TM_CCOEFF_NORMED);


        // Set a threshold for the correlation value to determine if the icon is present

        Mat mask = new Mat();

        Core.compare(result, new Scalar(threshold), mask, Core.CMP_GE);
        // Find the location of the icon in the input image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Loop through the contours and draw a rectangle around each one
        if (referencePoints != null && !referencePoints.isEmpty()) {
            System.out.println("Reference " + referencePoints.size());
            double minDistance = Double.MAX_VALUE;
            Point nearestIcon = null;
            for (int i = 0; i < contours.size(); i++) {
                Rect rect = Imgproc.boundingRect(contours.get(i));
                Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
                double x = (rect.x + p.x) / 2;
                double y = (rect.y + p.y) / 2;
                Point point = Util.getScaledPoint(new Point(x, y));
                for (Point referencePoint : referencePoints) {
                    double distance = Math.sqrt(Math.pow(point.x - referencePoint.x, 2) + Math.pow(point.y - referencePoint.y, 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestIcon = point;
                    }
                }


            }
            if (nearestIcon != null) {
                points.add(nearestIcon);
                return points;
            } else {
                return null;
            }
        } else {
            for (int i = 0; i < contours.size(); i++) {
                Rect rect = Imgproc.boundingRect(contours.get(i));

                Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
                double x = (rect.x + p.x) / 2;
                double y = (rect.y + p.y) / 2;

                points.add(Util.getScaledPoint(new Point(x, y)));


                //	Imgproc.rectangle(inputImage, rect.tl(), p, new Scalar(0, 0, 255), 2);
            }
            return points;
        }

    }


    /**
     * @param image Use to convert image to Matrix
     * @return Mat
     */
    public static Mat imageToMat(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buffer);
        return mat;
    }


    public static String textFromImage(BufferedImage image) {
        //File imageFile = new File("C:\\Users\\Hassam\\eclipse-workspace\\ScriptedVirtualKeyboard\\full.PNG");
        ITesseract instance = new Tesseract();
        try {
            instance.setDatapath(Constant.path);
//            instance.setPageSegMode(2);
          //  Mat imageMat =Util.bufferedImageToMat(image);



            String result = instance.doOCR(image);
            return result;
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
            return null;
            //  return "Error while reading image";
        }
    }

    public static WritableImage returnHighlightedOfContentInImage(String text, BufferedImage screenShot) throws Exception {
        OpenCV.loadLocally();
        String[] splitText = null;
        if (text.contains(" ")) {
            splitText = text.split(" ");
        } else if (text.contains("/")) {
            splitText = text.split("/");
        } else {
            splitText = new String[1];
            splitText[0] = text;
        }
        int incr = 0;
        ITesseract instance = new Tesseract();
        instance.setDatapath(Constant.path);
        List<Word> words = instance.getWords(screenShot, 3);
        System.out.println(words.size());
        List<Rectangle> rectangles = new ArrayList<>();
        Boolean isConsistent = false;
        for (Word word : words) {
            System.out.println("-outer " + word.getText() + word.getText().length() + ": search " + splitText[incr] + splitText[incr].length());
            if (splitText[incr].equals(word.getText().trim())) {
                isConsistent = true;
                rectangles.add(word.getBoundingBox());
                System.out.println(word.getText());
                incr++;
                if (incr == splitText.length) {
                    break;
                }
            } else {
                if (isConsistent) {
                    incr = 0;
                    rectangles.clear();
                }
                isConsistent = false;
            }
        }
        if (rectangles.isEmpty()) {
            return null;
        }
        System.out.println(rectangles.size());
        Image screenShotImage = SwingFXUtils.toFXImage(screenShot, null);
        Mat src = imageToMat(screenShotImage);
        Point p1 = new Point(rectangles.get(0).x, rectangles.get(0).y);
        Point p2 = new Point(rectangles.get(rectangles.size() - 1).getMaxX(), rectangles.get(rectangles.size() - 1).getMaxY());
        Imgproc.rectangle(src, p1, p2, new Scalar(255, 0, 0), 6);
        return Util.Mat2WritableImage1(src);
    }

    public static Point returnLocationOfContentInImage(String text, BufferedImage screenShot) throws Exception {
        OpenCV.loadLocally();
        String[] splitText = null;
        if (text.contains(" ")) {
            splitText = text.split(" ");
        } else {
            splitText = new String[]{text.toLowerCase()};
        }
        int incr = 0;
        Rectangle rectangle = null;
        ITesseract instance = new Tesseract();
        instance.setDatapath(Constant.path);




        List<Word> words = instance.getWords(screenShot,  ITessAPI.TessPageIteratorLevel.RIL_WORD);
/**
 * end here
 */

      //  List<Word> words = instance.getWords(screenShot, 3);
        System.out.println("Enhance image  " +words.size());
        List<Rectangle> rectangles = new ArrayList<>();
        Boolean isConsistent = false;
        for (Word word : words) {

            System.out.println(word.getConfidence() + "ScreenShot word :  " + word.getText().replaceAll("[^a-zA-Z0-9 ]", "") + "  --  Search word : " + splitText[incr].replaceAll("[^a-zA-Z0-9 ]", "") + "  = " + isOneCharMissing(Util.rmSpecialChr(splitText[incr]), Util.rmSpecialChr(word.getText())));
            if (Util.rmSpecialChr(splitText[incr].toLowerCase()).equals(Util.rmSpecialChr(word.getText().toLowerCase().trim()))
                    || isOneCharMissing(Util.rmSpecialChr(splitText[incr]), Util.rmSpecialChr(word.getText()))) {
                isConsistent = true;
                rectangle = word.getBoundingBox();
                rectangles.add(word.getBoundingBox());
                System.out.println("Select  " + word.getText());
                System.out.println(splitText[incr]);

                incr++;
                if (incr == splitText.length) {
                    System.out.println("Break ");
                    break;
                }
            } else {
                if (isConsistent) {
                    incr = 0;
                    rectangles.clear();
                }
                isConsistent = false;
            }

        }
        if (rectangles.isEmpty()) {
            return null;
        }
        Point p1 = new Point(rectangles.get(0).x, rectangles.get(0).y);
        Point p2 = new Point(rectangles.get(rectangles.size() - 1).getMaxX(), rectangles.get(rectangles.size() - 1).getMaxY());
        double x = (p1.x + p2.x) / 2;
        double y = (p1.y + p2.y) / 2;
        return new Point(x, y);
    }


    public static Point returnLocationOfContentInImageWith2(String text, BufferedImage screenShot) throws Exception {
        OpenCV.loadLocally();
        int incr = 0;
        Rectangle rectangle = null;
        ITesseract instance = new Tesseract();
        instance.setDatapath(Constant.path);

        List<Word> words = instance.getWords(screenShot, 2);
        System.out.println(words.size());

        for (Word word : words) {
            System.out.println("ScreenShot word :  " + word.getText().trim().replaceAll("[^a-zA-Z0-9 ]", "") + "  --  Search word : " + text.trim().replaceAll("[^a-zA-Z0-9 ]", ""));
            if (Util.rmSpecialChr(word.getText().trim()).contains(Util.rmSpecialChr(text.trim()))) {
                rectangle = word.getBoundingBox();

                System.out.println(word.getText());
                break;
            }
        }
        if (rectangle == null) {
            return null;
        }


        return new Point(rectangle.getCenterX(), rectangle.getCenterY());
    }


    public static WritableImage locate() throws IOException {
        OpenCV.loadLocally();
        Mat inputImage = Imgcodecs.imread(Constant.path + "Full.png");
        Mat icon = Imgcodecs.imread(Constant.path + "eventflow.png");

        //Imgproc.resize(inputImage, inputImage, new Size(1920, 1080));
// Match the icon to the input image using normalized cross-correlation
        Mat result = new Mat();
        Imgproc.matchTemplate(inputImage, icon, result, Imgproc.TM_CCOEFF_NORMED);

// Set a threshold for the correlation value to determine if the icon is present
        double threshold = 1;
        Mat mask = new Mat();
        Core.compare(result, new Scalar(threshold), mask, Core.CMP_GE);
        System.out.println("--" + result.rows());
// Find the location of the icon in the input image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println(contours.size());
// Loop through the contours and draw a rectangle around each one
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));

            System.out.println(rect.area());
            System.out.println(new ObjectMapper().writeValueAsString(rect.tl()));
            System.out.println(new ObjectMapper().writeValueAsString(rect.br()));
            System.out.println("--------------");
            //Imgproc.rectangle(inputImage, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
            //
            Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));

            Imgproc.rectangle(inputImage, rect.tl(), p, new Scalar(0, 0, 255), 2);
        }
        return Util.Mat2WritableImage1(inputImage);
    }

    public static boolean isOneCharMissing(String inputStr, String imageStr) {
        // If the strings are the same length, they can't differ by more than one character
        if (inputStr.length() < imageStr.length() || inputStr.length() - imageStr.length() > 1) {

            return false;
        }
        try {
            int diffCount = 0;
            int imgStrIndex = 0;
            for (int i = 0; i < inputStr.length() && imgStrIndex < imageStr.length(); i++) {
                if (inputStr.charAt(i) != imageStr.charAt(imgStrIndex)) {
                    diffCount++;
                    if (diffCount > 1) {
                        return false; // strings differ by more than two characters
                    }
                } else {
                    imgStrIndex++;
                }
            }
        } catch (Exception ex) {
            System.out.println(inputStr + " , " + imageStr);
            throw ex;
        }

        System.out.println("Return true from one char missing");
        // If we reach the end of the loop without finding a mismatch, str1 is one character longer than str2
        return true;
    }


    public static List<Point> iconlocation(BufferedImage screenShot, Image image, double threshold) throws IOException {
        OpenCV.loadLocally();
        Mat inputImage = Util.bufferedImageToMat(screenShot);
        Imgproc.resize(inputImage, inputImage, new Size(Constant.RESOLUTION_WIDTH, Constant.RESOLUTION_HEIGHT));
        System.out.println(Constant.path + "Full.png");
        //Mat inputImage = Imgcodecs.imread("D:\\Upwork\\Intellij Projects\\ScriptedVirtualKeyboard\\EventFlow\\full.png");
        //Mat icon = Imgcodecs.imread("D:\\Upwork\\Intellij Projects\\ScriptedVirtualKeyboard\\EventFlow\\reference.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
//        // Load the icon image
        Mat icon = Util.bufferedImageToMat(bufferedImage);
        Mat result = new Mat();
        Imgproc.matchTemplate(inputImage, icon, result, Imgproc.TM_CCOEFF_NORMED);


        // Set a threshold for the correlation value to determine if the icon is present

        Mat mask = new Mat();

        Core.compare(result, new Scalar(threshold), mask, Core.CMP_GE);
        // Find the location of the icon in the input image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Loop through the contours and draw a rectangle around each one
        List<Point> points = new ArrayList<>();
        System.out.println(contours.size());
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));

            Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
            double x = (rect.x + p.x) / 2;
            double y = (rect.y + p.y) / 2;
            System.out.println("Point added");
            points.add(Util.getScaledPoint(new Point(x, y)));

            //  Imgproc.rectangle(inputImage, rect.tl(), p, new Scalar(0, 0, 255), 2);
        }

        return points;

        // return Util.Mat2WritableImage1(inputImage);
    }


    public static List<Point> findLocationsOfIconInScreenShotWithAxis(BufferedImage screenShot, Image image, double threshold, List<Point> referencePoints, String referenceAlig) throws IOException {
        List<Point> points = new ArrayList<>();
        Mat inputImage = Util.bufferedImageToMat(screenShot);

        Imgproc.resize(inputImage, inputImage, new Size(Constant.RESOLUTION_WIDTH, Constant.RESOLUTION_HEIGHT));

        //Mat icon = Imgcodecs.imread(image);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        Mat icon = Util.bufferedImageToMat(bufferedImage);


        Mat result = new Mat();
        Imgproc.matchTemplate(inputImage, icon, result, Imgproc.TM_CCOEFF_NORMED);


        // Set a threshold for the correlation value to determine if the icon is present

        Mat mask = new Mat();

        Core.compare(result, new Scalar(threshold), mask, Core.CMP_GE);
        // Find the location of the icon in the input image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Loop through the contours and draw a rectangle around each one
        if (referencePoints != null && !referencePoints.isEmpty()) {

            System.out.println("Reference " + referencePoints.size() + " alig " + referenceAlig);
            if (referenceAlig.equals("both")) {
                double minDistance = Double.MAX_VALUE;
                Point nearestIcon = null;
                for (int i = 0; i < contours.size(); i++) {
                    Rect rect = Imgproc.boundingRect(contours.get(i));
                    Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
                    double x = (rect.x + p.x) / 2;
                    double y = (rect.y + p.y) / 2;
                    Point point = Util.getScaledPoint(new Point(x, y));
                    for (Point referencePoint : referencePoints) {
                        double distance = Math.sqrt(Math.pow(point.x - referencePoint.x, 2) + Math.pow(point.y - referencePoint.y, 2));


                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestIcon = point;
                        }
                    }


                }
                if (nearestIcon != null) {
                    points.add(nearestIcon);
                    return points;
                } else {
                    return null;
                }
            } else if (referenceAlig.equals(Constant.ROW)) {
                double minDistance = Double.MAX_VALUE;
                Point nearestIcon = null;
                for (int i = 0; i < contours.size(); i++) {
                    Rect rect = Imgproc.boundingRect(contours.get(i));
                    Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
                    double x = (rect.x + p.x) / 2;
                    double y = (rect.y + p.y) / 2;
                    Point point = Util.getScaledPoint(new Point(x, y));
                    for (Point referencePoint : referencePoints) {
                        double verticalDistance = Math.abs(point.y - referencePoint.y);

                        // Define a threshold for parallel detection
                        double parallelThreshold = 10; // Adjust this value as needed
                        System.out.println("Vertical distance " + verticalDistance);
                        // Check if the detected point is parallel to the reference point
                        if (verticalDistance <= parallelThreshold) {
                            System.out.println("Vertical distance: " + verticalDistance + "= First one :(" + point.x + "," + point.y + ") . and refrenece " + "(" + referencePoint.x + "," + referencePoint.y + ")");
                            if (verticalDistance < minDistance) {
                                minDistance = verticalDistance;
                                nearestIcon = point;
                            }
                        }
                    }


                }
                if (nearestIcon != null) {
                    points.add(nearestIcon);
                    return points;
                } else {
                    return null;
                }

            } else if (referenceAlig.equals(Constant.COL)) {
                double minDistance = Double.MAX_VALUE;
                Point nearestIcon = null;
                for (int i = 0; i < contours.size(); i++) {
                    Rect rect = Imgproc.boundingRect(contours.get(i));
                    Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
                    double x = (rect.x + p.x) / 2;
                    double y = (rect.y + p.y) / 2;
                    Point point = Util.getScaledPoint(new Point(x, y));
                    for (Point referencePoint : referencePoints) {
                        double verticalDistance = Math.abs(point.x - referencePoint.x);
                        // Define a threshold for parallel detection
                        double parallelThreshold = 10; // Adjust this value as needed
                        // Check if the detected point is parallel to the reference point
                        if (verticalDistance <= parallelThreshold) {
                            System.out.println("Vertical distance: " + verticalDistance + "= First one :(" + point.x + "," + point.y + ") . and refrenece " + "(" + referencePoint.x + "," + referencePoint.y + ")");
                            if (verticalDistance < minDistance) {
                                minDistance = verticalDistance;
                                nearestIcon = point;
                            }
                        }
                    }


                }
                if (nearestIcon != null) {
                    points.add(nearestIcon);
                    return points;
                } else {
                    return null;
                }

            }

        } else {


            for (int i = 0; i < contours.size(); i++) {
                Rect rect = Imgproc.boundingRect(contours.get(i));

                Point p = new Point((rect.x + icon.cols()), (rect.y + icon.rows()));
                double x = (rect.x + p.x) / 2;
                double y = (rect.y + p.y) / 2;

                points.add(Util.getScaledPoint(new Point(x, y)));

                //	Imgproc.rectangle(inputImage, rect.tl(), p, new Scalar(0, 0, 255), 2);
            }
            return points;
        }
        return null;
    }


}
