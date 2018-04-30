package controller.tools;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ParamDrawToolController {
	@FXML
	private Slider ThicknessSlider;
	
	@FXML
	private TextField ThicknessTextField;
	
	@FXML
	private HBox ParamDrawTools;
	
	@FXML
	private Slider OpacitySlider;
	
	@FXML
	private TextField OpacityTextField;
	
	private Double thicknessValue = 10.0; // FIXME: valeur par défaut pour l'épaisseur;
	
	private Double opacityValue = 10.0; // FIXME: valeur par défaut pour l'opacité
	
	@FXML
	private void initialize() {
		// Handle Slider value change events.
		ThicknessSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			thicknessValue = Double.parseDouble(newValue.toString());
			ThicknessTextField.setText(thicknessValue.toString());
		});
		OpacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			opacityValue = Double.parseDouble(newValue.toString());
			OpacityTextField.setText(opacityValue.toString());
		});
		// Handle TextField text changes.
		ThicknessTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			thicknessValue = Double.parseDouble(newValue);
			ThicknessSlider.setValue(thicknessValue);
		});
		OpacityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			opacityValue = Double.parseDouble(newValue);
			OpacitySlider.setValue(opacityValue);
		});
	}
	
	public Double getThicknessValue() {
		return thicknessValue;
	}
	
	public void setThicknessValue(Double thicknessValue) {
		this.thicknessValue = thicknessValue;
	}
	
	public Double getOpacityValue() {
		return opacityValue;
	}
	
	public void setOpacityValue(Double opacityValue) {
		this.opacityValue = opacityValue;
	}
}
