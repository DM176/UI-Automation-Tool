package com.virtualkeyboard.client.util;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import com.virtualkeyboard.client.models.Response;
import com.virtualkeyboard.client.view.LandingPageController;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.opencv.core.CvType;
import org.opencv.core.Point;
import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

import org.opencv.imgcodecs.Imgcodecs;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class Util {


	public static void postSleep() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void definedSleep(long value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static WritableImage Mat2WritableImage1(Mat mat) throws IOException{
		//Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", mat, matOfByte);
		//Storing the encoded Mat in a byte array
		byte[] byteArray = matOfByte.toArray();
		//Preparing the Buffered Image
		ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);
		System.out.println("Image Loaded");
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
		return writableImage;
	}
	 public static WritableImage Mat2WritableImage(Mat m) {
		    // Fastest code
		    // output can be assigned either to a BufferedImage or to an Image

		    int type = BufferedImage.TYPE_BYTE_GRAY;
		    if ( m.channels() > 1 ) {

		        type = BufferedImage.TYPE_3BYTE_BGR;
		    }
		    int bufferSize = m.channels()*m.cols()*m.rows();
		    byte [] b = new byte[bufferSize];
		    m.get(0,0,b); // get all the pixels
		    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		    System.arraycopy(b, 0, targetPixels, 0, b.length);
			WritableImage writableImage = SwingFXUtils.toFXImage(image, null);
		    return writableImage;
		}

	 //not used
	 public static BufferedImage Mat2BufferedImage(Mat mat) throws IOException{
	      //Encoding the image
	      MatOfByte matOfByte = new MatOfByte();
	      Imgcodecs.imencode(".png", mat, matOfByte);
	      //Storing the encoded Mat in a byte array
	      byte[] byteArray = matOfByte.toArray();
	      //Preparing the Buffered Image
	     ByteArrayInputStream in=new ByteArrayInputStream(byteArray);
	      // ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
	      BufferedImage bufImage = ImageIO.read(in);
	      return bufImage;
	   }

	 public static BufferedImage toBufferedImage(Mat m) {
		    if (!m.empty()) {
		        int type = BufferedImage.TYPE_BYTE_GRAY;
		        if (m.channels() > 1) {
		            type = BufferedImage.TYPE_3BYTE_BGR;
		        }
		        int bufferSize = m.channels() * m.cols() * m.rows();
		        byte[] b = new byte[bufferSize];
		        m.get(0, 0, b); // get all the pixels
		        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		        System.arraycopy(b, 0, targetPixels, 0, b.length);
		        return image;
		    }
		    System.out.println("bull");
		    return null;
		}


	 public static Boolean textExistInImageText(String imageText,String text) {


		 System.out.println("Text from Image : " +imageText.trim());
		 System.out.println("input text : " +text.trim());
		 if(imageText.trim().contains(text.trim())) {
				return true;
		 }else {
			 System.out.println("Not exist contain");
				return false;
			}


		}

	public static Boolean anyWordExistInImageText(String imageText,String text) {

		String[] split=null;
		boolean byWordSearch=false;
		if(text.contains(" ")){
			 split=text.split(" ");
			if(split.length>3){
				byWordSearch=true;
			}
		}if(text.contains("/")){
			split=text.split("/");
			if(split.length>3){
				byWordSearch=true;
			}
		}if(text.contains("\\\\")){
			split=text.split("\\\\");
			if(split.length>3){
				byWordSearch=true;
			}
		}
		if(byWordSearch) {
			int start=1;
			int end=split.length;
			Random random = new Random();
			int passed=0;
			for(int i=0; i<3; i++){
				int randomNumber=random.nextInt(end-start+1)+start;

				if(imageText.trim().contains(split[i])){
					passed++;
				}
				System.out.println("Randon number : " +randomNumber + " passed : " + passed + " text : " + split[i]);
			}
			if (passed>1){
				return true;
			}
			return  false;
		}else {
		System.out.println(text.length() + " - " + text.substring(0,text.length()/2)  );
			System.out.println(text.length() + " - " + text.substring(text.length()/2,text.length())  );
			if(imageText.trim().contains(text.trim())) {
			return true;
		}else if(text.length()>15 && (imageText.trim().contains(text.substring(0,text.length()/2))
				|| imageText.trim().contains(text.substring(text.length()/2,text.length())))
		){
				return true;
			}
		else {
			System.out.println("Not exist contain");
			return false;
		}
		}


	}

		public  static  String returnEmptyIfNull(String str){
			if(str==null)
				return "";
			return str;
		}

		public  static Point getScaledPoint(Point point){
			// Define the original screen resolution


			// Define the new screen resolution
//			int newWidth = 1280;
//			int newHeight = 720;
			int newWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			int newHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

			// Calculate the scaling factors
			double scaleX = (double) newWidth / Constant.RESOLUTION_WIDTH;
			double scaleY = (double) newHeight / Constant.RESOLUTION_HEIGHT;

			// Scale the point coordinates
			double scaledX = point.x * scaleX;
			double scaledY = point.y * scaleY;
			Point scaledPoint = new Point((int)scaledX, (int)scaledY);
			return scaledPoint;
		}

	public static Mat bufferedImageToMat(BufferedImage image) {
		BufferedImage convertedImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		convertedImg.getGraphics().drawImage(image, 0, 0, null);
		Mat mat = new Mat(convertedImg.getHeight(), convertedImg.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) convertedImg.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

	public static Alert showPopupMessage(String alertType,String title, String header, String context,Exception ex)
	{
		if(alertType.equals(Constant.Status.ERROR)){
			Alert alert=new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(context);
			return  alert;
		}
		else if(alertType.equals(Constant.Status.INFO)){
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(context);
			return  alert;
		}
		else if(alertType.equals(Constant.Status.EXC)){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(context);

		//	Exception ex = new FileNotFoundException("Could not find file blabla.txt");

// Create expandable Exception.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String exceptionText = sw.toString();

			Label label = new Label("The exception stacktrace was:");

			TextArea textArea = new TextArea(exceptionText);
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
			alert.getDialogPane().setExpandableContent(expContent);
			return  alert;
		}


		return null;


	}

	public static Response inputDialog(HashMap subEventHashmap){
		Response response=new Response();
		boolean isOpen = true;
		String message="Please enter your name:";
		String headerTest="";
		while (isOpen) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Please mention Sub Events");
			dialog.setHeaderText(headerTest);
			dialog.setContentText(message);

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String name = result.get().toLowerCase();
				if (!name.isEmpty()) {
					if(subEventHashmap.get(name)!=null){
						headerTest=name +" is already exist";
					}else{
					System.out.println("Your name: " + name);
					response.setStatus(Constant.Status.SUCCESS);
					response.setMessage(name);
						return response;
					}

				} else {
					headerTest=name +" mandatory";

				}
			} else {
				response.setStatus(Constant.Status.FAIL);
				return  response;
			}


		}

		response.setStatus(Constant.Status.FAIL);
		return  response;

	}


	public static void disableColumnsInTable(TableView eventTable,Boolean disableSelect){
		for (Object tableColumn : eventTable.getColumns()) {
			if (tableColumn instanceof TableColumn) {

				TableColumn<?, ?> column = (TableColumn<?, ?>) tableColumn;
				System.out.println("Instance of collumn " + column.getText());
				if(column.getText().equals("Select")){
					column.setEditable(disableSelect);
					column.setVisible(disableSelect);
					eventTable.refresh();
					System.out.println("entered");
				}

			}
		}
	}


	public static String rmSpecialChr(String str){
		return str.replaceAll("[^a-zA-Z0-9 ]", "");
	}


	public static Boolean confirmationDialog(){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirmation!");
		alert.setContentText("Are you sure to remove the content");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return  true;
		} else {
			return false;
			// ... user chose CANCEL or closed the dialog
		}
	}


}
