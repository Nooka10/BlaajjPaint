package controller;

import controller.tools.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Project implements Serializable {
	
	private int width;
	
	private int height;
	
	private LinkedList<Layer> layers;
	
	private Background backgroundImage;
	
	private Layer currentLayer;
	
	private Color currentColor;
	
	private static Project projectInstance;
	
	private AnchorPane workspace;
	
	private Rectangle redBorder;
	
	private Rectangle clip;
	/**
	 * Projet étant un singleton on peut récupérer son instance unique avec getInstance
	 *
	 * @return
	 */
	public static Project getInstance() {
		if (projectInstance == null) {
			projectInstance = new Project();
		}
		
		return projectInstance;
	}
	
	/**
	 * Crée un nouveau projet
	 */
	private Project() {
		currentColor = Color.BLACK;
	}
	
	//*** GETTER  ***//
	
	public AnchorPane getWorkspace() {
		return workspace;
	}
	
	/**
	 * Retourne la largeur du workspace (l'image finale qui sera exportée).
	 * @return la largeur du workspace
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Retourne la hauteur du workspace (l'image finale qui sera exportée).
	 *
	 * @return la hauteur du workspace
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Retourne le currentLayer. Utiliser SetCurrentLayer pour le modifier UNIQUEMENT (pour que ce dernier gères les eventHandlers
	 *
	 * @return
	 */
	public Layer getCurrentLayer() {
		
		return currentLayer;
	}
	
	//*** SETTER ***//
	
	/**
	 * Change la couleur sélectionnée pour les outils, met a jour le RightMenu Prooncipalement utilisé par la pipette
	 *
	 * @param color
	 */
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
		this.width = width;
		this.height = height;
		workspace = new AnchorPane();
		
		MainViewController.getInstance().getScrollPane().setContent(workspace);
		
		clip = new Rectangle(width, height);
		double x = Math.round((MainViewController.getInstance().getAnchorPaneCenter().getWidth() - width) / 2);
		double y = Math.round(((MainViewController.getInstance().getAnchorPaneCenter().getHeight() - height - MainViewController.getInstance().getParamBar().getHeight()) / 2));
		workspace.setClip(clip);
		workspace.setTranslateX(x);
		workspace.setTranslateY(y);
		
		MainViewController.getInstance().getScrollPane().setMaxWidth(width);
		MainViewController.getInstance().getScrollPane().setMaxHeight(height);
		
		Image image = new Image("/outils/background.png");
		BackgroundSize bgSize = new BackgroundSize(128, 128, false, false, false, false);
		BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, bgSize);
		backgroundImage = new Background(bgImage);
		
		//MainViewController.getInstance().getScrollPane().setBackground(backgroundImage);
		workspace.setBackground(backgroundImage);
		
		if (isNew) {
			setCurrentLayer(new Layer(width, height, false));
			layers.add(currentLayer);
			workspace.getChildren().add(currentLayer);
			MainViewController.getInstance().getRightMenuController().updateLayerList();
			drawWorkspace();
		}
		
	}
	
	/**
	 * Méthode qui ferme un projet.
	 */
	public void close() {
		width = 0;
		height = 0;
		layers = null;
		backgroundImage = null;
		currentLayer = null;
		Layer.reset(); // réinitialise le compteur définissant les ids des calques
		projectInstance = null;
		workspace = null;
		redBorder = null;
		clip = null;
		
	}
	
	//TODO: METTRE UN COMMENTAIRE PERTINENT SUR CETTE FONCTION JE CAPTES PAS CE QU'ELLE FAIT EN DéTAIL
	//bah elle draw le workspace
	public void drawWorkspace() {
		workspace.getChildren().clear();
		Iterator it = layers.descendingIterator();
		
		while (it.hasNext()) {
			Layer layer = (Layer) it.next();
			if (layer.isVisible()) {
				workspace.getChildren().add(layer);
			}
		}
		workspace.getChildren().add(redBorder);
	}
	
	/**
	 * Retourne la couleur actuellement sélectionnée
	 *
	 * @return
	 */
	public Color getCurrentColor() {
		return currentColor;
	}
	
	/**
	 * Ajoutes un nouveau layer avec les dimensions définies dans le projet
	 */
	public void addNewLayer() {
		addLayer(new Layer(width, height, false));
	}
	
	/**
	 * Ajoutes un nouveau layer passé en paramètre
	 *
	 * @param newLayer
	 */
	public void addLayer(Layer newLayer) {
		setCurrentLayer(newLayer);
		layers.addFirst(newLayer);
		workspace.getChildren().add(currentLayer);
		MainViewController.getInstance().getRightMenuController().updateLayerList();
		drawWorkspace();
	}
	
	/**
	 * Retournes la linked list de layers
	 */
	public LinkedList<Layer> getLayers() {
		return layers;
	}
	
	/**
	 * Change le currentLayer en prenant soin de retirer les eventHandlers sur l'ancien et de les remettre sur le nouveau
	 *
	 * @param currentLayer
	 */
	public void setCurrentLayer(Layer currentLayer) {
		workspace.getChildren().remove(redBorder);
		removeEventHandler(Tool.getCurrentTool());
		this.currentLayer = currentLayer;
		
		redBorder = new Rectangle(0, 0, Color.TRANSPARENT);
		redBorder.setStroke(Color.CADETBLUE);
		redBorder.setStrokeWidth(1);
		
		redBorder.setTranslateX(currentLayer.getBoundsInParent().getMinX());
		redBorder.setTranslateY(currentLayer.getBoundsInParent().getMinY());
		redBorder.setWidth(currentLayer.getBoundsInParent().getWidth());
		redBorder.setHeight(currentLayer.getBoundsInParent().getHeight());
		
		redBorder.setMouseTransparent(true);
		
		
		redBorder.setManaged(false);
		currentLayer.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable,
			                    Bounds oldValue, Bounds newValue) {
				redBorder.setTranslateX(currentLayer.getBoundsInParent().getMinX());
				redBorder.setTranslateY(currentLayer.getBoundsInParent().getMinY());
				redBorder.setWidth(currentLayer.getBoundsInParent().getWidth());
				redBorder.setHeight(currentLayer.getBoundsInParent().getHeight());
			}
		});
		
		workspace.getChildren().add(redBorder);
		addEventHandlers(Tool.getCurrentTool());
		MainViewController.getInstance().getMenuBarController().changeHideButtonText();
		drawWorkspace();
	}
	
	public void addEventHandlers(Tool tool) {
		if (this.currentLayer != null) {
			this.currentLayer.setMouseTransparent(false);
			if (tool != null) {
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, tool.getCurrentOnMousePressedEventHandler());
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, tool.getCurrentOnMouseDraggedEventHandler());
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_RELEASED, tool.getCurrentOnMouseRelesedEventHandler());
			}
		}
	}
	
	public void removeEventHandler(Tool tool) {
		if (this.currentLayer != null) {
			this.currentLayer.setMouseTransparent(true);
			if (tool != null) {
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_PRESSED, tool.getCurrentOnMousePressedEventHandler());
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, tool.getCurrentOnMouseDraggedEventHandler());
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_RELEASED, tool.getCurrentOnMouseRelesedEventHandler());
			}
		}
	}
	
	public void export(File file) {
		if (file != null) {
			Layer resultLayer = new Layer(1,1, true);
			double minX = 0;
			double minY = 0;
			
			for (Layer layer : layers) {
				resultLayer = resultLayer.mergeLayers(layer, true);
				/*
				if(layer.getLayoutX() < minX){
					minX = layer.getLayoutX();
				}
				if(layer.getLayoutY() < minY){
					minY = layer.getLayoutY();
				}
				*/
				if(layer.getTranslateX() < minX){
					minX = layer.getTranslateX();
				}
				if(layer.getTranslateY() < minY){
					minY = layer.getTranslateY();
				}
			}
			
			// redimensionne le calque resultant pour qu'il soit à la taille du projet
			resultLayer = resultLayer.crop(-minX, -minY, -minX + width, -minY + height);
			
			String chosenExtension = "";
			int i = file.getPath().lastIndexOf('.');
			if (i > 0) {
				chosenExtension = file.getPath().substring(i + 1);
			}
			
			if (chosenExtension.equals("png")) {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(Utils.makeSnapshot(resultLayer), null), chosenExtension, file);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
			} else if (chosenExtension.equals("jpg")) {
				SnapshotParameters params = new SnapshotParameters();
				
				params.setFill(Color.WHITE);
				//TODO : antoine, ne marche plus à cause de benoit
				Image canvas = Utils.makeSnapshot(resultLayer);
				
				BufferedImage image = SwingFXUtils.fromFXImage(canvas, null);
				
				BufferedImage imageRGB = new BufferedImage(width, height, BufferedImage.OPAQUE);
				
				Graphics2D graphics = imageRGB.createGraphics();
				
				graphics.drawImage(image, 0, 0, width, height, null);
				try {
					ImageIO.write(imageRGB, "jpg", file);
					graphics.dispose();
				}catch (IOException ex) {
					ex.printStackTrace();
				}
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
			
			Layer newLayer = new Layer((int) image.getWidth(), (int) image.getHeight(), false);
			addLayer(newLayer);
			
			newLayer.getGraphicsContext2D().drawImage(image, 0, 0);
		}
	}
	
	public void deleteCurrentLayer() {
		if (layers.size() != 1) {
			workspace.getChildren().remove(currentLayer);
			int index = layers.indexOf(currentLayer);
			layers.remove(index);
			if (index >= layers.size()) {
				index--;
			}
			setCurrentLayer(layers.get(index));
			MainViewController.getInstance().getRightMenuController().updateLayerList();
			drawWorkspace();
		}
	}
	
	//*** SERIALISATION  ***//
	
	/**
	 * Permet de dé-serialiser en lisant un flux, la projet à l'aide de l'interface Serializable
	 *
	 * @param s flux de lecture
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		projectInstance = this; // pour pas recréer une instance de projet
		
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
		
		MainViewController.getInstance().getRightMenuController().updateLayerList();
		drawWorkspace();
	}

	/**
	 * Permet de serialiser le projet en écrivant dans un flux, à l'aide de l'interface Serializable
	 *
	 * @param s flux d'écriture
	 *
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
		//s.defaultWriteObject();
		// Dimentions du projet
		s.writeInt(width);
		s.writeInt(height);
		
		// Calques
		s.writeInt(layers.size()); // Nombre de calques
		
		Iterator li = layers.descendingIterator();
		
		while (li.hasNext()) {
			s.writeObject(li.next());
		}
	}
}