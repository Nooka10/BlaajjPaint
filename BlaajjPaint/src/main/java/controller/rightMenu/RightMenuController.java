package controller.rightMenu;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RightMenuController {
	@FXML
	private AnchorPane rightMenu;
	
	@FXML
	private Button addNewLayer;

	@FXML
	private ColorPicker colorPicker;

	@FXML
	private VBox LayersList;
	
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
	void selectColor() {
		System.out.println("Changed color!");
		Project project = Project.getInstance();
		project.setCurrentColor(colorPicker.getValue());
	}
	
	@FXML
	void addNewLayer() {
		Project.getInstance().addLayer(new Layer(Project.getInstance().getDimension()));
		updateLayerList();
	}

	private HBox createLayoutUI(Layer layer){
		HBox container = new HBox();
		CheckBox visibility = new CheckBox(layer.toString());
		visibility.setSelected(layer.isVisible());

		visibility.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				layer.setVisible(new_val);
				Project.getInstance().drawWorkspace();
			}
		});

		container.getChildren().add(visibility);
		return container;
	}

	public void updateLayerList(){
		System.out.println(Project.getInstance().getLayers().size());
		LayersList.getChildren().clear();
		for(Layer layer: Project.getInstance().getLayers()){
			LayersList.getChildren().add(createLayoutUI(layer));
		}
	}
}
