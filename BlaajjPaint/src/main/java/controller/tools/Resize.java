package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil de déplacement
 */
public class Resize extends Tool {
	
	private static Resize toolInstance = new Resize();
	
	
	public static Resize getInstance() {
		return toolInstance;
	}
	
	private Resize() {
		toolType = ToolType.RESIZE;
	}
	
	public class ResizeSave extends ICmd {
		
		
		
		@Override
		public void execute() {
		
		}
		
		@Override
		public void undo() {
		
		
		}
		
		@Override
		public void redo() {
		
		}
		
		@Override
		public String toString() {
			return "Resize Layer";
		}
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			
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
