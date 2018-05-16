package controller.tools.Shapes;

import controller.tools.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import utils.Utils;

public class ParamShapeController {
    @FXML
    public ToggleButton filledRect;
    @FXML
    public ToggleButton emptyEllipse;
    @FXML
    public ToggleButton filledEllipse;
    @FXML
    public ToggleButton emptyRect;
    @FXML
	public ToggleGroup Forme;
	@FXML
    private Slider thicknessSlider;
    @FXML
    private TextField thicknessTextField;
    @FXML
    private HBox paramShapeTools;
    
    /**
     * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
     */
    @FXML
    private void initialize() {
        thicknessTextField.setText(String.valueOf(Math.round(EmptyRectangle.getInstance().getThickness())));
        thicknessSlider.setValue(EmptyRectangle.getInstance().getThickness());
	
	    // Ajoute un changeListener à textFieldWidth -> la méthode changed() est appelée à chaque fois que le texte de textFieldWidth est modifié
	    thicknessTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    // vrai si l'utilisateur n'a pas entré un chiffre ou que le contenu de thicknessTextField n'est pas valides
			    if (!newValue.matches("\\d*") || !Utils.checkTextFieldValueGTZero(thicknessTextField)) {
				    thicknessTextField.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
			    } else { // vrai si l'utilisateur a entré un chiffre et que le contenu de thicknessTextField est valide
				    int thickness = Integer.parseInt(thicknessTextField.getText());
				    if (thickness > 200) {
					    thickness = 200;
					    thicknessTextField.setText(String.valueOf(thickness));
				    }
				    EmptyRectangle.getInstance().setThickness(thickness);
				    EmptyEllipse.getInstance().setThickness(thickness);
				    thicknessSlider.setValue(thickness);
			    }
		    }
	    });
    }
    
    public void handleSliderValueChanged() {
        thicknessTextField.setText(String.valueOf(Math.round(thicknessSlider.getValue())));
        EmptyRectangle.getInstance().setThickness(thicknessSlider.getValue());
        EmptyEllipse.getInstance().setThickness(thicknessSlider.getValue());
    }

    @FXML
    public void handleEmptyRect() {
        Tool.setCurrentTool(EmptyRectangle.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
        }
    }

    @FXML
    public void handleFilledRect() {
        Tool.setCurrentTool(FilledRectangle.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
        }
    }

    @FXML
    public void handleEmptyEllipse() {
        Tool.setCurrentTool(EmptyEllipse.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
        }
    }

    @FXML
    public void handleFilledEllipse() {
        Tool.setCurrentTool(FilledEllipse.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
        }
    }
}
