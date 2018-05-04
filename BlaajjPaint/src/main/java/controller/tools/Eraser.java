/*
Author: Benoît
 */
package controller.tools;

import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Eraser extends ToolDrawer {
	
	private static Eraser toolInstance = new Eraser();
	
	public static Eraser getInstance() {
		return toolInstance;
	}
	
	private Eraser() {
		toolType = ToolType.ERASER;
	}
	
	private Canvas mask;
	private GraphicsContext maskGC;
	
	@Override
	public EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentTrait = new Trait();
				//Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter().setColor((int) event.getX(), (int) event.getY(), Color.TRANSPARENT);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(event.getX() - thickness / 2, event.getY() - thickness / 2, thickness, thickness);
				
				// create mask
				mask = new Canvas(Project.getInstance().getCurrentLayer().getWidth(), Project.getInstance().getCurrentLayer().getHeight());
				maskGC = mask.getGraphicsContext2D();
				maskGC.setFill(Color.WHITE);
				maskGC.fillRect(0, 0, mask.getWidth(), mask.getHeight());
				maskGC.beginPath();
				maskGC.moveTo(event.getX(), event.getY());
				maskGC.setLineWidth(thickness); // définit l'épaisseur de la gomme
				maskGC.setStroke(Color.BLACK); // définit la couleur de la gomme
				//maskGC.setGlobalAlpha(opacity / 100);
				maskGC.stroke();
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter().setColor((int) event.getX(), (int) event.getY(), Color.TRANSPARENT);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(event.getX() - thickness / 2, event.getY() - thickness / 2, thickness, thickness);
				
				maskGC.lineTo(event.getX(), event.getY());
				maskGC.stroke();
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				maskGC.closePath();
				
				// get image from mask
				WritableImage srcMask = new WritableImage((int) mask.getWidth(), (int) mask.getHeight());
				srcMask = mask.snapshot(null, srcMask);
				
				PixelReader maskReader = srcMask.getPixelReader();
				
				int width = (int) srcMask.getWidth();
				int height = (int) srcMask.getHeight();
				
				PixelWriter writer = Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter();
				
				// blend image and mask
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						Color maskColor = maskReader.getColor(x, y);
						if (maskColor.equals(Color.BLACK)) {
							writer.setColor(x, y, Color.TRANSPARENT);
						}
					}
				}
				
				currentTrait.execute();
			}
		};
	}
	
	@Override
	protected void onOpacitySet() {
		Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity / 100);
	}
	
	@Override
	protected void onThicknessSet() {
		Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
	}
}
