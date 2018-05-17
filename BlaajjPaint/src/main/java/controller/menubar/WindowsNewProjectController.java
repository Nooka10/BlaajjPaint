package controller.menubar;

import controller.MainViewController;
import controller.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Utils;

/**
 * contrôleur associé au fichier FXML WindowsNewProject.fxml et gérant l'ensemble des actions associées à la fenêtre ouverte lorsque l'utilisateur clique sur le
 * bouton du menu <b>Fichier -> Nouveau</b>.
 */
public class WindowsNewProjectController {
	@FXML
	private Button cancel;
	
	@FXML
	private Button createButton;
	
	@FXML
	private TextField width;
	
	@FXML
	private TextField height;
	
	/**
	 * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		width.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) { // vrai si l'utilisateur n'a pas entré un chiffre
					width.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
				} else {
					Utils.checkWidthHeightValidity(width, height, createButton); // vrai si l'utilisateur a entré un chiffre et que la largeur et la hauteur sont des entrées valides
				}
			}
		});
		height.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) { // vrai si l'utilisateur n'a pas entré un chiffre
					height.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
				} else {
					Utils.checkWidthHeightValidity(width, height, createButton); // vrai si l'utilisateur a entré un chiffre et que la largeur et la hauteur sont des entrées valides
				}
			}
		});
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b><créer</b>.
	 * Crée un nouveau projet aux dimensions qui ont été indiquées par l'utilisateur puis ferme la fenêtre.
	 */
	@FXML
	private void handleCreateNewProject() {
		//FIXME: vérifier qu'un projet est déjà ouvert! Sinon on ferme pour rien!
		MainViewController.getInstance().closeProject(); // ferme le projet actuellement ouvert s'il y en a un.
		
		
		int width = Integer.parseInt(this.width.getText());
		int height = Integer.parseInt(this.height.getText());
		
		Project.getInstance().initData(width, height, true); // initialise le nouveau projet
		
		MainViewController.getInstance().enableButtons(); // réactive les boutons de la GUI qui ne peuvent pas être utilisés sans avoir un projet ouvert
		
		Stage stage = (Stage) createButton.getScene().getWindow();
		stage.close(); // ferme la fenêtre
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton <b><Annuler</b>.
	 * Annule la création d'un nouveau projet et ferme la fenêtre.
	 */
	@FXML
	private void handleCancel() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close(); // ferme la fenêtre
	}
}
