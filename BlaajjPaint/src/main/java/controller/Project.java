package controller;

import controller.tools.Tool;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
	
	private static Project projectInstance;
	
	private Group workspace;
	
	private AnchorPane anchorPaneWorkspace;
	
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
	
	public Group getWorkspace() {
		return workspace;
	}
	
	/**
	 * Retourne la taille de l'image définie dans le projet ( C'est les dimensions qu'aura l'image finale lors de son export )
	 *
	 * @return
	 */
	public Dimension getDimension() {
		
		return dimension;
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
	 * @param isNew
	 * 		Indique si on doit créer un nouveau calque de fond
	 */
	public void initData(int width, int height, boolean isNew) {
		layers = new LinkedList<>();
		dimension = new Dimension(width, height);
		
		anchorPaneWorkspace = new AnchorPane();
		workspace = new Group();
		anchorPaneWorkspace.getChildren().add(workspace);
		setClip(width, height, workspace);
		
		//MainViewController.getInstance().getScrollPane().setMaxWidth(width);
		//MainViewController.getInstance().getScrollPane().setMaxHeight(height);
		
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
			setCurrentLayer(new Layer(width, height));
			layers.add(currentLayer);
			workspace.getChildren().add(currentLayer);
			MainViewController.getInstance().getRightMenuController().updateLayerList();
			drawWorkspace();
		}
		MainViewController.getInstance().getScrollPane().setContent(anchorPaneWorkspace);
		
	}
	
	private void setClip(double width, double height, Node node) {
		Rectangle clip = new Rectangle(width, height);
		clip.setLayoutX(Math.round((MainViewController.getInstance().getAnchorPaneCenter().getWidth() - width) / 2));
		clip.setLayoutY(Math.round(((MainViewController.getInstance().getAnchorPaneCenter().getHeight() - height - MainViewController.getInstance().getParamBar().getHeight()) / 2) + MainViewController.getInstance().getParamBar().getHeight()));
		node.setClip(clip);
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
	
	//TODO: METTRE UN COMMENTAIRE PERTINENT SUR CETTE FONCTION JE CAPTES PAS CE QU'ELLE FAIT EN DéTAIL
	//bah elle draw le workspace
	public void drawWorkspace() {
		workspace.getChildren().clear();
		workspace.getChildren().add(backgroungImage);
		Iterator it = layers.descendingIterator();
		
		while (it.hasNext()) {
			Layer layer = (Layer) it.next();
			if (layer.isVisible()) {
				workspace.getChildren().add(layer);
			}
		}
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
		addLayer(new Layer(dimension.width, dimension.height));
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
		MainViewController.getInstance().getRightMenuController().addNewLayer(newLayer);
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
		removeEventHandler(Tool.getCurrentTool());
		this.currentLayer = currentLayer;
		addEventHandlers(Tool.getCurrentTool());
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
			Layer resultLayer = new Layer(dimension.width, dimension.height);
			for (Layer layer : layers) {
				resultLayer = resultLayer.mergeLayers(layer);
			}
			String chosenExtension = "";
			int i = file.getPath().lastIndexOf('.');
			if (i > 0) {
				chosenExtension = file.getPath().substring(i + 1);
			}
			
			if (chosenExtension.equals("png")) {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(resultLayer.createImageFromCanvas(4), null), chosenExtension, file);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
			} else if (chosenExtension.equals("jpg")) {
				SnapshotParameters params = new SnapshotParameters();
				
				params.setFill(Color.WHITE);
				
				Image lol = resultLayer.createImageFromCanvasJPG(4).getImage();
				
				PixelReader reader = lol.getPixelReader();
				WritableImage newImage = new WritableImage(reader, (int) -resultLayer.getLayoutX(), (int) -resultLayer.getLayoutY(), dimension.width * 4, dimension.height * 4);
				
				System.out.println(-resultLayer.getLayoutX());
				
				BufferedImage image = SwingFXUtils.fromFXImage(newImage, null);
				
				BufferedImage imageRGB = new BufferedImage(dimension.width, dimension.height, BufferedImage.OPAQUE);
				
				Graphics2D graphics = imageRGB.createGraphics();
				
				graphics.drawImage(image, 0, 0, dimension.width, dimension.height, null);
				try {
					ImageIO.write(imageRGB, "jpg", file);
					graphics.dispose();
				} catch (IOException e) {
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
			
			Layer newLayer = new Layer(new Dimension((int) image.getWidth(), (int) image.getHeight()));
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
		
		MainViewController.getInstance().getRightMenuController().updateLayerList();
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