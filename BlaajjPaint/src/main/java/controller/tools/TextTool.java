package controller.tools;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import javafx.scene.text.*;
import utils.UndoException;

public class TextTool extends Tool {
    private static TextTool instance = new TextTool();
    private AddText addText;

    private Font font;
    private Layer textLayer;
    private Layer oldCurrentLayer;
    private String text;
    private int x;
    private int y;

    private TextTool(){
        toolType = ToolType.TEXT;
    }

    public static TextTool getInstance(){
        return instance;
    }

    public void setFont(Font font){
        this.font = font;
        changeTextOnLayout();
    }

    public Font getFont(){
        return font;
    }

    public void validate(){
        if(addText != null) {
            textLayer.mergeLayers(oldCurrentLayer);
            Project.getInstance().setCurrentLayer(oldCurrentLayer);
            Project.getInstance().drawWorkspace();
            text = "";
            addText.execute();
            addText = null; // end of the session of adding a text
        }
    }

    public void changeTextValue(String text){
        this.text = text;
        changeTextOnLayout();
    }

    private void changeTextOnLayout(){
        if(addText != null){
            GraphicsContext graphics = textLayer.getGraphicsContext2D();
            graphics.clearRect(0, 0, textLayer.getWidth(), textLayer.getWidth());
            graphics.setFont(font);
            graphics.fillText(text ,x ,y);
            Project.getInstance().drawWorkspace();;
        }
    }

    @Override
    public void CallbackOldToolChanged() {
        validate();
    }

    @Override
    protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // first click
                if(addText == null){
                    addText = new AddText();
                    oldCurrentLayer = Project.getInstance().getCurrentLayer();
                    textLayer = new Layer(Project.getInstance().getDimension());
                    textLayer.setVisible(true);
                    Project.getInstance().setCurrentLayer(textLayer);
                    Project.getInstance().getLayers().addFirst(textLayer);
                    Project.getInstance().drawWorkspace();
                }
                // set new position
                x = (int)event.getX();
                y = (int)event.getY();

                changeTextOnLayout();
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = (int)event.getX();
                y = (int)event.getY();
                changeTextOnLayout();
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                 // do nothing
            }
        };
    }

    class AddText implements ICmd {
        private Image undosave;
        private Image redosave = null;
        private SnapshotParameters params;

        public AddText(){
            params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            this.undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
        }
        @Override
        public void execute() {
            RecordCmd.getInstance().saveCmd(this);
        }

        @Override
        public void undo() throws UndoException {
            if (undosave == null) {
                throw new UndoException();
            }
            redosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
            Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(undosave, 0, 0);
            undosave = null;
        }

        @Override
        public void redo() throws UndoException {
            if (redosave == null) {
                throw new UndoException();
            }
            undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
            Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(redosave, 0, 0);
            redosave = null;
        }
    }
}
