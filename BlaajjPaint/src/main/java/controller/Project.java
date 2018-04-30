package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
//import javafx.scene.layout.StackPane;

public class Project {
	private Dimension dimension;
	private LinkedList<Layer> layers = new LinkedList<>();
	private Canvas backgroungImage; // TODO surement overkill de faire un canevas pour ca
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
	
	public void setData(int width, int height, MainViewController mainViewController) {
		this.mainViewController = mainViewController;
		dimension = new Dimension(width, height);
		currentLayer = new Layer(width, height);
		backgroungImage = new Canvas(width, height);
		gc = backgroungImage.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, dimension.width, dimension.height);
		gc.setFill(Color.LIGHTGRAY);
		
		int rectSize = 10;
		for (int i = 0; i < dimension.width; i = i + rectSize) {
			for (int j = 0; j < dimension.height; j = j + rectSize) {
				if (i % (rectSize * 2) == 0 ^ j % (rectSize * 2) == 0) {
					gc.fillRect(i, j, rectSize, rectSize);
				}
			}
		}
		
		layers.add(currentLayer);
		mainViewController.getRightMenuController().updateLayerList();
		drawWorkspace();
	}
	
	public Layer getCurrentLayer() {
		return currentLayer;
	}
	
	public void drawWorkspace() {
		Group layersGroup = new Group();
		layersGroup.getChildren().add(backgroungImage);
		for (Layer layer : layers)
			if (layer.isVisible())
				layersGroup.getChildren().add(layer);
		
		mainViewController.drawLayers(layersGroup);
	}
	
	public void setCurrentColor(Color color) {
		currentColor = color;
	}
	
	public Color getCurrentColor() {
		return currentColor;
	}
	
	public void addLayer(Layer newLayer) {
		currentLayer = newLayer;
		layers.add(newLayer);
		gc = currentLayer.getGraphicsContext2D(); // TODO test a enlever
		gc.setFill(Color.GREEN);
		gc.fillOval(30, 30, 9, 5);
		drawWorkspace();
	}
	
	public LinkedList<Layer> getLayers() {
		return layers;
	}
	
	public void setCurrentLayer(Layer currentLayer) {
		this.currentLayer = currentLayer;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	
	public void export(File file) {
		if (file != null) {
			Layer resultLayer = new Layer(dimension.width, dimension.height);
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
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void importImage(File file) {
		
		Layer newLayer = new Layer(dimension);
		addLayer(newLayer);
		
		String chosenExtension = "";
		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
			chosenExtension = file.getPath().substring(i + 1);
		}
		
		javafx.scene.image.Image image = null;
		
		if (chosenExtension.equals("png") || chosenExtension.equals("jpg")) {
			image = new Image(file.toURI().toString());
		}
		
		newLayer.getGraphicsContext2D().drawImage(image, 0, 0);
	}

	public Canvas getBackgroungImage() {
		return backgroungImage;
	}
}