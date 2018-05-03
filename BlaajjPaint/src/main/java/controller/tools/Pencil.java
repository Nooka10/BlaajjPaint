/*
Author: Benoît
 */
package controller.tools;

import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;


public class Pencil extends ToolDrawer {
	
	private static Pencil toolInstance = new Pencil();
	
	public static Pencil getInstance() {
		return toolInstance;
	}
	
	private Pencil() {
		toolType = ToolType.PENCIL;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentTrait = new Trait();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalBlendMode(BlendMode.SCREEN);
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
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur du pencil
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor()); // définit la couleur du pencil
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
	
	@Override
	protected void onOpacitySet() {
		Project.getInstance().getCurrentLayer().getGraphicsContext2D().setGlobalAlpha(opacity / 100);
	}
	
	@Override
	protected void onThicknessSet() {
		Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
	}
}
