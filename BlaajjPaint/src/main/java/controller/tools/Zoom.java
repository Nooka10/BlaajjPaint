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
	
	public void zoomIn(double posX, double posY) {
		zoom(posX, posY, 1.2);
	}
	
	public void zoomOut(double posX, double posY) {
		zoom(posX, posY, 1 / 1.2);
	}
	
	private void zoom(double posX, double posY, double zoomFactor) {
		/*
		Bounds viewPort = MainViewController.getInstance().getScrollPane().getViewportBounds();
		Bounds contentSize = Project.getInstance().getWorkspace().getBoundsInParent();
		
		double centerPosX = (contentSize.getWidth() - viewPort.getWidth()) * MainViewController.getInstance().getScrollPane().getHvalue() + viewPort.getWidth() / 2;
		double centerPosY = (contentSize.getHeight() - viewPort.getHeight()) * MainViewController.getInstance().getScrollPane().getVvalue() + viewPort.getHeight() / 2;
		
		Project.getInstance().zoom(zoomFactor);
		zoom *= zoomFactor;
		
		double newCenterX = centerPosX * zoomFactor;
		double newCenterY = centerPosY * zoomFactor;
		
		MainViewController.getInstance().getScrollPane().setHvalue((newCenterX - viewPort.getWidth() / 2) / (contentSize.getWidth() * zoomFactor - viewPort.getWidth()));
		MainViewController.getInstance().getScrollPane().setVvalue((newCenterY - viewPort.getHeight() / 2) / (contentSize.getHeight() * zoomFactor - viewPort.getHeight()));
		
		Project.getInstance().zoom(zoomFactor);
		MainViewController.getInstance().setTextZoomLabel(Math.round(zoom * 100) + "%");
		*/
		
		
		zoom *= zoomFactor;
		
		Project.getInstance().zoom(zoomFactor);
		MainViewController.getInstance().moveView(posX, posY);
		MainViewController.getInstance().setTextZoomLabel(Math.round(zoom * 100) + "%");
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isShiftDown()) {
					zoomOut(event.getX(), event.getY());
				} else {
					zoomIn(event.getX(), event.getY());
				}
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			
			}
		};
	}
}
