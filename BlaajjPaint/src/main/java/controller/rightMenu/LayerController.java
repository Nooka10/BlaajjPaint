package controller.rightMenu;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class LayerController {
	
	@FXML
	private HBox layerElem;
	
	@FXML
	private CheckBox visibility;
	
	@FXML
	private Label label;
	
	private Layer layerName;
	
	public HBox getLayerElem() {
		return layerElem;
	}
	
	public void setLayerName(Layer layerName) {
		this.layerName = layerName;
		visibility.setSelected(layerName.isVisible());
		label.setText(layerName.toString());
	}
	
	@FXML
	void handleMouseClicked() {
		Project.getInstance().setCurrentLayer(layerName);
		MainViewController.getInstance().getRightMenuController().setOpacitySlider(layerName.getLayerOpacity());
		MainViewController.getInstance().getRightMenuController().setOpacityTextField(String.valueOf(layerName.getLayerOpacity()));
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	@FXML
	void handleVisibilityChange() {
		layerName.setVisible(visibility.isSelected());
		Project.getInstance().drawWorkspace();
	}
}
