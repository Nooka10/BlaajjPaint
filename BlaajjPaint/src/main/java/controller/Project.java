package controller;

import controller.tools.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
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

/**
 * Classe implémentant un projet .blaajj avec le modèle Singleton. Gère l'ensemble des données définissant un projet.
 */
public class Project implements Serializable {
	private static Project projectInstance; // l'instance unique du singleton Project
	private int width; // la largeur du projet (de l'espace de travail)
	private int height; // la hauteur du projet (de l'espace de travail)
	private LinkedList<Layer> layers; // la liste contenant tous les calques du projet
	private Background backgroundImage; // l'image d'arrière-plan
	private Layer currentLayer; // le calque actuellement sélectionné
	private Color currentColor; // la couleur actuellement sélectionnée
	private Rectangle layerBorders; // le rectangle dessinant les bordures colorées du calque sélectionné
	private Rectangle clip; // le rectangle rempli masquant tout élément situé hors de l'espace de travail
	private AnchorPane workspace; // l'espace de travail
	
	/**
	 * Crée un nouveau projet.
	 */
	private Project() {
		currentColor = MainViewController.getInstance().getRightMenuController().getColorPicker().getValue();
	}
	
	/**
	 * Retourne l'instance unique du singleton Project.
	 *
	 * @return l'instance unique du singleton Project.
	 */
	public static Project getInstance() {
		if (projectInstance == null) {
			projectInstance = new Project();
		}
		
		return projectInstance;
	}
	
	/**
	 * Retourne la largeur de l'espace de travail.
	 *
	 * @return la largeur de l'espace de travail.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Retourne la hauteur de l'espace de travail.
	 *
	 * @return la hauteur de l'espace de travail.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Retourne le calque actuellement sélectionné.
	 *
	 * @return le calque actuellement sélectionné.
	 */
	public Layer getCurrentLayer() {
		return currentLayer;
	}
	
	/**
	 * Retourne la couleur actuellement sélectionnée.
	 *
	 * @return la couleur actuellement sélectionnée.
	 */
	public Color getCurrentColor() {
		return currentColor;
	}
	
	/**
	 * Change la couleur actuellement sélectionnée pour les outils.
	 *
	 * @param color, la nouvelle couleur à sélectionner.
	 */
	public void setCurrentColor(Color color) {
		currentColor = color;
		MainViewController.getInstance().getRightMenuController().setColorPickerColor(color); // FIXME: à checker si utile...
	}
	
	/**
	 * Retourne la liste contenant tout les calques du projet.
	 */
	public LinkedList<Layer> getLayers() {
		return layers;
	}
	
	/**
	 * remplace le calque actuellement sélectionné par le calque courant. Retire les eventHandlers appliqués à l'ancien calque et les remet sur le nouveau.
	 *
	 * @param currentLayer, le calque que l'on souhaite définir comme calque actuellement sélectionné.
	 */
	public void setCurrentLayer(Layer currentLayer) {
		workspace.getChildren().remove(layerBorders);
		removeEventHandler(Tool.getCurrentTool());
		this.currentLayer = currentLayer;
		
		layerBorders = new Rectangle(currentLayer.getBoundsInParent().getWidth(), currentLayer.getBoundsInParent().getHeight(), Color.TRANSPARENT);
		layerBorders.setStroke(Color.CADETBLUE);
		layerBorders.setStrokeWidth(1);
		
		layerBorders.setTranslateX(currentLayer.getBoundsInParent().getMinX());
		layerBorders.setTranslateY(currentLayer.getBoundsInParent().getMinY());
		
		layerBorders.setMouseTransparent(true);
		layerBorders.setManaged(false);
		currentLayer.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				layerBorders.setTranslateX(currentLayer.getBoundsInParent().getMinX());
				layerBorders.setTranslateY(currentLayer.getBoundsInParent().getMinY());
				layerBorders.setWidth(currentLayer.getBoundsInParent().getWidth());
				layerBorders.setHeight(currentLayer.getBoundsInParent().getHeight());
			}
		});
		
		workspace.getChildren().add(layerBorders);
		addEventHandlers(Tool.getCurrentTool());
		
		MainViewController.getInstance().getRightMenuController().setOpacitySlider(currentLayer.getLayerOpacity());
		MainViewController.getInstance().getRightMenuController().setOpacityTextField(String.valueOf(currentLayer.getLayerOpacity()));
		MainViewController.getInstance().getMenuBarController().changeHideButtonText();
		drawWorkspace();
	}
	
	/**
	 * Initialise le projet selon la largeur et la hauteur données en paramètre.
	 *
	 * @param width, la largeur du projet.
	 * @param height, la hauteur du projet.
	 * @param isNew, si vrai, crée un premier calque vide, si false, n'en crée pas.
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
		
		workspace.setBackground(backgroundImage);
		
		if (isNew) {
			setCurrentLayer(new Layer(width, height, false));
			layers.add(currentLayer);
			workspace.getChildren().add(currentLayer);
			MainViewController.getInstance().getRightMenuController().updateLayerList();
		}
	}
	
	/**
	 * Dessine l'espace de travail en dessinant tous les calques du projet.
	 */
	public void drawWorkspace() {
		workspace.getChildren().clear();
		Iterator it = layers.descendingIterator();
		
		while (it.hasNext()) {
			Layer layer = (Layer) it.next();
			if (layer.isVisible()) {
				workspace.getChildren().add(layer);
			}
		}
		workspace.getChildren().add(layerBorders);
	}
	
	/**
	 * Ajoute un nouveau calque au projet. Le calque aura la même taille que le projet.
	 */
	public void addNewLayer() {
		addLayer(new Layer(width, height, false));
	}
	
	/**
	 * Ajoute le calque passé en paramètre au projet.
	 *
	 * @param newLayer, le calque à ajouter au projet.
	 */
	public void addLayer(Layer newLayer) {
		setCurrentLayer(newLayer);
		layers.addFirst(newLayer);
		workspace.getChildren().add(currentLayer);
		if (!newLayer.isTempLayer()) {
			MainViewController.getInstance().getRightMenuController().updateLayerList();
		}
		drawWorkspace();
	}
	
	/**
	 * Supprime le calque passé en paramètre.
	 * @param layer, le calque à supprimer du projet.
	 */
	public void removeLayer(Layer layer) {
		layers.remove(layer);
		drawWorkspace();
	}
	
	/**
	 * Ajoute les eventHandler de l'outil passé en paramètre au calque actuellement sélectionné.
	 * @param tool, l'outil dont on souhaite ajouter les EventHandler au calque actuellement sélectionné.
	 */
	public void addEventHandlers(Tool tool) {
		if (this.currentLayer != null) {
			this.currentLayer.setMouseTransparent(false);
			if (tool != null) {
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, tool.getCurrentOnMousePressedEventHandler());
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, tool.getCurrentOnMouseDraggedEventHandler());
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_RELEASED, tool.getCurrentOnMouseRelesedEventHandler());
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_ENTERED, tool.getCurrentOnMouseEnteredEventHandler());
				this.currentLayer.addEventHandler(MouseEvent.MOUSE_EXITED, tool.getCurrentOnMouseExitedEventHandler());
			}
		}
	}
	
	/**
	 * Supprime les eventHandler de l'outil passé en paramètre du calque actuellement sélectionné.
	 * @param tool, l'outil dont on souhaite supprimer les EventHandler du calque actuellement sélectionné.
	 */
	public void removeEventHandler(Tool tool) {
		if (this.currentLayer != null) {
			this.currentLayer.setMouseTransparent(true);
			if (tool != null) {
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_PRESSED, tool.getCurrentOnMousePressedEventHandler());
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, tool.getCurrentOnMouseDraggedEventHandler());
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_RELEASED, tool.getCurrentOnMouseRelesedEventHandler());
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_ENTERED, tool.getCurrentOnMouseEnteredEventHandler());
				this.currentLayer.removeEventHandler(MouseEvent.MOUSE_EXITED, tool.getCurrentOnMouseExitedEventHandler());
			}
		}
	}
	
	/**
	 * Exporte le projet dans le fichier passé en paramètre.
	 * @param file, le fichier dans lequel on souhaite exporter le projet.
	 */
	public void export(File file) {
		if (file != null) {
			Layer resultLayer = new Layer(1, 1, true); // crée un calque temporaire
			double minX = 0;
			double minY = 0;
			
			for (Layer layer : layers) {
				resultLayer = resultLayer.mergeLayers(layer, true); // fusionne chaque calque sur le calque temporaire
				if (layer.getTranslateX() < minX) {
					minX = layer.getTranslateX();
				}
				if (layer.getTranslateY() < minY) {
					minY = layer.getTranslateY();
				}
			}
			
			// redimensionne le calque final pour qu'il soit à la taille du projet
			resultLayer = resultLayer.crop(-minX, -minY, -minX + width, -minY + height);
			
			// récupère l'exntension du fichier
			String chosenExtension = "";
			int i = file.getPath().lastIndexOf('.');
			if (i > 0) {
				chosenExtension = file.getPath().substring(i + 1);
			}
			
			if (chosenExtension.equals("png")) { // vrai si l'utilisateur souhaite exporter en .png
				try {
					// fait un snapshot du calque puis le sauvegarde en tant qu'image au format png
					ImageIO.write(SwingFXUtils.fromFXImage(Utils.makeSnapshot(resultLayer, Color.TRANSPARENT), null), chosenExtension, file);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else if (chosenExtension.equals("jpg")) { // vrai si l'utilisateur souhaite exporter en .jpg
				// fait un snapshot du calque puis le sauvegarde en tant que BufferedImage
				BufferedImage image = SwingFXUtils.fromFXImage(Utils.makeSnapshot(resultLayer, Color.WHITE), null);
				BufferedImage imageRGB = new BufferedImage(width, height, BufferedImage.OPAQUE);
				Graphics2D graphics = imageRGB.createGraphics();
				graphics.drawImage(image, 0, 0, width, height, null);
				try {
					ImageIO.write(imageRGB, "jpg", file); // Dessine l'image au format jpg
					graphics.dispose();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Importe l'image du fichier passé en paramètre en tant que nouveau calque et l'ajoute au projet.
	 * @param file, l'image à importer.
	 */
	public void importImage(File file) {
		String chosenExtension = "";
		// récupère l'extension du fichier
		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
			chosenExtension = file.getPath().substring(i + 1);
		}
		
		// importe l'image si le fichier a une extension valide (jpg ou png)
		if (chosenExtension.equals("png") || chosenExtension.equals("jpg")) {
			Image image = new Image(file.toURI().toString());
			
			Layer newLayer = new Layer((int) image.getWidth(), (int) image.getHeight(), false); // crée un nouveau calque de la taille de l'image
			newLayer.getGraphicsContext2D().drawImage(image, 0, 0); // dessine l'image sur le calque
			addLayer(newLayer); // ajoute le calque au projet
		}
	}
	
	/**
	 * Supprime le calque actuellement sélectionné du projet.
	 */
	public void deleteCurrentLayer() {
		if (layers.size() != 1) { // impossible de supprimer le calque si c'est le seul calque du projet
			workspace.getChildren().remove(currentLayer);
			int index = layers.indexOf(currentLayer);
			layers.remove(index);
			if (index >= layers.size()) {
				index--;
			}
			setCurrentLayer(layers.get(index));
		}
	}
	
	/**
	 * Ferme le projet actuellement en cours d'édition.
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
		layerBorders = null;
		clip = null;
	}
	
	/**
	 * Permet de déserialiser une sauvegarde du projet en lisant le flux reçu en paramètre.
	 *
	 * @param s,
	 * 		le flux à lire et déserialiser.
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		projectInstance = this;
		initData(s.readInt(), s.readInt(), false); //intialise les données du projet
		
		int nbCalques = s.readInt(); // lit et définit la nombre de calques du projet
		
		int maxCount = 1;
		for (int i = 0; i < nbCalques; ++i) {
			Layer l = (Layer) s.readObject(); // lit et définit le calque
			addLayer(l); // ajoute ce calque au projet
			if (l.id() > maxCount) {
				maxCount = l.id(); // récupère le plus grand id de calque pour configurer correctement la variable count de la classe Layer
			}
			
		}
		Layer.setCount(maxCount); // configure la variable count de la classe layer
		currentColor = MainViewController.getInstance().getRightMenuController().getColorPicker().getValue(); // définit la couleur actuellement sélectionnée
		setCurrentLayer(layers.getFirst()); // définit le calque actuellement sélectionné
		MainViewController.getInstance().getRightMenuController().updateLayerList(); // met à jour la liste des calques en bas à droite de la GUI
	}
	
	/**
	 * Permet de sérialiser le projet pour en faire un sauvegarde en écrivant dans un flux.
	 *
	 * @param s,
	 * 		le flux dans lequel on veux écrire.
	 *
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
		// Dimensions du projet
		s.writeInt(width); // sauve la largeur du projet
		s.writeInt(height); // sauve la hauteur du projet
		
		s.writeInt(layers.size()); // sauve le nombre de calques
		
		Iterator li = layers.descendingIterator();
		while (li.hasNext()) {
			s.writeObject(li.next()); // sauvegarde chaque calque du projet
		}
	}
}