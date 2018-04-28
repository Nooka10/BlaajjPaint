package controller.menubar;

import controller.MainViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;

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
	private MainViewController mainViewController; // Reference to the menuBarController
	
	/**
	 * Appelé par le MainViewController pour donner une référence vers lui-même.
	 *
	 * @param mainViewController, une référence vers le mainViewController
	 *
	 *                            Créé par Benoît Schopfer
	 */
	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}
	
	@FXML
	private void initialize() {
		cancel.setCancelButton(true);
		createButton.setDefaultButton(true);
		
		//width.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
		//height.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
		
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
	
	@FXML
	void createNewProject(ActionEvent event) {
		mainViewController.newCanevas(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
		Stage stage = (Stage) createButton.getScene().getWindow();
		// do what you have to do
		stage.close();
	}
	
	@FXML
	void cancel(ActionEvent event) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		// do what you have to do
		stage.close();
	}
}
