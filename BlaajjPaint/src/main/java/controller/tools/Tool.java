package controller.tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Tool {

    protected Canvas canvas;
    public enum ToolType {PENCIL, ERASER, OTHER};
    public ToolType toolType = ToolType.OTHER;
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    public Tool(Canvas canvas){
        this.canvas = canvas;
    }

    public void setPixel(int x, int y, Color color){
        throw new NotImplementedException();
    }

    public GraphicsContext getGraphics(){

        throw new NotImplementedException();
    }
    
    public abstract void unregisterEventHandlers();

}
