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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.dc.pr.PRError;


public class ResizeLayerController {
	
	@FXML
	private TextField textFieldWidth;
	
	@FXML
	private TextField textFieldHeight;
	
	@FXML
	private CheckBox checkBoxRatio;
	
	@FXML
	private CheckBox checkBoxResizeImage;
	
	@FXML
	private Button validateResizeButton;
	
	private double ratioImage;
	
	@FXML
	private void initialize() {
		
		int width = (int) Project.getInstance().getCurrentLayer().getWidth();
		int height = (int) Project.getInstance().getCurrentLayer().getHeight();
		
		ratioImage = (double) width / height;
		
		// affiche la taille du calque actuel
		textFieldWidth.setText(Integer.toString(width));
		textFieldHeight.setText(Integer.toString(height));
		
		textFieldHeight.setDisable(true);
		
		checkBoxRatio.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				textFieldHeight.setDisable(checkBoxRatio.isSelected());
				
				if (textFieldHeight.isDisable()) {
					if (!textFieldWidth.getText().isEmpty()) {
						textFieldHeight.setText(Integer.toString((int) (Math.round(Double.valueOf(textFieldWidth.getText()) / ratioImage))));
					}
				}
			}
		});
		
		textFieldWidth.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
			                    String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldWidth.setText(newValue.replaceAll("[^\\d]", ""));
				} else {
					if (textFieldHeight.isDisable()) {
						if (!textFieldWidth.getText().isEmpty()) {
							textFieldHeight.setText(Integer.toString((int) (Math.round(Double.valueOf(textFieldWidth.getText()) / ratioImage))));
						}
					}
				}
			}
		});
		
		textFieldHeight.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
			                    String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldHeight.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		
		
	}
	
	
	@FXML
	public void validateResize() {
		if (!textFieldWidth.getText().isEmpty() && !textFieldHeight.getText().isEmpty()) {
			
			if (checkBoxResizeImage.isSelected()) {
				Image image = Project.getInstance().getCurrentLayer().createImageFromCanvas(1);
				ImageView image2 = new ImageView(image);
				
				image2.setFitWidth(Double.valueOf(textFieldWidth.getText()));
				image2.setFitHeight(Double.valueOf(textFieldHeight.getText()));
				
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(0, 0, Project.getInstance().getCurrentLayer().getWidth(), Project.getInstance().getCurrentLayer().getHeight());
				Project.getInstance().getCurrentLayer().setWidth(Double.valueOf(textFieldWidth.getText()));
				Project.getInstance().getCurrentLayer().setHeight(Double.valueOf(textFieldHeight.getText()));
				
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(image2.getImage(), 0, 0, image2.getFitWidth(), image2.getFitHeight());
				
			} else {
				Project.getInstance().getCurrentLayer().setWidth(Double.valueOf(textFieldWidth.getText()));
				Project.getInstance().getCurrentLayer().setHeight(Double.valueOf(textFieldHeight.getText()));
				
			}
		}
		Stage stage = (Stage) validateResizeButton.getScene().getWindow();
		stage.close();
	}
	
	
}