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
	
	private static Move toolInstance = null;
	
	private double oldX;
	
	private double oldY;
	
	private MoveSave currentSave;
	
	public static Move getInstance() {
		if(toolInstance == null){
			toolInstance = new Move();
		}
		return toolInstance;
	}
	
	private Move() {
		toolType = ToolType.MOVE;
	}
	
	public class MoveSave implements ICmd {
		private double oldXSave;
		private double oldYSave;
		private double newXSave;
		private double newYSave;
		
		public MoveSave() {
			//oldXSave = Project.getInstance().getCurrentLayer().getLayoutX();
			//oldYSave = Project.getInstance().getCurrentLayer().getLayoutY();
			oldXSave = Project.getInstance().getCurrentLayer().getTranslateX();
			oldYSave = Project.getInstance().getCurrentLayer().getTranslateY();
		}
		
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			//newXSave = Project.getInstance().getCurrentLayer().getLayoutX();
			//newYSave = Project.getInstance().getCurrentLayer().getLayoutY();
			newXSave = Project.getInstance().getCurrentLayer().getTranslateX();
			newYSave = Project.getInstance().getCurrentLayer().getTranslateY();
			
			//Project.getInstance().getCurrentLayer().setLayoutX(oldXSave);
			//Project.getInstance().getCurrentLayer().setLayoutY(oldYSave);
			Project.getInstance().getCurrentLayer().setTranslateX(oldXSave);
			Project.getInstance().getCurrentLayer().setTranslateY(oldYSave);
		}
		
		@Override
		public void redo() {
			//Project.getInstance().getCurrentLayer().setLayoutX(newXSave);
			//Project.getInstance().getCurrentLayer().setLayoutY(newYSave);
			Project.getInstance().getCurrentLayer().setTranslateX(oldXSave);
			Project.getInstance().getCurrentLayer().setTranslateY(oldYSave);
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
				//Project.getInstance().getCurrentLayer().setLayoutX(Project.getInstance().getCurrentLayer().getLayoutX() + event.getX() - oldX);
				//Project.getInstance().getCurrentLayer().setLayoutY(Project.getInstance().getCurrentLayer().getLayoutY() + event.getY() - oldY);
				
				Project.getInstance().getCurrentLayer().setTranslateX(Project.getInstance().getCurrentLayer().getTranslateX() + event.getX() - oldX);
				Project.getInstance().getCurrentLayer().setTranslateY(Project.getInstance().getCurrentLayer().getTranslateY() + event.getY() - oldY);
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
