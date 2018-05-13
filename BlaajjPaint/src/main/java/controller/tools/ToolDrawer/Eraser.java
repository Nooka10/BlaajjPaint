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
	
	private GraphicsContext eraserMaskGC;
	
	public class EraserStrike extends Trait {
		public String toString() {
			return "Trait de gomme";
		}
	}
	
	/**
	 * Retourne l'instance unique de la gomme
	 *
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
				currentTrait = new EraserStrike();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter().setColor((int) event.getX(), (int) event.getY(), Color.TRANSPARENT);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(event.getX() - thickness / 2, event.getY() - thickness / 2, thickness, thickness);
				
				// crée le masque de suppression
				eraserMask = new Canvas(Project.getInstance().getCurrentLayer().getWidth(), Project.getInstance().getCurrentLayer().getHeight());
				eraserMaskGC = eraserMask.getGraphicsContext2D();
				eraserMaskGC.setFill(Color.WHITE);
				eraserMaskGC.fillRect(0, 0, eraserMask.getWidth(), eraserMask.getHeight());
				eraserMaskGC.beginPath();
				eraserMaskGC.moveTo(event.getX(), event.getY());
				eraserMaskGC.setLineWidth(thickness); // définit l'épaisseur de la gomme
				eraserMaskGC.setStroke(Color.BLACK); // définit la couleur de la gomme
				eraserMaskGC.stroke();
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
				
				eraserMaskGC.lineTo(event.getX(), event.getY());
				eraserMaskGC.stroke();
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				eraserMaskGC.closePath();
				
				// On récupère l'image du masque de suppression
				WritableImage srcMask = new WritableImage((int) eraserMask.getWidth(), (int) eraserMask.getHeight());
				srcMask = eraserMask.snapshot(null, srcMask);
				
				PixelReader maskReader = srcMask.getPixelReader();
				
				int width = (int) srcMask.getWidth();
				int height = (int) srcMask.getHeight();
				
				PixelWriter writer = Project.getInstance().getCurrentLayer().getGraphicsContext2D().getPixelWriter();
				
				// on fusionne le calque et le masque de suppression en colorant en transparent (donc en effaçant)
				// les pixels du calque qui ont été coloriés dans le masque de suppression
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
