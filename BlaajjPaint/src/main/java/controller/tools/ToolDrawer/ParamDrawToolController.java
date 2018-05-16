/*
Author : Benoit
Modified by : Adrien
 */
package controller.tools.ToolDrawer;

import controller.tools.Shapes.EmptyEllipse;
import controller.tools.Shapes.EmptyRectangle;
import controller.tools.Tool;
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
	
	/**
	 * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	
	@FXML
	private void initialize() {
		thicknessTextField.setText(String.valueOf(Math.round(tool.thickness)));
		thicknessSlider.setValue(tool.thickness);
		
		// FIXME: À changer pour utiliser FXML !!!
		
		/*
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
		*/
	}
	
	public void textFieldOnTextChange() {
		tool.thickness = Double.parseDouble(thicknessTextField.getText());
		thicknessSlider.setValue(tool.thickness);
		((ToolDrawer) Tool.getCurrentTool()).setThickness(tool.thickness);
	}
	
	public void sliderValueChanged() {
		tool.thickness = thicknessSlider.getValue();
		thicknessTextField.setText(String.valueOf(Math.round(tool.thickness)));
		((ToolDrawer) Tool.getCurrentTool()).setThickness(tool.thickness);
	}
}
