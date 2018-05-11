package controller;

import controller.tools.Tool;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Project implements Serializable {
	private Dimension dimension;
	private LinkedList<Layer> layers;
	private Canvas backgroungImage; // TODO surement overkill de faire un canevas pour ca
	// TODO: effectivement... utiliser une BackgroundImage semble plus logique non?^
	private Layer currentLayer;
	
	private Color currentColor;
	
	private static Project projectInstance = new Project();
	
	public static Project getInstance() {
		return projectInstance;
	}
	
	private Project() {
		currentColor = Color.BLACK;
	}
	
	//*** GETTER  ***//
	public Dimension getDimension() {
		return dimension;
	}
	
	public Layer getCurrentLayer() {
		return currentLayer;
	}
	
	//*** SETTER ***//
	public void setCurrentColor(Color color) {
		currentColor = color;
		MainViewController.getInstance().getRightMenuController().setColorPickerColor(color);
	}
	
	/**
	 * Méthode qui crée l'état initial du projet.
	 *
	 * @param width
	 * @param height
	 * @param isNew  Indique si on doit créer un nouveau calque de fond
	 */
	public void initData(int width, int height, boolean isNew) {
		layers = new LinkedList<>();
		dimension = new Dimension(width, height);
		
		if (isNew) {
			setCurrentLayer(new Layer(width, height));
		}
		
		backgroungImage = new Canvas(width, height);
		GraphicsContext gc = backgroungImage.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		gc.setFill(Color.LIGHTGRAY);
		
		
		int rectSize = 10;
		for (int i = 0; i < width; i = i + rectSize) {
			for (int j = 0; j < height; j = j + rectSize) {
				if (i % (rectSize * 2) == 0 ^ j % (rectSize * 2) == 0) {
					gc.fillRect(i, j, rectSize, rectSize);
				}
			}
		}
		
		if (isNew) {
			layers.add(currentLayer);
			MainViewController.getInstance().getRightMenuController().createLayerList();
			drawWorkspace();
		}
		
	}
	
	/**
	 * Méthode qui ferme un projet.
	 */
	public void close() {
		backgroungImage = null;
		dimension = null;
		layers = null;
		currentLayer = null;
		
		Layer.reset();
	}
	
	
	public void drawWorkspace() {
		AnchorPane workspace = new AnchorPane();
		workspace.getChildren().add(backgroungImage);
		Iterator it = layers.descendingIterator();
		
		MainViewController.getInstance().getScrollPane().setContent(workspace);
		/*
		// Permet de dessiner le contour du currentLayer
		final javafx.scene.shape.Rectangle redBorder = new Rectangle(0, 0, Color.TRANSPARENT);
		redBorder.setStroke(Color.LIGHTBLUE);
		redBorder.setManaged(false);
		currentLayer.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			
			@Override
			public void changed(ObservableValue<? extends Bounds> observable,
			                    Bounds oldValue, Bounds newValue) {
				redBorder.setLayoutX(currentLayer.getBoundsInParent().getMinX());
				redBorder.setLayoutY(currentLayer.getBoundsInParent().getMinY());
				redBorder.setWidth(currentLayer.getBoundsInParent().getWidth());
				redBorder.setHeight(currentLayer.getBoundsInParent().getHeight());
			}
			
		});
		MainViewController.getInstance().getWorkspace().getChildren().add(redBorder);
		*/
		
		while (it.hasNext()) {
			Layer layer = (Layer) it.next();
			if (layer.isVisible()) {
				workspace.getChildren().add(layer);
			}
		}
		
		workspace.setMinSize(dimension.width, dimension.height);
		workspace.setPrefSize(dimension.width, dimension.height);
		workspace.setMaxSize(dimension.width, dimension.height);
	}
	
	
	public Color getCurrentColor() {
		return currentColor;
	}
	
	public void addNewLayer() {
		addLayer(new Layer(dimension.width, dimension.height));
	}
	
	public void addLayer(Layer newLayer) {
		setCurrentLayer(newLayer);
		layers.addFirst(newLayer);
		MainViewController.getInstance().getRightMenuController().addNewLayer(newLayer);
		drawWorkspace();
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
			boolean transparent = true;
			if (chosenExtension.equals("png")) {
				params.setFill(Color.TRANSPARENT);
				
			} else if (chosenExtension.equals("jpg")) {
				//params.setFill(Color.TRANSPARENT);
				//params.setFill(Color.WHITE);
				transparent = false;
			}
			
			try {
				
				ImageIO.write(SwingFXUtils.fromFXImage(resultLayer.createImageFromCanvasJPG(1, params, transparent), null), chosenExtension, file);
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
	
	public void deleteCurrentLayer() {
		if (layers.size() != 1) {
			int index = layers.indexOf(currentLayer);
			layers.remove(index);
			if (index >= layers.size()) {
				index--;
			}
			currentLayer = layers.get(index);
			MainViewController.getInstance().getRightMenuController().deleteLayer(index);
			drawWorkspace();
		}
	}
	
	public Canvas getBackgroungImage() {
		return backgroungImage;
	}
	
	
	//*** SERIALISATION  ***//
	
	/**
	 * Permet de dé-serialiser la projet à l'aide de l'interface Serializable
	 *
	 * @param s
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		
		// pour pas recréer une instance de projet
		projectInstance = this;
		
		initData(s.readInt(), s.readInt(), false);
		
		int nbCalques = s.readInt();
		
		int maxCount = 1;
		for (int i = 0; i < nbCalques; ++i) {
			
			Layer l = (Layer) s.readObject();
			addLayer(l);
			if (l.id() > maxCount)
				maxCount = l.id();
			
		}
		
		Layer.setCount(maxCount);
		
		setCurrentLayer(layers.getFirst());
		
		MainViewController.getInstance().getRightMenuController().createLayerList();
		drawWorkspace();
	}
	
	/**
	 * Permet de serialiser la projet à l'aide de l'interface Serializable
	 *
	 * @param s
	 *
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
		//s.defaultWriteObject();
		// Dimentions du projet
		s.writeInt(dimension.width);
		s.writeInt(dimension.height);
		
		
		// Calques
		s.writeInt(layers.size());        // Nombre de qualques
		
		Iterator li = layers.descendingIterator();
		
		while (li.hasNext()) {
			//System.out.println(li.next());
			s.writeObject(li.next());
		}
	}
	
	
	public void zoom(double factor) {
		for (Layer l : layers) {
			l.setScaleX(l.getScaleX() * factor);
			l.setScaleY(l.getScaleY() * factor);
		}
		backgroungImage.setScaleX(backgroungImage.getScaleX() * factor);
		backgroungImage.setScaleY(backgroungImage.getScaleY() * factor);
	}
}