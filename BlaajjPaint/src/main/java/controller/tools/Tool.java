package controller.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Tool {
	
	public enum ToolType {PENCIL, ERASER, OTHER}
	
	protected ToolType toolType = ToolType.OTHER;
	
	public
	
	private Tool() {
	
	}
	
	public void setPixel(int x, int y, Color color) {
		throw new NotImplementedException();
	}
	
	public GraphicsContext getGraphics() {
		
		throw new NotImplementedException();
	}
	
	public abstract void unregisterEventHandlers();
	
}
