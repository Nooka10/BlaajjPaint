/*
Author : Benoit
Modified by : Adrien
 */
package controller.tools.ToolDrawer;

import controller.tools.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import utils.Utils;

public class ParamDrawToolController {
	@FXML
	private Slider thicknessSlider;
	
	@FXML
	private TextField thicknessTextField;
	
	@FXML
	private HBox paramDrawTools;
	
	private ToolDrawer tool = (ToolDrawer) Tool.getCurrentTool();
	
	/**
	 * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		thicknessTextField.setText(String.valueOf(Math.round(tool.thickness)));
		thicknessSlider.setValue(tool.thickness);
		
		// Ajoute un changeListener à textFieldWidth -> la méthode changed() est appelée à chaque fois que le texte de textFieldWidth est modifié
		thicknessTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// vrai si l'utilisateur n'a pas entré un chiffre ou que le contenu de textFieldSizeFont n'est pas valide
				if (!newValue.matches("\\d*") || !Utils.checkTextFieldValueGTZero(thicknessTextField)) {
					thicknessTextField.setText(oldValue); // on annule la dernière modification
				} else { // vrai si l'utilisateur a entré un chiffre et que le contenu de thicknessTextField est valide
					tool.thickness = Double.parseDouble(thicknessTextField.getText());
					if (tool.thickness > 200) {
						tool.thickness = 200;
						thicknessTextField.setText("200");
					}
					thicknessSlider.setValue(tool.thickness);
					((ToolDrawer) Tool.getCurrentTool()).setThickness(tool.thickness);
				}
			}
		});
	}
	
	public void handleSliderValueChanged() {
		tool.thickness = thicknessSlider.getValue();
		thicknessTextField.setText(String.valueOf(Math.round(tool.thickness)));
		((ToolDrawer) Tool.getCurrentTool()).setThickness(tool.thickness);
	}
}
