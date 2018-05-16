package controller.menubar;

import controller.MainViewController;
import controller.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Utils;

/**
 * Controleur de la fenêtre qui apparait lors de la demande de création d'un nouveau projet
 */
public class WindowsNewProject {
	@FXML
	private Button cancel;
	
	@FXML
	private Button createButton;
	
	@FXML
	private TextField width;
	
	@FXML
	private TextField height;
	
	/**
	 * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
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
	 * Crée, initalise le nouveau projet avec la taille choisie.
	 * Réactive les boutons
	 *
	 * @param event
	 */
	@FXML
	private void createNewProject(ActionEvent event) {
		if (Utils.checkWidthHeightValidity(width, height, createButton)) {
			// Nettoyage du projet
			MainViewController.getInstance().closeProject();
			
			int width = Integer.parseInt(this.width.getText());
			int height = Integer.parseInt(this.height.getText());
			
			Project.getInstance().initData(width, height, true);
			
			Stage stage = (Stage) createButton.getScene().getWindow();
			stage.close();
			
			// Réactive les boutons
			MainViewController.getInstance().enableButton();
		}
	}

	/**
	 * Annule la création d'un nouveau projet. Appellé par le boutton "Annulé"
	 * @param event
	 */
	@FXML
	private void cancel(ActionEvent event) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		// do what you have to do
		stage.close();
	}
}
