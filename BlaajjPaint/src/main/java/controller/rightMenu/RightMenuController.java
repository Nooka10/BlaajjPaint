package controller.rightMenu;

import controller.Layer;
import controller.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class RightMenuController {
	@FXML
	private AnchorPane rightMenu;
	
	@FXML
	private Button addNewLayer;
	
	@FXML
	private ColorPicker colorPicker;
	
	@FXML
	private VBox LayersList;
	
	// LayerList config
	private final Background focusBackground = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
	
	private final Background unfocusBackground = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	
	@FXML
	private void initialize() {
		colorPicker.setValue(Color.BLACK);
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
	void upLayer(ActionEvent event) {
		Project.getInstance().currentLayerToFront();
	}
	
	@FXML
	void downLayer(ActionEvent event) {
		Project.getInstance().currentLayerToBack();
	}
	
	@FXML
	void fusionLayer(ActionEvent event) {
		
	}
	
	private HBox createLayoutUI(Layer layer) {
		HBox container = new HBox();
		CheckBox visibility = new CheckBox();
		Label layerName = new Label(layer.toString());
		
		visibility.setSelected(layer.isVisible());
		
		container.setOnMouseClicked((e) ->
		{
			Project.getInstance().setCurrentLayer(layer);
			updateLayerList();
		});
		
		// use different backgrounds for focused and unfocused states
        /*container.backgroundProperty().bind( Bindings
                .when( container.focusedProperty() )
                .then( focusBackground )
                .otherwise( unfocusBackground )
        );*/
		
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
		LayersList.getChildren().clear();
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
}
