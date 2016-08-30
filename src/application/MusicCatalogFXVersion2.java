package application;

import java.io.IOException;
import java.util.Observable;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MusicCatalogFXVersion2 extends Application {
	
	private MusicCatalogDS musicCatalogDS;
	
	private final Button saveBtn = new Button("Save Data");
	
	private ObservableList<MusicAlbum> tableData = FXCollections.observableArrayList();
	private TableView<MusicAlbum> tableView;
	private TableColumn<MusicAlbum, String> idColumn;
	private TableColumn<MusicAlbum, String> nameColumn;
	private TableColumn<MusicAlbum, String> genreColumn;
	private TableColumn<MusicAlbum, Boolean> isCompilationColumn;
	private TableColumn<MusicAlbum, Integer> trackCountColumn;
	
	private final Label filterLabel = new Label("Filter: ");
	private TextField filterTF = new TextField();
	
	private final Button sortBtn = new Button("Sort by Genre and Track Count");
	private final Button restoreOrderBtn = new Button("Restore Order");
	private final Button addBtn = new Button("Add Album");
	private final Button deleteBtn = new Button("Delete Selected Album");
	
	public MusicCatalogFXVersion2() {
		 musicCatalogDS = new MusicCatalogDS();
		try {
			musicCatalogDS.loadData("MusicCatalog.dat");
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Data has been loaded successfully");
			alert.showAndWait();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		for(MusicAlbum musicAlbum : musicCatalogDS.getAll())
			tableData.add(musicAlbum);
	}
	

	@Override
	public void start(Stage primaryStage) {
		build(primaryStage);
		primaryStage.setTitle(getClass().getName());
		createEventListeners(primaryStage);
		primaryStage.show();
		
	}

	private void createEventListeners(Stage primaryStage) {
		// TODO add all the listeners 
		
		//Table filter
		FilteredList<MusicAlbum> filteredList = new FilteredList<>(tableData, p -> true);
		filterTF.textProperty()
		.addListener((observable, oldValue, newValue) ->
		{
			filteredList.setPredicate(product ->
			{
				// If filter text is empty, display all products
				if (newValue == null || newValue.isEmpty())
				{
					return true;
				}

				// Compare product's name with filter text
				// If match, return true. Otherwise return false
				//
				String filterString = newValue.toUpperCase();

				if (product.getName().toUpperCase().contains(filterString))
				{
					return true;
				}
				else
				{
					return false;
				}
			});
		});
		SortedList<MusicAlbum> sortedList = new SortedList<MusicAlbum>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);
		
		//sorting 
		sortBtn.setOnAction(event -> {
			genreColumn.setSortType(TableColumn.SortType.ASCENDING);
			trackCountColumn.setSortType(TableColumn.SortType.DESCENDING);
			tableView.getSortOrder().clear();
			tableView.getSortOrder().add(genreColumn);
			tableView.getSortOrder().add(trackCountColumn);
		});
		
		addBtn.setOnAction(event -> {
			AddMusicAlbumApp.createAndShow(primaryStage, musicCatalogDS, tableData);
		});
		
		deleteBtn.setOnAction(event -> {
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setContentText("Are you sure you wanna add buddy?");
			confirmation.showAndWait();
			MusicAlbum album = tableView.getSelectionModel().getSelectedItem();
			musicCatalogDS.remove(album.getId());
			tableData.remove(album);
		});

		restoreOrderBtn.setOnAction((e) ->
		{
			tableView.getSortOrder().clear();
		});
		
		saveBtn.setOnAction((e) ->
		{
			try {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Data has been saved successfully");
				alert.showAndWait();
				musicCatalogDS.saveData("MusicCatalog.dat");
			} catch (IOException e1) {
				new RuntimeException(e1.getMessage());
			}
		});
	}

	private void build(Stage primaryStage) {
		// TODO create the view
		
		VBox root = new VBox();
		
		HBox saveBox = new HBox();
		saveBox.setId("saveBox");
		saveBox.getChildren().addAll(saveBtn);
		
		//set the columns headers
		idColumn = new TableColumn<MusicAlbum, String>("ID");
		nameColumn = new TableColumn<MusicAlbum, String>("Name");
		genreColumn = new TableColumn<MusicAlbum, String>("Genre");
		isCompilationColumn = new TableColumn<MusicAlbum, Boolean>("Compilation?");
		trackCountColumn = new TableColumn<MusicAlbum, Integer>("# Tracks");
		
		//set column cells 
		//has to follow the beans rules e.g "getIsCompilation" for isCompilation variable
		idColumn.setCellValueFactory(new PropertyValueFactory<MusicAlbum, String>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<MusicAlbum, String>("name"));
		genreColumn.setCellValueFactory(new PropertyValueFactory<MusicAlbum, String>("genre"));
		isCompilationColumn.setCellValueFactory(new PropertyValueFactory<MusicAlbum, Boolean>("isCompilation"));
		trackCountColumn.setCellValueFactory(new PropertyValueFactory<MusicAlbum, Integer>("trackCount"));
		
		tableView = new TableView<MusicAlbum>();
		
		tableView.getColumns().add(idColumn);
		tableView.getColumns().add(nameColumn);
		tableView.getColumns().add(genreColumn);
		tableView.getColumns().add(isCompilationColumn);
		tableView.getColumns().add(trackCountColumn);
		
		tableView.setItems(tableData);
		
		HBox filterBox = new HBox();
		filterTF = new TextField();
		filterBox.getChildren().addAll(filterLabel, filterTF);
		filterBox.setId("filterBox");
		
		HBox sortBox = new HBox();
		sortBox.getChildren().addAll(sortBtn, restoreOrderBtn);
		sortBox.setId("sortBox");
		
		HBox addRemoveBox = new HBox();
		addRemoveBox.getChildren().addAll(addBtn, deleteBtn);
		addRemoveBox.setId("addRemoveBox");
		
		root.getChildren().addAll(saveBox, tableView, filterBox, sortBox, addRemoveBox);
		

		Scene scene = new Scene(root, 800,600);
		scene.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());
		primaryStage.setScene(scene);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}