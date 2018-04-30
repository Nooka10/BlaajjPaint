package controller;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;

public class Layer extends Canvas{
	
	private boolean visibility;
	final private int id;
	private static int count = 0;
	
	/**
	 * Constructeur
	 * @param width la largeur de notre calque
	 * @param height la hauteur de notre calque
	 */
	public Layer(int width, int height){
		super(width,height);
		visibility = true;
		id = count++;
	}

	public Layer(Dimension dimension){
		this(dimension.width, dimension.height);
	}
	
	/**
	 * Constructeur de copie, pour copier un calque
	 * @param toCopy le calque à copier
	 */
	public Layer(Layer toCopy){
		super(toCopy.getWidth(),toCopy.getHeight());
		visibility = toCopy.visibility;
		id = count++;
	}
	
	// TODO : Antoine
	public Image createImageFromCanvas(int scale) {
		final Bounds bounds = getLayoutBounds();
		
		final WritableImage image = new WritableImage(
				(int) Math.round(bounds.getWidth() * scale),
				(int) Math.round(bounds.getHeight() * scale));
		
		final SnapshotParameters spa = new SnapshotParameters();
		spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
		spa.setFill(Color.TRANSPARENT);
		final ImageView view = new ImageView(snapshot(spa, image));
		view.setFitWidth(bounds.getWidth());
		view.setFitHeight(bounds.getHeight());
		
		return view.getImage();
	}
	
	/**
	 * Permet de fusionner deux calques
	 * @param backgroundLayer le calque à l'arrière-plan (sur lequel on va dessiner)
	 */
	public void mergeLayers(Layer backgroundLayer) {
		Image image = createImageFromCanvas(4);
		backgroundLayer.getGraphicsContext2D().drawImage(image, 0, 0, getWidth(), getHeight());
		// Project.getInstance().getLayers().remove(this); // TODO : enlever ca maybe
	}
	
	/**
	 *
	 * @param visibility
	 */
	public void setVisibility(boolean visibility) {
		System.out.println(visibility);
		this.visibility = visibility;
	}

	public boolean getVisibility(){
		return visibility;
	}
	
	@Override
	public String toString(){
		return "Calque " + id;
	}
}
