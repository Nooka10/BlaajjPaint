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
import javafx.scene.shape.StrokeLineCap;

/**
 * Classe implémentant l'outil gomme
 */
public class Eraser extends ToolDrawer {
	
	private static Eraser toolInstance = null; // l'instance unique du singleton Eraser
	
	private Canvas eraserMask; // le masque sur lequel on "peint" la zone à effacer
	
	private GraphicsContext eraserMaskGC;
	
	/**
	 * Retourne l'instance unique de la gomme
	 *
	 * @return l'instance unique de la gomme
	 */
	public static Eraser getInstance() {
		if (toolInstance == null) {
			toolInstance = new Eraser();
		}
		return toolInstance;
	}
	
	/**
	 * Constructeur privé (modèle singleton).
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
				startErase(event);
			}
		};
	}
	
	private void startErase(MouseEvent event){
		// crée le masque de suppression
		eraserMask = new Canvas(Project.getInstance().getCurrentLayer().getWidth(), Project.getInstance().getCurrentLayer().getHeight());
		eraserMaskGC = eraserMask.getGraphicsContext2D();
		eraserMaskGC.setFill(Color.WHITE);
		eraserMaskGC.fillRect(0, 0, eraserMask.getWidth(), eraserMask.getHeight());
		eraserMaskGC.beginPath();
		eraserMaskGC.moveTo(event.getX(), event.getY());
		eraserMaskGC.setLineCap(StrokeLineCap.ROUND);
		eraserMaskGC.setLineWidth(thickness); // définit l'épaisseur de la gomme
		eraserMaskGC.setStroke(Color.BLACK); // définit la couleur de la gomme
		eraserMaskGC.lineTo(event.getX(), event.getY());
		eraserMaskGC.stroke();
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				eraserMaskGC.lineTo(event.getX(), event.getY());
				eraserMaskGC.stroke();
				
				closeErase(event);
				startErase(event);
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				closeErase(event);
				currentTrait.execute();
			}
		};
	}
	
	private void closeErase(MouseEvent event){
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
	}
	
	public class EraserStrike extends Trait {
		@Override
		public String toString() {
			return "Trait de gomme";
		}
	}
}
