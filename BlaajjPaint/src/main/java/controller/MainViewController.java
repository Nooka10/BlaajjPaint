package controller;

import controller.history.RecordCmd;
import controller.menubar.MenuBarController;
import controller.rightMenu.RightMenuController;
import controller.tools.ToolBarController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import main.Main;
import utils.SaveProject;

import java.io.File;

public class MainViewController {
	
	private static MainViewController mainViewControllerInstance = null; // L'instance unique du singleton MainViewController
	private Main main; // Reference to the main application
	@FXML
	private MenuBarController menuBarController; // le lien vers le menuBarController est fait automatiquement.
	@FXML
	private ToolBarController toolBarController; // le lien vers le toolBarController est fait automatiquement.
	@FXML
	private RightMenuController rightMenuController; // le lien vers le rightMenuController est fait automatiquement.
	@FXML
	private AnchorPane paramBar;
	@FXML
	private AnchorPane anchorPaneCenter;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private AnchorPane workspace;
	
	/**
	 * Constructeur privé (modèle Singleton)
	 */
	private MainViewController() {
	}
	
	/**
	 * Retourne l'instance unique du singleton MainViewController
	 *
	 * @return l'instance unique du singleton MainViewController
	 */
	public static MainViewController getInstance() {
		if (mainViewControllerInstance == null) {
			mainViewControllerInstance = new MainViewController();
		}
		return mainViewControllerInstance;
	}
	
	@FXML
	private void initialize() {
		mainViewControllerInstance = this;
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		disableButton();
	}
	
	public Main getMain() {
		return main;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public ScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public AnchorPane getAnchorPaneCenter() {
		return anchorPaneCenter;
	}
	
	public void resetScrollPane() {
		scrollPane = new ScrollPane();
	}
	
	public AnchorPane getParamBar() {
		return paramBar;
	}
	
	public RightMenuController getRightMenuController() {
		return rightMenuController;
	}
	
	@FXML
	private void KeyPressed(KeyEvent event) {
		KeyCombination cntrlN = new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlO = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajC = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		
		// New
		if (cntrlN.match(event)) {
			menuBarController.handleNew();
		}
		
		// Open
		if (cntrlO.match(event)) {
			menuBarController.handleOpen();
		}
		
		// Undo
		if (cntrlZ.match(event)) {
			menuBarController.handleUndo();
		}
		
		// Redo
		if (cntrlMajZ.match(event)) {
			menuBarController.handleRedo();
		}
		
		// Save As
		if (cntrlMajS.match(event)) {
			menuBarController.handleSaveAs();
		}

		// Save
		if (cntrlS.match(event)) {
			menuBarController.handleSave();
		}
		
		// Add new layer
		if (cntrlMajC.match(event)) {
			// Appelle de la fonction addNewLayer de RightMenu (action du clic sur le bouton + des calques)
			// Peut être mieux fait et mettre dans project si on a du temps
			MainViewController.getInstance().getRightMenuController().addNewLayer();
		}
		
		// delete current layer
		if (event.getCode() == KeyCode.DELETE) {
			Project.getInstance().deleteCurrentLayer();
		}
		
		// select pencil tool
		if (event.getCode() == KeyCode.B) {
			toolBarController.handlePencil(null);
		}
		
		// select eraser tool
		if (event.getCode() == KeyCode.E) {
			toolBarController.handleEraser(null);
		}
	}

	/**
	 * Permet de fermer le projet en cours d'execution
	 * Permet aussi le nettoyage du projet (mise à 0)
	 */
	public void closeProject(){
		Project.getInstance().close();
		RecordCmd.getInstance().clear();
		MainViewController.getInstance().getRightMenuController().clearLayerList();
		MainViewController.getInstance().getScrollPane().setContent(null);
		SaveProject.getInstance().clear();
		
		// désactive les bouttons
		disableButton();
	}

	public void openProject(){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".blaaj files(*.blaajj)", "*.blaajj");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(MainViewController.getInstance().getMain().getPrimaryStage());

		if (file != null) {
			closeProject();
			SaveProject.getInstance().openFile(file);
		}

		// réactive les bouttons
		enableButton();
	}

	/**
	 * Permet la sauvegarde du projet depuis n'importe quelle autre aubjet du package.
	 */
	public void saveAs(){
		menuBarController.handleSaveAs();
	}
	
	
	/**
	 * Permet de déscativer les bouttons.
	 * A appeler à la fermeture d'un projet ou à la création de l'application
	 */
	public void disableButton(){
		menuBarController.disableButton();
		rightMenuController.disableButton();
		toolBarController.disableButton();
	}

	/**
	 * Permet d'activer les bouttons.
	 * A appeler dès qu'un project est ouvert ou créé
	 */
	public void enableButton(){
		menuBarController.enableButton();
		rightMenuController.enableButton();
		toolBarController.enableButton();
	}
	
	public AnchorPane getWorkspace() {
		return workspace;
	}
}