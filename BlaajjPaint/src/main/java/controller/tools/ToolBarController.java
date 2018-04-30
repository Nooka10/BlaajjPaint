package controller.tools;

import controller.MainViewController;
import controller.Project;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * This class represents the tool bar controller which is responsible for handling the operations that can be done from the tool bar as geometric shapes drawing, moving,
 * resizing, deleting and so on.
 */
public class ToolBarController {
	
	@FXML
	private ToggleButton brushTool;
	
	@FXML
	private ToggleButton eraseTool;
	
	@FXML
	private ToggleButton textTool;
	
	@FXML
	private ToggleButton lassoTool;
	
	@FXML
	private ToggleButton mouseTool;
	
	@FXML
	private ToggleButton moveTool;
	
	@FXML
	private AnchorPane toolBar;
	
	@FXML
	private ToggleGroup ToolBarButtons;
	
	@FXML
	private ToggleButton shapeTool;
	
	@FXML
	private ToggleButton handTool;
	
	@FXML
	private ToggleButton blurTool;
	
	@FXML
	private ToggleButton selectTool;
	
	@FXML
	private ToggleButton cropTool;
	
	@FXML
	private ToggleButton pipetteTool;
	
	@FXML
	private ToggleButton zoomTool;
	
	private MainViewController mainViewController; // Reference to the mainViewController
	
	private Tool currentTool; // contient une référence vers le tool actuellement sélectionné
	
	/**
	 * Appelé par le MainViewController pour donner une référence vers lui-même.
	 *
	 * @param mainViewController, une référence vers le mainViewController
	 *
	 *                            Créé par Benoît Schopfer
	 */
	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}
	
	@FXML
	void mainPaneId(ActionEvent event) {
	
	}
	
	@FXML
	void moveView(ActionEvent event) {
	
	}
	
	@FXML
	void select(ActionEvent event) {
	
	}
	
	@FXML
	void lasso(ActionEvent event) {
	
	}
	
	@FXML
	void crop(ActionEvent event) {
	
	}
	
	@FXML
	void pipette(ActionEvent event) {
	
	}
	
	@FXML
	void drawBrush(ActionEvent event) {
		if (currentTool == null || currentTool.toolType != Tool.ToolType.PENCIL) {
			if(currentTool != null) {
				currentTool.unregisterEventHandlers();
			}
			mainViewController.setEventHandler(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					currentTool = new Pencil(Project.getInstance().getCurrentCanvas());
				}
			});
			currentTool = new Pencil(Project.getInstance().getCurrentCanvas());
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/tools/ParamDrawTool.fxml"));
				Parent param = fxmlLoader.load();
				AnchorPane paramBar = mainViewController.getParamBar();
				paramBar.getChildren().add(param);
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	void brushTool(ActionEvent event) {
	}
	
	@FXML
	void erase(ActionEvent event) {
		if (currentTool == null || currentTool.toolType != Tool.ToolType.ERASER) {
			if(currentTool != null) {
				currentTool.unregisterEventHandlers();
			}
			mainViewController.setEventHandler(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					currentTool = new Eraser(Project.getInstance().getCurrentCanvas());
				}
			});
			currentTool = new Eraser(Project.getInstance().getCurrentCanvas());
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/tools/ParamDrawTool.fxml"));
				Parent param = fxmlLoader.load();
				mainViewController.getParamBar().getChildren().add(param);
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	void blur(ActionEvent event) {
	
	}
	
	@FXML
	void addText(ActionEvent event) {
	
	}
	
	@FXML
	void mouse(ActionEvent event) {
	
	}
	
	@FXML
	void addShape(ActionEvent event) {
	
	}
	
	@FXML
	void hand(ActionEvent event) {
	
	}
	
	@FXML
	void zoom(ActionEvent event) {
	
	}
	
	public static void displayError() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Erreur, aucune image n'est ouverte!");
		alert.setHeaderText(null);
		alert.setContentText("Commencez par créer un nouveau projet ou ouvrir un projet existant.");
		
		alert.showAndWait();
	}
}