package application;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene; 

public class Main extends Application {

	static Stage primaryStage = null;
	public static String title;
	public String resource = "application.css";
	Scene scene;
	String stylesheetStatus;
	private FXMLLoader fxmlLoader;

	public void start(Stage mainStage) {
		title = "Typewritten";
		URL location = getClass().getResource("Typewritten.fxml");

		try {
			fxmlLoader = new FXMLLoader();  
			fxmlLoader.setLocation(location);  
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());  
			Parent root = (Parent) fxmlLoader.load(location.openStream());
			scene = new Scene(root);
			mainStage.setTitle(title);
			scene.getStylesheets().add(getClass().getResource(resource).toExternalForm());
			mainStage.setScene(scene);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		primaryStage = mainStage; 		 
	}

	//Ensures controller's promptForSave method runs when stage closed
	//Need to figure out how to prompt for saving *before* stage closes
	@Override  
	public void stop() {  
		((TypewrittenController) fxmlLoader.getController()).closeFile(null);  
	}  


	public static void main(String[] args) {
		launch(args);


	}
}
