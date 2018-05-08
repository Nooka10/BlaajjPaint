package controller;

import controller.history.RecordCmd;
import controller.menubar.MenuBarController;
import controller.rightMenu.RightMenuController;
import controller.tools.ToolBarController;
import controller.tools.Zoom;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.Main;

import java.io.File;

public class MainViewController {
	
	private static MainViewController mainViewControllerInstance = new MainViewController(); // L'instance unique du singleton MainViewController
	public SaveProjects saveBlaajj;
	private Project project; // FIXME: laissé pour code Jerem... Voir si il peut utililser le Singleton de Project
	private Main main; // Reference to the main application
	@FXML
	private javafx.scene.canvas.Canvas canvas;
	@FXML
	private Parent menuBar;
	@FXML
	private Parent toolBar;
	@FXML
	private Parent rightMenu;
	@FXML
	private MenuBarController menuBarController; // le lien vers le menuBarController est fait automatiquement.
	@FXML
	private ToolBarController toolBarController; // le lien vers le toolBarController est fait automatiquement.
	@FXML
	private RightMenuController rightMenuController; // le lien vers le rightMenuController est fait automatiquement.
	@FXML
	private BorderPane mainView;
	@FXML
	private AnchorPane paramBar;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Label zoomLabel;
	
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
		return mainViewControllerInstance;
	}
	
	@FXML
	private void initialize() {
		mainViewControllerInstance = this;
		
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
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
	
	
	// --------------------------------------Partie Jerem. Était contenu dans MasterController--------------------------------------------------
	private RecordCmd instance = RecordCmd.getInstance();
	private SaveProjects saveProjects = SaveProjects.getInstance();
	
	public void newModel() {
		project = Project.getInstance();
	}

	/*
	public void openModel(File f) {
		try {
			project = (Project) saveProjects.rebuild(f);
		} catch (Exception e) {
			//todo
		}
	}
	
	public void saveAsModel(File f) {
		saveProjects.saveAs(f);
	}
	*/
	// -------------------------------------- Fin partie Jerem. Était contenu dans MasterController--------------------------------------------------
	
	
	public AnchorPane getParamBar() {
		return paramBar;
	}
	
	public RightMenuController getRightMenuController() {
		return rightMenuController;
	}
	
	@FXML
	private void KeyPressed(KeyEvent event) {
		KeyCombination cntrlN = new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlO = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajC = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		
		// New
		if (cntrlN.match(event)) {
			menuBarController.handleNew(null);
		}
		
		// Open
		if (cntrlO.match(event)) {
			menuBarController.handleOpen(null);
		}
		
		// Undo
		if (cntrlZ.match(event)) {
			menuBarController.handleUndo(null);
		}
		
		// Redo
		if (cntrlMajZ.match(event)) {
			menuBarController.handleRedo(null);
		}
		
		// Save As
		if (cntrlMajS.match(event)) {
			menuBarController.handleSaveAs(null);
		}
		
		// Add new layer
		if (cntrlMajC.match(event)) {
			Project.getInstance().addNewLayer();
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
		
		// select zoom tool
		if (event.getCode() == KeyCode.Z) {
			toolBarController.handleZoom(null);
		}
	}
	
	@FXML
	private void zoomIn() {
		Zoom.getInstance().zoomIn(Project.getInstance().getCurrentLayer().getWidth() / 2, Project.getInstance().getCurrentLayer().getHeight() / 2);
	}
	
	@FXML
	private void zoomOut() {
		Zoom.getInstance().zoomOut(Project.getInstance().getCurrentLayer().getWidth() / 2, Project.getInstance().getCurrentLayer().getHeight() / 2);
	}
	
	public void setTextZoomLabel(String text) {
		zoomLabel.setText(text);
	}
}