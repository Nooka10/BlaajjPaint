package controller.tools.Shapes;

import controller.Project;

public class FilledEllipse extends ShapeDrawer {

    private static FilledEllipse toolInstance = new FilledEllipse(); // l'instance unique du pinceau

    /**
     * Retourne l'instance unique de l'ellipse
     *
     * @return l'instance unique de l'ellipse
     */
    public static FilledEllipse getInstance() {
        return toolInstance;
    }

    /**
     * Constructeur privé (modèle singleton)
     */
    private FilledEllipse() {
        toolType = ToolType.FILLEDELLIPSE;
        tooltipHistory = "Ellipse pleine";
        nomForme = "Ellipse pleine";
    }


    @Override
    protected void drawShape() {
        shapeLayer.getGraphicsContext2D().setFill(Project.getInstance().getCurrentColor());
        shapeLayer.getGraphicsContext2D().fillOval(startPosX, startPosY, width, height);
    }

}
