/*
Author: Benoît
 */
package controller.tools.ToolDrawer;

import controller.Project;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.StrokeLineCap;

/**
 * Classe implémentant l'outil pinceau permettant de dessiner des pixels du calque à l'aide de la souris et de leur attribuer la couleur actuellement sélectionnée
 * dans le sélecteur de couleur.
 */
public class Pencil extends ToolDrawer {
	
	private static Pencil toolInstance = null; // l'instance unique du singleton Pencil
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private Pencil() {
		toolType = Tool.ToolType.PENCIL;
	}
	
	/**
	 * Retourne l'instance unique du pinceau.
	 * @return l'instance unique du pinceau.
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
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().beginPath();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().moveTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineCap(StrokeLineCap.ROUND); // définit la forme du pinceau
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur du pinceau
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor()); // définit la couleur du pinceau
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().lineTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().stroke();
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// déplace le trait de l'ancienne position à la position actuelle
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().lineTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().stroke(); // tire le trait entre l'ancienne et la nouvelle position
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// déplace le trait de l'ancienne position à la position actuelle
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().lineTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().stroke(); // tire le trait entre l'ancienne et la nouvelle position
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().closePath(); // clôt le trait de pinceau
				currentStrike.execute();
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