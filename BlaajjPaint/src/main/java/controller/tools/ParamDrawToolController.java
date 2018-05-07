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
	
	private ToolDrawer tool = (ToolDrawer) Tool.getCurrentTool();
	@FXML
	private void initialize() {
		thicknessTextField.setText(String.valueOf(tool.thickness));
		
		// Handle Slider value change events.
		thicknessSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			tool.thickness = Double.parseDouble(newValue.toString());
			thicknessTextField.setText(String.valueOf(tool.thickness));
			((ToolDrawer) Tool.getCurrentTool()).setThickness(tool.thickness);
		});
		// Handle TextField text changes.
		thicknessTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			tool.thickness = Double.parseDouble(newValue);
			thicknessSlider.setValue(tool.thickness);
			((ToolDrawer) Tool.getCurrentTool()).setThickness(tool.thickness);
		});
	}
}
