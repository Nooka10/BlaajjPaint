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
import java.awt.*;
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

		snapshot(spa, image);
		return image;
		//return view.getImage();
	}
	
	
	// TODO : false
	public WritableImage createImageFromCanvasJPG(int scale, SnapshotParameters spa, boolean tr) {
		//final Bounds bounds = getLayoutBounds();
		
		createImageFromCanvas(scale);
		final WritableImage image = new WritableImage(
				Math.round(Project.getInstance().getDimension().width * scale),
				Math.round(Project.getInstance().getDimension().height * scale));
		
		if (!tr) {
			for (int x = 0; x < Project.getInstance().getDimension().width; ++x) {
				for (int y = 0; y < Project.getInstance().getDimension().width; ++y) {
					if (image.getPixelReader().getColor(x, y) == Color.TRANSPARENT)
						image.getPixelWriter().setColor(x, y, Color.WHITE);
				}
			}
		}
		
		
		//final SnapshotParameters spa = new SnapshotParameters();
		spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
		//spa.setFill(Color.TRANSPARENT);
		
		this.snapshot(spa, image);
		/*final ImageView view = new ImageView(snapshot(spa, image));
		view.setFitWidth(Project.getInstance().getDimension().width);
		view.setFitHeight(Project.getInstance().getDimension().height);*/
		
		return image;
	}
	
	/**
	 * Permet de fusionner deux calques
	 *
	 * @param backgroundLayer
	 * 		le calque à l'arrière-plan (sur lequel on va dessiner)
	 */
	public void mergeLayers(Layer backgroundLayer) {
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
		
		Project.getInstance().getLayers().remove(this);
		Project.getInstance().getLayers().remove(backgroundLayer);
		Project.getInstance().addLayer(mergeLayer);
		MainViewController.getInstance().getRightMenuController().updateLayerList();
		
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
			setLayerOpacity(oldOpacity);
		}
		
		@Override
		public void redo() {
			setLayerOpacity(newOpacity);
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

		double lX = s.readDouble();
		double lY=s.readDouble();
		System.out.println("BEF: " + lX + " " + lY);

		super.setLayoutX(lX);
		super.setLayoutY(lY);

		System.out.println("AFR: " + getLayoutX() + " " + getLayoutY());

		double tmpOpacity = s.readDouble();
					// opacité de Canevas [0;1]
		super.setOpacity(tmpOpacity);
		super.setVisible(s.readBoolean());
		
		Image image = SwingFXUtils.toFXImage(ImageIO.read(s), null);
		
		getGraphicsContext2D().drawImage(image, 0, 0, getWidth(), getHeight());



	}
	
	private void writeObject(ObjectOutputStream s) throws IOException {
		//s.defaultWriteObject();
		s.writeInt(id);
		s.writeDouble(super.getWidth());
		s.writeDouble(super.getHeight());
		System.out.println(getLayoutX() + " " + getLayoutY());
		s.writeDouble(getLayoutX());
		s.writeDouble(getLayoutY());

		double tmpOpacity = super.getOpacity();
		boolean tmpVisible = super.isVisible();

		s.writeDouble(tmpOpacity);					// opacité de Canevas [0;1]
		s.writeBoolean(super.isVisible());

		this.setVisible(true);
		this.setOpacity(1);							// enlève l'opacité pour la sauvegardes
		ImageIO.write(SwingFXUtils.fromFXImage(createImageFromCanvas(1), null), "png", s);
		this.setOpacity(tmpOpacity);				// Remet l'opacité
		this.setVisible(tmpVisible);				// Remet la visibilité

	}

	public static void reset() {
		count = 1;
	}
}