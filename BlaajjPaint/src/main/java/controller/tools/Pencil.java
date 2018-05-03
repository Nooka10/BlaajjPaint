/*
Author: Benoît
 */
package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import utils.UndoException;


public class Pencil extends ToolDrawer {
	
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
				currentTrait = new Trait();
				
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().beginPath();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().moveTo(event.getX(), event.getY());
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur du pencil
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor()); // définit la couleur du pencil
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity / 100);
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
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity / 100);
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
