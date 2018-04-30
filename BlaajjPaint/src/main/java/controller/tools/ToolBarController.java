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
	
	private ParamDrawToolController paramDrawToolControler;
	
	private Parent paramBar;
	
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
	void handleMoveView(ActionEvent event) {
	
	}
	
	@FXML
	void handleSelect(ActionEvent event) {
	
	}
	
	@FXML
	void handleLasso(ActionEvent event) {
	
	}
	
	@FXML
	void handleCrop(ActionEvent event) {
	
	}
	
	@FXML
	void handlePipette(ActionEvent event) {
	
	}
	
	@FXML
	void handlePencil(ActionEvent event) {
		if (currentTool == null || currentTool.toolType != Tool.ToolType.PENCIL) {
			if (currentTool != null) {
				currentTool.unregisterEventHandlers();
			}
			addParamDrawBar("/view/tools/ParamDrawTool.fxml");
			mainViewController.setEventHandler(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					currentTool = new Pencil(Project.getInstance().getCurrentCanvas(), paramDrawToolControler.getThicknessValue(), paramDrawToolControler.getOpacityValue());
				}
			});
			currentTool = new Pencil(Project.getInstance().getCurrentCanvas(), paramDrawToolControler.getThicknessValue(), paramDrawToolControler.getOpacityValue());
		}
	}
	
	@FXML
	void handleEraser(ActionEvent event) {
		if (currentTool == null || currentTool.toolType != Tool.ToolType.ERASER) {
			if (currentTool != null) {
				currentTool.unregisterEventHandlers();
			}
			addParamDrawBar("/view/tools/ParamDrawTool.fxml");
			mainViewController.setEventHandler(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					currentTool = new Eraser(Project.getInstance().getCurrentCanvas(), paramDrawToolControler.getThicknessValue());
				}
			});
			currentTool = new Eraser(Project.getInstance().getCurrentCanvas(), paramDrawToolControler.getThicknessValue());
		}
	}
	
	@FXML
	void handleBlur(ActionEvent event) {
	
	}
	
	@FXML
	void handleAddText(ActionEvent event) {
	
	}
	
	@FXML
	void handleMouse(ActionEvent event) {
	
	}
	
	@FXML
	void handleAddShape(ActionEvent event) {
	
	}
	
	@FXML
	void handleHand(ActionEvent event) {
	
	}
	
	@FXML
	void handleZoom(ActionEvent event) {
	
	}
	
	public static void displayError() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Erreur, aucune image n'est ouverte!");
		alert.setHeaderText(null);
		alert.setContentText("Commencez par créer un nouveau projet ou ouvrir un projet existant.");
		
		alert.showAndWait();
	}
	
	private void addParamDrawBar(String FXMLpath) {
		double thicknessValue = 10; // FIXME: valeur par défaut pour l'épaisseur
		double opacityValue = 10; // FIXME: valeur par défat pour l'opacité
		if (paramBar != null) {
			thicknessValue = paramDrawToolControler.getThicknessValue();
			opacityValue = paramDrawToolControler.getOpacityValue();
			mainViewController.getParamBar().getChildren().remove(paramBar);
		}
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLpath));
			paramBar = fxmlLoader.load();
			paramDrawToolControler = fxmlLoader.getController();
			mainViewController.getParamBar().getChildren().add(paramBar);
			paramDrawToolControler.setThicknessValue(thicknessValue);
			paramDrawToolControler.setOpacityValue(opacityValue);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}