package controller;

import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.Utils;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Classe implémentant un calque.
 */
public class Layer extends Canvas implements Serializable {
	private int id; // l'id du calque
	private static int count = 0; // le nombre de calques qui ont été créés
	private String nomCalque; // le nom du calque
	private String layerDescription; // sa description (si c'est une forme, un texte, ...)
	private boolean isTempLayer; // vrai si le layer est temporaire, false sinon
	
	/**
	 * Construit un calque de la largeur et la hauteur fournis en paramètres.
	 *
	 * @param width, la largeur du calque.
	 * @param height, la hauteur du calque.
	 * @param isTempLayer, un booléen valant true s'il s'agit d'un calque temporaire (n'incrémente pas le count), false sinon.
	 */
	public Layer(int width, int height, boolean isTempLayer) {
		super(width, height);
		if (!isTempLayer) {
			count++;
		}
		id = count;
		if (isTempLayer) {
			nomCalque = "Calque temporaire";
		} else {
			nomCalque = "Calque " + id;
		}
		this.isTempLayer = isTempLayer;
	}
	
	/**
	 * Construit un calque de la largeur et la hauteur fournis en paramètres et lui ajoute la description également reçue en paramètre.
	 *
	 * @param width  la largeur de notre calque
	 * @param height la hauteur de notre calque
	 * @param layerDescription le layerDescription à donner au calque (layerDescription affiché dans la liste de calque)
	 * @param isTempLayer booléen valant True s'il s'agit d'un calque temporaire (n'incrémente pas l'id), false sinon
	 */
	public Layer(int width, int height, String layerDescription, boolean isTempLayer) {
		this(width, height, isTempLayer);
		if (!isTempLayer) {
			nomCalque = "Calque " + id + ", " + layerDescription;
		}
		this.layerDescription = layerDescription;
	}
	
	/**
	 * Construit un nouveau calque en copiant les propriétés du calque reçu en paramètre.
	 *
	 * @param toCopy, le calque à copier.
	 */
	public Layer(Layer toCopy, boolean isTempLayer) {
		this((int) toCopy.getWidth(), (int) toCopy.getHeight(), toCopy.layerDescription, isTempLayer);
		boolean visibility = toCopy.isVisible(); // enregistre la visibilité du calque à copier
		toCopy.setVisible(true); // rend le calque à copier visible visible
		
		// fait un snapshot du calque à copier puis le dessine sur le nouveau calque.
		getGraphicsContext2D().drawImage(Utils.makeSnapshot(toCopy, Color.TRANSPARENT), 0, 0, getWidth(), getHeight());
		toCopy.setVisible(visibility); // remet la visibilité d'origine du calque à copier
		setVisible(visibility); // attribue la même visibilité au nouveau calque que la visibilité d'origine du calque à copier
		
		setTranslateX(toCopy.getTranslateX()); // attribue au nouveau calque le même décalage sur l'axe des X que celui du calque à copier
		setTranslateY(toCopy.getTranslateY()); // attribue au nouveau calque le même décalage sur l'axe des Y que celui du calque à copier
	}
	
	/**
	 * Retourne vrai si le calque est un calque temporaire, false sinon.
	 * @return vrai si le calque est un calque temporaire, false sinon.
	 */
	public boolean isTempLayer() {
		return isTempLayer;
	}
	
	/**
	 * Permet de définir la valeur du compteur de calques. Utile pour l'ouverture d'un projet enregistré.
	 * @param _count, la valeur à donner au compteur de calques.
	 */
	static void setCount(int _count) {
		count = ++_count;
	}
	
	/**
	 * Retourne l'id du calque.
	 * @return l'id du calque.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * Fusionne le calque (this) avec le calque passé en paramètre et retourne un nouveau calque contenant la fusion des deux calques.
	 *
	 * @param backgroundLayer, le calque à l'arrière-plan (sur lequel on va dessiner)
	 * @param mergeSurCalqueTemporaire, true si on fait un merge sur des calques temporaires (le count du calque retourné n'est pas incrémenté)
	 * @return un nouveau calque contenant la fusion du calque courant et de celui reçu en paramètre.
	 */
	public Layer mergeLayers(Layer backgroundLayer, boolean mergeSurCalqueTemporaire) {
		Image image1 = Utils.makeSnapshot(this, Color.TRANSPARENT);
		Image image2 = Utils.makeSnapshot(backgroundLayer, Color.TRANSPARENT);
		
		// prend le décalage minimum des deux calques
		double minX = getTranslateX() < backgroundLayer.getTranslateX() ? getTranslateX() : backgroundLayer.getTranslateX();
		double minY = getTranslateY() < backgroundLayer.getTranslateY() ? getTranslateY() : backgroundLayer.getTranslateY();
		
		// prend le décalage maximum des deux calques
		double maxX = getTranslateX() + getWidth() > backgroundLayer.getTranslateX() + backgroundLayer.getWidth() ? getTranslateX() + getWidth() : backgroundLayer.getTranslateX() + backgroundLayer.getWidth();
		double maxY = getTranslateY() + getHeight() > backgroundLayer.getTranslateY() + backgroundLayer.getHeight() ? getTranslateY() + getHeight() : backgroundLayer.getTranslateY() + backgroundLayer.getHeight();
		
		// crée un nouveau calque qui contiendra la fusion des deux autres
		Layer mergeLayer = new Layer((int) (maxX - minX), (int) (maxY - minY), mergeSurCalqueTemporaire);
		
		// dessine les deux calques sur notre nouveau calque fusion
		mergeLayer.getGraphicsContext2D().drawImage(image2, backgroundLayer.getTranslateX() - minX, backgroundLayer.getTranslateY() - minY, backgroundLayer.getWidth(), backgroundLayer.getHeight());
		mergeLayer.getGraphicsContext2D().drawImage(image1, getTranslateX() - minX, getTranslateY() - minY, getWidth(), getHeight());
		
		// place le calque fusionné avec le bon décalage
		mergeLayer.setTranslateX(minX);
		mergeLayer.setTranslateY(minY);
		
		return mergeLayer;
	}
	
	/**
	 * Classe interne qui encapsule la commande de changement d'opacité. Permet de faire un undo/redo sur le changement d'opacité. Attention à TOUJOURS setNewOpacity en
	 * premier.
	 */
	public class OpacitySave implements ICmd {
		
		double oldOpacity;
		double newOpacity;
		
		public OpacitySave(double newOpacity) {
			// par défaut on set à l'opacité courante au cas ou un débile oublie de faire le setNewOpacity pas que ça passe à 0
			oldOpacity = getLayerOpacity();
			this.newOpacity = newOpacity;
		}
		
		public OpacitySave(double oldOpacity, double newOpacity) {
			this.oldOpacity = oldOpacity;
			this.newOpacity = newOpacity;
		}
		
		@Override
		public void execute() {
			updateLayerOpacity(newOpacity);
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			MainViewController.getInstance().getRightMenuController().setOpacityTextField(String.valueOf(oldOpacity));
			MainViewController.getInstance().getRightMenuController().setOpacitySlider(oldOpacity);
			updateLayerOpacity(oldOpacity);
		}
		
		@Override
		public void redo() {
			MainViewController.getInstance().getRightMenuController().setOpacityTextField(String.valueOf(newOpacity));
			MainViewController.getInstance().getRightMenuController().setOpacitySlider(newOpacity);
			updateLayerOpacity(newOpacity);
		}
		
		@Override
		public String toString() {
			return "Opacité changée de " + Math.round(oldOpacity) + " à " + Math.round(newOpacity);
		}
	}
	
	public void setLayerOpacity(double opacity) {
		new OpacitySave(opacity).execute();
	}
	
	public void setLayerOpacity(double oldOpacity, double newOpacity) {
		new OpacitySave(oldOpacity, newOpacity).execute();
	}
	
	public void updateLayerOpacity(double opacity) {
		Project.getInstance().getCurrentLayer().setOpacity(opacity / 100);
	}
	
	public int getLayerOpacity() {
		return (int) Math.round(Project.getInstance().getCurrentLayer().getOpacity() * 100);
	}
	
	@Override
	public String toString() {
		return nomCalque;
	}
	
	
	/** 
	 * SERIALISATION
	 **/

	/**
	 * Permet de dé-serialiser en lisant un flux, la projet à l'aide de l'interface Serializable
	 *
	 * @param s flux de lecture
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {

		id = s.readInt();
		nomCalque = (String) s.readObject();
		super.setWidth(s.readDouble());
		super.setHeight(s.readDouble());
		
		//super.setLayoutX(s.readDouble());
		//super.setLayoutY(s.readDouble());
		super.setTranslateX(s.readDouble());
		super.setTranslateY(s.readDouble());


		double tmpOpacity = s.readDouble();
		// opacité de Canevas [0;1]
		super.setOpacity(tmpOpacity);
		super.setVisible(s.readBoolean());
		
		Image image = SwingFXUtils.toFXImage(ImageIO.read(s), null);
		
		getGraphicsContext2D().drawImage(image, 0, 0, getWidth(), getHeight());

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
		s.writeInt(id);
		s.writeObject(nomCalque);
		s.writeDouble(super.getWidth());
		s.writeDouble(super.getHeight());
		
		//s.writeDouble(getLayoutX());
		//s.writeDouble(getLayoutY());
		
		s.writeDouble(getTranslateX());
		s.writeDouble(getTranslateY());
		
		double tmpOpacity = super.getOpacity();
		boolean tmpVisible = super.isVisible();
		
		s.writeDouble(tmpOpacity);                    // opacité de Canevas [0;1]
		s.writeBoolean(tmpVisible);
		
		this.setVisible(true);
		this.setOpacity(1);                            // enlève l'opacité pour la sauvegardes
		ImageIO.write(SwingFXUtils.fromFXImage(Utils.makeSnapshot(this, Color.TRANSPARENT), null), "png", s);
		this.setOpacity(tmpOpacity);                // Remet l'opacité
		this.setVisible(tmpVisible);                // Remet la visibilité
		
	}
	
	public static void reset() {
		count = 0;
	}

	public Layer crop(double x1, double y1, double x2, double y2){
		// Récupération du PixelReader - permet de lire les pixels sur le graphics context
		WritableImage srcMask = new WritableImage((int) getWidth(), (int) getHeight());
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		srcMask = snapshot(params, srcMask);
		PixelReader pixelReader = srcMask.getPixelReader();
		double x = x1 < x2 ? x1 : x2;
		double y = y1 < y2 ? y1 : y2;
		double width = Math.abs(x1 - x2);
		double height = Math.abs(y1 - y2);
		// Test si les positions sont dans le calque
		if(x < 0){
			x = 0;
		}
		if(y < 0){
			y = 0;
		}
		if(x + width > getWidth()){
			width = width - (x + width - getWidth());
		}
		if(y + height > getHeight()){
			height = height - (y + height - getHeight());
		}

		WritableImage newImage = new WritableImage(pixelReader, (int)x, (int)y, (int)width, (int)height);
		
		this.setWidth(width);
		this.setHeight(height);

		this.setTranslateX(x + this.getTranslateX());
		this.setTranslateY(y + this.getTranslateY());
		
		Utils.redrawSnapshot(this, newImage);
		return this;
	}
}