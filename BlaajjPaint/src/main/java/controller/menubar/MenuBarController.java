package controller.menubar;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;
import utils.SaveProject;

import java.io.File;
import java.util.LinkedList;

/**
 * Controller associé au fichier FXML MenuBar.fxml et controlant l'ensemble des actions associées aux menus de la barre de menu située en haut de la GUI.
 */
public class MenuBarController {
	
	@FXML
	private Menu menuBar_transformations;
	@FXML
	public MenuBar menuBar;
	@FXML
	private MenuItem menuBar_nouveau;
	@FXML
	private MenuItem menuBar_ouvrir;
	@FXML
	private MenuItem menuBar_enregistrer;
	@FXML
	private MenuItem menuBar_enregistrerSous;
	@FXML
	private MenuItem menuBar_exporter;
	@FXML
	private MenuItem menuBar_importer;
	@FXML
	private MenuItem menuBar_fermer;
	@FXML
	private MenuItem menuBar_undo;
	@FXML
	private MenuItem menuBar_redo;
	@FXML
	private MenuItem menuBar_nouveauCalque;
	@FXML
	private MenuItem menuBar_dupliquerCalque;
	@FXML
	private MenuItem menuBar_supprimerCalque;
	@FXML
	private MenuItem menuBar_redimensionnerCalque;
	@FXML
	private MenuItem menuBar_fusionnerCalques;
	@FXML
	private MenuItem menuBar_aplatirCalques;
	@FXML
	private MenuItem menuBar_masquerCalques;
	@FXML
	private MenuItem menuBar_aPropos;
	@FXML
	private MenuItem menuBar_manuel;
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Nouveau</b>.
	 * Crée un nouveau projet selon la largeur et la hauteur entrés par l'utilisateur.
	 */
	@FXML
	public void handleNew() {
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
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Ouvrir</b>. Ouvre le projet .blaajj (sauvegardé précédemment) sélectionné par
	 * l'utilisateur.
	 */
	@FXML
	public void handleOpen() {
		MainViewController.getInstance().openProject();
	}
	
	/**
	 *
	 */
	@FXML
	public void handleSave() {
		System.out.println("appeler la fonction de sauvegarde!");
		
		SaveProject.getInstance().save();
		// FIXME appeler fonction sauvegarder
	}
	
	/**
	 *
	 */
	@FXML
	public void handleSaveAs() {
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
			
			SaveProject.getInstance().saveAs(file);
			//mainApp.savePersonDataToFile(file);
			// FIXME: appeler fct sauvegarder
		}
	}
	
	/**
	 *
	 */
	@FXML
	public void handleExport() {
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
	
	/**
	 *
	 */
	@FXML
	public void handleImportImage() {
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
	
	
	/**
	 * Fermeture du projet en cours d'execution.
	 *
	 */
	@FXML
	public void handleClose() {
		MainViewController.getInstance().closeProject();
		
	}
	
	/**
	 *
	 */
	@FXML
	public void handleUndo() {
		RecordCmd.getInstance().undo();
	}
	
	/**
	 *
	 */
	@FXML
	public void handleRedo() {
		RecordCmd.getInstance().redo();
	}
	
	/**
	 *
	 */
	@FXML
	public void handleNewLayer() {
		// peut être mieux fait dans project si on a le temps
		MainViewController.getInstance().getRightMenuController().addNewLayer();
	}
	
	/**
	 *
	 */
	@FXML
	public void handleDuplicateLayer() {
		Project.getInstance().addLayer(new Layer(Project.getInstance().getCurrentLayer(), false));
	}
	
	/**
	 *
	 */
	@FXML
	public void handleDeleteLayer() {
		Project.getInstance().deleteCurrentLayer();
	}
	
	/**
	 *
	 * @param event, inutilisé.
	 */
	@FXML
	public void handleFusionLayer(ActionEvent event) {
		MainViewController.getInstance().getRightMenuController().mergeLayer(event);
	}
	
	/**
	 *
	 */
	@FXML
	private void handleResizeLayer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/menubar/ResizeLayer.fxml"));
			Parent resizeWindow = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Redimensionner calque");
			stage.setScene(new Scene(resizeWindow));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void mergeAllLayer() {
		MergeAllSave mas = new MergeAllSave();
		Layer resultLayer = new Layer(1,1, false); // FIXME: 1,1 la taille??
		
		for (Layer layer : Project.getInstance().getLayers()) {
			resultLayer = resultLayer.mergeLayers(layer);
		}
		Project.getInstance().getLayers().clear();
		Project.getInstance().addLayer(resultLayer);
		
		Project.getInstance().drawWorkspace();
		MainViewController.getInstance().getRightMenuController().updateLayerList();
		mas.execute();
	}
	
	@FXML
	public void hideCurrentLayer() {
		Project.getInstance().getCurrentLayer().setVisible(false);
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	@FXML
	public void handleAPropos() {
	
	}
	
	@FXML
	public void handleHelp() {
	
	}
	
	/**
	 * Permet d'activer les bouttons. A appeler dès qu'un project est ouvert, créé
	 */
	public void enableButton() {
		menuBar_enregistrer.setDisable(false);
		menuBar_enregistrerSous.setDisable(false);
		menuBar_exporter.setDisable(false);
		menuBar_importer.setDisable(false);
		menuBar_fermer.setDisable(false);
		menuBar_undo.setDisable(false);
		menuBar_redo.setDisable(false);
		menuBar_nouveauCalque.setDisable(false);
		menuBar_dupliquerCalque.setDisable(false);
		menuBar_supprimerCalque.setDisable(false);
		menuBar_redimensionnerCalque.setDisable(false);
		menuBar_fusionnerCalques.setDisable(false);
		menuBar_aplatirCalques.setDisable(false);
		menuBar_masquerCalques.setDisable(false);
	}
	
	/**
	 * Permet de déscativer les bouttons. A appeler à la fermeture d'un projet ou à la création de l'application
	 */
	public void disableButton() {
		menuBar_enregistrer.setDisable(true);
		menuBar_enregistrerSous.setDisable(true);
		menuBar_exporter.setDisable(true);
		menuBar_importer.setDisable(true);
		menuBar_fermer.setDisable(true);
		menuBar_undo.setDisable(true);
		menuBar_redo.setDisable(true);
		menuBar_nouveauCalque.setDisable(true);
		menuBar_dupliquerCalque.setDisable(true);
		menuBar_supprimerCalque.setDisable(true);
		menuBar_redimensionnerCalque.setDisable(true);
		menuBar_fusionnerCalques.setDisable(true);
		menuBar_aplatirCalques.setDisable(true);
		menuBar_masquerCalques.setDisable(true);
		menuBar_transformations.setDisable(false);
	}
	
	public class MergeAllSave extends ICmd {
		
		private LinkedList<Layer> allMergedLayers;
		private Layer oldCurrentLayer;
		private Layer newLayer;
		
		/**
		 * Prend tous les calques qui vont être fusionnés
		 */
		public MergeAllSave() {
			oldCurrentLayer = Project.getInstance().getCurrentLayer();
			allMergedLayers = new LinkedList<>();
			allMergedLayers.addAll(Project.getInstance().getLayers());
		}
		
		@Override
		public void execute() {
			newLayer = Project.getInstance().getCurrentLayer();
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			Project.getInstance().getLayers().clear();
			for (Layer layer : allMergedLayers) {
				Project.getInstance().getLayers().add(layer);
			}
			
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
			MainViewController.getInstance().getRightMenuController().updateLayerList();
		}
		
		@Override
		public void redo() {
			Project.getInstance().getLayers().clear();
			Project.getInstance().getLayers().add(newLayer);
			Project.getInstance().setCurrentLayer(newLayer);
			MainViewController.getInstance().getRightMenuController().updateLayerList();
		}
		
		public String toString() {
			return "Aplatissement de tous les calques";
		}
	}
	
}
