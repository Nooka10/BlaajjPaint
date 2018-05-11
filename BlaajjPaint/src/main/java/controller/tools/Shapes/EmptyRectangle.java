package controller.tools.Shapes;

import controller.Project;

public class EmptyRectangle extends ShapeDrawer {

    protected double thickness = 1; // l'épaisseur de l'outil

    private static EmptyRectangle toolInstance = new EmptyRectangle(); // l'instance unique du pinceau

    /**
     * Retourne l'instance unique du rectangle
     *
     * @return l'instance unique du rectangle
     */
    public static EmptyRectangle getInstance() {
        return toolInstance;
    }

    /**
     * Constructeur privé (modèle singleton)
     */
    private EmptyRectangle() {
        toolType = ToolType.EMPTYRECTANGLE;
    }

    @Override
    protected void drawShape() {
        shapeLayer.getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor());
        shapeLayer.getGraphicsContext2D().strokeRect(startPosX, startPosY, width, height);
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
    }

}
