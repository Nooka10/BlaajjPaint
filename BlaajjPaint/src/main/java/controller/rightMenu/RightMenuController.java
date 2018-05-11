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
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import utils.UndoException;

import java.util.Collections;

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

	public class NewLayerSave implements ICmd {

		@Override
		public void execute() {

		}

		@Override
		public void undo() throws UndoException {

		}

		@Override
		public void redo() throws UndoException {

		}
	}

	@FXML
	void addNewLayer(ActionEvent event) {
		Project.getInstance().addNewLayer();
		updateLayerList();
	}
	
	@FXML
	void deleteLayer(ActionEvent event) {
		Project.getInstance().deleteCurrentLayer();
		updateLayerList();
	}
	
	@FXML
	void downLayer(ActionEvent event) {
		int index = Project.getInstance().getLayers().indexOf(Project.getInstance().getCurrentLayer());
		if (index < Project.getInstance().getLayers().size() - 1) {
			Collections.swap(Project.getInstance().getLayers(), index, index + 1);
			
			Node toMove = layersList.getChildren().get(index);
			layersList.getChildren().remove(index);
			layersList.getChildren().add(index + 1, toMove);
			
			Project.getInstance().drawWorkspace();
		}
	}
	
	@FXML
	void upLayer(ActionEvent event) {
		int index = Project.getInstance().getLayers().indexOf(Project.getInstance().getCurrentLayer());
		
		if (index != 0) {
			Collections.swap(Project.getInstance().getLayers(), index, index - 1);
			
			Node toMove = layersList.getChildren().get(index);
			layersList.getChildren().remove(index);
			layersList.getChildren().add(index - 1, toMove);
			Project.getInstance().drawWorkspace();
		}
	}
	
	@FXML
	void fusionLayer(ActionEvent event) {
		Layer currentLayer = Project.getInstance().getCurrentLayer();
		int index = Project.getInstance().getLayers().indexOf(currentLayer);
		if (index != Project.getInstance().getLayers().size() - 1) {
			currentLayer.mergeLayers(Project.getInstance().getLayers().get(index + 1));
			
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

	public void clearHistoryList() { historyList.getChildren().clear();}
	
	public void setOpacitySlider(double opacitySlider) {
		this.opacitySlider.setValue(opacitySlider);
	}
	
	public void setOpacityTextField(String opacityTextField) {
		this.opacityTextField.setText(opacityTextField);
	}
	
	public void addUndoHistory(ICmd iCmd) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/rightMenu/History.fxml"));
			Parent newHistory = fxmlLoader.load();
			HistoryController h = fxmlLoader.getController();
			h.setLabel(iCmd.toString());
			historyList.getChildren().add(newHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateHistoryList(){
		historyList.getChildren().clear();
		for (ICmd cmd: RecordCmd.getInstance().getUndoStack()) {
			addUndoHistory(cmd);
		}
	}
	
	public void undoHistory() {
		historyList.getChildren().remove(historyList.getChildren().size() - 1);
		//updateHistoryList();
	}
}
