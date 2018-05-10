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
import utils.UndoException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Layer extends Canvas implements Serializable {

	private int id;					// pas final pour la serealisation
	private static int count = 1;

	public Layer(){
		//id = count ++;
	};

	/**
	 * Constructeur
	 *
	 * @param width  la largeur de notre calque
	 * @param height la hauteur de notre calque
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
	 * @param toCopy le calque à copier
	 */
	public Layer(Layer toCopy) {
		super(toCopy.getWidth(), toCopy.getHeight());
		id = count++;
	}

	// TODO : Antoine
	public Image createImageFromCanvas(int scale) {
		//final Bounds bounds = getLayoutBounds();

		final WritableImage image = new WritableImage(
				(int) Math.round(Project.getInstance().getDimension().width * scale),
				(int) Math.round(Project.getInstance().getDimension().height * scale));

		final SnapshotParameters spa = new SnapshotParameters();
		spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
		spa.setFill(Color.TRANSPARENT);
		final ImageView view = new ImageView(snapshot(spa, image));
		view.setFitWidth(Project.getInstance().getDimension().width);
		view.setFitHeight(Project.getInstance().getDimension().height);

		return view.getImage();
	}

	public WritableImage createImageFromCanvasJPG(int scale, SnapshotParameters spa, boolean tr) {
		//final Bounds bounds = getLayoutBounds();


		createImageFromCanvas(scale);
		final WritableImage image = new WritableImage(
				(int) Math.round(Project.getInstance().getDimension().width * scale),
				(int) Math.round(Project.getInstance().getDimension().height * scale));

		if(!tr) {
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
	 * @param backgroundLayer le calque à l'arrière-plan (sur lequel on va dessiner)
	 */
	public void mergeLayers(Layer backgroundLayer) {
		Image image = createImageFromCanvas(4);
		backgroundLayer.getGraphicsContext2D().drawImage(image, 0, 0, Project.getInstance().getDimension().width, Project.getInstance().getDimension().height);
		// Project.getCurrentTool().getLayers().remove(this); // TODO : enlever ca maybe
	}

	/**
	 * Classe interne qui encapsule la commande de changement d'opacité
	 * Permet de faire une undo/redo sur le changement d'opacité
	 * Attention a TOUJOURS setNewOpacity en premier
	 */
	public class OpacitySave implements ICmd {

		double oldOpacity;
		double newOpacity;

		public OpacitySave(){
			// par défaut on set a l'opacité courante au cas ou un débile oublie de faire le setNewOpacity pas que
			// ça passe a 0
			oldOpacity = newOpacity = getLayerOpacity();
		}

		public OpacitySave setNewOpacity(double newOpacity){
			this.newOpacity = newOpacity;
			return this;
		}

		@Override
		public void execute() {
			oldOpacity = getLayerOpacity();
			setOpacity(newOpacity / 100);
			RecordCmd.getInstance().saveCmd(this);
		}

		@Override
		public void undo() throws UndoException {
			setOpacity(oldOpacity / 100);
		}

		@Override
		public void redo() throws UndoException {
			setOpacity(newOpacity / 100);
		}

		@Override
		public String toString() {
			return "Opacity Change from " +oldOpacity + " to "+ newOpacity;
		}
	}

	public void setLayerOpacity(double opacity) {
		new OpacitySave().setNewOpacity(opacity).execute();
	}

	public double getLayerOpacity() {
		return this.getOpacity() * 100;
	}

	@Override
	public String toString() {
		return "Calque " + id;
	}


	/** SERIALISATION **/
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		//s.defaultReadObject();
		id = (s.readInt());
		super.setWidth(s.readDouble());
		super.setHeight(s.readDouble());
		setLayerOpacity(s.readDouble());
		Image image;
		image = SwingFXUtils.toFXImage(ImageIO.read(s), null);

		this.getGraphicsContext2D().drawImage(image, 0, 0);

		//return this;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		//s.defaultWriteObject();
		s.writeInt(id);
        s.writeDouble(super.getWidth());
		s.writeDouble(super.getHeight());

		s.writeDouble(getLayerOpacity());

        ImageIO.write(SwingFXUtils.fromFXImage(generateImage(this, (int)super.getWidth(), (int) super.getHeight()), null), "png", s);
	}

	private Image generateImage(Layer c, int weight, int height){
		SnapshotParameters params = new SnapshotParameters();
		WritableImage writableImage = new WritableImage(weight, height);
		params.setFill(Color.TRANSPARENT);
		c.snapshot(params, writableImage);
		return writableImage;
	}
}