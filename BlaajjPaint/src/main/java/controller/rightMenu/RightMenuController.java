package controller.rightMenu;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
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
import main.Main;

public class RightMenuController {
	@FXML
	private VBox layersList;
	
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
	void upLayer(ActionEvent event) {
		Project.getInstance().currentLayerToFront();
	}
	
	@FXML
	void downLayer(ActionEvent event) {
		Project.getInstance().currentLayerToBack();
	}
	
	@FXML
	void fusionLayer(ActionEvent event) {
		//Project.getInstance().getCurrentLayer().mergeLayers();
	}
	
	public void createLayerList() {
		layersList.getChildren().clear();
		for (Layer layer : Project.getInstance().getLayers()) {
			addNewLayer(layer);
		}
	}
	
	public void deleteLayer(int index){
		layersList.getChildren().remove(index);
	}
	
	public void updateLayerList(){
	
	}
	
	public void moveLayer(int index, int indexDest){
		Node toMove = layersList.getChildren().get(index);
		layersList.getChildren().remove(index);
		layersList.getChildren().add(indexDest, toMove);
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
	
	public void setOpacitySlider(double opacitySlider) {
		this.opacitySlider.setValue(opacitySlider);
	}
	
	public void setOpacityTextField(String opacityTextField) {
		this.opacityTextField.setText(opacityTextField);
	}
}
