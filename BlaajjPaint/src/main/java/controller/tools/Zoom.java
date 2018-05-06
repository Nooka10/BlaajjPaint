/*
Author: Benoît
 */
package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import javafx.event.EventHandler;
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
	
	public double getZoom() {
		return zoom;
	}
	
	private boolean majPressed;
	
	public boolean isMajPressed() {
		return majPressed;
	}
	
	public void setMajPressed(boolean majPressed) {
		this.majPressed = majPressed;
	}
	
	public void zoomIn() {
		zoom *= 2;
		Project.getInstance().getBackgroungImage().setScaleX(zoom);
		Project.getInstance().getBackgroungImage().setScaleY(zoom);
		for (Layer layer : Project.getInstance().getLayers()) {
			layer.setScaleX(zoom);
			layer.setScaleY(zoom);
		}
		MainViewController.getInstance().setTextZoomLabel(zoom * 100 + "%");
		
	}
	
	public void zoomOut() {
		zoom /= 2;
		Project.getInstance().getBackgroungImage().setScaleX(zoom);
		Project.getInstance().getBackgroungImage().setScaleY(zoom);
		for (Layer layer : Project.getInstance().getLayers()) {
			layer.setScaleX(zoom);
			layer.setScaleY(zoom);
			
		}
		MainViewController.getInstance().setTextZoomLabel(zoom * 100 + "%");
	}
	
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (majPressed) {
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
