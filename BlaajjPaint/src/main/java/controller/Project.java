package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
//import javafx.scene.layout.StackPane;

public class Project {
	private Dimension dimension;
	private LinkedList<Layer> layers = new LinkedList<>();
	private Layer currentLayer;
	private MainViewController mainViewController;
	
	private GraphicsContext gc;
	//private StackPane pane = new StackPane();

	private Color currentColor;
	
	private static Project projectInstance = new Project();
	
	public static Project getInstance() {
		return projectInstance;
	}
	
	private Project() {
		currentColor = Color.BLACK;
	}
	
	public void setData (int width, int height, MainViewController mainViewController){
		this.mainViewController = mainViewController;
		dimension = new Dimension(width, height);
		currentLayer = new Layer(width, height);
		gc = currentLayer.getGraphicsContext2D();
		layers.add(currentLayer);
		draw();
	}
	
	public Canvas getCurrentCanvas(){
		return currentLayer;
	}
	
	private void draw() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, currentLayer.getWidth(), currentLayer.getHeight());
		currentLayer.toFront();
		mainViewController.showCanvas(currentLayer);
	}

	public void setCurrentColor(Color color){
		currentColor = color;
	}

	public Color getCurrentColor(){
		return currentColor;
	}

	public LinkedList<Layer> getLayers() {
		return layers;
	}
	
	
	public void export(File file) {
		if (file != null) {
			Layer resultLayer = new Layer(dimension.width,dimension.height);
			for (Layer layer : layers) {
				resultLayer.mergeLayers(layer);
				resultLayer = layer;
			}
			
			SnapshotParameters params = new SnapshotParameters();
			String chosenExtension = "";
			
			int i = file.getPath().lastIndexOf('.');
			if (i > 0) {
				chosenExtension = file.getPath().substring(i + 1);
			}
			if (chosenExtension.equals("png")) {
				params.setFill(Color.TRANSPARENT);
			} else if (chosenExtension.equals("jpg")) {
				params.setFill(Color.WHITE);
			}
			
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(resultLayer.createImageFromCanvas(4), null), chosenExtension, file);
			}catch (IOException ex){
				ex.printStackTrace();
			}
			
			
			
			
			//ImageIO.write(renderedImage, chosenExtension, file);
			
			
			
		}
	}
}
