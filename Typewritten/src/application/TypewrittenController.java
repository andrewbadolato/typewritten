/**
TypewrittenController.java
Last Updated: January 1, 2018
Version 0.1.0

 */

package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TypewrittenController {

	//Create document object
	public Document doc = new Document("");

	@FXML
	private BorderPane primaryStage;

	@FXML
	private Label lblStatus;

	@FXML
	private MenuBar mnuBar;

	@FXML
	private Menu mnuFile;

	@FXML
	private MenuItem mnuNew;

	@FXML
	private MenuItem mnuOpen;

	@FXML
	private MenuItem mnuSave;

	@FXML
	private MenuItem mnuSaveAs;

	@FXML
	private MenuItem mnuQuit;

	@FXML
	private Menu mnuEdit;

	@FXML
	private MenuItem mnuUndo;

	@FXML
	private MenuItem mnuRedo;

	@FXML
	private Menu mnuTheme;

	@FXML
	private MenuItem mnuThemeDef;

	@FXML
	private MenuItem mnuThemeDesktop;

	@FXML
	private Menu mnuHelp;

	@FXML
	private MenuItem mnuAbout;

	@FXML
	private MenuItem mnuHelpFile;

	@FXML
	private TextArea txtLockedPage;

	@FXML
	private TextField txtDocArea;

	@FXML
	private CheckBox chkAutosave;

	private Stage mainStage;
	public Theme theme = new Theme();

	//File objects and strings for saving and opening files
	File activeFile, selectedFile;
	String fileName = "";

	//Titles and status bar messages
	//	String title = Main.title;
	String title = doc.getTitle();
	String statusMessage;
	String statusSaved = "File saved as ";
	String statusOpened = "File opened: ";
	String statusNew = "New file created: ";

	public void setMainStage(Stage mainStage){
		this.mainStage = mainStage;
	}

	public void initialize() {

		//Wrap text on locked text area; clear label
		txtLockedPage.setWrapText(true);
		lblStatus.setText("");


		txtDocArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					fillLockedPage();
				}
			}
		});	
		

	}

	//Copies text typed into editable text field into locked text area	
	@FXML
	void fillLockedPage() { 		

		//Appends text in editable field to locked text area
		if (txtDocArea.getText().trim().length() <= 0)  {
			txtLockedPage.appendText(System.getProperty("line.separator"));
		}
		else {
			String text = txtDocArea.getText() + " ";
			txtLockedPage.appendText(text);
			txtLockedPage.positionCaret(txtLockedPage.getLength());

			//Sets current content on locked page in Document object
			//and clears text field line
			doc.setDocText(txtLockedPage.getText());
			txtDocArea.clear();
			lblStatus.setText("");	
		}
	}	

	//If there is content in locked area, dialog box appears with a prompt to save
	void promptForSave(ActionEvent event) {
		fillLockedPage();
		if (txtLockedPage.getText().trim().length() > 0) {
			System.out.println("length was greater than 0");
			Alert alert = new Alert(AlertType.CONFIRMATION);
			System.out.println("Dialog created");
			alert.setTitle("Typewritten - Save File");
			alert.setHeaderText("Save File");
			alert.setContentText("Do you want to save your current document?");

			ButtonType buttonTypeYes = new ButtonType("Yes", ButtonData.YES);
			ButtonType buttonTypeNo = new ButtonType("No", ButtonData.NO);
			ButtonType buttonTypeCancel = new ButtonType("Cancel", 
					ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeYes){
				//Run checkSaved
				this.checkSaved(event);
				this.newFile(event);
			} else if (result.get() == buttonTypeNo) {
				this.newFile(event);
			} else {
				System.out.println("promptForSave - Cancel pressed");
			}}	
	}

	//Open an existing file
	@FXML
	void openFile(ActionEvent event) {
		System.out.println("Open File Initiated");
		this.promptForSave(event);

		txtDocArea.clear();
		txtLockedPage.clear();

		//FileChooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		selectedFile = fileChooser.showOpenDialog(mainStage);
		if (selectedFile != null) {
			doc.setFilename(selectedFile.getPath());
			System.out.println("Open File selectedFile = " + selectedFile);
			System.out.println("Filename = " + doc.getFilename());

			//Fill text area with contents of selected file
			String line = null;
			try {
				// FileReader reads text files in the default encoding
				FileReader fileReader = 
						new FileReader(selectedFile);
				// Wrap FileReader in BufferedReader
				BufferedReader bufferedReader = 
						new BufferedReader(fileReader);
				while((line = bufferedReader.readLine()) != null) {
					txtLockedPage.appendText(String.format("%s", line));

				}   
				doc.setDocText(txtLockedPage.getText());
				bufferedReader.close();        
			}
			catch(FileNotFoundException ex) {
				System.out.println(
						"Unable to open file '" + 
								selectedFile + "'");                
			}
			catch(IOException ex) {
				System.out.println(
						"Error reading file '" 
								+ selectedFile + "'");                  
				ex.printStackTrace();
			}		

			statusMessage = statusOpened;
			changeTitle(event);
		}}


	//Check whether a current file is active for saving
	@FXML
	void checkSaved(ActionEvent event) {
		//If a filename has been set, save within current file;
		if (!doc.getFilename().equals("")) {
			System.out.println("checkSaved result = writeFile");
			this.writeFile(event);		}	

		//If this is a new file, save new file
		else {
			System.out.println("checkSaved result = saveAsFile");
			this.saveAsFile(event);
		}
	}

	//Save as a new file
	@FXML
	void saveAsFile(ActionEvent event) {

		fillLockedPage();
		System.out.println(doc.getDocText());

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
		//Would like to default to main documents folder here
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		selectedFile = fileChooser.showSaveDialog(mainStage);

		if (selectedFile == null) {
			System.out.println("Save cancelled");
		}

		else {	
			doc.setFilename(selectedFile.getPath());
			System.out.println(selectedFile);
			System.out.println("Filename = " + doc.getFilename());
			this.writeFile(event);
			statusMessage = statusSaved;
			this.changeTitle(event);
		}


	}

	//Change title and status bar messages
	@FXML
	void changeTitle(ActionEvent event) {
		String newTitle = title + " - " + selectedFile.getName();
		System.out.println(newTitle);
		Main.primaryStage.setTitle(newTitle);
		lblStatus.setText(statusMessage + selectedFile.getName() + "''");
	}

	//Write content to the currently open file
	@FXML
	void writeFile(ActionEvent event) {

		fillLockedPage();
		System.out.println(selectedFile);
		//Save to file name typed or selected in FileChooser
		List<String> lines = Arrays.asList(doc.getDocText().toString());
		Path filePath = Paths.get(selectedFile.toString());
		try {
			Files.write(filePath, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Check whether there is currently content in txtLockedPage and prompt to save
	@FXML
	void checkNew(ActionEvent event) {
		promptForSave(event);
		this.newFile(event);
	}

	//Clear content from document object and txtDocArea
	@FXML
	void newFile(ActionEvent event) {
		doc.setDocText("");
		doc.setFilename("");
		txtLockedPage.clear();
		System.out.println(statusNew);
	}

	//Display "About" dialog box
	@FXML
	void launchAbout(ActionEvent event) {

		String 	version = "0.1.0",
				date = "2018",
				newline = "\n";

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About " + title);
		alert.setHeaderText("About " + title);
		alert.setContentText(date + newline +
				"Version " + version + newline);
		//add contact information/website
		alert.showAndWait();
	}

	@FXML
	void launchHelp(ActionEvent event) {
		//Add help launcher code here
	}

	//Theme switcher.  Get string with stylesheet path based on menu option selected
	//There has to be a better way to do this. Investigate later
	void switchTheme(Theme theme) {	
		primaryStage.getScene().getStylesheets().clear();
		primaryStage.getScene().getStylesheets().add(getClass()
				.getResource(theme.getTheme()).toExternalForm());
	}

	//Default Theme
	@FXML
	void selectThemeDef(ActionEvent event) {
		System.out.println("Select Theme: Default");
		theme.setTheme("");
		switchTheme(theme);
	}

	//Desktop Theme
	@FXML
	void selectThemeDesktop(ActionEvent event) {
		System.out.println("Select Theme: Desktop");
		theme.setTheme("desktop");
		switchTheme(theme);
	}


	//	Implement timed autosave feature later
	// @FXML
	//	void toggleAutosave(ActionEvent event) {
	//
	//	}


	//Undo command
	//Revisit - customize to make more useful
	@FXML
	void undoAction(ActionEvent event) {
		this.txtDocArea.undo();
	}

	//Redo command
	//Revisit - customize to make more useful
	@FXML
	void redoAction(ActionEvent event) {
		this.txtDocArea.redo();
	}

	//Close program
	@FXML
	public void closeFile(ActionEvent event) {
		promptForSave(event);
		System.exit(0);
	}
	
}
