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
	
	/*
	@FXML
	private MenuItem menuBar_nouveau; // bouton "Nouveau" de la MenuBar
	
	@FXML
	private MenuItem menuBar_ouvrir; // bouton "Ouvrir" de la MenuBar
	
	@FXML
	private MenuItem menuBar_enregistrer; // bouton "Enregistrer" de la MenuBar
	
	@FXML
	private MenuItem menuBar_enregistrerSous; // bouton "Enregistrer sous" de la MenuBar
	
	@FXML
	private MenuItem menuBar_exporter; // bouton "Exporter" de la MenuBar
	
	@FXML
	private MenuItem menuBar_fermer; // bouton "Fermer" de la MenuBar
	
	@FXML
	private MenuItem menuBar_undo; // bouton "Undo" de la MenuBar
	
	@FXML
	private MenuItem menuBar_redo; // bouton "Redo" de la MenuBar
	
	@FXML
	private MenuItem menuBar_nouveauCalque; // bouton "Nouveau calque" de la MenuBar
	
	@FXML
	private MenuItem menuBar_dupliquerCalque; // bouton "Dupliquer calque" de la MenuBar
	
	@FXML
	private MenuItem menuBar_supprimerCalque; // bouton "Supprimer calque" de la MenuBar
	
	@FXML
	private MenuItem menuBar_fusionnerCalques; // bouton "Fusionner calques" de la MenuBar
	
	@FXML
	private MenuItem menuBar_aplatirCalques; // bouton "Aplatir calques" de la MenuBar
	
	@FXML
	private MenuItem menuBar_masquerCalques; // bouton "Masquer calques" de la MenuBar
	
	@FXML
	private MenuItem menuBar_aPropos; // bouton "À propos" de la MenuBar
	
	@FXML
	private MenuItem menuBar_manuel; // bouton "Manuel" de la MenuBar
	*/
	
	@FXML
	void handleNew(ActionEvent event) {
		openNewProjectWindows();
	}
	
	@FXML
	void handleOpen(ActionEvent event) {
		openProject();
	}
	
	@FXML
	void handleSave(ActionEvent event) {
		System.out.println("appeler la fonction de sauvegarde!");
		// FIXME appeler fonction sauvegarder
	}
	
	@FXML
	void handleSaveAs(ActionEvent event) {
		saveAs();
	}
	
	@FXML
	void undo(ActionEvent event) {
		undoAction();
	}
	
	@FXML
	void redo(ActionEvent event) {
		redoAction();
	}
	
	@FXML
	void newLayer(ActionEvent event) {
		Project.getInstance().addNewLayer();
	}
	
	@FXML
	void duplicateLayer(ActionEvent event) {
		//to do
	}
	
	@FXML
	void deleteLayer(ActionEvent event) {
		Project.getInstance().deleteCurrentLayer();
	}
	
	@FXML
	void fusionLayer(ActionEvent event) {
		// to do
	}
	
	@FXML
	void hideCurrentLayer(ActionEvent event) {
		Project.getInstance().getCurrentLayer().setVisible(false);
	}
	
	// James do not work
	
	public void openNewProjectWindows() {
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
	
	public void openProject() {
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
	
	public void undoAction() {
		System.out.println("undo");
		RecordCmd.getInstance().undo();
	}
	
	public void redoAction() {
		RecordCmd.getInstance().redo();
	}
	
	public void saveAs() {
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
	
	public void mergeAllLayer() {
		Layer layer = Project.getInstance().getLayers().getLast();
		for (int i = Project.getInstance().getLayers().size() - 2; i >= 0; --i)
			
			Project.getInstance().getLayers().get(i).mergeLayers(layer);
		
		Project.getInstance().getLayers().clear();
		Project.getInstance().getLayers().add(layer);
		
		
		Project.getInstance().drawWorkspace();
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	public void export() {
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
	
	public void importImage() {
		
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
	
}
