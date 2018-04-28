package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.LinkedList;
import javafx.scene.paint.Color;
//import javafx.scene.layout.StackPane;

public class Project {
	private Dimension dimension;
	private LinkedList<Canvas> layers = new LinkedList<>();
	private Canvas currentLayer;
	private MainViewController mainViewController;
	
	private GraphicsContext gc;
	//private StackPane pane = new StackPane();
	
	private static Project projectInstance = new Project();
	
	public static Project getInstance() {
		return projectInstance;
	}
	
	private Project() {
	}
	
	public void setData (int width, int height, MainViewController mainViewController){
		this.mainViewController = mainViewController;
		dimension = new Dimension(width, height);
		currentLayer = new Canvas(width, height);
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
		currentLayer.setVisible(true);
		
		mainViewController.showCanvas(currentLayer);
	}
}
