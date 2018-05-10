package controller.tools;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.text.*;

public class TextTool extends Tool {
    private static TextTool instance = new TextTool();
    private Font font;

    private TextTool(){
        toolType = ToolType.TEXT;
    }

    public static TextTool getInstance(){
        return instance;
    }

    public void setFont(Font font){
        this.font = font;
    }

    public Font getFont(){
        return font;
    }

    public void validate(){

    }

    public void changeTextValue(){

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
