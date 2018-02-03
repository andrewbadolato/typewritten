package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene; 

public class Main extends Application {

static Stage primaryStage = null;
public static String title;
public String resource = "application.css";
Parent root;
Scene scene;
String stylesheetStatus;


	public void start(Stage mainStage) {
		title = "Typewritten";
		
		try {
			root = FXMLLoader.load(getClass().getResource("Typewritten.fxml"));

			scene = new Scene(root);
			mainStage.setTitle(title);
			scene.getStylesheets().add(getClass().getResource(resource).toExternalForm());
			mainStage.setScene(scene);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		primaryStage = mainStage; }
		
	

	
	public static void main(String[] args) {
		launch(args);
		
		
	}
}
