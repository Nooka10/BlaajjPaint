package controller.rightMenu;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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

	// LayerList config
	private final Background focusBackground = new Background( new BackgroundFill( Color.RED, CornerRadii.EMPTY, Insets.EMPTY ) );
	private final Background unfocusBackground = new Background( new BackgroundFill( Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY ) );


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
		CheckBox visibility = new CheckBox();
		Label layerName = new Label(layer.toString());

		visibility.setSelected(layer.isVisible());

		container.setOnMouseClicked( ( e ) ->
		{
			container.requestFocus();
			Project.getInstance().setCurrentLayer(layer);
		} );

        // use different backgrounds for focused and unfocused states
        container.backgroundProperty().bind( Bindings
                .when( container.focusedProperty() )
                .then( focusBackground )
                .otherwise( unfocusBackground )
        );

		visibility.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				layer.setVisible(new_val);
				Project.getInstance().drawWorkspace();
			}
		});


		container.getChildren().addAll(visibility, layerName);

		return container;
	}

	public void updateLayerList(){
		LayersList.getChildren().clear();
		for(Layer layer: Project.getInstance().getLayers()){
		    HBox newEl = createLayoutUI(layer);
            LayersList.getChildren().add(newEl);
            if(layer.equals(Project.getInstance().getCurrentLayer())){
                newEl.requestFocus();
            }
		}
	}
}
