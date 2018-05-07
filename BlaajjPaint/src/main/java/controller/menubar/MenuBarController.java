package controller.menubar;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.RecordCmd;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;

import java.io.File;

public class MenuBarController {
	
	@FXML
	private MenuBar menuBar;
	
	
	@FXML
	public void handleNew(ActionEvent event) {
		/*
		if (changesMade) {
			changesWarning(false);
		}
		gc.clearRect(0, 0, drawingCanvasWidth, drawingCanvasHeight);
		list = new ArrayList<>();
		changesMade = false;
		firstTimeSave = true;
		*/
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/menubar/WindowsNewProject.fxml"));
			Parent newProjectWindow = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(newProjectWindow));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".blaaj files(*.blaajj)", "*.blaajj");
		fileChooser.getExtensionFilters().add(extFilter);
		
		// Show save file dialog
		File file = fileChooser.showOpenDialog(MainViewController.getInstance().getMain().getPrimaryStage());
		
		if (file != null) {
			// main.loadBlaajjFile(file); // FIXME: appeler fonction ouvrir
			System.out.println("path fichier choisi: " + file.getPath());
		}
	}
	
	@FXML
	public void handleSave(ActionEvent event) {
		System.out.println("appeler la fonction de sauvegarde!");
		// FIXME appeler fonction sauvegarder
	}
	
	@FXML
	public void handleSaveAs(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".blaajj files(*.blaajj)", "*.blaajj");
		fileChooser.getExtensionFilters().add(extFilter);
		
		Main mainApp = MainViewController.getInstance().getMain();
		
		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".blaajj")) {
				file = new File(file.getPath() + ".blaajj");
			}
			//mainApp.savePersonDataToFile(file);
			System.out.println("appeler la fonction de sauvegarde!"); // FIXME: appeler fct sauvegarder
		}
	}
	
	
	@FXML
	public void handleExport(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extPNG =
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
		FileChooser.ExtensionFilter extJPG =
				new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extPNG, extJPG);
		
		Main mainApp = MainViewController.getInstance().getMain();
		
		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		
		Project.getInstance().export(file);
	}
	
	@FXML
	public void handleImportImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("Import an image");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All images files", "*.png", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"),
				new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg"));
		
		Main mainApp = MainViewController.getInstance().getMain();
		
		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		
		Project.getInstance().importImage(file);
	}
	
	@FXML
	public void handleClose(ActionEvent event) {
	
	}
	
	@FXML
	public void handleUndo(ActionEvent event) {
		RecordCmd.getInstance().undo();
	}
	
	@FXML
	public void handleRedo(ActionEvent event) {
		RecordCmd.getInstance().redo();
	}
	
	@FXML
	public void handleNewLayer(ActionEvent event) {
		Project.getInstance().addNewLayer();
	}
	
	@FXML
	public void handleDuplicateLayer(ActionEvent event) {
		//to do
	}
	
	@FXML
	public void handleDeleteLayer(ActionEvent event) {
		Project.getInstance().deleteCurrentLayer();
	}
	
	@FXML
	public void handleFusionLayer(ActionEvent event) {
		// to do
	}
	
	@FXML
	public void mergeAllLayer(ActionEvent event) {
		Layer layer = Project.getInstance().getLayers().getLast();
		for (int i = Project.getInstance().getLayers().size() - 2; i >= 0; --i)
			
			Project.getInstance().getLayers().get(i).mergeLayers(layer);
		
		Project.getInstance().getLayers().clear();
		Project.getInstance().getLayers().add(layer);
		
		
		Project.getInstance().drawWorkspace();
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	@FXML
	public void hideCurrentLayer(ActionEvent event) {
		Project.getInstance().getCurrentLayer().setVisible(false);
	}
	
	@FXML
	public void handleAPropos(ActionEvent event) {
	
	}
	
	@FXML
	public void handleHelp(ActionEvent event) {
	
	}
}
