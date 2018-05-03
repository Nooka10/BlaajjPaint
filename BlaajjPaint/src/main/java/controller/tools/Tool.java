package controller.tools;


import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class Tool {
	
	protected final EventHandler<MouseEvent> currentOnMousePressedEventHandler = createMousePressedEventHandlers();
	protected final EventHandler<MouseEvent> currentOnMouseDraggedEventHandler = createMouseDraggedEventHandlers();
	protected final EventHandler<MouseEvent> currentOnMouseRelesedEventHandler = createMouseReleasedEventHandlers();
	
	public enum ToolType {PENCIL, ERASER, OTHER}
	
	public static boolean toolHasChanged = false;
	
	protected ToolType toolType = ToolType.OTHER;
	
	public Tool() { }
	
	private static Tool currentTool;// = Pencil.getInstance(); // TODO: pencil est l'outil sélectionné par défaut
	
	public static Tool getCurrentTool() {
		return currentTool;
	}
	
	public static void setCurrentTool(Tool currentTool) {
		if(Tool.currentTool != currentTool){
			toolHasChanged = true;
			
			Project.getInstance().removeEventHandler(Tool.currentTool); // on enlève les EventHandler de l'outil actuel
			Tool.currentTool = currentTool;
			Project.getInstance().addEventHandlers(Tool.currentTool); // on met les EventHandler du nouvel outil
		}
	}
	
	public static boolean getToolHasChanged(){
		return toolHasChanged;
	}
	
	public static void setToolHasChanged(boolean value){
		toolHasChanged = value;
	}
	
	public EventHandler<MouseEvent> getCurrentOnMousePressedEventHandler() {
		return currentOnMousePressedEventHandler;
	}
	
	public EventHandler<MouseEvent> getCurrentOnMouseDraggedEventHandler() {
		return currentOnMouseDraggedEventHandler;
	}
	
	public EventHandler<MouseEvent> getCurrentOnMouseRelesedEventHandler() {
		return currentOnMouseRelesedEventHandler;
	}
	
	protected abstract EventHandler<MouseEvent> createMousePressedEventHandlers();
	
	protected abstract EventHandler<MouseEvent> createMouseDraggedEventHandlers();
	
	protected abstract EventHandler<MouseEvent> createMouseReleasedEventHandlers();
}
