/*
Author: Benoît
 */
package controller.tools;

import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import utils.UndoException;

/**
 * Classe implémentant l'outil pipette
 */
public class Hand extends Tool {
	
	private static Hand toolInstance = new Hand();
	
	public static Hand getInstance() {
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
