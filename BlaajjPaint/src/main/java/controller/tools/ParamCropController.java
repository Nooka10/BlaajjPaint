package controller.tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ParamCropController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button validateButton;

    @FXML
    void cancel(ActionEvent event) {
        Crop.getInstance().cancel();
    }

    @FXML
    void validate(ActionEvent event) {
        Crop.getInstance().validate();
    }

    @FXML
    private void initialize(){

    }

}
