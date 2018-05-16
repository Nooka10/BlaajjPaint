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
import utils.UndoException;
import utils.Utils;

/**
 * Outil pour rogner un calque grâce à une séléction réctangulaire implémentant le modèle Singleton)
 */
public class Crop extends Tool {
    // Attributs de l'outil Crop
	private static Crop toolInstance = null;
    private CropSave cropSave;
    private Layer oldCurrentLayer;
    private Layer selectionCropLayer;
    private double startX;
    private double startY;
    private double posX;
    private double posY;

    /**
     * Contructeur privé (modèle Singleton)
     */
    private Crop() {
        toolType = ToolType.CROP;
    }

    /**
     * Retourne l'instance unique du singleton Crop. La crée si elle n'existe pas déjà.
     * @return l'instance unique du singleton Crop.
     */
    public static Crop getInstance(){
	    if (toolInstance == null) {
		    toolInstance = new Crop();
	    }
	    return toolInstance;
    }

    /**
     * Fonction appelé pour validé le rognage de l'image par rapport à la séléction
     */
    public void validate(){
        if(cropSave != null) {
            // Rognage du calque
            oldCurrentLayer.crop(startX, startY, posX, posY);
            // Remise en place des calques
            Project.getInstance().setCurrentLayer(oldCurrentLayer);
            Project.getInstance().getLayers().remove(selectionCropLayer);
            Project.getInstance().drawWorkspace();

            MainViewController.getInstance().getRightMenuController().updateLayerList();
            cropSave.execute(); // Exécution de la cmd
            cancel(); // Permet d'être prêt à recevoir un nouveau rognage
        }
    }

    /**
     * Annulation de la séléction actuel et préparation à une nouvelle séléction/rognage
     */
    public void cancel(){
        reset(); // reset des attributs
        initCrop(); // Préparation à un nouvelle séléction pour le rognage
    }

    /**
     * Remet en place les calque et les attributs. Permet de quitter l'outil proprement ou reset avant un nouvelle séléction
     */
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


    /**
     * Permet de dessiner le réctangle de séléction pour le rognage
     */
    private void drawRectOnLayer() {
        // Test si la séléction est initialisé
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
        initCrop(); // besoin d'être initialisé pour que le premier clique fonctionne correctement
    }

    /**
     * Initialisation de la zone de séléction et les outils utile pour le rognage
     */
    private void initCrop(){
        // Sauvegarde du calque sur lequel on va rogner
        oldCurrentLayer = Project.getInstance().getCurrentLayer();
        // Création du calque temporaire utilisé pour la séléction
        selectionCropLayer = new Layer((int) oldCurrentLayer.getWidth(), (int) oldCurrentLayer.getHeight(), true);
	    //selectionCropLayer.setLayoutX(oldCurrentLayer.getLayoutX());
	    //selectionCropLayer.setLayoutY(oldCurrentLayer.getLayoutY());
	    selectionCropLayer.setTranslateX(oldCurrentLayer.getTranslateX());
	    selectionCropLayer.setTranslateY(oldCurrentLayer.getTranslateY());
    
        selectionCropLayer.setVisible(true);

        oldCurrentLayer = Project.getInstance().getCurrentLayer();
        Project.getInstance().setCurrentLayer(selectionCropLayer);
        Project.getInstance().getLayers().addFirst(selectionCropLayer);
        Project.getInstance().drawWorkspace();
        // initialisation des attributs
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

    /**
     * Commande permettant le undo/redo
     */
    class CropSave extends ICmd{
        // Attributs nécessaire pour l'undo / redo
        private Image undosave;
        private Image redosave = null;
        private double widthLayer;
        private double heightLayer;
        private double layoutXLayer;
        private double layoutYLayer;
        private Layer layerCroped;

        /**
         * Constructeu
         * @param layerToCrop - layer sur lequel on va rogner
         */
        public CropSave(Layer layerToCrop){
            undosave = Utils.makeSnapshot(layerToCrop);
            widthLayer = layerToCrop.getWidth();
            heightLayer = layerToCrop.getHeight();
            //layoutXLayer = layerToCrop.getLayoutX();
            //layoutYLayer = layerToCrop.getLayoutY();
	        layoutXLayer = layerToCrop.getTranslateX();
	        layoutYLayer = layerToCrop.getTranslateY();
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
            redosave = Utils.makeSnapshot(layerCroped);
            // Sauvegarde du calque sur lequel on va rogner
            Layer currentLayer = layerCroped;
            // Sauvegarde des dimensions
            double widthTemp = currentLayer.getWidth();
            double heightTemp = currentLayer.getHeight();
	        //double layoutXTemp = currentLayer.getLayoutX();
	        //double layoutYTemp = currentLayer.getLayoutY();
	
	        double layoutXTemp = currentLayer.getTranslateX();
	        double layoutYTemp = currentLayer.getTranslateY();
            // Remise à jour des anciennes dimensions et décallage
            layerCroped.setWidth(widthLayer);
            layerCroped.setHeight(heightLayer);
	        // layerCroped.setLayoutX(layoutXLayer);
	        // layerCroped.setLayoutY(layoutYLayer);
	        layerCroped.setTranslateX(layoutXLayer);
	        layerCroped.setTranslateY(layoutYLayer);
	        // Netoyage du calque
            layerCroped.getGraphicsContext2D().clearRect(0, 0, widthLayer, heightLayer);
            // Remise de l'image du calque durant l'opération précédente
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

            undosave = Utils.makeSnapshot(layerCroped);
            // Sauvegarde du calque sur lequel on va rogner
            Layer currentLayer = layerCroped;
            // Sauvegarde des dimensions
            double widthTemp = currentLayer.getWidth();
            double heightTemp = currentLayer.getHeight();
	        //double layoutXTemp = currentLayer.getLayoutX();
	        //double layoutYTemp = currentLayer.getLayoutY();
	        double layoutXTemp = currentLayer.getTranslateX();
	        double layoutYTemp = currentLayer.getTranslateY();
            // Netoyage du calque - ne pas change de place (ou changer les attribut width et height) peut être amélioré
            layerCroped.getGraphicsContext2D().clearRect(0, 0, layerCroped.getWidth(), layerCroped.getHeight());
            // Remise à jour des anciennes dimensions et décallage (calque undo)
            layerCroped.setWidth(widthLayer);
            layerCroped.setHeight(heightLayer);
	        //layerCroped.setLayoutX(layoutXLayer);
	        //layerCroped.setLayoutY(layoutYLayer);
	        layerCroped.setTranslateX(layoutXLayer);
	        layerCroped.setTranslateY(layoutYLayer);
	
	        // Remise de l'image du calque durant l'opération précédente
            layerCroped.getGraphicsContext2D().drawImage(redosave, 0, 0);
            widthLayer = widthTemp;
            heightLayer = heightTemp;
            layoutXLayer = layoutXTemp;
            layoutYLayer = layoutYTemp;
            redosave = null;
        }

        /**
         * Définit le nom de la commande. Utilisé pour l'historique
         * @return le nom de la commmande
         */
        public String toString(){
            return "Image rognée";
        }
    }
}
