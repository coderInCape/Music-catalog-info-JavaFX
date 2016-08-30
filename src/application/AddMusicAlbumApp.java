package application;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddMusicAlbumApp{
	
	private final static Label idLabel = new Label("ID: ");
	private static Label generatedIdLabel;
	
	private final static Label nameLabel = new Label("Name: ");
	private static TextField nameTextField;
	
	private final static Label genreLabel = new Label("Genre: ");
	private static TextField genreTextField;
	
	private static CheckBox isCompilationCheckBox = new CheckBox();
	private final static Label isCompilationLabel = new Label("Is Compilation: ");
	
	private final static Label trackCountLabel = new Label("Track Count: ");
	private static Spinner<Integer> trackCountSpinner;
	
	private static final Button okBtn = new Button("OK");
	private static final Button cancelBtn = new Button("Cancel");
	private static Class thisClass;
	private static Stage stage;


	public static void createAndShow(Stage primaryStage, MusicCatalogDS musicCatalogDS, ObservableList<MusicAlbum> tableData){
		build(primaryStage, musicCatalogDS);
		createEventListeners(musicCatalogDS, tableData);
	}

	private static void createEventListeners(MusicCatalogDS musicCatalogDS, ObservableList<MusicAlbum> tableData) {
		cancelBtn.setOnAction(event -> {
			stage.close();
		});
		
		okBtn.setOnAction(event -> {
			String id = generatedIdLabel.getText().trim();
			String name = nameTextField.getText().trim();
			String genre = genreTextField.getText().trim();
			boolean isCompilation = isCompilationCheckBox.isSelected();
			int trackCount = trackCountSpinner.getValue();
			
			MusicAlbum album = new MusicAlbum(id, name, genre, isCompilation, trackCount);
			try {
				musicCatalogDS.add(album);
				tableData.add(album);
				stage.close();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	private static void build(Stage primaryStage, MusicCatalogDS musicCatalogDS) {
		stage = new Stage();
		stage.setTitle("Add A New Music Album");

		stage.initStyle(StageStyle.UTILITY);
		stage.initModality(Modality.APPLICATION_MODAL);

		// stage.setMinWidth(400);
		stage.setX(primaryStage.getX() + 300);
		stage.setY(primaryStage.getY() + 200);
		
		HBox idBox = new HBox();
		generatedIdLabel = new Label(musicCatalogDS.generateId());
		idBox.getChildren().addAll(idLabel, generatedIdLabel);
		
		
		HBox nameBox = new HBox();
		nameTextField = new TextField();
		nameBox.getChildren().addAll(nameLabel, nameTextField);
		
		HBox genreBox = new HBox();
		genreTextField = new TextField();
		genreBox.getChildren().addAll(genreLabel, genreTextField);
		
		HBox isCompilationBox = new HBox();
		isCompilationCheckBox = new CheckBox();
		isCompilationBox.getChildren().addAll(isCompilationLabel, isCompilationCheckBox);
		
		HBox trackCountBox = new HBox();
		trackCountSpinner = new Spinner<Integer>(0, 999, 1);
		trackCountBox.getChildren().addAll(trackCountLabel, trackCountSpinner);
		
		
		HBox btnsBox = new HBox();
		btnsBox.getChildren().addAll(okBtn, cancelBtn);
		
		VBox root = new VBox();
		root.getChildren().addAll(idBox, nameBox, genreBox, isCompilationBox, trackCountBox, btnsBox);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("file:/home/waleed/workspace/lab3/bin/application/application.css");
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.show();
	}
}
