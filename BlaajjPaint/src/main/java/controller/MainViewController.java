package controller;

import controller.history.RecordCmd;
import controller.menubar.MenuBarController;
import controller.rightMenu.RightMenuController;
import controller.tools.ToolBarController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import main.Main;

import java.io.File;

public class MainViewController {
	
	private Project project; // FIXME: laissé pour code Jerem... Voir si il peut utililser le Singleton de Project
	
	public SaveProjects saveBlaajj;
	
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
	private AnchorPane paramBar;
	
	@FXML
	private ScrollPane scrollPane;
	
	private EventHandler<MouseEvent> eventHandler = null;
	
	@FXML
	private void initialize() {
		menuBarController.setMainViewController(this);
		toolBarController.setMainViewController(this);
		rightMenuController.setMainViewController(this);
		
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
	}
	
	public void showCanvas(Canvas canvas){
		scrollPane.setContent(canvas);
	}
	
	public Main getMain() {
		return main;
	}
	
	public void setEventHandler(EventHandler<MouseEvent> eventHandler) {
		if (this.eventHandler != null) {
			Project.getInstance().getCurrentCanvas().removeEventHandler(MouseEvent.MOUSE_CLICKED, this.eventHandler);
		}
		this.eventHandler = eventHandler;
		Project.getInstance().getCurrentCanvas().addEventHandler(MouseEvent.MOUSE_CLICKED, this.eventHandler);
		
	}
	
	// --------------------------------------Partie Jerem. Était contenu dans MasterController--------------------------------------------------
	private RecordCmd instance = RecordCmd.getInstance();
	private SaveProjects saveProjects = SaveProjects.getInstance();
	
	public void newModel() {
		project = Project.getInstance();
	}
	
	public void openModel(File f) {
		try {
			project = (Project) saveProjects.rebuild(f);
		} catch (Exception e) {
			//todo
		}
	}
	
	public void saveAsModel(File f) {
		saveProjects.generateCompact(f, project);
	}
	// -------------------------------------- Fin partie Jerem. Était contenu dans MasterController--------------------------------------------------

	// James

	public AnchorPane getParamBar() {
		return paramBar;
	}

	@FXML
	private void KeyPressed(KeyEvent event){
		KeyCombination cntrlN = new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlO = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		// New
		if(cntrlN.match(event)){
			menuBarController.openNewProjectWindows();
		}

		// Open
		if(cntrlO.match(event)){
			menuBarController.openProject();
		}

		// Undo
		if(cntrlZ.match(event)){
			menuBarController.undoAction();
		}

		// Redo
		if(cntrlMajZ.match(event)){
			menuBarController.redoAction();
		}

		//Save As
		if(cntrlMajS.match(event)){
			menuBarController.saveAs();
		}
	}
}