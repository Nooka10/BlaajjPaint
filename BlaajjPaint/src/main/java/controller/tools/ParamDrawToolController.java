/*
Author : Benoit
Modified by : Adrien
 */
package controller.tools;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ParamDrawToolController {
	@FXML
	private Slider thicknessSlider;
	
	@FXML
	private TextField thicknessTextField;
	
	@FXML
	private HBox paramDrawTools;
	
	@FXML
	private Slider opacitySlider;
	
	@FXML
	private TextField opacityTextField;
	
	private ToolDrawer tool = (ToolDrawer) Tool.getCurrentTool();
	@FXML
	private void initialize() {
		thicknessTextField.setText(String.valueOf(tool.thickness));
		opacityTextField.setText(String.valueOf(tool.opacity));
		
		// Handle Slider value change events.
		thicknessSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			tool.thickness = Double.parseDouble(newValue.toString());
			thicknessTextField.setText(String.valueOf(tool.thickness));
			((ToolDrawer) Tool.getCurrentTool()).setThickness();
		});
		opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			tool.opacity = Double.parseDouble(newValue.toString());
			opacityTextField.setText(String.valueOf(tool.opacity));
			((ToolDrawer) Tool.getCurrentTool()).setOpacity();
		});
		// Handle TextField text changes.
		thicknessTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			tool.thickness = Double.parseDouble(newValue);
			thicknessSlider.setValue(tool.thickness);
			((ToolDrawer) Tool.getCurrentTool()).setThickness();
		});
		opacityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			tool.opacity = Double.parseDouble(newValue);
			opacitySlider.setValue(tool.opacity);
			((ToolDrawer) Tool.getCurrentTool()).setOpacity();
		});
	}
}
