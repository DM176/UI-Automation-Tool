package com.virtualkeyboard.client;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.virtualkeyboard.client.comparing.image.ImageDetection;
import com.virtualkeyboard.client.screenshot.ScreenShot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.opencv.core.Point;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader;
		//loader = new FXMLLoader(new File("./src/com/virtualkeyboard/client/view/LandingPage.fxml").toURI().toURL());
		System.out.println(getClass().getResource("/LandingPage.fxml"));
		loader = new FXMLLoader(getClass().getResource("/LandingPage.fxml"));
		//loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));;
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/jclasslogo.jpg")));
		Parent root= loader.load();
		System.out.println(System.getProperty("user.dir") +"/icons/jclasslogo.jpg");
		//stage.getIcons().add(new Image(new FileInputStream(System.getProperty("user.dir") +"/icons/jclasslogo.jpg")));
		stage.setTitle("J-Class Automation");
		Scene scene=new Scene(root);

		stage.setScene(scene);
		stage.show();




//         stage.setTitle("Process List");
//         Button button=new Button();
//
//         button.setText("Chat Automation start");
//         button.setMaxWidth(150);
//         button.setMaxHeight(50);
//         Group root=new Group(button);
//
//         Scene scene=new Scene(root,200,200);
//         stage.setScene(scene);
//         stage.show();
//
//
//         button.setOnAction(value ->  {
//        	stage.hide();
//        	 ProcessExecution processExecution=new ProcessExecution();
//             try {
//				processExecution.executeProcess();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (AWTException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//             stage.show();
//        	 System.out.println("executed");
//
//          });

//
//		System.out.println("Started");
//Thread.sleep(6000);
//
		
		
		//ScreenShot screenShotObj = new ScreenShot();
//		ImageDetection imageDetection = new ImageDetection();
		
//		
//		//Taking the screenshot of Screen size
		//BufferedImage screenShot=screenShotObj.captureScreenShotofScreenSize();
//		imageDetection.textFromImage(screenShot);
//		D:\Upwork\Intellij Projects

	//	ProcessExecution processExecution=new ProcessExecution();
	//	processExecution.executeChatProcess();
		
		
		
//		Point point=imageDetection.returnLocationOfIconInScreenShot(screenShot,"D:/Upwork/SampleForSearch/windowJava.PNG");
//		if(point!=null) {
//			System.out.println(point.x+","+point.y);
//			MouseControl.click((int) point.x, (int) point.y);
//		//MouseControl.click(10, 695);
//		}



//Thread.sleep(8000);

//	WritableImage writableImage= ImageDetection.iconlocationDelete(ScreenShot.captureScreenShotofScreenSize(),0.8);
//
//		ImageView imageView = new ImageView(writableImage);
//
//		// Setting the position of the image
//		imageView.setX(10);
//		imageView.setY(10);
//
//		// setting the fit height and width of the image view
//		imageView.setFitHeight(1920);
//		imageView.setFitWidth(1080);
//
//		// Setting the preserve ratio of the image view
//		imageView.setPreserveRatio(true);
//
//		// Creating a Group object
//		Group root1 = new Group(imageView);
//
//		// Creating a scene object
//		Scene scene1 = new Scene(root1, 600, 600);
//
//		// Setting title to the Stage
//		stage.setTitle("Colored to grayscale image");
//
//		// Adding scene to the stage
//		stage.setScene(scene1);
//
//		// Displaying the contents of the stage
//		stage.show();


	}

	public static void  main(String[] args){launch(args);}
}
