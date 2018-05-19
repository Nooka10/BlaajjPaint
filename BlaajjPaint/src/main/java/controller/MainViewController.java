package controller;

import controller.history.RecordCmd;
import controller.menubar.MenuBarController;
import controller.rightMenu.RightMenuController;
import controller.tools.ToolBarController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import main.Main;
import utils.SaveProject;

import java.io.File;

/**
 * Contrôleur associé au fichier FXML MainView.fxml et gérant l'ensemble de la GUI ainsi que la communication entre les différents contrôleurs.
 */
public class MainViewController {
	
	private static MainViewController mainViewControllerInstance; // L'instance unique du singleton MainViewController
	private Main main; // référence vers l'instance du main
	@FXML
	private MenuBarController menuBarController; // référence vers l'instance du menuBarController
	@FXML
	private ToolBarController toolBarController; // référence vers l'instance du toolBarController
	@FXML
	private RightMenuController rightMenuController; // référence vers l'instance du rightMenuController
	@FXML
	private AnchorPane anchorPaneCenter; // référence vers l'anchorPane contenant le workspace et la paramBar
	@FXML
	private AnchorPane paramBar; // référence vers l'instance de la barre de paramètre d'outils actuellement ouverte (peut être null)
	@FXML
	private ScrollPane scrollPane; // référence vers le scrollPane contenant le l'espace de travail (workspace)
	
	/**
	 * Constructeur privé (modèle Singleton).
	 */
	private MainViewController() {
	}
	
	/**
	 * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		mainViewControllerInstance = this;
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		disableButtons();
	}
	
	/**
	 * Retourne l'instance unique du singleton MainViewController.
	 *
	 * @return l'instance unique du singleton MainViewController.
	 */
	public static MainViewController getInstance() {
		if (mainViewControllerInstance == null) {
			mainViewControllerInstance = new MainViewController();
		}
		return mainViewControllerInstance;
	}
	
	/**
	 * Retourne l'instance du main.
	 *
	 * @return l'instance du main.
	 */
	public Main getMain() {
		return main;
	}
	
	/**
	 * Permet de définir l'instance du main à utiliser.
	 *
	 * @param main,
	 * 		l'instance du main à utiliser.
	 */
	public void setMain(Main main) {
		this.main = main;
	}
	
	/**
	 * Retourne l'instance du menuBarController.
	 *
	 * @return l'instance du menuBarController.
	 */
	public MenuBarController getMenuBarController() {
		return menuBarController;
	}
	
	/**
	 * Retourne l'instance du toolBarController
	 *
	 * @return l'instance du toolBarController
	 */
	public ToolBarController getToolBarController() {
		return toolBarController;
	}
	
	/**
	 * Retourne l'instance du rightMenuController.
	 *
	 * @return l'instance du rightMenuController.
	 */
	public RightMenuController getRightMenuController() {
		return rightMenuController;
	}
	
	/**
	 * Retourne le scrollPane contenant l'espace de travail.
	 *
	 * @return le scrollPane contenant l'espace de travail.
	 */
	public ScrollPane getScrollPane() {
		return scrollPane;
	}
	
	/**
	 * Retourne l'anchorPane contenant la paramBar et le scrollPane.
	 *
	 * @return l'anchorPane contenant la paramBar et le scrollPane.
	 */
	public AnchorPane getAnchorPaneCenter() {
		return anchorPaneCenter;
	}
	
	/**
	 * Retourne l'instance actuelle de la paramBar. Peut être null si l'outil actuellement sélectionné n'utilise pas de barre de paramètre.
	 *
	 * @return l'instance actuelle de la paramBar.
	 */
	public AnchorPane getParamBar() {
		return paramBar;
	}
	
	/**
	 * Désactive les boutons de la menuBar, du rightMenu ainsi que de la toolBar.
	 */
	private void disableButtons() {
		menuBarController.disableButton();
		rightMenuController.disableButton();
		toolBarController.disableButton();
	}
	
	/**
	 * Active les boutons de la menuBar, du rightMenu ainsi que de la toolBar.
	 */
	public void enableButtons() {
		menuBarController.enableButton();
		rightMenuController.enableButton();
		toolBarController.enableButton();
	}
	
	/**
	 * Affiche une fenêtre permettant à l'utilisateur de choisir un projet avec l'extension .blaajj et de l'ouvrir.
	 */
	public void openProject() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".blaaj files(*.blaajj)", "*.blaajj");
		fileChooser.getExtensionFilters().add(extFilter);
		
		// Ouvre une fenêtre invitant l'utilisateur à sélectionner le fichier .blaajj à ouvrir
		File file = fileChooser.showOpenDialog(MainViewController.getInstance().getMain().getPrimaryStage());
		
		if (file != null) {
			closeProject(); // ferme le projet précédemment ouvert
			try {
				SaveProject.getInstance().openFile(file); // déserialise le fichier .blaajj et l'ouvre dans le programme.
				enableButtons(); // active les boutons des différents menus
			} catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("File is corrupted");
			}
		}
	}
	
	/**
	 * Ferme le projet en cours d'édition.
	 */
	public void closeProject() {
		Project.getInstance().close(); // ferme le projet actuellement ouvert
		RecordCmd.getInstance().clear(); // supprimer l'historique des commandes (la liste des undo/redo)
		SaveProject.getInstance().clear(); // réinitialise la sauvegarde
		scrollPane.setContent(null);
		rightMenuController.clearLayerList(); // vide l'historique sans le redessiner
		rightMenuController.clearHistoryList();
		
		disableButtons(); // désactive les boutons de la GUI qui ne peuvent pas être utilisés sans avoir un projet ouvert
	}
	
	/**
	 * Méthode appelée lorsqu'une touche du clavier de l'utilisateur est pressée.
	 *
	 * @param event,
	 * 		l'évènement du clavier qui a déclanché l'appel à cette méthode.
	 */
	@FXML
	private void handleOnKeyPressed(KeyEvent event) {
		// raccourci clavier permettant de créer un nouveau projet
		if (event.isControlDown() && event.getCode() == KeyCode.N) {
			menuBarController.handleNew();
		}
		
		// raccourci clavier permettant d'ouvrir un projet
		if (event.isControlDown() && event.getCode() == KeyCode.O) {
			menuBarController.handleOpen();
		}
		
		// FIXME: vérifier qu'un projet est ouvert pour activer les raccourci ci-dessous...!
		try {
			// raccourci clavier permettant de fermer un projet
			if (event.isControlDown() && event.getCode() == KeyCode.Q) {
				menuBarController.handleClose();
			}
			
			// raccourci clavier permettant d'exporter le projet en JPG ou PNG
			if (event.isControlDown() && event.getCode() == KeyCode.E) {
				menuBarController.handleExport();
			}
			
			// raccourci clavier permettant d'importer une image JPG ou PNG en tant que nouveau calque dans le projet
			if (event.isControlDown() && event.getCode() == KeyCode.I) {
				menuBarController.handleImportImage();
			}
			
			// raccourci clavier permettant d'annuler (undo) la dernière action
			if (event.isControlDown() && event.getCode() == KeyCode.Z) {
				menuBarController.handleUndo();
			}
			
			// raccourci clavier permettant de rétablir (redo) la dernière action annulée
			if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.Z) {
				menuBarController.handleRedo();
			}
			
			// raccourci clavier permettant d'ouvrir le menu enregistrer sous
			if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.S) {
				menuBarController.handleSaveAs();
			}
			
			// raccourci clavier permettant d'ouvrir un nouveau projet
			if (event.isControlDown() && event.getCode() == KeyCode.S) {
				menuBarController.handleSave();
			}
			
			// raccourci clavier permettant d'ajouter un nouveau calque au projet
			if (event.isControlDown() && event.getCode() == KeyCode.D) {
				menuBarController.handleDuplicateLayer();
			}
			
			// raccourci clavier permettant d'ajouter un nouveau calque au projet
			if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.J) {
				rightMenuController.handleAddNewLayer();
			}
			
			// raccourci clavier permettant de supprimer le calque actuellement sélectionné du projet
			if (event.getCode() == KeyCode.DELETE) {
				menuBarController.handleDeleteLayer();
			}
		} catch (Exception e){
			System.out.println("Aucun projet n'est ouvert!");
		}
	}
}