package controller.menubar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Contrôleur associé au fichier FXML AboutUs.fxml et gérant la fenêtre d'informations qui apparaît lorsque l'utilisateur clique sur le menu <br>Aide -> À propos</br>.
 */
public class AboutUsController {
	@FXML
	private Button closeButton;
	
	/**
	 * Ferme la fenêtre "À propos".
	 */
	@FXML
	private void handleClose() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
}
