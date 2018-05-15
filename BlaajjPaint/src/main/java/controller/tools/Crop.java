package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.SnapshotMaker;
import utils.UndoException;

public class Crop extends Tool {
	private static Crop toolInstance = null;
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
	    if (toolInstance == null) {
		    toolInstance = new Crop();
	    }
	    return toolInstance;
    }

    public void validate(){
        if(cropSave != null) {
            oldCurrentLayer.crop(startX, startY, posX, posY);
            Project.getInstance().setCurrentLayer(oldCurrentLayer);
            Project.getInstance().getLayers().remove(selectionCropLayer);
            Project.getInstance().drawWorkspace();

            MainViewController.getInstance().getRightMenuController().updateLayerList();
            cropSave.execute();
            cancel();
        }
    }

    public void cancel(){
        reset();
        initCrop();
    }

    private void reset(){
        if(Project.getInstance().getCurrentLayer().equals(selectionCropLayer)) {
            // Suppression du calque d'ajout de text (textLayer)
            Project.getInstance().getLayers().remove(selectionCropLayer);
            Project.getInstance().setCurrentLayer(oldCurrentLayer);
            // redessine les layers et list de layers
            MainViewController.getInstance().getRightMenuController().updateLayerList();
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
            gc.clearRect(0, 0, selectionCropLayer.getWidth(), selectionCropLayer.getHeight());
            gc.setStroke(Color.BLUE);
            gc.strokeRect(x, y, width, height);
        }
    }

    @Override
    public void CallbackOldToolChanged() {
        super.CallbackOldToolChanged();
        reset();
    }

    @Override
    public void CallbackNewToolChanged(){
        super.CallbackNewToolChanged();
        initCrop();
    }

    public void initCrop(){
        oldCurrentLayer = Project.getInstance().getCurrentLayer();
        selectionCropLayer = new Layer((int) oldCurrentLayer.getWidth(), (int) oldCurrentLayer.getHeight(), true);
        selectionCropLayer.setLayoutX(oldCurrentLayer.getLayoutX());
        selectionCropLayer.setLayoutY(oldCurrentLayer.getLayoutY());
        selectionCropLayer.setVisible(true);

        oldCurrentLayer = Project.getInstance().getCurrentLayer();
        Project.getInstance().setCurrentLayer(selectionCropLayer);
        Project.getInstance().getLayers().addFirst(selectionCropLayer);
        Project.getInstance().drawWorkspace();
        startX = 0;
        startY = 0;
        posX = 0;
        posY = 0;
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
                    cropSave = new CropSave(oldCurrentLayer);
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

    class CropSave extends ICmd{
        private Image undosave;
        private Image redosave = null;
        private double widthLayer;
        private double heightLayer;
        private double layoutXLayer;
        private double layoutYLayer;
        private Layer layerCroped;

        public CropSave(Layer layerToCrop){
            undosave = SnapshotMaker.makeSnapshot(layerToCrop);
            widthLayer = layerToCrop.getWidth();
            heightLayer = layerToCrop.getHeight();
            layoutXLayer = layerToCrop.getLayoutX();
            layoutYLayer = layerToCrop.getLayoutY();
            layerCroped = layerToCrop;
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
            redosave = SnapshotMaker.makeSnapshot(layerCroped);
            Layer currentLayer = layerCroped;
            double widthTemp = currentLayer.getWidth();
            double heightTemp = currentLayer.getHeight();
            double layoutXTemp = currentLayer.getLayoutX();
            double layoutYTemp = currentLayer.getLayoutY();
            layerCroped.setWidth(widthLayer);
            layerCroped.setHeight(heightLayer);
            layerCroped.setLayoutX(layoutXLayer);
            layerCroped.setLayoutY(layoutYLayer);
            layerCroped.getGraphicsContext2D().clearRect(0, 0, widthLayer, heightLayer);
            layerCroped.getGraphicsContext2D().drawImage(undosave, 0, 0);
            widthLayer = widthTemp;
            heightLayer = heightTemp;
            layoutXLayer = layoutXTemp;
            layoutYLayer = layoutYTemp;

            undosave = null;
        }

        @Override
        public void redo() throws UndoException {
            if (redosave == null) {
                throw new UndoException();
            }

            undosave = SnapshotMaker.makeSnapshot(layerCroped);
            Layer currentLayer = layerCroped;
            double widthTemp = currentLayer.getWidth();
            double heightTemp = currentLayer.getHeight();
            double layoutXTemp = currentLayer.getLayoutX();
            double layoutYTemp = currentLayer.getLayoutY();
            layerCroped.getGraphicsContext2D().clearRect(0, 0, layerCroped.getWidth(), layerCroped.getHeight());
            layerCroped.setWidth(widthLayer);
            layerCroped.setHeight(heightLayer);
            layerCroped.setLayoutX(layoutXLayer);
            layerCroped.setLayoutY(layoutYLayer);
            layerCroped.getGraphicsContext2D().drawImage(redosave, 0, 0);
            widthLayer = widthTemp;
            heightLayer = heightTemp;
            layoutXLayer = layoutXTemp;
            layoutYLayer = layoutYTemp;
            redosave = null;
        }

        public String toString(){
            return "Image rognée";
        }
    }
}
