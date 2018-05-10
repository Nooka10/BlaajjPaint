package controller.tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
    }

    @FXML
    public void changeFont(MouseEvent event) {
        textTool.setFont(new Font(fontList.getValue(),sliderSizeFont.getValue()));
    }

    @FXML
    void changeFontSlider(MouseEvent event) {
        textFieldSizeFont.setText(String.valueOf(Math.round(sliderSizeFont.getValue())));
        textTool.setFont(new Font(fontList.getValue(),sliderSizeFont.getValue()));
    }

    @FXML
    void changeFontText(ActionEvent event) {
        sliderSizeFont.setValue(Integer.parseInt(textFieldSizeFont.getText()));
        textTool.setFont(new Font(fontList.getValue(),sliderSizeFont.getValue()));
    }

    @FXML
    void validateText(ActionEvent event) {
        textTool.validate();
    }

    @FXML
    void changeValue(ActionEvent event) {
        textTool.changeTextValue();
    }
}
