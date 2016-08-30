package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MusicCatalogFXVersion1 extends Application {
	
	private MusicCatalogDS musicCatalogDS = new MusicCatalogDS();
	
	private TextArea displayTextArea;
	
	private final Label idLabel = new Label("ID: ");
	private TextField idTextField;
	
	private final Label nameLabel = new Label("Name: ");
	private TextField nameTextField;
	
	private final Label genreLabel = new Label("Genre: ");
	private TextField genreTextField;
	
	private final Label isCompilationLabel = new Label("Is Compilation: ");
	private TextField isCompilationTextField;
	
	private final Label trackCountLabel = new Label("Track Count: ");
	private TextField trackCountTextField;
	
	private final Button loadBtn = new Button("Load Data");
	private final Button displayAlbumsBtn = new Button("Display Albums");
	private final Button searchAlbumsBtn = new Button("Search Albums");
	private final Button addAlbumBtn = new Button("Add Album");
	private final Button removeAlbumBtn = new Button("Remove Album");
	private final Button saveDataBtn = new Button("Save Data");
	
	private Scene scene;

	@Override
	public void start(Stage primaryStage) {
		build(primaryStage);
		primaryStage.setTitle(getClass().getName());
		createEventsListeners();
		primaryStage.show();
		
		Thread.currentThread().setUncaughtExceptionHandler((thread, exception) ->
		{
			// System.out.println("ERROR: " + exception);
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("Exception thrown: " + exception);
			alert.showAndWait();
		});
	}

	private void createEventsListeners() {
		
		loadBtn.setOnAction(event -> loadData());
		
		displayAlbumsBtn.setOnAction(event -> displayAlbums());
		
		searchAlbumsBtn.setOnAction(event -> searchAlbums());
		
		addAlbumBtn.setOnAction(event -> addAlbum());
		
		removeAlbumBtn.setOnAction(event -> removeAlbum());
		
		saveDataBtn.setOnAction(event -> saveData());
	}

	private void saveData() {
		try {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Data has been saved successfully");
			alert.showAndWait();
			musicCatalogDS.saveData("MusicCatalog.dat");
		} catch (IOException e) {
			new RuntimeException(e.getMessage());
		}
	}

	private void removeAlbum() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you wanna add buddy?");
		alert.showAndWait();
		musicCatalogDS.remove(idTextField.getText().trim());
	}

	private void addAlbum() {
		
		String id = idTextField.getText().trim();
		String name = nameTextField.getText().trim();
		String genre = genreTextField.getText().trim();
		boolean isCompilation = Boolean.parseBoolean(isCompilationTextField.getText().trim());
		int trackCount = Integer.parseInt(trackCountTextField.getText().trim());
		
		MusicAlbum album = new MusicAlbum(id, name, genre, isCompilation, trackCount);
		try {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setContentText("Are you sure you wanna add buddy?");
			alert.showAndWait();
			musicCatalogDS.add(album);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void searchAlbums() {
		MusicAlbum result = musicCatalogDS.get(idTextField.getText());
		if(result != null){
			idTextField.setText(result.getId());
			nameTextField.setText(result.getName());
			genreTextField.setText(result.getGenre());
			isCompilationTextField.setText(result.getIsCompilation() + "");
			trackCountTextField.setText(result.getTrackCount() + "");
			
			displayTextArea.setText(result.toString());
		}
	}

	private void displayAlbums() {
		if(musicCatalogDS != null)
			displayTextArea.setText(musicCatalogDS.toString());
	}

	private void loadData() {
		try {
			musicCatalogDS.loadData("MusicCatalog.dat");
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Data has been loaded successfully");
			alert.showAndWait();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void build(Stage primaryStage) {
		
		VBox root = new VBox();
		
		displayTextArea = new TextArea();
		
		HBox idBox = new HBox();
		idTextField = new TextField();
		idBox.getChildren().addAll(idLabel, idTextField);
		
		
		HBox nameBox = new HBox();
		nameTextField = new TextField();
		nameBox.getChildren().addAll(nameLabel, nameTextField);
		
		HBox genreBox = new HBox();
		genreTextField = new TextField();
		genreBox.getChildren().addAll(genreLabel, genreTextField);
		
		HBox isCompilationBox = new HBox();
		isCompilationTextField = new TextField();
		isCompilationBox.getChildren().addAll(isCompilationLabel, isCompilationTextField);
		
		HBox trackCountBox = new HBox();
		trackCountTextField = new TextField();
		trackCountBox.getChildren().addAll(trackCountLabel, trackCountTextField);
		
		
		HBox btnsBox = new HBox();
		btnsBox.getChildren().addAll(loadBtn, displayAlbumsBtn, searchAlbumsBtn, addAlbumBtn, removeAlbumBtn, saveDataBtn);
		
		root.getChildren().addAll(displayTextArea, idBox, nameBox, genreBox, isCompilationBox, trackCountBox, btnsBox);
		Scene scene = new Scene(root, 800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
