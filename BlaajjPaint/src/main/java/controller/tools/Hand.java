/*
Author: Benoît
 */
package controller.tools;

import controller.MainViewController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil pipette
 */
public class Hand extends Tool {
	
	private static Hand toolInstance = null;
	
	public static Hand getInstance() {
		if (toolInstance == null) {
			toolInstance = new Hand();
		}
		return toolInstance;
	}
	
	private Hand() {
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
				// do nothing
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
