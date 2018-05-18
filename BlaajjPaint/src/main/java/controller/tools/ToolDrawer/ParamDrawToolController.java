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

/**
 * Contrôleur associé au fichier FXML ParamDrawToolController.fxml et gérant l'ensemble des actions associées à la barre de menus des outils pinceau et gomme qui
 * apparaît dans la GUI en dessous de la barre de menus, lorsqu'on sélectionne l'outil <b>Pinceau</b> ou l'outil <b>Gomme</b>.
 */
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
		// initialise le textField à l'épaisseur de l'outil actuellement sélectionné (gomme ou pinceau)
		thicknessTextField.setText(String.valueOf(Math.round(tool.thickness)));
		thicknessSlider.setValue(tool.thickness); // initialise le slider à l'épaisseur de l'outil actuellement sélectionné (gomme ou pinceau)
		
		// Ajoute un changeListener à textFieldWidth -> la méthode changed() est appelée à chaque fois que le texte de textFieldWidth est modifié
		thicknessTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// vrai si l'utilisateur n'a pas entré un chiffre ou que le contenu de textFieldSizeFont n'est pas valide
				if (!newValue.matches("\\d*") || !Utils.checkTextFieldValueGTZero(thicknessTextField)) {
					thicknessTextField.setText(oldValue); // on annule la dernière modification
				} else { // vrai si l'utilisateur a entré un chiffre et que le contenu de thicknessTextField est valide
					tool.setThickness(Integer.parseInt(newValue)); // épaisseur comprise entre 1 et 200
					thicknessSlider.setValue(tool.thickness);
					if (tool.thickness > 200) {
						tool.thickness = 200;
						thicknessTextField.setText("200");
					}
				}
			}
		});
	}
	
	/**
	 * Méthode appelée par le slider de la barre de menu des outils <b>Pinceau</b> et <b>Gomme</b> lorsque l'utilisateur clique avec la souris sur le slider d'opacité
	 * du calque, lorsqu'il reste appuyé et glisse ou encore lorsqu'il relâche le clic. Modifie l'épaisseur de l'outil de dessin sélectionné (<b>pinceau</b> ou <b>gomme</b>).
	 */
	public void handleSliderValueChanged() {
		tool.thickness = (int) Math.round(thicknessSlider.getValue());
		thicknessTextField.setText(String.valueOf(Math.round(tool.thickness)));
		((ToolDrawer) Tool.getCurrentTool()).setThickness(tool.thickness);
	}
}
