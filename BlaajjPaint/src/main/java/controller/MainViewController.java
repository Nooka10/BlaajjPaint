package controller;

import controller.history.RecordCmd;
import controller.menubar.MenuBarController;
import controller.rightMenu.RightMenuController;
import controller.tools.ToolBarController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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
	
	@FXML
	private Label zoomLabel;
	
	private EventHandler<MouseEvent> eventHandler = null;
	
	private double zoom = 1.0;
	
	@FXML
	private void initialize() {
		menuBarController.setMainViewController(this);
		toolBarController.setMainViewController(this);
		rightMenuController.setMainViewController(this);
		
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
	}
	
	// TODO : mais Benoit cette fonction ne doit pas être la..
	public void drawLayers(Group group){
		scrollPane.setContent(group);
	}
	
	public void setMain(Main main){
		this.main = main;
	}
	
	public Main getMain() {
		return main;
	}
	
	public void setEventHandler(EventHandler<MouseEvent> eventHandler) {
		if (this.eventHandler != null) {
			Project.getInstance().getCurrentLayer().removeEventHandler(MouseEvent.MOUSE_CLICKED, this.eventHandler);
		}
		this.eventHandler = eventHandler;
		Project.getInstance().getCurrentLayer().addEventHandler(MouseEvent.MOUSE_CLICKED, this.eventHandler);
		
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

	public RightMenuController getRightMenuController(){
		return rightMenuController;
	}

	@FXML
	private void KeyPressed(KeyEvent event){
		KeyCombination cntrlN = new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlO = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlMajC = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination delete = new KeyCodeCombination(KeyCode.DELETE);
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

		if(cntrlMajC.match(event)){
		    Project.getInstance().addNewLayer();
        }

        if(delete.match(event)){
		    Project.getInstance().deleteCurrentLayer();
        }
	}
	
	@FXML
	private void zoomIn(){
		zoom *= 2;
		Project.getInstance().getBackgroungImage().setScaleX(zoom);
		Project.getInstance().getBackgroungImage().setScaleY(zoom);
		for (Layer layer : Project.getInstance().getLayers()){
			layer.setScaleX(zoom);
			layer.setScaleY(zoom);
		}
		zoomLabel = new Label(zoom*100 +"%");
		
	}
	
	@FXML
	private void zoomOut(){
		zoom /= 2;
		Project.getInstance().getBackgroungImage().setScaleX(zoom);
		Project.getInstance().getBackgroungImage().setScaleY(zoom);
		for (Layer layer : Project.getInstance().getLayers()){
			layer.setScaleX(zoom);
			layer.setScaleY(zoom);
			
		}
		zoomLabel = new Label(zoom*100 +"%");
	}
}