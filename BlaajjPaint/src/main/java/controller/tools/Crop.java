package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;

public class Crop extends Tool {
    private static Crop instance = new Crop();
    private CropSave cropSave;

    private Layer oldCurrentLayer;
    private Layer selectionCropLayer;
    private double startX;
    private double startY;
    private double posX;
    private double posY;



    private Crop() {
        toolType = ToolType.CROP;
    }

    public static Crop getInstance(){
        return instance;
    }

    public void validate(){
        if(cropSave != null) {
            Project.getInstance().deleteCurrentLayer();
            Project.getInstance().setCurrentLayer(oldCurrentLayer);
            oldCurrentLayer.crop(startX, startY, posX, posY);
            cropSave = null;
            selectionCropLayer = null;
        }
    }

    public void cancel(){
        if(Project.getInstance().getCurrentLayer().equals(selectionCropLayer)) {
            // Suppression du calque d'ajout de text (textLayer)
            Project.getInstance().deleteCurrentLayer();
            // Le calque courant redevient l'ancien calque courant
            Project.getInstance().setCurrentLayer(oldCurrentLayer);
            // redessine les layers et list de layers
            MainViewController.getInstance().getRightMenuController().updateLayerList();
            Project.getInstance().drawWorkspace();
        }
        cropSave = null;
        selectionCropLayer = null;
    }

    private void drawRectOnLayer() {
        if(cropSave != null) {
            double width = Math.abs(startX - posX);
            double height = Math.abs(startY - posY);
            double x = startX <= posX ? startX : posX;
            double y = startY <= posY ? startY : posY;
            GraphicsContext gc = selectionCropLayer.getGraphicsContext2D();
            gc.clearRect(0, 0, selectionCropLayer.getWidth(), selectionCropLayer.getWidth());
            gc.setStroke(Color.BLUE);
            gc.strokeRect(x, y, width, height);
            Project.getInstance().drawWorkspace(); // Refresh du calque
        }
    }

    @Override
    public void CallbackOldToolChanged() {
        super.CallbackOldToolChanged();
        cancel();
    }

    @Override
    public void CallbackNewToolChanged(){
        initCrop();
    }

    public void initCrop(){
        oldCurrentLayer = Project.getInstance().getCurrentLayer();
        selectionCropLayer = new Layer(Project.getInstance().getDimension());
        selectionCropLayer.setVisible(true);

        oldCurrentLayer = Project.getInstance().getCurrentLayer();
        Project.getInstance().setCurrentLayer(selectionCropLayer);
        Project.getInstance().getLayers().addFirst(selectionCropLayer);
        Project.getInstance().drawWorkspace();
    }

    @Override
    protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // premier clique - set des valeurs et création du calque
                if (cropSave == null) {
                    // A initialisé si l'outil a été utilisé
                    if(selectionCropLayer == null){
                        initCrop();
                    }
                    cropSave = new CropSave();
                    startX = event.getX();
                    startY = event.getY();
                    posX = event.getX();
                    posY = event.getY();
                } else {
                    // récupération de la position du clique
                    if(Math.abs(posX - event.getX()) <= Math.abs(startX - event.getX())){
                        posX = event.getX();
                    } else {
                        startX = event.getX();
                    }
                    if(Math.abs(posY - event.getY()) <= Math.abs(startY - event.getY())){
                        posY = event.getY();
                    } else {
                        startY = event.getY();
                    }
                }

                drawRectOnLayer(); // affichage du text sur le layer
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // récupération de la position du clique
                if(Math.abs(posX - event.getX()) <= Math.abs(startX - event.getX())){
                    posX = event.getX();
                } else {
                    startX = event.getX();
                }
                if(Math.abs(posY - event.getY()) <= Math.abs(startY - event.getY())){
                    posY = event.getY();
                } else {
                    startY = event.getY();
                }

                drawRectOnLayer();
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

    class CropSave implements ICmd{
        private Image undosave;
        private Image redosave = null;
        private SnapshotParameters params;

        public CropSave(){
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

        public String toString(){
            return "Image rognée";
        }
    }
}
