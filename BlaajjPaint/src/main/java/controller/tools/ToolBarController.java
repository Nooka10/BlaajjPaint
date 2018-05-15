package controller.tools;

import controller.MainViewController;
import controller.tools.Shapes.EmptyRectangle;
import controller.tools.ToolDrawer.Eraser;
import controller.tools.ToolDrawer.Pencil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * This class represents the tool bar controller which is responsible for handling the operations that can be done from the tool bar as geometric shapes drawing, moving,
 * resizing, deleting and so on.
 */
public class ToolBarController {
	
	@FXML
	public AnchorPane toolBar;
	@FXML
	public ToggleButton handTool;
	@FXML
	public ToggleGroup ToolBarButtons;
	@FXML
	public ToggleButton moveTool;
	@FXML
	public ToggleButton cropTool;
	@FXML
	public ToggleButton pipetteTool;
	@FXML
	public ToggleButton pencilTool;
	@FXML
	public ToggleButton eraseTool;
	@FXML
	public ToggleButton bucketFillTool;
	@FXML
	public ToggleButton textTool;
	@FXML
	public ToggleButton shapeTool;
	@FXML
	private Parent paramBar;
	
	@FXML
	public void handleMoveView(ActionEvent event) {
		Tool.setCurrentTool(Move.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleCrop(ActionEvent event) {
		Tool.setCurrentTool(Crop.getInstance());
		if(Tool.getToolHasChanged()){
			addParamBar("/view/tools/ParamCrop.fxml");
			Tool.setToolHasChanged(false);
		}
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
			addParamBar("/view/tools/ParamDrawTool.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleEraser(ActionEvent event) {
		Tool.setCurrentTool(Eraser.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamBar("/view/tools/ParamDrawTool.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleBucketFill(ActionEvent event) {
		Tool.setCurrentTool(BucketFill.getInstance());
		if(Tool.getToolHasChanged()){
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleAddText(ActionEvent event) {
		Tool.setCurrentTool(TextTool.getInstance());
		if(Tool.getToolHasChanged()){
			addParamBar("/view/tools/ParamText.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	@FXML
	public void handleAddShape(ActionEvent event) {
		Tool.setCurrentTool(EmptyRectangle.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamBar("/view/tools/ParamShapeTool.fxml");
			Tool.setToolHasChanged(false);
		}
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
	private void addParamBar(String FXMLpath) {
		if (paramBar != null) { // une barre de paramètre est déjà affichée --> on la supprime
			closeCurrentParamBar();
		}
		// on crée la nouvelle barre de paramètre
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLpath));
			paramBar = fxmlLoader.load();
			MainViewController.getInstance().getParamBar().getChildren().add(paramBar); // on ajoute la barre de paramètre au MainViewController
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void closeCurrentParamBar(){
		MainViewController.getInstance().getParamBar().getChildren().remove(paramBar);
	}

	/**
	 * Permet d'activer les bouttons.
	 * A appeler dès qu'un project est ouvert ou créé
	 */
	public void enableButton(){

	}

	/**
	 * Permet de déscativer les bouttons.
	 * A appeler à la fermeture d'un projet ou à la création de l'application
	 */
	public void disableButton(){

	}
	
	
}