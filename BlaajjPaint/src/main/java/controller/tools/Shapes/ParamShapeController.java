package controller.tools.Shapes;

import controller.tools.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import utils.Utils;

/**
 * Contrôleur associé au fichier FXML ParamShapeController.fxml et gérant l'ensemble des actions associées à la barre de menus des formes qui apparaît dans la GUI en
 * dessous de la barre de menus, lorsqu'on sélectionne l'outil <b>Formes</b>.
 */
public class ParamShapeController {
	@FXML
	public ToggleButton filledRect;
	@FXML
	public ToggleButton emptyEllipse;
	@FXML
	public ToggleButton filledEllipse;
	@FXML
	public ToggleButton emptyRect;
	@FXML
	public ToggleGroup Forme;
	@FXML
	private Slider thicknessSlider;
	@FXML
	private TextField thicknessTextField;
	@FXML
	private HBox paramShapeTools;
	
	/**
	 * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		thicknessTextField.setText(String.valueOf(Math.round(EmptyRectangle.getInstance().getThickness())));
		thicknessSlider.setValue(EmptyRectangle.getInstance().getThickness());
		
		// Ajoute un changeListener à textFieldWidth -> la méthode changed() est appelée à chaque fois que le texte de textFieldWidth est modifié
		thicknessTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// vrai si l'utilisateur n'a pas entré un chiffre ou que le contenu de thicknessTextField n'est pas valide
				if (!newValue.matches("\\d*") || !Utils.checkTextFieldValueGTZero(thicknessTextField)) {
					thicknessTextField.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
				} else { // vrai si l'utilisateur a entré un chiffre et que le contenu de thicknessTextField est valide
					int thickness = Integer.parseInt(newValue);
					if (thickness > 200) {
						thickness = 200;
						thicknessTextField.setText("200");
					}
					EmptyRectangle.getInstance().setThickness(thickness);
					EmptyEllipse.getInstance().setThickness(thickness);
					thicknessSlider.setValue(thickness);
				}
			}
		});
	}
	
	/**
	 * Méthode appelée par le slider de la barre de menu des formes lorsque l'utilisateur clique avec la souris sur le slider d'opacité du calque, lorsqu'il reste appuyé
	 * et glisse ou encore lorsqu'il relâche le clic. Modifie l'épaisseur des contours des formes vides. Ne fait rien pour les formes pleines.
	 */
	public void handleSliderValueChanged() {
		thicknessTextField.setText(String.valueOf(Math.round(thicknessSlider.getValue())));
		EmptyRectangle.getInstance().setThickness(thicknessSlider.getValue());
		EmptyEllipse.getInstance().setThickness(thicknessSlider.getValue());
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b>Rectangle vide</b> de la barre de menus des formes située en dessous de la barre de menus dans la
	 * GUI. Sélectionne l'outil <b>Rectangle vide</b> permettant à l'utilisateur de dessiner un rectangle vide avec des bordures de l'épaisseur définie par le slider et
	 * le textField, et de la couleur actuellement sélectionnée dans le sélecteur de couleurs.
	 */
	@FXML
	public void handleEmptyRect() {
		Tool.setCurrentTool(EmptyRectangle.getInstance());
		if (Tool.getToolHasChanged()) {
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b>Rectangle plein</b> de la barre de menus des formes située en dessous de la barre de menus dans la
	 * GUI. Sélectionne l'outil <b>Rectangle plein</b> permettant à l'utilisateur de dessiner un rectangle plein de la couleur actuellement sélectionnée dans le sélecteur
	 * de couleurs.
	 */
	@FXML
	public void handleFilledRect() {
		Tool.setCurrentTool(FilledRectangle.getInstance());
		if (Tool.getToolHasChanged()) {
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b>Ellipse vide</b> de la barre de menus des formes située en dessous de la barre de menus dans la GUI.
	 * Sélectionne l'outil <b>Ellipse vide</b> permettant à l'utilisateur de dessiner une ellipse vide avec des bordures de l'épaisseur définie par le slider et le
	 * textField, et de la couleur actuellement sélectionnée dans le sélecteur de couleurs.
	 */
	@FXML
	public void handleEmptyEllipse() {
		Tool.setCurrentTool(EmptyEllipse.getInstance());
		if (Tool.getToolHasChanged()) {
			Tool.setToolHasChanged(false);
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b>Ellipse pleine</b> de la barre de menus des formes située en dessous de la barre de menus dans la
	 * GUI. Sélectionne l'outil <b>Ellipse pleine</b> permettant à l'utilisateur de dessiner une ellipse pleine de la couleur actuellement sélectionnée dans le sélecteur
	 * de couleurs.
	 */
	@FXML
	public void handleFilledEllipse() {
		Tool.setCurrentTool(FilledEllipse.getInstance());
		if (Tool.getToolHasChanged()) {
			Tool.setToolHasChanged(false);
		}
	}
}
