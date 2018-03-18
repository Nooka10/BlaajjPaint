package main.java.controller.menubar;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.io.File;

public class MainController {
	
	@FXML
	private MenuBar menuBar;
	
	
	@FXML
	private BorderPane borderPane;
	
	
	@FXML
	
	private Canvas canvas;
	
	
	@FXML
	
	private ColorPicker colorPicker;
	
	
	@FXML
	
	private TextField brushSize;
	
	
	@FXML
	
	private CheckBox gomme;
	
	
	public void initialize() {
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		canvas.setOnMouseDragged(e -> {
			
			double size = Double.parseDouble(brushSize.getText());
			
			double x = e.getX() - size;
			
			double y = e.getY() - size;
			
			
			if (gomme.isSelected()) {
				
				g.clearRect(x, y, size, size);
				
			} else {
				
				g.setFill(colorPicker.getValue());
				
				g.fillRect(x, y, size, size);
				
			}
			
		});
		
	}
	
	public void onSave() {
		
		try {
			
			Image snapshot = canvas.snapshot(null, null);
			
			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
			
		} catch (Exception e) {
			
			System.out.println("Failed to save image: " + e);
			
		}
	}
}