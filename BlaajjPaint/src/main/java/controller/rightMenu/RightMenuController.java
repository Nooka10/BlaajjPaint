package controller.rightMenu;

import controller.MainViewController;
import controller.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class RightMenuController {
	@FXML
	private AnchorPane rightMenu;
	
	@FXML
	private Button addNewLayer;

	@FXML
	private ColorPicker colorPicker;
	
	private MainViewController mainViewController; // Reference to the mainViewController
	
	/**
	 * Appelé par le MainViewController pour donner une référence vers lui-même.
	 *
	 * @param mainViewController, une référence vers le mainViewController
	 *
	 * Créé par Benoît Schopfer
	 */
	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	@FXML
	private void initialize(){
		colorPicker.setValue(Color.BLACK);
	}
	
	@FXML
	void selectColor(ActionEvent event) {
		System.out.println("Changed color!");
		Project project = Project.getInstance();
		project.setCurrentColor(colorPicker.getValue());
	}
	
	@FXML
	void addNewLayer(ActionEvent event) {
		//mainViewController.getCurrentCanvas().addLayer(new Layer(mainViewController));
	}
}
