/*
Author: Benoît
 */
package controller.tools;

import controller.MainViewController;
import controller.Project;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil pipette
 */
public class Zoom extends Tool {
	
	private static Zoom toolInstance = new Zoom();
	
	public static Zoom getInstance() {
		return toolInstance;
	}
	
	private Zoom() {
		toolType = ToolType.ZOOM;
	}
	
	private double zoom = 1.0;
	
	public void zoomIn() {
		zoom(1.2);
	}
	
	public void zoomOut() {
		zoom(1 / 1.2);
	}
	
	private void zoom(double zoomFactor) {
		
		zoom *= zoomFactor;
		Project.getInstance().zoom(zoomFactor);
		//MainViewController.getInstance().moveView(posX, posY);
		MainViewController.getInstance().setTextZoomLabel(Math.round(zoom * 100) + "%");
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isShiftDown()) {
					zoomOut();
				} else {
					zoomIn();
				}
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// do nothing
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// do nothing
			}
		};
	}
}
