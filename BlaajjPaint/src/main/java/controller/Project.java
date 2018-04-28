package controller;

import javafx.scene.canvas.Canvas;

import java.awt.*;
import java.util.LinkedList;

//import javafx.scene.layout.StackPane;

public class Project {
	private Dimension dimension;
	private LinkedList<Canvas> layers = new LinkedList<>();
	private Canvas currentLayer;
	
	private static Project projectInstance = new Project();
	
	public static Project getInstance() {
		return projectInstance;
	}
	
	private Project() {
	}
	
	public void setData (int width, int height){
		dimension = new Dimension(width, height);
		currentLayer = new Canvas(width, height);
		layers.add(currentLayer);
	}
	
	/*
	private javafx.scene.canvas.Canvas canvas;
	private GraphicsContext gc;
	//private StackPane pane = new StackPane();
	
	public javafx.scene.canvas.Canvas getCanvas() {
		if (canvas == null) {
			// FIXME: lancer une erreur...?
		}
		return canvas;
	}
	
	private void draw() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
		this.canvas.toFront();
		this.canvas.setVisible(true);
		
		for (Layer l: layers) {
			gc.setFill(Color.GREEN);
			gc.fillRect(0, 0, width, height);
			this.canvas.toFront();
			this.canvas.setVisible(true);
		}
	}
	
	public void setCanvas(javafx.scene.canvas.Canvas canvas) {
		this.canvas = canvas;
		height = this.canvas.getHeight();
		width = this.canvas.getWidth();
		gc = this.canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK); //FIXME: À remettre en blanc!
		gc.fillRect(0, 0, width, height);
		//pane = new StackPane();
		//pane.getChildren().add(canvas);
	}
	
	//public StackPane getPane() { return pane; }
	
	public void setHeight(double height) {
		this.height = height;
		this.canvas.setHeight(height);
		draw();
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setWidth(double width) {
		this.width = width;
		this.canvas.setWidth(width);
		draw();
	}
	
	public double getWidth() {
		return width;
	}
	
	public void addLayer(Layer layer) {
		layers.add(layer); // FIXME: Est-ce qu'on check que le layer à ajouter ne soit pas déjà dans la liste...?
	}
	*/
}
