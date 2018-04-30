/*
Author: Benoît
 */
package controller.tools;

import controller.Project;
import controller.history.ICmd;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import utils.UndoException;


public class Pencil extends ToolDrawer implements ICmd {
	
	private static Pencil toolInstance = new Pencil();
	
	public static Pencil getInstance() {
		return toolInstance;
	}
	
	private Pencil() {
		super(1, 100);
		toolType = ToolType.PENCIL;
	}
	
	@Override
	public EventHandler<MouseEvent> addMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().beginPath();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().moveTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur du pencil
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor()); // définit la couleur du pencil
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity/100);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().stroke();
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> addMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().lineTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur du pencil
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor()); // définit la couleur du pencil
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity/100);
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().stroke();
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> addMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().closePath();
				// System.out.println("release : " + realid); // FIXME: à virer -> juste pour tests
				execute();
			}
		};
	}
	
	
	/**
	 * Execute la Cmd et la sauve dans le RecordCmd
	 */
	@Override
	public void execute() {
	
	}
	
	/**
	 * Retourne le model à son état précédent l'exécution de la commande
	 */
	@Override
	public void undo() throws UndoException {
	
	}
	
	/**
	 * Retourne le model à son état suivant l'exécution de la commande
	 */
	@Override
	public void redo() throws UndoException {
	
	}
	
	/**
	 * Evènement appelé sur les enfants au moment ou l'opacité est changée Doit être surchargé par les enfants
	 *
	 * @Author Adrien
	 */
	@Override
	protected void onOpacitySet() {
	
	}
	
	/**
	 * Evènement appelé sur les enfants au moment ou l'épaisseur est changée Doit être surchargé par les enfants
	 *
	 * @Author Adrien
	 */
	@Override
	protected void onThicknessSet() {
	
	}
}
