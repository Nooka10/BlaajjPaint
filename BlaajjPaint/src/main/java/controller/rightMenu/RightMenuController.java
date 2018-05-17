package controller.rightMenu;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.Iterator;

/**
 * Contrôleur associé au fichier FXML RightMenu.fxml et gérant l'ensemble des actions associées à la barre de menu de droite de la GUI.
 */
public class RightMenuController {
	@FXML
	private VBox layersList;
	
	@FXML
	private VBox historyList;
	
	@FXML
	private Button fusion;
	
	@FXML
	private Button upLayer;
	
	@FXML
	private Button addNewLayer;
	
	@FXML
	private Button deleteLayer;
	
	@FXML
	private Button downLayer;
	
	@FXML
	private ColorPicker colorPicker;
	
	@FXML
	private Slider opacitySlider;
	
	@FXML
	public TextField opacityTextField;
	
	@FXML
	private AnchorPane rightMenu;
	
	private double oldOpacity;
	
	private double newOpacity;
	
	// définit la couleur du background d'un élément sélectionné dans la liste des calques en bas à droite de la GUI
	private final static Background focusBackground = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
	
	// définit la couleur du background d'un élément non sélectionné dans la liste des calques en bas à droite de la GUI
	private final static Background unfocusBackground = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	
	/**
	 * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		colorPicker.setValue(Color.BLACK);
		
		// Ajoute un changeListener à opacityTextField -> la méthode changed() est appelée à chaque fois que le texte de opacityTextField est modifié
		opacityTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
						newOpacity = Integer.parseInt(newValue); // parse en int ou lance une NumberFormatException si le parsing n'est pas possible
						if (newOpacity < 0) { // 0 est la valeur minimale de l'opacité
							opacityTextField.setText("0");
							newOpacity = 0;
						} else if (newOpacity > 100) { // 100 est la valeur maximale de l'opacité
							opacityTextField.setText("100");
							newOpacity = 100;
						}
						opacitySlider.setValue(newOpacity);
					} catch (NumberFormatException e) { // une erreur s'est produite pendant le parsing en int -> l'entrée est invalide
					if (!oldValue.isEmpty()) {
						opacityTextField.setText(oldValue); // on annule la dernière modification
					}
				}
			}
		});
		
		// Ajoute un changeListener à opacityTextField -> la méthode changed() est appelée à chaque fois que le texte est désélectionné
		opacityTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (oldPropertyValue) {
					Project.getInstance().getCurrentLayer().setLayerOpacity(newOpacity); // enregistre la modification de l'opacité dans l'historique
				}
			}
		});
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique avec la souris sur le slider d'opacité du calque, au centre à droite de la GUI.
	 * Modifie l'opacité du calque en fonction de la valeur du slider et met à jour le textField de l'opacité.
	 */
	@FXML
	void handleOnMousePressed() {
		oldOpacity = Math.round(opacitySlider.getValue());
		opacityTextField.setText(String.valueOf(oldOpacity));
		Project.getInstance().getCurrentLayer().updateLayerOpacity(oldOpacity);
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique, reste appuyé et glisse avec la souris sur le slider d'opacité du calque, au centre à droite de la GUI.
	 * Modifie l'opacité du calque en fonction de la valeur du slider et met à jour le textField de l'opacité.
	 */
	@FXML
	void handleOnMouseDragged() {
		newOpacity = opacitySlider.getValue();
		opacityTextField.setText(String.valueOf(newOpacity));
		Project.getInstance().getCurrentLayer().updateLayerOpacity(newOpacity);
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur relâche le clic de la souris sur le slider d'opacité du calque, au centre à droite de la GUI.
	 * Modifie l'opacité du calque en fonction de la valeur du slider et met à jour le textField de l'opacité.
	 */
	@FXML
	void handleOnMouseReleased() {
		newOpacity = Math.round(opacitySlider.getValue());
		opacityTextField.setText(String.valueOf(newOpacity));
		Project.getInstance().getCurrentLayer().setLayerOpacity(oldOpacity, newOpacity);
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le sélecteur de couleur (ColorPicker), tout en haut à droite de la GUI.
	 * Modifie la couleur actuellement sélectionnée.
	 */
	@FXML
	void handleSelectColor() {
		Project.getInstance().setCurrentColor(colorPicker.getValue());
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton '+', en bas à droite de la GUI.
	 * Ajoute un nouveau calque vide au projet et le définit comme calque actif.
	 * @apiNote : ATTENTION, cette méthode est sauvegardée dans l'historique. Il ne faut pas l'appeler à l'intérieur d'une autre sauvegarde.
	 */
	@FXML
	public void handleAddNewLayer() {
		NewLayerSave ls = new NewLayerSave();
		Project.getInstance().addNewLayer();
		ls.execute();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton '-', en bas à droite de la GUI.
	 * Supprime le calque sélectionné.
	 * @apiNote : ATTENTION, cette méthode est sauvegardée dans l'historique. Il ne faut pas l'appeler à l'intérieur d'une autre sauvegarde.
	 */
	@FXML
	public void handleDeleteLayer() {
		if (Project.getInstance().getLayers().size() != 1) {
			DeleteLayerSave dls = new DeleteLayerSave();
			Project.getInstance().deleteCurrentLayer();
			dls.execute();
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton 'v', en bas à droite de la GUI.
	 * Descend le calque sélectionné d'un cran dans la liste des calques.
	 * @apiNote : ATTENTION, cette méthode est sauvegardée dans l'historique. Il ne faut pas l'appeler à l'intérieur d'une autre sauvegarde.
	 */
	@FXML
	void handleDownLayer() {
		int index = Project.getInstance().getLayers().indexOf(Project.getInstance().getCurrentLayer());
		if (index < Project.getInstance().getLayers().size() - 1) {
			new LayerPosSave(index).execute();
			Collections.swap(Project.getInstance().getLayers(), index, index + 1);
			Project.getInstance().drawWorkspace();
			MainViewController.getInstance().getRightMenuController().updateLayerList();
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton '^', en bas à droite de la GUI.
	 * Monte le calque sélectionné d'un cran dans la liste des calques.
	 * @apiNote : ATTENTION, cette méthode est sauvegardée dans l'historique. Il ne faut pas l'appeler à l'intérieur d'une autre sauvegarde.
	 */
	@FXML
	void handleUpLayer() {
		int index = Project.getInstance().getLayers().indexOf(Project.getInstance().getCurrentLayer());
		if (index != 0) {
			new LayerPosSave(index - 1).execute();
			Collections.swap(Project.getInstance().getLayers(), index, index - 1);
			Project.getInstance().drawWorkspace();
			MainViewController.getInstance().getRightMenuController().updateLayerList();
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton 'fusion', en bas à droite de la GUI.
	 * Fusionne le calque sélectionné avec le calque situé juste au dessous dans la liste des calques. Ne fait rien si le calque sélectionné est le dernier de la liste.
	 * @apiNote : ATTENTION, cette méthode est sauvegardée dans l'historique. Il ne faut pas l'appeler à l'intérieur d'une autre sauvegarde.
	 */
	@FXML
	public void handleMergeLayer() {
		Layer currentLayer = Project.getInstance().getCurrentLayer();
		int index = Project.getInstance().getLayers().indexOf(currentLayer);
		if (index != Project.getInstance().getLayers().size() - 1) {
			MergeSave ms = new MergeSave();
			Layer backgroundLayer = Project.getInstance().getLayers().get(index + 1);
			Layer mergeLayer = currentLayer.mergeLayers(backgroundLayer, false);
			Project.getInstance().getLayers().remove(currentLayer);
			Project.getInstance().getLayers().remove(backgroundLayer);
			Project.getInstance().addLayer(mergeLayer);
			ms.execute();
		}
	}
	
	/**
	 * Active les clics de souris dans la liste des calques en bas à droite de la GUI.
	 */
	public void activateLayerListClick() {
		layersList.setDisable(false);
	}
	
	/**
	 * Désactive les clics de souris dans la liste des calques en bas à droite de la GUI.
	 */
	public void disableLayerListClick() {
		layersList.setDisable(true);
	}
	
	/**
	 * Définit la valeur du slider d'opacité.
	 * @param opacitySlider, la valeur à attribuer au slider.
	 */
	public void setOpacitySlider(double opacitySlider) {
		this.opacitySlider.setValue(opacitySlider);
	}
	
	/**
	 * Définit la valeur du textField d'opacité.
	 * @param opacityTextField, la valeur à attribuer au textField.
	 */
	public void setOpacityTextField(String opacityTextField) {
		this.opacityTextField.setText(opacityTextField);
	}
	
	
	/**
	 * Définit la couleur actuellement sélectionnée dans le sélecteur de couleur (ColorPicker).
	 * @param color, la couleur à attribuer au sélecteur de couleur.
	 */
	public void setColorPickerColor(Color color) {
		colorPicker.setValue(color);
	}
	
	/**
	 * Retourne le sélecteur de couleur (ColorPicker).
	 * @return ColorPicker, le sélecteur de couleur.
	 */
	public ColorPicker getColorPicker() {
		return colorPicker;
	}
	
	/**
	 * Met à jour la liste des calques située en bas à droite de la GUI.
	 */
	public void updateLayerList() {
		layersList.getChildren().clear();
		for (Layer layer : Project.getInstance().getLayers()) {
			if (!layer.isTempLayer()) {
				handleAddNewLayer(layer);
			}
		}
		//opacitySlider.setValue(Project.getInstance().getCurrentLayer().getLayerOpacity());
		//opacityTextField.setText(String.valueOf(opacitySlider.getValue()));
	}
	
	/**
	 * Ajoute un nouveau calque à la liste des calques située en bas à droite de la GUI.
	 * @param layer, le nouveau calque à ajouter.
	 */
	private void handleAddNewLayer(Layer layer) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/rightMenu/Layer.fxml"));
			Parent newLayer = fxmlLoader.load();
			LayerController l = fxmlLoader.getController();
			l.setLayerName(layer);
			layersList.getChildren().add(newLayer);
			
			if (layer.equals(Project.getInstance().getCurrentLayer())) {
				l.setBackground(focusBackground);
			} else {
				l.setBackground(unfocusBackground);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Efface la liste des calques située en bas à droite de la GUI.
	 */
	public void clearLayerList() {
		layersList.getChildren().clear();
	}
	
	/**
	 * Met à jour la liste de l'historique située en bas à droite de la GUI.
	 */
	public void updateHistoryList() {
		historyList.getChildren().clear();
		
		Iterator<ICmd> undoIter = RecordCmd.getInstance().getUndoStack().descendingIterator();
		int id = 0;
		int nbUndo = RecordCmd.getInstance().getUndoStack().size();
		while (undoIter.hasNext()) {
			ICmd cmd = undoIter.next();
			addHistory(cmd, false, id++, nbUndo);
		}
		for (ICmd cmd : RecordCmd.getInstance().getRedoStack()) {
			addHistory(cmd, true, ++id, nbUndo);
		}
	}
	
	/**
	 * Ajoute une nouvelle commande à la liste de l'historique située en haut à droite de la GUI.
	 * @param iCmd, la commande à ajouter à la liste de l'historique.
	 * @param isRedo, un booléen valant vrai si la commande à ajouter est une commande redo, false si c'est une commande undo.
	 * @param id, l'id de la commande (sa position dans la liste de l'historique).
	 * @param nbCmd, le nombre total de commande undo-able (le nombre d'éléments dans la undoStack).
	 */
	private void addHistory(ICmd iCmd, boolean isRedo, int id, int nbCmd) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/rightMenu/History.fxml"));
			Parent newHistory = fxmlLoader.load();
			HistoryController h = fxmlLoader.getController();
			h.setID(id);
			h.setLabel(iCmd.toString());
			h.setCurrentUndoId(nbCmd);
			if (isRedo) {
				h.setRedoOpacity();
			}
			historyList.getChildren().add(newHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Efface la liste de l'historique située en haut à droite de la GUI.
	 */
	public void clearHistoryList() {
		historyList.getChildren().clear();
	}
	
	/**
	 * Permet d'activer les boutons de la barre de menus de droite.
	 */
	public void enableButton() {
		addNewLayer.setDisable(false);
		deleteLayer.setDisable(false);
		upLayer.setDisable(false);
		downLayer.setDisable(false);
		fusion.setDisable(false);
		opacityTextField.setDisable(false);
		opacitySlider.setDisable(false);
	}
	
	/**
	 * Permet de désactiver les boutons de la barre de menus de droite.
	 */
	public void disableButton() {
		addNewLayer.setDisable(true);
		deleteLayer.setDisable(true);
		upLayer.setDisable(true);
		downLayer.setDisable(true);
		fusion.setDisable(true);
		opacityTextField.setDisable(true);
		opacitySlider.setDisable(true);
	}
	
	/**
	 * Classe implémentant une commande sauvegardant l'action du bouton <b>+</b> en bas à droite de la GUI.
	 * Enregistre la création du nouveau calque et définit l'action à effectuer en cas d'appel à undo() ou redo() sur cette commande.
	 */
	public class NewLayerSave implements ICmd {
		private Layer oldCurrentLayer;
		private Layer newLayer;
		
		/**
		 * Construit une commande sauvegardant l'action du bouton <b>+</b> en bas à droite de la GUI.
		 */
		private NewLayerSave() {
			oldCurrentLayer = Project.getInstance().getCurrentLayer();
		}
		
		@Override
		public void execute() {
			newLayer = Project.getInstance().getCurrentLayer();
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			Project.getInstance().setCurrentLayer(newLayer);
			Project.getInstance().deleteCurrentLayer();
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
		}
		
		@Override
		public void redo() {
			Project.getInstance().addLayer(newLayer);
		}
		
		@Override
		public String toString() {
			return "Création du " + newLayer;
		}
	}
	
	/**
	 * Classe implémentant une commande sauvegardant l'action du bouton <b>-</b> en bas à droite de la GUI.
	 * Enregistre la suppression du calque sélectionné et définit l'action à effectuer en cas d'appel à undo() ou redo() sur cette commande.
	 */
	public class DeleteLayerSave implements ICmd {
		private Layer oldCurrentLayer;
		private Layer newLayer;
		private int index;
		
		/**
		 * Construit une commande sauvegardant l'action du bouton <b>-</b> en bas à droite de la GUI.
		 */
		private DeleteLayerSave() {
			oldCurrentLayer = Project.getInstance().getCurrentLayer();
			index = Project.getInstance().getLayers().indexOf(oldCurrentLayer);
		}
		
		@Override
		public void execute() {
			newLayer = Project.getInstance().getCurrentLayer();
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			Project.getInstance().getLayers().add(index, oldCurrentLayer);
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
		}
		
		@Override
		public void redo() {
			Project.getInstance().getLayers().remove(oldCurrentLayer);
			Project.getInstance().setCurrentLayer(newLayer);
		}
		
		@Override
		public String toString() {
			return "Suppression de " + oldCurrentLayer;
		}
	}
	
	/**
	 * Classe implémentant une commande sauvegardant l'action des boutons <b>v</b> et <b></b> en bas à droite de la GUI.
	 * Enregistre le déplacement du calque sélectionné et définit l'action à effectuer en cas d'appel à undo() ou redo() sur cette commande.
	 */
	public class LayerPosSave implements ICmd {
		private int index;
		
		/**
		 * Construit une commande sauvegardant l'action du bouton <b>^</b> ou du bouton <b>v</b> en bas à droite de la GUI.
		 */
		private LayerPosSave(int index) {
			this.index = index;
		}
		
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			Collections.swap(Project.getInstance().getLayers(), index, index + 1);
			Project.getInstance().drawWorkspace();
		}
		
		@Override
		public void redo() {
			Collections.swap(Project.getInstance().getLayers(), index, index + 1);
			Project.getInstance().drawWorkspace();
		}
		
		@Override
		public String toString() {
			return "Modification de l'ordre des Calques";
		}
	}
	
	/**
	 * Classe implémentant une commande sauvegardant l'action du bouton <b>fusion</b> en bas à droite de la GUI.
	 * Enregistre la fusion du calque sélectionné avec le calque situé juste en dessous, et définit l'action à effectuer en cas
	 * d'appel à undo() ou redo() sur cette commande.
	 */
	public class MergeSave implements ICmd {
		
		private Layer oldCurrentLayer1;
		private Layer oldCurrentLayer2;
		private Layer newLayer;
		private int index;
		
		/**
		 * Construit une commande sauvegardant l'action du bouton <b>fusion</b> en bas à droite de la GUI.
		 */
		private MergeSave() {
			oldCurrentLayer1 = Project.getInstance().getCurrentLayer();
			index = Project.getInstance().getLayers().indexOf(oldCurrentLayer1);
			oldCurrentLayer2 = Project.getInstance().getLayers().get(index + 1);
		}
		
		@Override
		public void execute() {
			newLayer = Project.getInstance().getCurrentLayer();
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			Project.getInstance().getLayers().add(index, oldCurrentLayer2);
			Project.getInstance().getLayers().add(index, oldCurrentLayer1);
			
			Project.getInstance().getLayers().remove(newLayer);
			
			Project.getInstance().setCurrentLayer(oldCurrentLayer1);
		}
		
		@Override
		public void redo() {
			Project.getInstance().getLayers().remove(oldCurrentLayer1);
			Project.getInstance().getLayers().remove(oldCurrentLayer2);
			Project.getInstance().getLayers().add(index, newLayer);
			Project.getInstance().setCurrentLayer(newLayer);
		}
		
		@Override
		public String toString() {
			return "Fusion de deux calques";
		}
	}
}
