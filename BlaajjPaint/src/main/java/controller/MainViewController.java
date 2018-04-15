package controller;


import controller.menubar.MenuBarController;
import controller.rightMenu.RightMenuController;
import controller.tools.ToolBarController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.Main;

import java.util.LinkedList;
import java.util.List;

public class MainViewController {

	public SaveProjects saveBlaajj;
	// Reference to the main application
	private Main main;
	
	
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
	
	
	/**
	 * Appelé par le main. Permet de récupérer une référence vers le main.
	 *
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;
		menuBarController.setMainViewController(this);
		toolBarController.setMainViewController(this);
		rightMenuController.setMainViewController(this);
	}
	
	public Main getMain() {
		return main;
	}
	
	public void createDrawZone(int width, int height){
		Canvas canvas = new Canvas(width,height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.BLUE);
		
		gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		System.out.println(canvas.getWidth() + " " + canvas.getHeight());
		Pane pane = new Pane();
		pane.getChildren().add(canvas);
		canvas.toFront();
		scrollPane.setContent(pane);
		
		
	}
	
	/*
	@FXML
	private MenuBar menuBar;
	
	
	@FXML
	private BorderPane borderPane;
	
	
	@FXML
	
	private Canvas canvas;
	
	
	@FXML
	
	private ColorPicker colorPicker;
	
	
	@FXML
	
	private TextField brushSize;
	
	
	@FXML
	
	private CheckBox gomme;
	
	public void initialize() {
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		canvas.setOnMouseDragged(e -> {
			
			double size = Double.parseDouble(brushSize.getText());
			
			double x = e.getX() - size;
			
			double y = e.getY() - size;
			
			
			if (gomme.isSelected()) {
				
				g.clearRect(x, y, size, size);
				
			} else {
				
				g.setFill(colorPicker.getValue());
				
				g.fillRect(x, y, size, size);
				
			}
			
		});
		
	}
	
	public void onSave() {
		
		try {
			
			Image snapshot = canvas.snapshot(null, null);
			
			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
			
		} catch (Exception e) {
			
			System.out.println("Failed to save image: " + e);
			
		}
	}
	*/
}