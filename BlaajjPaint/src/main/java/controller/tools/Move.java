package controller.tools;

import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import utils.UndoException;

/**
 * Classe implémentant l'outil de déplacement
 */
public class Move extends Tool {
	
	private static Move toolInstance = new Move();
	
	public static Move getInstance() {
		return toolInstance;
	}
	
	private Move() {
		toolType = ToolType.MOVE;
	}
	
	double oldX;
	double oldY;
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				oldX = event.getX();
				oldY = event.getY();
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().setLayoutX(Project.getInstance().getCurrentLayer().getLayoutX() + event.getX() - oldX);
				Project.getInstance().getCurrentLayer().setLayoutY(Project.getInstance().getCurrentLayer().getLayoutY() +event.getY() - oldY);
				
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
