package controller;

import controller.tools.Tool;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Project {
	private Dimension dimension;
	private LinkedList<Layer> layers = new LinkedList<>();
	private Canvas backgroungImage; // TODO surement overkill de faire un canevas pour ca
	// TODO: effectivement... utiliser une BackgroundImage semble plus logique non?^
	private Layer currentLayer;
	
	//private StackPane pane = new StackPane();
	
	private Color currentColor;
	
	//private Rectangle clip;
	
	private static Project projectInstance = new Project();
	
	public static Project getInstance() {
		return projectInstance;
	}
	
	private Project() {
		currentColor = Color.BLACK;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public void setData(int width, int height) {
		dimension = new Dimension(width, height);
		setCurrentLayer(new Layer(width, height));
		backgroungImage = new Canvas(width, height);
		GraphicsContext gc = backgroungImage.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		gc.setFill(Color.LIGHTGRAY);
		
		//clip = new Rectangle(width, height);
		
		int rectSize = 10;
		for (int i = 0; i < width; i = i + rectSize) {
			for (int j = 0; j < height; j = j + rectSize) {
				if (i % (rectSize * 2) == 0 ^ j % (rectSize * 2) == 0) {
					gc.fillRect(i, j, rectSize, rectSize);
				}
			}
		}
		
		layers.add(currentLayer);
		MainViewController.getInstance().getRightMenuController().updateLayerList();
		drawWorkspace();
	}
	
	public Layer getCurrentLayer() {
		return currentLayer;
	}
	
	public void drawWorkspace() {
		Group layersGroup = new Group();
		layersGroup.getChildren().add(backgroungImage);
		Iterator it = layers.descendingIterator();
		
		// centre le clip
		//clip.setLayoutX(Math.round((MainViewController.getInstance().getScrollPane().getWidth() - dimension.width) / 2));
		//clip.setLayoutY(Math.round((MainViewController.getInstance().getScrollPane().getHeight() - dimension.height) / 2));
		//MainViewController.getInstance().getScrollPane().setClip(clip);
		
		while (it.hasNext()) {
			Layer layer = (Layer) it.next();
			if (layer.isVisible()) {
				layersGroup.getChildren().add(layer);
			}
		}
		MainViewController.getInstance().getScrollPane().setContent(layersGroup);
	}
	
	public void setCurrentColor(Color color) {
		currentColor = color;
		MainViewController.getInstance().getRightMenuController().setColorPickerColor(color);
	}
	
	public Color getCurrentColor() {
		return currentColor;
	}
	
	public void addLayer(Layer newLayer) {
		setCurrentLayer(newLayer);
		layers.addFirst(newLayer);
		drawWorkspace();
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	public LinkedList<Layer> getLayers() {
		return layers;
	}
	
	public void setCurrentLayer(Layer currentLayer) {
		removeEventHandler(Tool.getCurrentTool());
		this.currentLayer = currentLayer;
		addEventHandlers(Tool.getCurrentTool());
	}
	
	public void addEventHandlers(Tool tool) {
		if (this.currentLayer != null && tool != null) {
			this.currentLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, tool.getCurrentOnMousePressedEventHandler());
			this.currentLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, tool.getCurrentOnMouseDraggedEventHandler());
			this.currentLayer.addEventHandler(MouseEvent.MOUSE_RELEASED, tool.getCurrentOnMouseRelesedEventHandler());
		}
	}
	
	public void removeEventHandler(Tool tool) {
		if (this.currentLayer != null && tool != null) {
			this.currentLayer.removeEventHandler(MouseEvent.MOUSE_PRESSED, tool.getCurrentOnMousePressedEventHandler());
			this.currentLayer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, tool.getCurrentOnMouseDraggedEventHandler());
			this.currentLayer.removeEventHandler(MouseEvent.MOUSE_RELEASED, tool.getCurrentOnMouseRelesedEventHandler());
		}
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
		String chosenExtension = "";
		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
			chosenExtension = file.getPath().substring(i + 1);
		}
		
		if (chosenExtension.equals("png") || chosenExtension.equals("jpg")) {
			javafx.scene.image.Image image;
			image = new Image(file.toURI().toString());
			
			Layer newLayer = new Layer(new Dimension((int) image.getWidth(), (int) image.getHeight()));
			addLayer(newLayer);
			
			newLayer.getGraphicsContext2D().drawImage(image, 0, 0);
		}
	}
	
	public void addNewLayer() {
		addLayer(new Layer(currentLayer));
	}
	
	public void deleteCurrentLayer() {
		if (layers.size() != 1) {
			int index = layers.indexOf(currentLayer);
			layers.remove(index);
			if (index >= layers.size()) {
				index--;
			}
			currentLayer = layers.get(index);
			drawWorkspace();
			MainViewController.getInstance().getRightMenuController().updateLayerList();
		}
	}
	
	public void currentLayerToFront() {
		int index = layers.indexOf(currentLayer);
		if (index != 0) {
			Collections.swap(layers, index, index - 1);
		}
		drawWorkspace();
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	public void currentLayerToBack() {
		int index = layers.indexOf(currentLayer);
		if (index < layers.size() - 1) {
			Collections.swap(layers, index, index + 1);
		}
		drawWorkspace();
		MainViewController.getInstance().getRightMenuController().updateLayerList();
	}
	
	public Canvas getBackgroungImage() {
		return backgroungImage;
	}
	
	public void zoom(double factor){
		for (Layer l:layers) {
			l.setScaleX(l.getScaleX() * factor);
			l.setScaleY(l.getScaleY() * factor);
		}
		backgroungImage.setScaleX(backgroungImage.getScaleX() * factor);
		backgroungImage.setScaleY(backgroungImage.getScaleY() * factor);
	}
}