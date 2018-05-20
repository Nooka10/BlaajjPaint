package controller.tools;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Contrôleur associé au fichier FXML ParamCropController.fxml et gérant l'ensemble des actions associées à la barre de menus de l'outil de rognage qui apparaît dans la
 * GUI en dessous de la barre de menus, lorsqu'on sélectionne l'outil <b>Rogner</b>.
 */
public class ParamCropController {
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b><Annuler</b>. Annule le rognage du calque et réinitialise l'état de l'outil Crop.
	 */
	@FXML
	private void handleCancel() {
		Crop.getInstance().cancel();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b><Valider</b>. Effectue le rognage du calque selon la sélection de l'utilisateur.
	 */
	@FXML
	private void handleValidate() {
		Crop.getInstance().validate();
	}
}
