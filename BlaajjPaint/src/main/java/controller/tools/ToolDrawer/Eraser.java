package controller.tools.ToolDrawer;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import utils.Utils;

/**
 * Classe implémentant l'outil <b>gomme</b> permettant d'effacer des pixels du calque à l'aide de la souris. Implémente le modèle Singleton.
 */
public class Eraser extends ToolDrawer {
	
	private static Eraser toolInstance; // l'instance unique du singleton Eraser
	
	private Layer eraserMask; // le masque sur lequel on "peint" la zone à effacer
	
	private GraphicsContext eraserMaskGC;
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private Eraser() {
		toolType = Tool.ToolType.ERASER;
	}
	
	/**
	 * Retourne l'instance unique du singleton Eraser.
	 *
	 * @return l'instance unique du singleton Eraser.
	 */
	public static Eraser getInstance() {
		if (toolInstance == null) {
			toolInstance = new Eraser();
		}
		return toolInstance;
	}
	
	@Override
	public EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentStrike = new EraserStrike(); // crée une sauvegarde du trait de gomme
				startErase(event); // initialise le trait de gomme
			}
		};
	}
	
	/**
	 * Initialise le trait de gomme. Crée un masque de suppression sur lequel l'utilisateur "dessine" les zones à effacer.
	 *
	 * @param event,
	 * 		l'évènement de la souris.
	 */
	private void startErase(MouseEvent event) {
		// crée un claque temporaire sur lequel on va "colorer" la zone à supprimer puis fusionner avec le calque courant pour effectuer l'effacement
		eraserMask = new Layer((int) Project.getInstance().getCurrentLayer().getWidth(), (int) Project.getInstance().getCurrentLayer().getHeight(), true);
		eraserMaskGC = eraserMask.getGraphicsContext2D();
		eraserMaskGC.setFill(Color.WHITE);
		eraserMaskGC.fillRect(0, 0, eraserMask.getWidth(), eraserMask.getHeight());
		eraserMaskGC.beginPath();
		eraserMaskGC.setLineCap(StrokeLineCap.ROUND); // définit la forme de la gomme
		eraserMaskGC.setLineJoin(StrokeLineJoin.ROUND);
		
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
				eraserMaskGC.lineTo(event.getX(), event.getY()); // déplace le trait de l'ancienne position à la position actuelle
				eraserMaskGC.stroke(); // tire le trait entre l'ancienne et la nouvelle position
				
				closeErase(event); // fusionne le calque et effectue l'effacement
				startErase(event); // initialise un nouveau trait de gomme
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				closeErase(event); // fusionne le calque et effectue l'effacement
				currentStrike.execute();
			}
		};
	}
	
	/**
	 * Clôt le trait de gomme et merge le calque temporaire. Toutes les zones dessinées sur le calque temporaire sont effacées sur le calque courant.
	 *
	 * @param event,
	 * 		l'évènement de la souris.
	 */
	private void closeErase(MouseEvent event) {
		eraserMaskGC.closePath(); // clôt le trait de gomme
		
		WritableImage srcMask = new WritableImage((int) eraserMask.getWidth(), (int) eraserMask.getHeight()); // On récupère l'image du masque de suppression
		srcMask = Utils.makeSnapshot(eraserMask, Color.TRANSPARENT, srcMask); // on en fait un snapshot
		
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
	
	@Override
	public void CallbackOldToolChanged() {
		MainViewController.getInstance().getToolBarController().eraseTool.setSelected(false);
	}
	
	@Override
	public void CallbackNewToolChanged() {
		MainViewController.getInstance().getToolBarController().eraseTool.setSelected(true);
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant un trait de gomme et définissant l'action à effectuer en cas d'appel à undo() ou redo() sur cette commande.
	 */
	private class EraserStrike extends Strike {
		@Override
		public String toString() {
			return "Trait de gomme";
		}
	}
}
