package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class BlaajjRectangle extends ShapeDrawer {

    private static BlaajjRectangle toolInstance = new BlaajjRectangle(); // l'instance unique du pinceau

    /**
     * Retourne l'instance unique du rectangle
     *
     * @return l'instance unique du rectangle
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
        //Project.getInstance().addEventHandlers(Tool.getCurrentTool());
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
                isBeingDrawn = true;
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Dans mouseDragged");
                if (isBeingDrawn == true) {
                    shapeLayer.getGraphicsContext2D().clearRect(0, 0, shapeLayer.getWidth(), shapeLayer.getHeight());
                    updateShape(event.getX(), event.getY());

                    if (shapeFilled) {
                        shapeLayer.getGraphicsContext2D().fillRect(startPosX,
                                startPosY, width, height);
                    } else {
                        shapeLayer.getGraphicsContext2D().strokeRect(startPosX,
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
                System.out.println("Dans mouseRelased");
                if (isBeingDrawn == true) {
                    isBeingDrawn = false;
                    Project.getInstance().getLayers().remove(shapeLayer);
                    Project.getInstance().addLayer(shapeLayer);
                    CallbackNewToolChanged();
                    currentShapeSave.execute();
                }
            }
        };
    }

}
