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

	MoveSave currentSave;

	public class MoveSave implements ICmd {

		double oldXSave;
		double oldYSave;
		double newXSave;
		double newYSave;

		public MoveSave(){

			oldXSave = Project.getInstance().getCurrentLayer().getLayoutX();
			oldYSave = Project.getInstance().getCurrentLayer().getLayoutY();
		}

		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}

		@Override
		public void undo() throws UndoException {
			newXSave = Project.getInstance().getCurrentLayer().getLayoutX();
			newYSave = Project.getInstance().getCurrentLayer().getLayoutY();

			Project.getInstance().getCurrentLayer().setLayoutX(oldXSave);
			Project.getInstance().getCurrentLayer().setLayoutY(oldYSave);

		}

		@Override
		public void redo() throws UndoException {
			Project.getInstance().getCurrentLayer().setLayoutX(newXSave);
			Project.getInstance().getCurrentLayer().setLayoutY(newYSave);
		}

		@Override
		public String toString(){
			return "Move Layer";
		}
	}

	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				oldX = event.getX();
				oldY = event.getY();

				currentSave = new MoveSave();
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getCurrentLayer().setLayoutX(Project.getInstance().getCurrentLayer().getLayoutX() + event.getX() - oldX);
				Project.getInstance().getCurrentLayer().setLayoutY(Project.getInstance().getCurrentLayer().getLayoutY() + event.getY() - oldY);
				
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentSave.execute();
			}
		};
	}
}
