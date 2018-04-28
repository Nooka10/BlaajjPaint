package controller;

import controller.menubar.MenuBarController;
import controller.rightMenu.RightMenuController;
import controller.tools.ToolBarController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import main.Main;

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
	
	public Main getMain() {
		return main;
	}
	
	public void newCanevas(int width, int height) {
		project = Project.getInstance();
		project.setData(width, height);
	}
	
	public Project getCanvas() {
		return project;
	}
}