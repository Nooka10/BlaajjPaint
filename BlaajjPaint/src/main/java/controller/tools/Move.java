package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil <b>Déplacer un calque</b> permettant de déplacer un calque dans l'espace de travail. Implémente le modèle Singleton.
 */
public class Move extends Tool {
	private static Move toolInstance = null; // l'instance unique du singleton Hand
	private double oldX;
	private double oldY;
	private MoveSave currentSave;
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private Move() {
		toolType = ToolType.MOVE;
	}
	
	/**
	 * Retourne l'instance unique du singleton Move.
	 * @return l'instance unique du singleton Move.
	 */
	public static Move getInstance() {
		if(toolInstance == null){
			toolInstance = new Move();
		}
		return toolInstance;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				oldX = event.getX();
				oldY = event.getY();
				
				currentSave = new MoveSave(); // crée une sauvegarde du déplacement du calque
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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

	@Override
	protected EventHandler<MouseEvent> createMouseEnteredEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				changeCursor(Cursor.MOVE);
			}
		};
	}

	@Override
	protected EventHandler<MouseEvent> createMouseExitedEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resetOldCursor();
			}
		};
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant le déplacement d'un calque et définissant l'action à effectuer en cas d'appel à undo() ou redo()
	 * sur cette commande.
	 */
	public class MoveSave implements ICmd {
		private double oldXSave;
		private double oldYSave;
		private double newXSave;
		private double newYSave;
		
		/**
		 * Construit une commande sauvegardant le déplacement d'un calque.
		 */
		private MoveSave() {
			oldXSave = Project.getInstance().getCurrentLayer().getTranslateX();
			oldYSave = Project.getInstance().getCurrentLayer().getTranslateY();
		}
		
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			newXSave = Project.getInstance().getCurrentLayer().getTranslateX();
			newYSave = Project.getInstance().getCurrentLayer().getTranslateY();
			
			Project.getInstance().getCurrentLayer().setTranslateX(oldXSave);
			Project.getInstance().getCurrentLayer().setTranslateY(oldYSave);
		}
		
		@Override
		public void redo() {
			Project.getInstance().getCurrentLayer().setTranslateX(newXSave);
			Project.getInstance().getCurrentLayer().setTranslateY(newYSave);
		}
		
		@Override
		public String toString() {
			return "Déplacement d'un calque";
		}
	}
}
