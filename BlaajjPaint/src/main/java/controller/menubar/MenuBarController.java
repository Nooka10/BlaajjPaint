package controller.menubar;

import controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;
import controller.history.RecordCmd;

import java.io.File;

public class MenuBarController {
	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private MainViewController mainViewController; // Reference to the mainViewController
	
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
	
	/**
	 * Appelé par le MainViewController pour donner une référence vers lui-même.
	 *
	 * @param mainViewController, une référence vers le mainViewController
	 *
	 * Créé par Benoît Schopfer
	 */
	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}
	
	@FXML
	void handleNew(ActionEvent event) {
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
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/menubar/windowsNewProject.fxml"));
			Parent newProjectWindow = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(newProjectWindow));
			stage.show();
			
			// Give the WindowsNesProject access to the menuBarController.
			WindowsNewProject windowsNewProject= fxmlLoader.getController();
			windowsNewProject.setMainViewController(mainViewController);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//mainViewController.newCanvas(width, height);
	}
	
	@FXML
	void handleOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".blaaj files(*.blaajj)", "*.blaajj");
		fileChooser.getExtensionFilters().add(extFilter);
		
		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainViewController.getMain().getPrimaryStage());
		
		if (file != null) {
			// main.loadBlaajjFile(file); // FIXME: appeler fonction ouvrir
			System.out.println("path fichier choisi: " + file.getPath());
		}
	}
	
	@FXML
	void handleSave(ActionEvent event) {
		System.out.println("appeler la fonction de sauvegarde!");
		// FIXME appeler fonction sauvegarder
	}
	
	@FXML
	void handleSaveAs(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".blaaj files(*.blaajj)", "*.blaajj");
		fileChooser.getExtensionFilters().add(extFilter);
		
		Main mainApp = mainViewController.getMain();
		
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
	void undo(ActionEvent event) {
		System.out.println("undo");
		RecordCmd.getInstance().undo();
	}
	
	@FXML
	void redo(ActionEvent event) {
		RecordCmd.getInstance().redo();
	}
}
