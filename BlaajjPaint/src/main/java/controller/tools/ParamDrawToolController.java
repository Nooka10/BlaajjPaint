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
	
	private Double thicknessValue = 1.0; // FIXME: valeur par défaut pour l'épaisseur;
	
	private Double opacityValue = 100.0; // FIXME: valeur par défaut pour l'opacité
	
	@FXML
	private void initialize() {
		thicknessTextField.setText(thicknessValue.toString());
		opacityTextField.setText(opacityValue.toString());
		
		// Handle Slider value change events.
		thicknessSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			thicknessValue = Double.parseDouble(newValue.toString());
			thicknessTextField.setText(thicknessValue.toString());
		});
		opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			opacityValue = Double.parseDouble(newValue.toString());
			opacityTextField.setText(opacityValue.toString());
		});
		// Handle TextField text changes.
		thicknessTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			thicknessValue = Double.parseDouble(newValue);
			thicknessSlider.setValue(thicknessValue);
		});
		opacityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			opacityValue = Double.parseDouble(newValue);
			opacitySlider.setValue(opacityValue);
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

	// by Adrien



	// end by Adrien
}
