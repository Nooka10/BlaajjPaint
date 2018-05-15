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

public class WindowsNewProject {
	@FXML
	private Button cancel;
	
	@FXML
	private Button createButton;
	
	@FXML
	private TextField width;
	
	@FXML
	private TextField height;
	
	@FXML
	private void initialize() {
		width.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
			                    String newValue) {
				if (!newValue.matches("\\d*")) {
					width.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		height.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
			                    String newValue) {
				if (!newValue.matches("\\d*")) {
					height.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

	/**
	 * Crée, initalise le nouveau projet avec la taille choisie.
	 * Réactive les bouttons
	 *
	 * @param event
	 */
	@FXML
	private void createNewProject(ActionEvent event) {
		// Nettoyage du projet
        MainViewController.getInstance().closeProject();
		
		int width = Integer.parseInt(this.width.getText());
		int height = Integer.parseInt(this.height.getText());
		
		Project.getInstance().initData(width, height, true);
		
		Stage stage = (Stage) createButton.getScene().getWindow();
		stage.close();

		// Réactive les bouttons
		MainViewController.getInstance().enableButton();
	}
	
	@FXML
	private void cancel(ActionEvent event) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		// do what you have to do
		stage.close();
	}
}
