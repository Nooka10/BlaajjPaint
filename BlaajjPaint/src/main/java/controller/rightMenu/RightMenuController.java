package controller.rightMenu;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import utils.UndoException;

import javax.swing.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

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
	private TextField opacityTextField;
	
	@FXML
	private AnchorPane rightMenu;
	
	private double oldOpacity;
	
	private double newOpacity;
	
	// LayerList config
	private final static Background focusBackground = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
	
	private final static Background unfocusBackground = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	
	@FXML
	private void initialize() {
		colorPicker.setValue(Color.BLACK);
		addNewLayer.setTooltip(new Tooltip("Ajouter un nouveau calque"));
		deleteLayer.setTooltip(new Tooltip("Supprimer le calque séléctionné"));
		upLayer.setTooltip(new Tooltip("Déplacer le calque vers le haut"));
		downLayer.setTooltip(new Tooltip("Déplacer le calque vers le bas"));
		fusion.setTooltip(new Tooltip("Fusionner avec le calque inférieur"));
	}
	
	public VBox getHistoryList() {
		return historyList;
	}
	
	@FXML
	void OnInputTextChanged(ActionEvent event) {
		newOpacity = Math.round(Double.parseDouble(opacityTextField.getText()));
		Project.getInstance().getCurrentLayer().setLayerOpacity(newOpacity);
		opacitySlider.setValue(newOpacity);
	}
	
	@FXML
	void OnMousePressed(MouseEvent event) {
		oldOpacity = Math.round(Double.parseDouble(opacityTextField.getText()));
		opacityTextField.setText(String.valueOf(oldOpacity));
		Project.getInstance().getCurrentLayer().updateLayerOpacity(oldOpacity);
	}
	
	@FXML
	void OnMouseDragged(MouseEvent event) {
		newOpacity = opacitySlider.getValue();
		opacityTextField.setText(String.valueOf(newOpacity));
		Project.getInstance().getCurrentLayer().updateLayerOpacity(newOpacity);
	}
	
	@FXML
	void OnMouseReleased(MouseEvent event) {
		newOpacity = Math.round(opacitySlider.getValue());
		opacityTextField.setText(String.valueOf(newOpacity));
		Project.getInstance().getCurrentLayer().setLayerOpacity(oldOpacity, newOpacity);
	}
	
	@FXML
	void selectColor(ActionEvent event) {
		Project.getInstance().setCurrentColor(colorPicker.getValue());
	}
	
	public void setColorPickerColor(Color color) {
		colorPicker.setValue(color);
	}
	
	
	@FXML
	/**
	 * CETTE FONCITON FAIT UNE SAVECMD POUR L'HISTORIQUE NE PAS APPELER A L'INTERIEUR D'UNE AUTRE SAUVEGARDE
	 */
	public void addNewLayer() {
		NewLayerSave ls = new NewLayerSave();
		Project.getInstance().addNewLayer();
		updateLayerList();
		ls.execute();
	}
	
	@FXML
	/**
	 * CETTE FONCITON FAIT UNE SAVECMD POUR L'HISTORIQUE NE PAS APPELER A L'INTERIEUR D'UNE AUTRE SAUVEGARDE
	 */
	void deleteLayer() {
		if (Project.getInstance().getLayers().size() != 1) {
			DeleteLayerSave dls = new DeleteLayerSave();
			Project.getInstance().deleteCurrentLayer();
			dls.execute();
			updateLayerList();
		}
	}
	
	@FXML
	/**
	 * CETTE FONCITON FAIT UNE SAVECMD POUR L'HISTORIQUE NE PAS APPELER A L'INTERIEUR D'UNE AUTRE SAUVEGARDE
	 */
	void downLayer(ActionEvent event) {
		int index = Project.getInstance().getLayers().indexOf(Project.getInstance().getCurrentLayer());
		if (index < Project.getInstance().getLayers().size() - 1) {

			new LayerPosSave(index).execute();

			Collections.swap(Project.getInstance().getLayers(), index, index + 1);

			updateLayerList();
			Project.getInstance().drawWorkspace();
		}
	}
	
	@FXML
	/**
	 * CETTE FONCITON FAIT UNE SAVECMD POUR L'HISTORIQUE NE PAS APPELER A L'INTERIEUR D'UNE AUTRE SAUVEGARDE
	 */
	void upLayer(ActionEvent event) {
		int index = Project.getInstance().getLayers().indexOf(Project.getInstance().getCurrentLayer());
		
		if (index != 0) {

			new LayerPosSave(index - 1).execute();

			Collections.swap(Project.getInstance().getLayers(), index, index - 1);

			updateLayerList();
			Project.getInstance().drawWorkspace();
		}
	}

	public class LayerPosSave extends ICmd {

		private int index;

		public LayerPosSave(int index) {
			this.index = index;
		}

		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}

		@Override
		public void undo() throws UndoException {
			Collections.swap(Project.getInstance().getLayers(), index, index + 1);

			updateLayerList();
			Project.getInstance().drawWorkspace();
		}

		@Override
		public void redo() throws UndoException {
			Collections.swap(Project.getInstance().getLayers(), index, index + 1);

			updateLayerList();
			Project.getInstance().drawWorkspace();
		}

		public String toString() {
			return "Ordre des Calque";
		}
	}

	@FXML
	/**
	 * CETTE FONCITON FAIT UNE SAVECMD POUR L'HISTORIQUE NE PAS APPELER A L'INTERIEUR D'UNE AUTRE SAUVEGARDE
	 */
	public void mergeLayer(ActionEvent event) {
		
		Layer currentLayer = Project.getInstance().getCurrentLayer();
		int index = Project.getInstance().getLayers().indexOf(currentLayer);
		if (index != Project.getInstance().getLayers().size() - 1) {
			MergeSave ms = new MergeSave();
			Layer backgroundLayer = Project.getInstance().getLayers().get(index + 1);
			Layer mergeLayer = currentLayer.mergeLayers(backgroundLayer);
			Project.getInstance().getLayers().remove(currentLayer);
			Project.getInstance().getLayers().remove(backgroundLayer);
			Project.getInstance().addLayer(mergeLayer);
			updateLayerList();
			ms.execute();
		}
		
	}
	
	public void updateLayerList() {
		layersList.getChildren().clear();
		for (Layer layer : Project.getInstance().getLayers()) {
			addNewLayer(layer);
		}
		opacitySlider.setValue(Project.getInstance().getCurrentLayer().getLayerOpacity());
		opacityTextField.setText(String.valueOf(Project.getInstance().getCurrentLayer().getLayerOpacity()));
	}
	
	public void deleteLayer(int index) {
		layersList.getChildren().remove(index);
	}
	
	public void addNewLayer(Layer layer) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/rightMenu/Layer.fxml"));
			Parent newLayer = fxmlLoader.load();
			LayerController l = fxmlLoader.getController();
			l.setLayerName(layer);
			layersList.getChildren().add(newLayer);
			
			if (layer.equals(Project.getInstance().getCurrentLayer())) {
				l.getLayerElem().setBackground(focusBackground);
			} else {
				l.getLayerElem().setBackground(unfocusBackground);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearLayerList() {
		layersList.getChildren().clear();
	}
	
	public void clearHistoryList() {
		historyList.getChildren().clear();
	}
	
	public void setOpacitySlider(double opacitySlider) {
		this.opacitySlider.setValue(opacitySlider);
	}
	
	public void setOpacityTextField(String opacityTextField) {
		this.opacityTextField.setText(opacityTextField);
	}
	
	public void addUndoHistory(ICmd iCmd) {
		addHistory(iCmd, false);
	}
	
	public void updateHistoryList() {
		historyList.getChildren().clear();
		Iterator<ICmd> li = RecordCmd.getInstance().getUndoStack().descendingIterator();
		
		while (li.hasNext()) {
			ICmd cmd = li.next();
			addUndoHistory(cmd);
		}
		for (ICmd cmd : RecordCmd.getInstance().getRedoStack()) {
			addRedoHistory(cmd);
		}
	}
	
	public void addRedoHistory(ICmd iCmd) {
		addHistory(iCmd, true);
	}
	
	private void addHistory(ICmd iCmd, boolean isRedo) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/rightMenu/History.fxml"));
			Parent newHistory = fxmlLoader.load();
			HistoryController h = fxmlLoader.getController();
			h.setLabel(iCmd.toString());
			h.setID(iCmd.getID());
			if (isRedo) {
				h.changeLabelOpacity(60);
			}
			historyList.getChildren().add(newHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'activer les bouttons.
	 * A appeler dès qu'un project est ouvert, créé
	 */
	public void enableButton(){

	}

	/**
	 * Permet de déscativer les bouttons.
	 * A appeler à la fermeture d'un projet ou à la création de l'application
	 */
	public void disableButton(){

	}
	
	public class NewLayerSave extends ICmd {
		
		Layer oldCurrentLayer;
		Layer newLayer;
		
		/**
		 * Prends l'ancien current layer
		 */
		public NewLayerSave() {
			oldCurrentLayer = Project.getInstance().getCurrentLayer();
		}
		
		@Override
		/**
		 * Sauvegarde le nouveau current layer, a appeler juste après avoir ajouté le nouveau current layer
		 */
		public void execute() {
			newLayer = Project.getInstance().getCurrentLayer();
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() throws UndoException {
			Project.getInstance().setCurrentLayer(newLayer);
			Project.getInstance().deleteCurrentLayer();
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
		}
		
		@Override
		public void redo() throws UndoException {
			Project.getInstance().addLayer(newLayer);
			updateLayerList();
		}
		
		public String toString() {
			return "Nouveau Calque";
		}
	}
	
	public class DeleteLayerSave extends ICmd {
		
		private Layer oldCurrentLayer;
		private Layer newLayer;
		private int index;
		
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
		public void undo() throws UndoException {
			Project.getInstance().getLayers().add(index, oldCurrentLayer);
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
			updateLayerList();
		}
		
		@Override
		public void redo() throws UndoException {
			Project.getInstance().getLayers().remove(oldCurrentLayer);
			Project.getInstance().setCurrentLayer(newLayer);
			updateLayerList();
		}
		
		public String toString() {
			return "Suppression de " + oldCurrentLayer;
		}
	}
	
	
	public class MergeSave extends ICmd {
		
		private Layer oldCurrentLayer1;
		private Layer oldCurrentLayer2;
		private Layer newLayer;
		private int index;
		
		/**
		 * Prends les deux calques à fusionner
		 */
		public MergeSave() {
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
		public void undo() throws UndoException {
			Project.getInstance().getLayers().add(index, oldCurrentLayer2);
			Project.getInstance().getLayers().add(index, oldCurrentLayer1);
			
			Project.getInstance().getLayers().remove(newLayer);
			
			Project.getInstance().setCurrentLayer(oldCurrentLayer1);
			updateLayerList();
		}
		
		@Override
		public void redo() throws UndoException {
			Project.getInstance().getLayers().remove(oldCurrentLayer1);
			Project.getInstance().getLayers().remove(oldCurrentLayer2);
			Project.getInstance().getLayers().add(index, newLayer);
			Project.getInstance().setCurrentLayer(newLayer);
			updateLayerList();
			}
		
		public String toString() {
			return "Fusion de deux calques";
		}
	}
}
