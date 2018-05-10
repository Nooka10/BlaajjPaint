package controller.tools;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;

import static java.lang.Math.abs;

public abstract class ShapeDrawer extends Tool {

    protected double thickness = 1; // l'épaisseur de l'outil
    protected Layer shapeLayer;
    protected ShapeSave currentShapeSave; // La forme actuellement dessinée
    protected boolean shapeFilled  =false; //Par défaut une forme n'est pas remplie
    protected double beginPointX;
    protected double beginPointY;
    protected double startPosX;
    protected double startPosY;
    protected double width;
    protected double height;
    protected boolean isBeingDrawn = false;

    public ShapeDrawer(){
        //Constructeur de ShapeDrawer
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
    }

    public void setShapeFilled(boolean shapeFilled){
        this.shapeFilled = shapeFilled;
    }

    protected void updateShape(double endPosX, double endPosY){
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
