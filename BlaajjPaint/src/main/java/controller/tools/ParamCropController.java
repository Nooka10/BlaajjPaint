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
	
	/**
	 * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	
	@FXML
	private void initialize() {
	
	}

    /**
     * Evenement appellé par le boutton Annuler qui annule le rognage actuel
     */
    @FXML
    void cancel() {
        Crop.getInstance().cancel();
    }

    /**
     * Evenement appellé par le boutton Valider qui valide le rognage du calque
     */
    @FXML
    void validate() {
        Crop.getInstance().validate();
    }
}
