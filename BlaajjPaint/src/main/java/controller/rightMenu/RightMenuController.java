package controller.rightMenu;

import controller.Layer;
import controller.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class RightMenuController {
	@FXML
	private VBox LayersList;
	
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
	}
	
	@FXML
	void deleteLayer(ActionEvent event) {
		Project.getInstance().deleteCurrentLayer();
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
	
	private HBox createLayoutUI(Layer layer) {
		HBox container = new HBox();
		CheckBox visibility = new CheckBox();
		Label layerName = new Label(layer.toString());
		
		
		visibility.setSelected(layer.isVisible());
		opacityTextField.setText(String.valueOf(layer.getLayerOpacity()));
		
		container.setOnMouseClicked((e) ->
		{
			Project.getInstance().setCurrentLayer(layer);
			opacitySlider.setValue(layer.getLayerOpacity());
			opacityTextField.setText(String.valueOf(layer.getLayerOpacity()));
			updateLayerList();
		});
		
		visibility.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				layer.setVisible(new_val);
				Project.getInstance().drawWorkspace();
			}
		});
		
		
		container.getChildren().addAll(visibility, layerName);
		
		return container;
	}
	
	public void updateLayerList() {
		clearLayerList();
		for (Layer layer : Project.getInstance().getLayers()) {
			HBox newEl = createLayoutUI(layer);
			LayersList.getChildren().add(newEl);
			if (layer.equals(Project.getInstance().getCurrentLayer())) {
				newEl.setBackground(focusBackground);
				// System.out.println("current Layer: " + layer.toString());
			} else {
				newEl.setBackground(unfocusBackground);
			}
		}
	}

	public void clearLayerList(){
		LayersList.getChildren().clear();
	}
}
