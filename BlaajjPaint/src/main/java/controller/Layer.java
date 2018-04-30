package controller;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Layer extends Canvas{
	
	private boolean visibility;
	
	public Layer(int width, int height){
		super(width,height);
		visibility = true;
	}
	
	public Layer(Layer toCopy){
	
	}
	
	// TODO : Antoine
	private Image createImageFromCanvas(int scale) {
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
	 *
	 * @param backgroundCanvas
	 */
	private void mergeLayers(Canvas backgroundCanvas) {
		Image image = createImageFromCanvas(4);
		backgroundCanvas.getGraphicsContext2D().drawImage(image, 0, 0, 300, 250);
	}
}
