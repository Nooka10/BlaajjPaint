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
 * Controlleur permettant de controller les paramètres du texte. (ParamBar)
 */
public class ParamTextController {
	
	@FXML
    private Slider sliderSizeFont;

    @FXML
    private TextField textFieldSizeFont;

    @FXML
    private ChoiceBox<String> fontList;

    @FXML
    private TextField textValue;
	
	@FXML
	public Button validateButton;
	
	@FXML
	public Button cancelButton;

    private TextTool textTool = TextTool.getInstance();
    
    /**
     * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
     */
    @FXML
    private void initialize() {
        // Récupération de la liste des polices d'écriture
        ObservableList<String> list = FXCollections.observableArrayList(Font.getFamilies());
        fontList.getItems().addAll(list);
        fontList.setValue(list.get(0));
        textTool.setFont(new Font(fontList.getValue(),Math.round(sliderSizeFont.getValue())));

        // Ajout d'événement lorsque un élément de la liste des polices et séléctionné
        fontList.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        System.out.println(fontList.getItems().get((int)newValue));
                        textTool.setFont(new Font(fontList.getItems().get((int)newValue),sliderSizeFont.getValue()));
                    }
                }
        );
	
	    // Ajoute un changeListener à textFieldWidth -> la méthode changed() est appelée à chaque fois que le texte de textFieldWidth est modifié
	    textFieldSizeFont.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    // vrai si l'utilisateur n'a pas entré un chiffre ou que le contenu de thicknessTextField n'est pas valides
			    if (!newValue.matches("\\d*") || !Utils.checkTextFieldValueGTZero(textFieldSizeFont, validateButton)) {
				    textFieldSizeFont.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
				
				    // vrai si l'utilisateur a entré un chiffre et que le contenu de textFieldSizeFont est valide
			    } else {
				    if (Integer.parseInt(newValue) > 500) {
					    textFieldSizeFont.setText(String.valueOf(500));
				    }
				    textTool.setFont(new Font(fontList.getValue(), Integer.parseInt(newValue)));
				    sliderSizeFont.setValue(Integer.parseInt(textFieldSizeFont.getText()));
			    }
		    }
	    });
    }

    /**
     * Evenement appellé par le slider changeant la taille de la police d'écriture
     */
    @FXML
    void changeFontSlider() {
        textTool.setFont(new Font(fontList.getValue(),sliderSizeFont.getValue()));
        textFieldSizeFont.setText(String.valueOf(Math.round(sliderSizeFont.getValue())));
    }

    /**
     * Evenement lors de la validation de l'ajout du texte
     */
    @FXML
    void validateText() {
        textTool.validate();
    }

    /**
     * Evenement lorsque le textechange dans le textField
     */
    @FXML
    void changeValue() {
        textTool.changeTextValue(textValue.getText());
    }

    /**
     * Evenement appelé par l'appuie du boutton annuler
     */
    @FXML
    void cancelTextCreation() {
        textTool.cancel();
    }
}
