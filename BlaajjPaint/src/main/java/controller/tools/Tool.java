package controller.tools;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class Tool {
	
	public enum ToolType {PENCIL, ERASER, OTHER}
	
	protected ToolType toolType = ToolType.OTHER;
	
	public Tool() {
	}
	
	private static Tool currentTool = Pencil.getInstance();
	
	public static Tool getCurrentTool() {
		return currentTool;
	}
	
	public static void setCurrentTool(Tool currentTool) {
		Tool.currentTool = currentTool;
	}
	
	public abstract EventHandler<MouseEvent> addMousePressedEventHandlers();
	
	public abstract EventHandler<MouseEvent> addMouseDraggedEventHandlers();

	public abstract EventHandler<MouseEvent> addMouseReleasedEventHandlers();
}
