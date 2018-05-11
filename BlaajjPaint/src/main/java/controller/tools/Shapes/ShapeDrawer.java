package controller.tools.Shapes;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;

public abstract class ShapeDrawer extends Tool {

    //
    protected Layer shapeLayer;
    protected ShapeSave currentShapeSave; // La forme actuellement dessinée
    protected double beginPointX;
    protected double beginPointY;
    protected double startPosX;
    protected double startPosY;
    protected double width;
    protected double height;
    protected double thickness = 1; // l'épaisseur de l'outil


    @Override
    public void CallbackNewToolChanged() {
        shapeLayer = new Layer(Project.getInstance().getDimension().width, Project.getInstance().getDimension().height);
        Project.getInstance().removeEventHandler(Tool.getCurrentTool());
        shapeLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, currentOnMousePressedEventHandler);
        shapeLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, currentOnMouseDraggedEventHandler);
        shapeLayer.addEventHandler(MouseEvent.MOUSE_RELEASED, currentOnMouseRelesedEventHandler);

        Project.getInstance().getLayers().addFirst(shapeLayer);
        Project.getInstance().drawWorkspace();
    }

    @Override
    public void CallbackOldToolChanged() {
        Project.getInstance().getLayers().remove(shapeLayer);
        shapeLayer.removeEventHandler(MouseEvent.MOUSE_PRESSED, currentOnMousePressedEventHandler);
        shapeLayer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, currentOnMouseDraggedEventHandler);
        shapeLayer.removeEventHandler(MouseEvent.MOUSE_RELEASED, currentOnMouseRelesedEventHandler);
        Project.getInstance().drawWorkspace();
    }


    @Override
    protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Dans mousePressed");
                currentShapeSave = new ShapeSave();
                beginPointX = event.getX();
                beginPointY = event.getY();
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Dans mouseDragged");
                shapeLayer.getGraphicsContext2D().clearRect(0, 0, shapeLayer.getWidth(), shapeLayer.getHeight());
                updateShape(event.getX(), event.getY());

                drawShape();

            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Dans mouseRelased");
                Project.getInstance().getLayers().remove(shapeLayer);
                Project.getInstance().addLayer(shapeLayer);
                MainViewController.getInstance().getRightMenuController().createLayerList();
                CallbackNewToolChanged();
                currentShapeSave.execute();

            }
        };
    }

    abstract protected void drawShape();

    public double getThickness(){
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
    }

    private void updateShape(double endPosX, double endPosY){
        if(endPosX < beginPointX){
            this.startPosX = endPosX;
            width = beginPointX - endPosX;
        } else {
            this.startPosX = beginPointX;
            width = endPosX - beginPointX;
        }

        if(endPosY < beginPointY){
            this.startPosY = endPosY;
            height = beginPointY - endPosY;
        } else {
            this.startPosY = beginPointY;
            height = endPosY - beginPointY;
        }

    }

    class ShapeSave implements ICmd {

        private Image undosave;
        private Image redosave = null;
        private SnapshotParameters params;

        public ShapeSave() {
            // configuration des paramètres utilisés pour la sauvegarde du canevas
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
            if(Project.getInstance().getCurrentLayer() != Project.getInstance().getLayers().getFirst()){
                Project.getInstance().getLayers().removeFirst();
            }
            redosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
            Project.getInstance().getLayers().removeFirst();
            MainViewController.getInstance().getRightMenuController().deleteLayer(0);
            Project.getInstance().setCurrentLayer(Project.getInstance().getLayers().getFirst());
            Project.getInstance().drawWorkspace();
            MainViewController.getInstance().getRightMenuController().createLayerList();
            undosave = null;
        }

        @Override
        public void redo() throws UndoException {
            if (redosave == null) {
                throw new UndoException();
            }
            undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
            Layer redoLayer = new Layer((int)redosave.getWidth(), (int)redosave.getHeight());
            redoLayer.getGraphicsContext2D().drawImage(redosave,0,0);
            MainViewController.getInstance().getRightMenuController().createLayerList();
            Project.getInstance().addLayer(redoLayer);
            redosave = null;
        }
    }
}
