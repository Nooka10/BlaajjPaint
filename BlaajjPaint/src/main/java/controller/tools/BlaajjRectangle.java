package controller.tools;

import controller.Layer;
import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class BlaajjRectangle extends ShapeDrawer {

    private static BlaajjRectangle toolInstance = new BlaajjRectangle(); // l'instance unique du pinceau

    /**
     * Retourne l'instance unique du pinceau
     *
     * @return l'instance unique du pinceau
     */
    public static BlaajjRectangle getInstance() {
        return toolInstance;
    }

    /**
     * Constructeur privé (modèle singleton)
     */
    private BlaajjRectangle() {
        toolType = ToolType.RECTANGLE;
    }

    @Override
    public void CallbackNewToolChanged() {
        shapeLayer = new Layer(Project.getInstance().getDimension().width, Project.getInstance().getDimension().height);
        Project.getInstance().removeEventHandler(Tool.getCurrentTool());
        Project.getInstance().addEventHandlers(this);
    }

    @Override
    public void CallbackOldToolChanged() {
        Project.getInstance().removeEventHandler(this);
        Project.getInstance().addEventHandlers(Tool.getCurrentTool());
    }


    @Override
    protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentShapeSave = new ShapeSave();
                beginPointX = event.getX();;
                beginPointY = event.getY();
                isBeingDrawn = true;
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isBeingDrawn == true){
                    Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(0, 0, Project.getInstance().getDimension().getWidth(), Project.getInstance().getDimension().getHeight());
                    updateShape(event.getX(), event.getY());

                    if(shapeFilled){
                        Project.getInstance().getCurrentLayer().getGraphicsContext2D().fillRect(startPosX,
                                startPosY, width, height);
                    }else {
                        Project.getInstance().getCurrentLayer().getGraphicsContext2D().strokeRect(startPosX,
                                startPosY, width, height);
                    }
                }
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isBeingDrawn == true) {
                    isBeingDrawn = false;
                    Project.getInstance().addLayer(shapeLayer);
                    CallbackNewToolChanged();
                    currentShapeSave.execute();
                }
            }
        };
    }

}
