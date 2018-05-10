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
public class Move extends Tool {
	
	private static Move toolInstance = new Move();
	
	public static Move getInstance() {
		return toolInstance;
	}
	
	private Move() {
		toolType = ToolType.MOVE;
	}

	/*
	public class MovementSave implements ICmd {

		double oldPositionV;
		double oldPositionH;
		double newPositionV;
		double newPositionH;

		public MovementSave(){
			setNewPosition();
			setOldPosition();
		}

		private void setOldPosition(){
			oldPositionV  = MainViewController.getInstance().getScrollPane().getVvalue();
			oldPositionH  = MainViewController.getInstance().getScrollPane().getHvalue();
		}
		private void setNewPosition(){
			newPositionV = MainViewController.getInstance().getScrollPane().getVvalue();
			newPositionH = MainViewController.getInstance().getScrollPane().getHvalue();
		}

		@Override
		public void execute() {
			setNewPosition();
		}

		@Override
		public void undo() throws UndoException {
			MainViewController.getInstance().getScrollPane().setVvalue(oldPositionV);
			MainViewController.getInstance().getScrollPane().getHvalue();
		}

		@Override
		public void redo() throws UndoException {

		}
	}
	*/

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
