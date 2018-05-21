package controller.tools;

import controller.MainViewController;
import controller.tools.Shapes.EmptyRectangle;
import controller.tools.ToolDrawer.Eraser;
import controller.tools.ToolDrawer.Pencil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;

import java.io.IOException;

/**
 * Contrôleur associé au fichier FXML Toolbar.fxml et gérant l'ensemble des actions associées à la barre d'outils qui apparaît à gauche dans la GUI.
 */
public class ToolBarController {
	
	@FXML
	public ToggleButton handTool;
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
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Main</b>.
	 */
	@FXML
	public void handleHand() {
		Tool.setCurrentTool(Hand.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Déplacer</b>.
	 */
	@FXML
	public void handleMoveView() {
		Tool.setCurrentTool(Move.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Rogner</b>.
	 */
	@FXML
	public void handleCrop() {
		Tool.setCurrentTool(Crop.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamBar("/view/tools/ParamCrop.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Pipette</b>.
	 */
	@FXML
	public void handlePipette() {
		Tool.setCurrentTool(Pipette.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Pinceau</b>.
	 */
	@FXML
	public void handlePencil() {
		Tool.setCurrentTool(Pencil.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamBar("/view/tools/ParamDrawTool.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Gomme</b>.
	 */
	@FXML
	public void handleEraser() {
		Tool.setCurrentTool(Eraser.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamBar("/view/tools/ParamDrawTool.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Pot de peinture</b>.
	 */
	@FXML
	public void handleBucketFill() {
		Tool.setCurrentTool(BucketFill.getInstance());
		if (Tool.getToolHasChanged()) {
			closeCurrentParamBar();
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Texte</b>.
	 */
	@FXML
	public void handleAddText() {
		Tool.setCurrentTool(TextTool.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamBar("/view/tools/ParamText.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur l'outil <b>Formes</b>.
	 */
	@FXML
	public void handleAddShape() {
		Tool.setCurrentTool(EmptyRectangle.getInstance());
		if (Tool.getToolHasChanged()) {
			addParamBar("/view/tools/ParamShapeTool.fxml");
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Ouvre le fichier FXML situé au chemin fourni en paramètre.
	 *
	 * @param FXMLpath,
	 * 		le chemin menant au fichier FXML à ouvrir.
	 */
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
	
	/**
	 * Ferme le fichier FXML actuellement ouvert.
	 */
	@FXML
	private void closeCurrentParamBar() {
		MainViewController.getInstance().getParamBar().getChildren().remove(paramBar);
	}
	
	/**
	 * Permet d'activer les boutons de la barre d'outils.
	 */
	public void enableButton() {
		handTool.setDisable(false);
		moveTool.setDisable(false);
		cropTool.setDisable(false);
		pipetteTool.setDisable(false);
		pencilTool.setDisable(false);
		eraseTool.setDisable(false);
		bucketFillTool.setDisable(false);
		textTool.setDisable(false);
		shapeTool.setDisable(false);
	}
	
	/**
	 * Permet de désactiver les boutons de la barre d'outils.
	 */
	public void disableButton() {
		handTool.setDisable(true);
		moveTool.setDisable(true);
		cropTool.setDisable(true);
		pipetteTool.setDisable(true);
		pencilTool.setDisable(true);
		eraseTool.setDisable(true);
		bucketFillTool.setDisable(true);
		textTool.setDisable(true);
		shapeTool.setDisable(true);
	}
}