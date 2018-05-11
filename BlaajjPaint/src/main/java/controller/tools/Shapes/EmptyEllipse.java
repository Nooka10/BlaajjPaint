package controller.tools.Shapes;

import controller.Project;
import controller.tools.Tool;

public class EmptyEllipse extends ShapeDrawer {

    private static EmptyEllipse toolInstance = new EmptyEllipse(); // l'instance unique de l'ellipse vide

    /**
     * Retourne l'instance unique de l'ellipse vide
     *
     * @return l'instance unique de l'ellipse vide
     */
    public static EmptyEllipse getInstance() {
        return toolInstance;
    }

    /**
     * Constructeur privé (modèle singleton)
     */
    private EmptyEllipse() {
        toolType = Tool.ToolType.EMPTYRECTANGLE;
    }


    @Override
    protected void drawShape() {
        shapeLayer.getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor());
        shapeLayer.getGraphicsContext2D().strokeOval(startPosX, startPosY, width, height);
    }

}
