package controller.tools;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ParamDrawToolController {
	@FXML
	private Slider ThicknessSlider;
	
	@FXML
	private TextField ThicknessValue;
	
	@FXML
	private HBox ParamDrawTools;
	
	@FXML
	private Slider OpacitySlider;
	
	@FXML
	private TextField OpacityValue;
	
	
	@FXML
	private void initialize() {
		// Handle Slider value change events.
		ThicknessSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			ThicknessValue.setText(newValue.toString());
		});
		OpacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			OpacityValue.setText(newValue.toString());
		});
		// Handle TextField text changes.
		ThicknessValue.textProperty().addListener((observable, oldValue, newValue) -> {
			ThicknessSlider.setValue(Double.parseDouble(newValue));
		});
		OpacityValue.textProperty().addListener((observable, oldValue, newValue) -> {
			OpacitySlider.setValue(Double.parseDouble(newValue));
		});
	}
}
