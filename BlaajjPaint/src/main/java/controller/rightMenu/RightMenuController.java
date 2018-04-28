package controller.rightMenu;

import controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class RightMenuController {
	@FXML
	private AnchorPane rightMenu;
	
	@FXML
	private Button addNewLayer;
	
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
	void selectColor(ActionEvent event) {
	
	}
	
	@FXML
	void addNewLayer(ActionEvent event) {
		//mainViewController.getCanvas().addLayer(new Layer(mainViewController));
	}
}
