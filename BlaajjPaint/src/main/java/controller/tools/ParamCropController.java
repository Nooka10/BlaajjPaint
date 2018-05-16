package controller.tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Contolleur des paramètres concerant l'outil Crop (Rogner) dans la paramBar
 */
public class ParamCropController {
    @FXML
    public Button cancelButton;
    @FXML
    public Button validateButton;
	
	@FXML
	private void initialize() {
	
	}

    /**
     * Evenement appellé par le boutton Annuler qui annule le rognage actuel
     * @param event
     */
    @FXML
    void cancel(ActionEvent event) {
        Crop.getInstance().cancel();
    }

    /**
     * Evenement appellé par le boutton Valider qui valide le rognage du calque
     * @param event
     */
    @FXML
    void validate(ActionEvent event) {
        Crop.getInstance().validate();
    }
}
