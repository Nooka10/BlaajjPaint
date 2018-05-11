package controller;

import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Layer extends Canvas implements Serializable {
	private int id; // l'id du calque
	private static int count = 1; // le nombre de calques qui ont été créés
	
	/**
	 * Constructeur
	 *
	 * @param width
	 * 		la largeur de notre calque
	 * @param height
	 * 		la hauteur de notre calque
	 */
	public Layer(int width, int height) {
		super(width, height);
		id = count++;
	}
	
	public Layer(Dimension dimension) {
		this(dimension.width, dimension.height);
	}
	
	/**
	 * Constructeur de copie, pour copier un calque
	 *
	 * @param toCopy
	 * 		le calque à copier
	 */
	public Layer(Layer toCopy) {
		super(toCopy.getWidth(), toCopy.getHeight());
		id = count++;
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
		
		return view.getImage();
	}
	
	
	// TODO : false, rend une image 4x plus grande
	public ImageView createImageFromCanvasJPG(int scale) {
		//final Bounds bounds = getLayoutBounds();
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
	 * @param backgroundLayer
	 * 		le calque à l'arrière-plan (sur lequel on va dessiner)
	 */
	public Layer mergeLayers(Layer backgroundLayer) {
		Image image1 = createImageFromCanvas(4);
		Image image2 = backgroundLayer.createImageFromCanvas(4);
		
		double minX = getLayoutX() < backgroundLayer.getLayoutX() ? getLayoutX() : backgroundLayer.getLayoutX();
		double minY = getLayoutY() < backgroundLayer.getLayoutY() ? getLayoutY() : backgroundLayer.getLayoutY();
		
		double maxX = getLayoutX() + getWidth() > backgroundLayer.getLayoutX() + backgroundLayer.getWidth() ? getLayoutX() + getWidth() : backgroundLayer.getLayoutX() + backgroundLayer.getWidth();
		double maxY = getLayoutY() + getHeight() > backgroundLayer.getLayoutY() + backgroundLayer.getHeight() ? getLayoutY() + getHeight() : backgroundLayer.getLayoutY() + backgroundLayer.getHeight();
		
		Layer mergeLayer = new Layer((int) (maxX - minX), (int) (maxY - minY));
		
		mergeLayer.getGraphicsContext2D().drawImage(image2, backgroundLayer.getLayoutX() - minX, backgroundLayer.getLayoutY() - minY, backgroundLayer.getWidth(), backgroundLayer.getHeight());
		mergeLayer.getGraphicsContext2D().drawImage(image1, getLayoutX() - minX, getLayoutY() - minY, getWidth(), getHeight());
		
		
		mergeLayer.setLayoutX(minX);
		mergeLayer.setLayoutY(minY);
		
		
		return mergeLayer;
	}
	
	/**
	 * Classe interne qui encapsule la commande de changement d'opacité Permet de faire une undo/redo sur le changement d'opacité Attention a TOUJOURS setNewOpacity en
	 * premier
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
			updateLayerOpacity(oldOpacity);
			MainViewController.getInstance().getRightMenuController().setOpacityTextField(String.valueOf(oldOpacity));
			MainViewController.getInstance().getRightMenuController().setOpacitySlider(oldOpacity);
		}
		
		@Override
		public void redo() {
			updateLayerOpacity(newOpacity);
			MainViewController.getInstance().getRightMenuController().setOpacityTextField(String.valueOf(newOpacity));
			MainViewController.getInstance().getRightMenuController().setOpacitySlider(newOpacity);
		}
		
		@Override
		public String toString() {
			return "L'opacité changée de " + Math.round(oldOpacity) + " à " + Math.round(newOpacity);
		}
	}
	
	public void setLayerOpacity(double opacity) {
		new OpacitySave(opacity).execute();
	}
	
	public void setLayerOpacity(double oldOpacity, double newOpacity) {
		new OpacitySave(oldOpacity, newOpacity).execute();
	}
	
	public void updateLayerOpacity(double opacity) {
		Project.getInstance().getCurrentLayer().setOpacity(opacity/100);
	}
	
	public double getLayerOpacity() {
		return Project.getInstance().getCurrentLayer().getOpacity() * 100;
	}
	
	@Override
	public String toString() {
		return "Calque " + id;
	}
	
	
	/**
	 * SERIALISATION
	 **/
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		//s.defaultReadObject();
		id = (s.readInt());
		super.setWidth(s.readDouble());
		super.setHeight(s.readDouble());

		double tmpOpacity = s.readDouble();
					// opacité de Canevas [0;1]
		super.setOpacity(tmpOpacity);
		super.setVisible(s.readBoolean());
		
		Image image = SwingFXUtils.toFXImage(ImageIO.read(s), null);
		
		this.getGraphicsContext2D().drawImage(image, 0, 0);

	}
	
	private void writeObject(ObjectOutputStream s) throws IOException {
		//s.defaultWriteObject();
		s.writeInt(id);
		s.writeDouble(super.getWidth());
		s.writeDouble(super.getHeight());

		double tmpOpacity = super.getOpacity();
		boolean tmpVisible = super.isVisible();

		s.writeDouble(tmpOpacity);					// opacité de Canevas [0;1]
		s.writeBoolean(super.isVisible());

		this.setVisible(true);
		this.setOpacity(1);							// enlève l'opacité pour la sauvegardes
		ImageIO.write(SwingFXUtils.fromFXImage(generateImage(this, (int) super.getWidth(), (int) super.getHeight()), null), "png", s);
		this.setOpacity(tmpOpacity);			// Remet l'opacité
		this.setVisible(tmpVisible);			// Remet la visibilité

	}
	
	private Image generateImage(Layer c, int weight, int height) {
		SnapshotParameters params = new SnapshotParameters();
		WritableImage writableImage = new WritableImage(weight, height);
		params.setFill(Color.TRANSPARENT);
		c.snapshot(params, writableImage);
		return writableImage;
	}
	
	public static void reset() {
		count = 1;
	}
}