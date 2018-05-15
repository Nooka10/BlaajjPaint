package controller.tools.Shapes;

import controller.Project;

public class FilledRectangle extends ShapeDrawer {

    private static FilledRectangle toolInstance = new FilledRectangle(); // l'instance unique du pinceau

    /**
     * Retourne l'instance unique du rectangle
     *
     * @return l'instance unique du rectangle
     */
    public static FilledRectangle getInstance() {
        return toolInstance;
    }

    /**
     * Constructeur privé (modèle singleton)
     */
    private FilledRectangle() {
        toolType = ToolType.FILLEDRECTANGLE;
        tooltipHistory = "Rectangle plein";
        nomForme = "Rectangle plein";
    }


    @Override
    protected void drawShape() {
        shapeLayer.getGraphicsContext2D().setFill(Project.getInstance().getCurrentColor());
        shapeLayer.getGraphicsContext2D().fillRect(startPosX, startPosY, width, height);
    }
}
