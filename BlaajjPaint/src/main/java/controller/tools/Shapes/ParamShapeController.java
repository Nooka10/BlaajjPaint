package controller.tools.Shapes;

import controller.Project;
import controller.tools.Tool;
import controller.tools.ToolDrawer.ToolDrawer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
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

    @FXML
    private void initialize() {
        thicknessTextField.setText(String.valueOf(EmptyRectangle.getInstance().getThickness()));
        thicknessSlider.setValue(EmptyRectangle.getInstance().getThickness());
    }

    public void sliderOnTextChange(ActionEvent actionEvent) {
        EmptyRectangle.getInstance().setThickness(Double.parseDouble(thicknessTextField.getText()));
        EmptyEllipse.getInstance().setThickness(Double.parseDouble(thicknessTextField.getText()));
        thicknessSlider.setValue(EmptyRectangle.getInstance().getThickness());
    }

    public void sliderOnMouseRelased(MouseEvent mouseEvent) {
        thicknessTextField.setText(String.valueOf(thicknessSlider.getValue()));
        EmptyRectangle.getInstance().setThickness(thicknessSlider.getValue());
        EmptyEllipse.getInstance().setThickness(thicknessSlider.getValue());
    }

    @FXML
    public void handleEmptyRect(ActionEvent actionEvent) {
        Tool.setCurrentTool(EmptyRectangle.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
            filledRect.setSelected(false);
            emptyEllipse.setSelected(false);
            filledEllipse.setSelected(false);
        }
    }

    @FXML
    public void handleFilledRect(ActionEvent actionEvent) {
        Tool.setCurrentTool(FilledRectangle.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
            emptyRect.setSelected(false);
            emptyEllipse.setSelected(false);
            filledEllipse.setSelected(false);
        }
    }

    @FXML
    public void handleEmptyEllipse(ActionEvent actionEvent) {
        Tool.setCurrentTool(EmptyEllipse.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
            emptyRect.setSelected(false);
            filledRect.setSelected(false);
            filledEllipse.setSelected(false);
        }
    }

    @FXML
    public void handleFilledEllipse(ActionEvent actionEvent) {
        Tool.setCurrentTool(FilledEllipse.getInstance());
        if (Tool.getToolHasChanged()) {
            Tool.setToolHasChanged(false);
            emptyRect.setSelected(false);
            filledRect.setSelected(false);
            emptyEllipse.setSelected(false);
        }
    }
}
