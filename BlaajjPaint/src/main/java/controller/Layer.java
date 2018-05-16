package controller;

import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Layer extends Canvas implements Serializable {
	private int id; // l'id du calque
	private static int count = 0; // le nombre de calques qui ont été créés
	private String nomCalque;
	
	/**
	 * Constructeur
	 *
	 * @param width  la largeur de notre calque
	 * @param height la hauteur de notre calque
	 * @param nom le nom à donner au calque (nom affiché dans la liste de calque)
	 * @param calqueTemporaire booléen valant True s'il s'agit d'un calque temporaire (n'incrémente pas l'id), false sinon
	 */
	public Layer(int width, int height, String nom, boolean calqueTemporaire) {
		this(width, height, calqueTemporaire);
		nomCalque = "Calque " + id + ", " + nom;
	}
	
	/**
	 * Constructeur
	 *
	 * @param width la largeur de notre calque
	 * @param height la hauteur de notre calque
	 * @param calqueTemporaire booléen valant True s'il s'agit d'un calque temporaire (n'incrémente pas l'id), false sinon
	 */
	public Layer(int width, int height, boolean calqueTemporaire) {
		super(width, height);
		if (!calqueTemporaire) {
			count++;
		}
		id = count;
		nomCalque = "Calque " + id;
	}
	
	/**
	 * Constructeur de copie, pour copier un calque
	 *
	 * @param toCopy le calque à copier
	 */
	public Layer(Layer toCopy) {
		this((int)toCopy.getWidth(), (int)toCopy.getHeight(), false);
		boolean visibility = toCopy.isVisible();
		toCopy.setVisible(true);
		this.getGraphicsContext2D().drawImage(toCopy.createImageFromCanvas(4), 0, 0, getWidth(), getHeight());
		setVisible(visibility);
		setLayoutX(toCopy.getLayoutX());
		setLayoutY(toCopy.getLayoutY());
		toCopy.setVisible(visibility);
	}
	
	/**
	 * @param _count
	 */
	public static void setCount(int _count) {
		count = ++_count;
	}
	
	public int id() {
		return id;
	}
	
	// TODO : Antoine
	public Image createImageFromCanvas(int scale) {
		
		final WritableImage image = new WritableImage(
				Math.round((int) getWidth() * scale),
				Math.round((int) getHeight() * scale));
		
		final SnapshotParameters spa = new SnapshotParameters();
		spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
		spa.setFill(Color.TRANSPARENT);
		final ImageView view = new ImageView(snapshot(spa, image));
		view.setFitWidth(getWidth());
		view.setFitHeight(getHeight());
		
		snapshot(spa, image);
		return image;
		//return view.getImage();
	}
	
	
	// TODO : false, rend une image 4x plus grande
	public ImageView createImageFromCanvasJPG(int scale) {
		final WritableImage image = new WritableImage(
				(int) getWidth() * scale,
				(int) getHeight() * scale);
		
		final SnapshotParameters spa = new SnapshotParameters();
		spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
		spa.setFill(Color.WHITE);
		
		final ImageView view = new ImageView(snapshot(spa, image));
		
		view.setFitWidth(getWidth());
		view.setFitHeight(getHeight());
		view.setPreserveRatio(true);
		
		return view;
	}
	
	/**
	 * Permet de fusionner deux calques
	 *
	 * @param backgroundLayer le calque à l'arrière-plan (sur lequel on va dessiner)
	 */
	public Layer mergeLayers(Layer backgroundLayer) {
		Image image1 = createImageFromCanvas(4);
		Image image2 = backgroundLayer.createImageFromCanvas(4);
		
		// prend le décalage (layout) minimum des deux calques
		double minX = getLayoutX() < backgroundLayer.getLayoutX() ? getLayoutX() : backgroundLayer.getLayoutX();
		double minY = getLayoutY() < backgroundLayer.getLayoutY() ? getLayoutY() : backgroundLayer.getLayoutY();
		
		// prend le décalage (layout) maximum des deux calques
		double maxX = getLayoutX() + getWidth() > backgroundLayer.getLayoutX() + backgroundLayer.getWidth() ? getLayoutX() + getWidth() : backgroundLayer.getLayoutX() + backgroundLayer.getWidth();
		double maxY = getLayoutY() + getHeight() > backgroundLayer.getLayoutY() + backgroundLayer.getHeight() ? getLayoutY() + getHeight() : backgroundLayer.getLayoutY() + backgroundLayer.getHeight();
		
		
		// crée un nouveau calque qui contiendra la fusion des deux autres
		Layer mergeLayer = new Layer((int) (maxX - minX), (int) (maxY - minY), true);
		
		// dessine les deux calques sur notre nouveau calque fusion
		mergeLayer.getGraphicsContext2D().drawImage(image2, backgroundLayer.getLayoutX() - minX, backgroundLayer.getLayoutY() - minY, backgroundLayer.getWidth(), backgroundLayer.getHeight());
		mergeLayer.getGraphicsContext2D().drawImage(image1, getLayoutX() - minX, getLayoutY() - minY, getWidth(), getHeight());
		
		// place le calque fusionné avec le bon décalage
		mergeLayer.setLayoutX(minX);
		mergeLayer.setLayoutY(minY);
		
		return mergeLayer;
	}
	
	/**
	 * Classe interne qui encapsule la commande de changement d'opacité. Permet de faire un undo/redo sur le changement d'opacité. Attention à TOUJOURS setNewOpacity en
	 * premier.
	 */
	public class OpacitySave extends ICmd {
		
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
	
	public double getLayerOpacity() {
		return Project.getInstance().getCurrentLayer().getOpacity() * 100;
	}
	
	@Override
	public String toString() {
		return nomCalque;
	}
	
	
	/** 
	 * SERIALISATION
	 **/
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {

		id = s.readInt();
		nomCalque = (String) s.readObject();
		super.setWidth(s.readDouble());
		super.setHeight(s.readDouble());

		super.setLayoutX(s.readDouble());
		super.setLayoutY(s.readDouble());


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

		s.writeDouble(getLayoutX());
		s.writeDouble(getLayoutY());
		
		double tmpOpacity = super.getOpacity();
		boolean tmpVisible = super.isVisible();
		
		s.writeDouble(tmpOpacity);                    // opacité de Canevas [0;1]
		s.writeBoolean(super.isVisible());
		
		this.setVisible(true);
		this.setOpacity(1);                            // enlève l'opacité pour la sauvegardes
		ImageIO.write(SwingFXUtils.fromFXImage(createImageFromCanvas(1), null), "png", s);
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
		// Test si les position sont dans le calque
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

		/*Project.getInstance().getLayers().remove(this);

		Layer newLayer = new Layer((int)width, (int)height);
		newLayer.setLayoutX(x + this.getLayoutX());
		newLayer.setLayoutY(y + this.getLayoutY());
		newLayer.getGraphicsContext2D().drawImage(newImage, 0,0);
		Project.getInstance().addLayer(newLayer);*/

		this.setWidth(width);
		this.setHeight(height);
		
		this.setLayoutX(x + this.getLayoutX());
		this.setLayoutY(y + this.getLayoutY());

		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0,0,this.getWidth(),this.getHeight());
		gc.drawImage(newImage,0,0);

		return this;
	}
}