/*
Author: Benoît
 */
package controller.tools;

import controller.MainViewController;
import controller.Project;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil pipette
 */
public class Move extends Tool {
	
	private static Move toolInstance = new Move();
	
	public static Move getInstance() {
		return toolInstance;
	}
	
	private Move() {
		toolType = ToolType.MOVE;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MainViewController.getInstance().getScrollPane().setPannable(true);
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
				MainViewController.getInstance().getScrollPane().setPannable(false);
			}
		};
	}
}
