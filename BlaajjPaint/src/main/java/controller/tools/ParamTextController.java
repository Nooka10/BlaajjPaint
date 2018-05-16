package controller.tools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

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

    private TextTool textTool = TextTool.getInstance();

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

        // Ajout d'événement lorsque la taille du text change dans le textField
        textFieldSizeFont.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldSizeFont.setText(newValue.replaceAll("[^\\d]", ""));
                } else if(!newValue.equals("")){
                    textTool.setFont(new Font(fontList.getValue(),Integer.parseInt(newValue)));
                    sliderSizeFont.setValue(Integer.parseInt(textFieldSizeFont.getText()));
                }
            }
        });
    }

    /**
     * Evenement appellé par le slider changeant la taille de la police d'écriture
     * @param event
     */
    @FXML
    void changeFontSlider(MouseEvent event) {
        textTool.setFont(new Font(fontList.getValue(),sliderSizeFont.getValue()));
        textFieldSizeFont.setText(String.valueOf(Math.round(sliderSizeFont.getValue())));
    }

    /**
     * Evenement lors de la validation de l'ajout du texte
     * @param event
     */
    @FXML
    void validateText(ActionEvent event) {
        textTool.validate();
    }

    /**
     * Evenement lorsque le text change dans le textField
     * @param event
     */
    @FXML
    void changeValue(KeyEvent event) {
        textTool.changeTextValue(textValue.getText());
    }

    /**
     * Evenement appelé par l'appuie du boutton annuler
     * @param event
     */
    @FXML
    void cancelTextCreation(ActionEvent event){
        textTool.cancel();
    }
}
