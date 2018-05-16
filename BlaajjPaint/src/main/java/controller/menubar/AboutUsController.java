package controller.menubar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controleur de la fenêtre d'informations qui apparait lorsque l'utilisateur clique sur le menu <br>Aide -> À propos</br>
 */
public class AboutUsController {
	@FXML
	private Button closeButton;
	
	/**
	 * Ferme la fenêtre "À propos"
	 */
	@FXML
	private void close() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
}
