package controller.tools.Shapes;

import controller.tools.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

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
    private Slider thicknessSlider;
    @FXML
    private TextField thicknessTextField;
    @FXML
    private HBox paramShapeTools;
    
    /**
     * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
     */
    @FXML
    private void initialize() {
        thicknessTextField.setText(String.valueOf(Math.round(EmptyRectangle.getInstance().getThickness())));
        thicknessSlider.setValue(EmptyRectangle.getInstance().getThickness());
    }
    
    public void textFieldOnTextChange() {
        EmptyRectangle.getInstance().setThickness(Double.parseDouble(thicknessTextField.getText()));
        EmptyEllipse.getInstance().setThickness(Double.parseDouble(thicknessTextField.getText()));
        thicknessSlider.setValue(EmptyRectangle.getInstance().getThickness());
    }
    
    public void sliderValueChanged() {
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
