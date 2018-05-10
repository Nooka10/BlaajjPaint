package controller.tools;

import controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * This class represents the tool bar controller which is responsible for handling the operations that can be done from the tool bar as geometric shapes drawing, moving,
 * resizing, deleting and so on.
 */
public class ToolBarController {
	
	private ParamDrawToolController paramDrawToolControler;
	
	private Parent paramBar;
	
	/* attributs pour FXML */
	
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
	
	@FXML
	public void handleMoveView(ActionEvent event) {
		Tool.setCurrentTool(Move.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleSelect(ActionEvent event) {
	
	}
	
	@FXML
	public void handleLasso(ActionEvent event) {
	
	}
	
	@FXML
	public void handleCrop(ActionEvent event) {
	
	}
	
	@FXML
	public void handlePipette(ActionEvent event) {
		Tool.setCurrentTool(Pipette.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handlePencil(ActionEvent event) {
		Tool.setCurrentTool(Pencil.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamDrawBar("/view/tools/ParamDrawTool.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleEraser(ActionEvent event) {
		Tool.setCurrentTool(Eraser.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamDrawBar("/view/tools/ParamDrawTool.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleBlur(ActionEvent event) {
	
	}
	
	@FXML
	public void handleAddText(ActionEvent event) {
	
	}
	
	@FXML
	public void handleMouse(ActionEvent event) {
	
	}
	
	@FXML
	public void handleAddShape(ActionEvent event) {
	
	}
	
	@FXML
	public void handleHand(ActionEvent event) {
		Tool.setCurrentTool(Hand.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleZoom(ActionEvent event) {
		Tool.setCurrentTool(Zoom.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	public static void displayError() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Erreur, aucune image n'est ouverte!");
		alert.setHeaderText(null);
		alert.setContentText("Commencez par créer un nouveau projet ou ouvrir un projet existant.");
		
		alert.showAndWait();
	}
	
	private void addParamDrawBar(String FXMLpath) {
		if (paramBar != null) { // une barre de paramètre est déjà affichée --> on la supprime
			closeCurrentParamBar();
		}
		// on crée la nouvelle barre de paramètre
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLpath));
			paramBar = fxmlLoader.load();
			paramDrawToolControler = fxmlLoader.getController();
			MainViewController.getInstance().getParamBar().getChildren().add(paramBar); // on ajoute la barre de paramètre au MainViewController
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeCurrentParamBar(){
		MainViewController.getInstance().getParamBar().getChildren().remove(paramBar);
	}
}