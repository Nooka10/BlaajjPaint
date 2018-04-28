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
import javafx.scene.input.MouseEvent;
import main.Main;

import java.io.File;

public class MainViewController {
	
	public SaveProjects saveBlaajj;
	
	private Main main; // Reference to the main application
	
	private Project project;
	
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
	private ScrollPane scrollPane;
	
	private EventHandler<MouseEvent> eventHandler;
	
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
	
	public Project getCanvas() {
		return project;
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
}