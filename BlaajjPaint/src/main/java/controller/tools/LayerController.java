package controller.tools;

import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Layer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LayerController {

    protected Canvas canvas;

    public LayerController(Canvas canvas){
        this.canvas = canvas;
    }

    public void setPixel(int x, int y, Color color){
        throw new NotImplementedException();
    }

    protected GraphicsContext getGraphics(){

        throw new NotImplementedException();
    }


}
