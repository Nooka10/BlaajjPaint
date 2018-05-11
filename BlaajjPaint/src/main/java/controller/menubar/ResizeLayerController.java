package controller.menubar;

import controller.Project;
import controller.tools.TextTool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class ResizeLayerController {
	
	@FXML
	private TextField textFieldWidth;
	
	@FXML
	private TextField textFieldHeight;
	
	@FXML
	private CheckBox checkBoxRatio;
	
	@FXML
	private CheckBox checkBoxResizeImage;
	
	private TextTool textTool = TextTool.getInstance();
	
	private double ratioImage;
	
	@FXML
	private void initialize() {
		
		double width = Project.getInstance().getCurrentLayer().getWidth();
		double height = Project.getInstance().getCurrentLayer().getHeight();
		
		// affiche la taille du calque actuel
		textFieldWidth.setText(Double.toString(width));
		textFieldHeight.setText(Double.toString(height));
		
		textFieldHeight.setDisable(true);
		
		checkBoxRatio.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				textFieldHeight.setDisable(checkBoxRatio.isSelected());
			}
		});
		
	}
	
	
	
	@FXML
	public void validateResize(){
	
	}
	
	
	

}