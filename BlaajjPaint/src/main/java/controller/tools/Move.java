package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil de déplacement
 */
public class Move extends Tool {
	
	private static Move toolInstance = new Move();
	
	private double oldX;
	
	private double oldY;
	
	private MoveSave currentSave;
	
	public static Move getInstance() {
		return toolInstance;
	}
	
	private Move() {
		toolType = ToolType.MOVE;
	}
	
	public class MoveSave extends ICmd {
		private double oldXSave;
		private double oldYSave;
		private double newXSave;
		private double newYSave;
		
		public MoveSave() {
			oldXSave = Project.getInstance().getCurrentLayer().getLayoutX();
			oldYSave = Project.getInstance().getCurrentLayer().getLayoutY();
		}
		
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			newXSave = Project.getInstance().getCurrentLayer().getLayoutX();
			newYSave = Project.getInstance().getCurrentLayer().getLayoutY();
			
			Project.getInstance().getCurrentLayer().setLayoutX(oldXSave);
			Project.getInstance().getCurrentLayer().setLayoutY(oldYSave);
			
		}
		
		@Override
		public void redo() {
			Project.getInstance().getCurrentLayer().setLayoutX(newXSave);
			Project.getInstance().getCurrentLayer().setLayoutY(newYSave);
		}
		
		@Override
		public String toString() {
			return "Move Layer";
		}
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getWorkspace().setAutoSizeChildren(false);
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
				Project.getInstance().getWorkspace().setAutoSizeChildren(true);
				currentSave.execute();
			}
		};
	}
}
