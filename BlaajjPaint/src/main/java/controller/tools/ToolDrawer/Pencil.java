/*
Author: Benoît
 */
package controller.tools.ToolDrawer;

import controller.Project;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/**
 * Classe implémentant l'outil <b>pinceau</b> permettant de dessiner des pixels du calque à l'aide de la souris et de leur attribuer la couleur actuellement sélectionnée
 * dans le sélecteur de couleur. Implémente le modèle Singleton.
 */
public class Pencil extends ToolDrawer {
	
	private static Pencil toolInstance = null; // l'instance unique du singleton Pencil
	
	private GraphicsContext pencilMaskGC;
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private Pencil() {
		toolType = Tool.ToolType.PENCIL;
	}
	
	/**
	 * Retourne l'instance unique du singleton Pencil.
	 * @return l'instance unique du singleton Pencil.
	 */
	public static Pencil getInstance() {
		if (toolInstance  == null) {
			toolInstance  = new Pencil();
		}
		return toolInstance;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentStrike = new PencilStrike(); // crée une sauvegarde du trait de pinceau
				pencilMaskGC = Project.getInstance().getCurrentLayer().getGraphicsContext2D();
				
				pencilMaskGC.beginPath();

				pencilMaskGC.setLineCap(StrokeLineCap.ROUND); // définit la forme du pinceau
				pencilMaskGC.setLineJoin(StrokeLineJoin.ROUND);
				
				pencilMaskGC.setLineWidth(thickness); // définit l'épaisseur du pinceau
				pencilMaskGC.setStroke(Project.getInstance().getCurrentColor()); // définit la couleur du pinceau
				pencilMaskGC.lineTo(event.getX(), event.getY());
				pencilMaskGC.stroke();
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// déplace le trait de l'ancienne position à la position actuelle
				
				pencilMaskGC.lineTo(event.getX(), event.getY());
				pencilMaskGC.stroke(); // tire le trait entre l'ancienne et la nouvelle position
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// déplace le trait de l'ancienne position à la position actuelle
				pencilMaskGC.lineTo(event.getX(), event.getY());
				pencilMaskGC.stroke(); // tire le trait entre l'ancienne et la nouvelle position
				pencilMaskGC.closePath(); // clôt le trait de pinceau
				currentStrike.execute();
			}
		};
	}

	@Override
	protected EventHandler<MouseEvent> createMouseEnteredEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Image img = new Image("/cursors/pinceauCursor.png"); // A CHANGER (PAS BEAU)
				changeCursor(new ImageCursor(img,3,30));
			}
		};
	}

	@Override
	protected EventHandler<MouseEvent> createMouseExitedEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resetOldCursor();
			}
		};
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant un trait de pinceau et définissant l'action à effectuer en cas d'appel à undo() ou redo() sur cette commande.
	 */
	public class PencilStrike extends Strike {
		public String toString(){
			return "Trait de pinceau";
		}
	}
}