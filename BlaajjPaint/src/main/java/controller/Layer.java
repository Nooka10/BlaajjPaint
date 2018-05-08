package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
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

	private int id;					// pas final pour la serealisation
	private static int count = 3;

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

	public void setLayerOpacity(double opacity) {
		setOpacity(opacity / 100);
	}

	public double getLayerOpacity() {
		return getOpacity() * 100;
	}

	@Override
	public String toString() {
		return "Calque " + id;
	}


	/** SERiALISATION **/
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
		c.snapshot(params, writableImage);
		return writableImage;
	}
}