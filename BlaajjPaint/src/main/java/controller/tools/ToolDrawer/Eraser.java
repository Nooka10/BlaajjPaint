/*
Author: Benoît
 */
package controller.tools.ToolDrawer;

import controller.Project;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Classe implémentant l'outil gomme
 */
public class Eraser extends ToolDrawer {
	
	private static Eraser toolInstance = new Eraser(); // l'instance unique de la gomme
	
	private Canvas eraserMask; // le masque sur lequel on "peint" la zone à effacer
	
	private GraphicsContext eraserMaskGraphicsContext;
/*
	public class EraserStrike extends Trait {
		public String toString(){
			return "Eraser Strike";
		}
	}
*/
	/**
	 * Retourne l'instance unique de la gomme
	 * @return l'instance unique de la gomme
	 */
	public static Eraser getInstance() {
		return toolInstance;
	}
	
	/**
	 * Constructeur privé (modèle singleton)
	 */
	private Eraser() {
		toolType = Tool.ToolType.ERASER;
	}
	
	@Override
	public EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentTrait = new Trait();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter().setColor((int) event.getX(), (int) event.getY(), Color.TRANSPARENT);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(event.getX() - thickness / 2, event.getY() - thickness / 2, thickness, thickness);
				
				// create eraserMask
				eraserMask = new Canvas(Project.getInstance().getCurrentLayer().getWidth(), Project.getInstance().getCurrentLayer().getHeight());
				eraserMaskGraphicsContext = eraserMask.getGraphicsContext2D();
				eraserMaskGraphicsContext.setFill(Color.WHITE);
				eraserMaskGraphicsContext.fillRect(0, 0, eraserMask.getWidth(), eraserMask.getHeight());
				eraserMaskGraphicsContext.beginPath();
				eraserMaskGraphicsContext.moveTo(event.getX(), event.getY());
				eraserMaskGraphicsContext.setLineWidth(thickness); // définit l'épaisseur de la gomme
				eraserMaskGraphicsContext.setStroke(Color.BLACK); // définit la couleur de la gomme
				//eraserMaskGraphicsContext.setGlobalAlpha(opacity / 100);
				eraserMaskGraphicsContext.stroke();
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter().setColor((int) event.getX(), (int) event.getY(), Color.TRANSPARENT);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(event.getX() - thickness / 2, event.getY() - thickness / 2, thickness, thickness);
				
				eraserMaskGraphicsContext.lineTo(event.getX(), event.getY());
				eraserMaskGraphicsContext.stroke();
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				eraserMaskGraphicsContext.closePath();
				
				// get image from eraserMask
				WritableImage srcMask = new WritableImage((int) eraserMask.getWidth(), (int) eraserMask.getHeight());
				srcMask = eraserMask.snapshot(null, srcMask);
				
				PixelReader maskReader = srcMask.getPixelReader();
				
				int width = (int) srcMask.getWidth();
				int height = (int) srcMask.getHeight();
				
				PixelWriter writer = Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter();
				
				// blend image and eraserMask
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
}
