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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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
	
	// LayerList config
	private final static Background focusBackground = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
	
	private final static Background unfocusBackground = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	
	@FXML
	private void initialize() {
		colorPicker.setValue(Color.BLACK);
		
		opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Project.getInstance().getCurrentLayer().setLayerOpacity(Double.parseDouble(newValue.toString()));
			opacityTextField.setText(String.valueOf(Project.getInstance().getCurrentLayer().getLayerOpacity()));
		});
		opacityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			Project.getInstance().getCurrentLayer().setLayerOpacity(Double.parseDouble(newValue));
			opacitySlider.setValue(Project.getInstance().getCurrentLayer().getLayerOpacity());
		});
	}
	
	@FXML
	void selectColor(ActionEvent event) {
		Project.getInstance().setCurrentColor(colorPicker.getValue());
	}
	
	public void setColorPickerColor(Color color) {
		colorPicker.setValue(color);
	}
	
	@FXML
	void addNewLayer(ActionEvent event) {
		Project.getInstance().addNewLayer();
		createLayerList();
	}
	
	@FXML
	void deleteLayer(ActionEvent event) {
		Project.getInstance().deleteCurrentLayer();
		createLayerList();
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
		if(index != Project.getInstance().getLayers().size()-1){
			currentLayer.mergeLayers( Project.getInstance().getLayers().get(index+1));
			
		}
		
	}
	
	public void createLayerList() {
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
	
	public void setOpacitySlider(double opacitySlider) {
		this.opacitySlider.setValue(opacitySlider);
	}
	
	public void setOpacityTextField(String opacityTextField) {
		this.opacityTextField.setText(opacityTextField);
	}
	
	public void createHistoryList() {
		historyList.getChildren().clear();
		for (ICmd iCmd: RecordCmd.getInstance().getUndoStack()) {
			addUndoHistory(iCmd);
		}
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
}
