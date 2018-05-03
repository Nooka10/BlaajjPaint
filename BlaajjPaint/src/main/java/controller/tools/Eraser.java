/*
Author: Benoît
 */
package controller.tools;

import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


public class Eraser extends ToolDrawer {
	
	private static Eraser toolInstance = new Eraser();
	
	public static Eraser getInstance() {
		return toolInstance;
	}
	
	private Eraser() {
		toolType = ToolType.ERASER;
	}
	
	@Override
	public EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentTrait = new Trait();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().beginPath();
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(event.getX(), event.getY(), thickness, thickness);
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(event.getX(), event.getY(), thickness, thickness);
			}
		};
	}
	
	@Override
	public EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
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
