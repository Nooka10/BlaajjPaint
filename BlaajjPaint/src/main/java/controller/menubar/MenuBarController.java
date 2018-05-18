package controller.menubar;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.SaveProject;

import java.io.File;
import java.util.LinkedList;

/**
 * Contrôleur associé au fichier FXML MenuBar.fxml et gérant l'ensemble des actions associées aux menus de la barre de menu située en haut de la GUI.
 */
public class MenuBarController {
	
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
	private Menu menuBar_transformations;
	@FXML
	private MenuItem menuBar_aPropos;
	@FXML
	private MenuItem menuBar_manuel;
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Nouveau</b>.
	 * Ouvre une nouvelle fenêtre demandant à l'utilisateur d'entrer une largeur et une hauteur,
	 * puis crée un projet à ces dimensions lorsque l'utilisateur clique sur le bouton créer.
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
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Ouvrir</b>.
	 * Invite l'utilisateur à sélectionner un fichier .blaajj stoqué sur son disque dur puis ouvre le projet sélectionné.
	 */
	@FXML
	public void handleOpen() {
	    MainViewController.getInstance().openProject();
        OpenSave openSave = new OpenSave();
	    openSave.execute();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Enregistrer</b>.
	 * Invite l'utilisateur à sélectionner l'emplacement où enregistrer le projet lors de sa première sauvegarde,
	 * puis enregistre le projet dans le fichier .blaajj actuel.
	 */
	@FXML
	public void handleSave() {
		handleSaveAs();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Enregistrer sous</b>.
	 * Invite l'utilisateur à sélectionner l'emplacement où enregistrer le projet puis crée un fichier .blaajj à cet emplacement.
	 */
	@FXML
	public void handleSaveAs() {
		if (!SaveProject.getInstance().fileIsSetted()) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".blaajj files(*.blaajj)", "*.blaajj"));
			
			// Ouvre une fenêtre invitant l'utilisateur à sélectionner l'endroit où enregistrer le projet
			File file = fileChooser.showSaveDialog(MainViewController.getInstance().getMain().getPrimaryStage());
			
			if (file != null) {
				if (!file.getPath().endsWith(".blaajj")) { // vrai si le fichier sélectionné possède la bonne extension
					file = new File(file.getPath() + ".blaajj");
				}
				SaveProject.getInstance().saveAs(file);
			}
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Exporter</b>.
	 * Invite l'utilisateur à sélectionner l'emplacement où enregistrer l'image exportée ainsi que son extension (.png ou .jpg) puis
	 * crée une image dans le format choisi et à l'emplacement sélectionné.
	 */
	@FXML
	public void handleExport() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
		FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extPNG, extJPG); // Ajoute les extensions autorisées
		
		// Ouvre une fenêtre invitant l'utilisateur à sélectionner l'endroit où enregistrer le projet ainsi que son extension
		File file = fileChooser.showSaveDialog(MainViewController.getInstance().getMain().getPrimaryStage());
		
		Project.getInstance().export(file);
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Importer une image</b>.
	 * Invite l'utilisateur à sélectionner une une image (.jpg ou .png) et l'importe dans le projet en ajoute un nouveau calque la contenant.
	 */
	@FXML
	public void handleImportImage() {
		ImportImageSave importImageSave = new ImportImageSave();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Import an image");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("fichiers PNG ou JPG", "*.png", "*.jpg"));

		// Ouvre une fenêtre invitant l'utilisateur à sélectionner un fichier utilisant l'extension autorisée
		File file = fileChooser.showOpenDialog(MainViewController.getInstance().getMain().getPrimaryStage());

		Project.getInstance().importImage(file);
		importImageSave.execute();
	}
	
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Fichier -> Fermer</b>.
	 * Ferme le projet actuellement ouvert.
	 */
	@FXML
	public void handleClose() {
		MainViewController.getInstance().closeProject();
		
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Edition -> Undo</b>.
	 * Annule la dernière action effectuée (undo la dernière commande enregistrée).
	 */
	@FXML
	public void handleUndo() {
		RecordCmd.getInstance().undo();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Edition -> Redo</b>.
	 * Rétablit la dernière action annulée (redo la dernière commande qui a été undo).
	 */
	@FXML
	public void handleRedo() {
		RecordCmd.getInstance().redo();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Nouveau</b>.
	 * Ajoute un nouveau calque vide.
	 */
	@FXML
	public void handleNewLayer() {
		// peut être mieux fait dans project si on a le temps
		MainViewController.getInstance().getRightMenuController().handleAddNewLayer();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Dupliquer</b>.
	 * Duplique le calque sélectionné. Ajoute donc un nouveau calque contenant exactement la même chose que le calque dupliqué.
	 */
	@FXML
	public void handleDuplicateLayer() {
		Project.getInstance().addLayer(new Layer(Project.getInstance().getCurrentLayer(), false));
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Supprimer</b>.
	 * Supprime le calque actuellement sélectionné.
	 */
	@FXML
	public void handleDeleteLayer() {
		MainViewController.getInstance().getRightMenuController().handleDeleteLayer();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Nouveau</b>. Ajoute un nouveau calque vide.
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
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Fusionner avec le calque inférieur</b>.
	 * Fusionne le calque sélectionné avec le calque situé juste au dessous dans la liste de calques en un nouveau calque.
	 * Supprime les 2 calques fusionnés.
	 */
	@FXML
	public void handleFusionLayer() {
		MainViewController.getInstance().getRightMenuController().handleMergeLayer();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Aplatir les calques</b>.
	 * Fusionne tous les calques du projet en un nouveau calque. Tous les calques existants sont supprimés.
	 */
	@FXML
	public void handleMergeAllLayer() {
		MergeAllSave mas = new MergeAllSave();
		Layer resultLayer = new Layer(1, 1, true); // FIXME: 1,1 la taille??
		
		int i = 0;
		for (Layer layer : Project.getInstance().getLayers()) {
			if (i == Project.getInstance().getLayers().size() - 1) {
				resultLayer = resultLayer.mergeLayers(layer, false);
			} else {
				resultLayer = resultLayer.mergeLayers(layer, true);
			}
			i++;
		}
		Project.getInstance().getLayers().clear();
		Project.getInstance().addLayer(resultLayer);
		mas.execute();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Masquer/afficher le calque sélectionné</b>.
	 * Masque le calque sélectionné. Il n'est pas possible d'utiliser un outil sur un calque masqué.
	 */
	@FXML
	public void handleHideCurrentLayer() {
		Project.getInstance().getCurrentLayer().setVisible(!Project.getInstance().getCurrentLayer().isVisible());
		changeHideButtonText();
		Project.getInstance().drawWorkspace();
	}
	
	/**
	 * Modifie le texte du bouton <b>Calque -> Masquer/Afficher le calque sélectionné</b> en fonction de la visibilité du calque sélectionné.
	 * Affiche "Masquer le calque sélectionné" si le calque sélectionner est visible et "Afficher le calque sélectionné" sinon.
	 */
	public void changeHideButtonText() {
		if (Project.getInstance().getCurrentLayer().isVisible()) {
			menuBar_masquerCalques.setText("Masquer le calque sélectionné");
		} else {
			menuBar_masquerCalques.setText("Afficher le calque sélectionné");
		}
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Aide -> À propose</b>.
	 * Ouvre une fenêtre affichant quelques informations sur l'application (nom, version et auteurs).
	 */
	@FXML
	public void handleAboutUs() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/menubar/AboutUs.fxml"));
			Parent aboutUsWindow = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(aboutUsWindow));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleHelp() {
	
	}
	
	/**
	 * Permet d'activer les boutons des menus.
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
		menuBar_transformations.setDisable(false);
	}
	
	/**
	 * Permet de désactiver les boutons des menus.
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
		menuBar_transformations.setDisable(true);
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant l'action du menu <b>Calque -> Aplatir les calques</b> et définissant l'action à effectuer en cas d'appel
	 * à undo() ou redo() sur cette commande.
	 */
	public class MergeAllSave implements ICmd {
		private LinkedList<Layer> allMergedLayers; // liste de tous les layers qui ont été aplatis
		private Layer oldCurrentLayer; // enregistre l'ancien calque courant (sélectionné)
		private Layer newLayer; // le nouveau calque sur lequel seront fusionnés tous les calques du projet
		
		/**
		 * Construit une commande sauvegardant l'action du menu <b>Calque -> Aplatir les calques</b>.
		 */
		private MergeAllSave() {
			oldCurrentLayer = Project.getInstance().getCurrentLayer();
			allMergedLayers = new LinkedList<>();
			allMergedLayers.addAll(Project.getInstance().getLayers()); // ajoute tous les calques du projet à la liste allMergedLayers
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
		}
		
		@Override
		public void redo() {
			Project.getInstance().getLayers().clear();
			Project.getInstance().getLayers().add(newLayer);
			Project.getInstance().setCurrentLayer(newLayer);
		}
		
		@Override
		public String toString() {
			return "Aplatissement de tous les calques";
		}
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant l'action du menu <b>Fichier -> Importer une image</b> et définissant l'action à effectuer en cas d'appel
	 * à undo() ou redo() sur cette commande.
	 */
	public class ImportImageSave implements ICmd {
		private Layer oldCurrentLayer;
		private Layer importImageLayer;
		
		/**
		 * Construit une commande sauvegardant l'action du menu <b>Fichier -> Importer une image</b>.
		 */
		private ImportImageSave(){
			oldCurrentLayer = Project.getInstance().getCurrentLayer(); // calque sélectionné avant l'importation de l'image
		}

		@Override
		public void execute() {
			importImageLayer = Project.getInstance().getCurrentLayer(); // calque sélectionné après importation de l'image
			RecordCmd.getInstance().saveCmd(this);
		}

		@Override
		public void undo() {
			Project.getInstance().getLayers().remove(importImageLayer);
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
		}

		@Override
		public void redo() {
			Project.getInstance().addLayer(importImageLayer);
		}

		public String toString(){
		    return "Import d'image";
        }
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant l'action du menu <b>Fichier -> Ouvrir</b> et définissant l'action à effectuer en cas d'appel à undo()
	 * ou redo() sur cette commande.
	 */
	public class OpenSave implements  ICmd {

        @Override
        public void execute() {
            RecordCmd.getInstance().saveCmd(this);
        }

        @Override
        public void undo() {
	        // ne fait rien
        }

        @Override
        public void redo() {
	        // ne fait rien
        }

        @Override
        public String toString(){
            return "Ouverture du projet";
        }
    }
}
