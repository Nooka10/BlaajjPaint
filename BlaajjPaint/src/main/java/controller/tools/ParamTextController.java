package controller.tools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import utils.Utils;

/**
 * Contrôleur associé au fichier FXML ParamTextController.fxml et gérant l'ensemble des actions associées à la barre de menus de l'outil <b>Texte</b> qui apparaît dans la
 * GUI en dessous de la barre de menus, lorsqu'on sélectionne l'outil <b>Texte</b>.
 */
public class ParamTextController {
	@FXML
	private Slider sliderSizeFont;
	
	@FXML
	private TextField textFieldSizeFont;
	
	@FXML
	private ChoiceBox<String> fontList;
	
	@FXML
	private TextField textValueTextField;
	
	@FXML
	private Button validateButton;
	
	private TextTool textTool = TextTool.getInstance();
	
	/**
	 * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		ObservableList<String> list = FXCollections.observableArrayList(Font.getFamilies()); // récupère la liste des polices disponibles
		fontList.getItems().addAll(list); // ajoute toutes les polices disponibles à la ChoiceBox
		fontList.setValue(list.get(0));
		textTool.setFont(new Font(fontList.getValue(), Math.round(sliderSizeFont.getValue())));
		
		// ajoute un changeListener à fontList -> la méthode changed() est appelée à chaque fois que l'élément sélectionné dans la liste fontList a changé
		fontList.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						textTool.setFont(new Font(fontList.getItems().get((int) newValue), sliderSizeFont.getValue()));
					}
				}
		);
		
		// ajoute un changeListener à textFieldSizeFont -> la méthode changed() est appelée à chaque fois que le texte de textFieldSizeFont est modifié
		textFieldSizeFont.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// vrai si l'utilisateur n'a pas entré un chiffre ou que le contenu de thicknessTextField n'est pas valides
				if (!newValue.matches("\\d*") || !Utils.checkTextFieldValueGTZero(textFieldSizeFont, validateButton)) {
					textFieldSizeFont.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
				} else { // vrai si l'utilisateur a entré un chiffre et que le contenu de textFieldSizeFont est valide
					if (Integer.parseInt(newValue) > 500) {
						textFieldSizeFont.setText("500");
					}
					textTool.setFont(new Font(fontList.getValue(), Integer.parseInt(newValue)));
					sliderSizeFont.setValue(Integer.parseInt(textFieldSizeFont.getText()));
				}
			}
		});
		
		// ajoute un changeListener à textValueTextField -> la méthode changed() est appelée à chaque fois que le texte de textValueTextField est modifié
		textValueTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				textTool.changeTextValue(textValueTextField.getText()); // on modifie le texte affiché sur le calque temporaire
			}
		});
	}
	
	/**
	 * Méthode appelée par le slider de la barre de menu de l'outil <b>Texte</b> lorsque l'utilisateur clique avec la souris sur le slider réglant la taille de la police,
	 * lorsqu'il reste appuyé et glisse ou encore lorsqu'il relâche le clic. Modifie la taille de la police pour le texte en train d'être ajouté.
	 */
	@FXML
	void handleChangeFontSlider() {
		textTool.setFont(new Font(fontList.getValue(), sliderSizeFont.getValue()));
		textFieldSizeFont.setText(String.valueOf(Math.round(sliderSizeFont.getValue())));
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b><Valider</b>. Crée un nouveau calque contenant le texte écrit à la taille choisie et de la couleur
	 * actuellement sélectionnée dans le sélecteur de couleur.
	 */
	@FXML
	void handleValidateText() {
		textTool.validate();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b><Annuler</b>. Annule la création d'un calque de texte.
	 */
	@FXML
	void handleCancelTextCreation() {
		textTool.cancel();
	}
}
