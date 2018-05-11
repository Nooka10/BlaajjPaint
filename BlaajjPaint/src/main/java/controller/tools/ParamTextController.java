package controller.tools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import javax.xml.soap.Text;

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
    private Button validateButton;

    private TextTool textTool = TextTool.getInstance();

    @FXML
    private void initialize() {
        ObservableList<String> list = FXCollections.observableArrayList(Font.getFamilies());
        fontList.getItems().addAll(list);
        fontList.setValue(list.get(0));
        textTool.setFont(new Font(fontList.getValue(),Math.round(sliderSizeFont.getValue())));

        fontList.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        System.out.println(fontList.getItems().get((int)newValue));
                        textTool.setFont(new Font(fontList.getItems().get((int)newValue),sliderSizeFont.getValue()));
                    }
                }
        );

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

    @FXML
    void changeFontSlider(MouseEvent event) {
        textTool.setFont(new Font(fontList.getValue(),sliderSizeFont.getValue()));
        textFieldSizeFont.setText(String.valueOf(Math.round(sliderSizeFont.getValue())));
    }

    @FXML
    void validateText(ActionEvent event) {
        textTool.validate();
    }

    @FXML
    void changeValue(KeyEvent event) {
        textTool.changeTextValue(textValue.getText());
    }
}
