package controller;

import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.embed.swing.SwingFXUtils;
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
	 * @param width,
	 * 		la largeur du calque.
	 * @param height,
	 * 		la hauteur du calque.
	 * @param isTempLayer,
	 * 		un booléen valant true s'il s'agit d'un calque temporaire (n'incrémente pas le count), false sinon.
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
	 * @param width
	 * 		la largeur de notre calque
	 * @param height
	 * 		la hauteur de notre calque
	 * @param layerDescription
	 * 		le layerDescription à donner au calque (layerDescription affiché dans la liste de calque)
	 * @param isTempLayer
	 * 		booléen valant True s'il s'agit d'un calque temporaire (n'incrémente pas l'id), false sinon
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
	 * @param toCopy,
	 * 		le calque à copier.
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
	 *
	 * @return vrai si le calque est un calque temporaire, false sinon.
	 */
	public boolean isTempLayer() {
		return isTempLayer;
	}
	
	/**
	 * Permet de définir la valeur du compteur de calques. Utile pour l'ouverture d'un projet enregistré.
	 *
	 * @param _count,
	 * 		la valeur à donner au compteur de calques.
	 */
	static void setCount(int _count) {
		count = ++_count;
	}
	
	/**
	 * Retourne l'id du calque.
	 *
	 * @return l'id du calque.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * Crée une commande sauvegardant le changement d'opacité d'un calque à la nouvelle valeur passée en paramètre. L'ancienne opacité est calculée et enregistrée
	 * automatiquement.
	 *
	 * @param newOpacity,
	 * 		la nouvelle opacité du calque.
	 *
	 * @apiNote ATTENTION, ne peut fonctionner que si l'ancienne opacité est encore appliquée au calque (n'a pas encore été modifiée) au moment de la création de cette
	 * commande.
	 */
	public void createOpacitySave(double newOpacity) {
		double oldOpacity = getLayerOpacity();
		createOpacitySave(oldOpacity, newOpacity);
	}
	
	/**
	 * Crée une commande sauvegardant le changement d'opacité d'un calque à la nouvelle valeur passée en paramètre.
	 *
	 * @param oldOpacity,
	 * 		l'ancienne opacité du calque.
	 * @param newOpacity,
	 * 		la nouvelle opacité du calque.
	 */
	public void createOpacitySave(double oldOpacity, double newOpacity) {
		if (oldOpacity != newOpacity) {
			new OpacitySave(oldOpacity, newOpacity).execute();
		}
	}
	
	/**
	 * Applique au calque la nouvelle opacité reçue en paramètre.
	 *
	 * @param opacity,
	 * 		un entier compris entre 0 et 100.
	 */
	public void updateLayerOpacity(double opacity) {
		Project.getInstance().getCurrentLayer().setOpacity(opacity / 100);
	}
	
	/**
	 * Retourne l'opacité du calque.
	 *
	 * @return l'opacité du calque, un entier entre 0 et 100.
	 */
	public int getLayerOpacity() {
		return (int) Math.round(Project.getInstance().getCurrentLayer().getOpacity() * 100);
	}
	
	@Override
	public String toString() {
		return nomCalque;
	}
	
	/**
	 * Réinitialise le compteur de calque. Appelé lorsqu'on ferme le projet.
	 */
	public static void reset() {
		count = 0;
	}
	
	/**
	 * Fusionne le calque (this) avec le calque passé en paramètre et retourne un nouveau calque contenant la fusion des deux calques.
	 *
	 * @param backgroundLayer,
	 * 		le calque à l'arrière-plan (sur lequel on va dessiner)
	 * @param mergeSurCalqueTemporaire,
	 * 		true si on fait un merge sur des calques temporaires (le count du calque retourné n'est pas incrémenté)
	 *
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
	 * Effectue le rognage du calque aux dimensions passées en paramètre.
	 *
	 * @param x1,
	 * 		la position X d'un des deux bords verticaux du calque (après rognage)
	 * @param y1,
	 * 		la position Y d'un des deux bords horizontaux du calque (après rognage)
	 * @param x2,
	 * 		la position X de l'autre des deux bords verticaux du calque (après rognage)
	 * @param y2,
	 * 		la position Y de l'autre des deux bords horizontaux du calque (après rognage)
	 *
	 * @return le calque rogné.
	 */
	public Layer crop(double x1, double y1, double x2, double y2) {
		WritableImage srcMask = new WritableImage((int) getWidth(), (int) getHeight());
		srcMask = Utils.makeSnapshot(this, Color.TRANSPARENT, srcMask); // fait un snapshot du calque (this)
		PixelReader pixelReader = srcMask.getPixelReader(); // récupère le PixelReader -> permet de lire les pixels sur le graphicsContext
		
		// calcule la nouvelle hauteur et la nouvelle largeur
		double x = x1 < x2 ? x1 : x2;
		double y = y1 < y2 ? y1 : y2;
		double width = Math.abs(x1 - x2);
		double height = Math.abs(y1 - y2);
		// vérifie que les paramètres reçus sont dans le calque (this) et les adaptes si ce n'est pas le cas
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (x + width > getWidth()) {
			width = getWidth() - x;
		}
		if (y + height > getHeight()) {
			height = getHeight() - y;
		}
		
		WritableImage newImage = new WritableImage(pixelReader, (int) x, (int) y, (int) width, (int) height);
		this.setWidth(width);
		this.setHeight(height);
		this.setTranslateX(x + this.getTranslateX());
		this.setTranslateY(y + this.getTranslateY());
		
		Utils.redrawSnapshot(this, newImage);
		return this;
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
		
		id = s.readInt(); // lit et définit l'id du calque
		nomCalque = (String) s.readObject(); // lit et définit le nom du calque
		
		setWidth(s.readDouble()); // lit et définit la largeur du calque
		setHeight(s.readDouble()); // lit et définit la hauteur du calque
		
		setTranslateX(s.readDouble()); // lit et définit le décalage du calque sur l'axe des X
		setTranslateY(s.readDouble()); // lit et définit le décalage du calque sur l'axe des Y
		
		setOpacity(s.readDouble()); // lit et définit l'opacité du calque
		setVisible(s.readBoolean()); // lit et définit la visibilité du calque
		
		Image image = SwingFXUtils.toFXImage(ImageIO.read(s), null); // récupère l'image du calque
		getGraphicsContext2D().drawImage(image, 0, 0, getWidth(), getHeight()); // redessine l'image sur le nouveau calque
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
		s.writeInt(id); // sauve l'id du calque
		s.writeObject(nomCalque); // sauve le nom du calque
		s.writeDouble(getWidth()); // sauve la largeur du calque
		s.writeDouble(getHeight()); // sauve la hauteur du calque
		
		s.writeDouble(getTranslateX()); // sauve le décalage du calque sur l'axe de X
		s.writeDouble(getTranslateY()); // sauve le décalage du calque sur l'axe de Y
		
		double tmpOpacity = getOpacity();
		boolean tmpVisible = isVisible();
		
		s.writeDouble(tmpOpacity); // sauve l'opacité du calque
		s.writeBoolean(tmpVisible); // sauve la visibilité du calque
		
		this.setVisible(true); // rend le calque visible afin de pouvoir en faire un snapshot
		this.setOpacity(1); // enlève l'opacité du calque pour pouvoir en faire un snapshot
		
		// fait un snapshot du calque puis le sauvegarde en tant qu'image au format png
		ImageIO.write(SwingFXUtils.fromFXImage(Utils.makeSnapshot(this, Color.TRANSPARENT), null), "png", s);
		
		this.setOpacity(tmpOpacity); // remet l'opacité d'origine du calque
		this.setVisible(tmpVisible); // remet la visibilité d'origine du calque
		
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant le changement d'opacité d'un calque et définissant l'action à effectuer en cas d'appel à undo() ou redo() sur
	 * cette commande.
	 */
	public class OpacitySave implements ICmd {
		double oldOpacity;
		double newOpacity;
		
		/**
		 * Construit une commande sauvegardant le changement d'opacité d'un calque.
		 *
		 * @param oldOpacity,
		 * 		l'ancienne opacité du calque.
		 * @param newOpacity,
		 * 		la nouvelle opacité du calque.
		 */
		private OpacitySave(double oldOpacity, double newOpacity) {
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
	
}
