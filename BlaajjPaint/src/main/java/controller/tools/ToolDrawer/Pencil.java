/*
Author: Benoît
 */
package controller.tools.ToolDrawer;

import controller.Project;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil pinceau
 */
public class Pencil extends ToolDrawer {
	
	private static Pencil toolInstance = new Pencil(); // l'instance unique du pinceau
	
	/**
	 * Retourne l'instance unique du pinceau
	 *
	 * @return l'instance unique du pinceau
	 */
	public static Pencil getInstance() {
		return toolInstance;
	}

	/*
	public class PercilStrike extends Trait {
		public String toString(){
			return "Pencil Strike";
		}
	}
	*/
	
	/**
	 * Constructeur privé (modèle singleton)
	 */
	private Pencil() {
		toolType = Tool.ToolType.PENCIL;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentTrait = new Trait();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().beginPath();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().moveTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur du pencil
				
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor()); // définit la couleur du pencil


				//Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity / 100);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().stroke();
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().lineTo(event.getX(), event.getY());
				//Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity / 100);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().stroke();
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().closePath();
				currentTrait.execute();
			}
		};
	}
}