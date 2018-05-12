package controller.tools.Shapes;

import controller.Project;

public class EmptyRectangle extends ShapeDrawer {

    private static EmptyRectangle toolInstance = new EmptyRectangle(); // l'instance unique du rectangle vide

    private double thickness; // l'épaisseur de l'outil

    /**
     * Retourne l'instance unique du rectangle vide
     *
     * @return l'instance unique du rectangle vide
     */
    public static EmptyRectangle getInstance() {
        return toolInstance;
    }

    /**
     * Constructeur privé (modèle singleton)
     */
    private EmptyRectangle() {

        toolType = ToolType.EMPTYRECTANGLE;
        tooltipHistory = "Rectangle vide";
    }

    public double getThickness(){
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
    }

    @Override
    protected void drawShape() {
        shapeLayer.getGraphicsContext2D().setLineWidth(thickness);
        shapeLayer.getGraphicsContext2D().setStroke(Project.getInstance().getCurrentColor());
        shapeLayer.getGraphicsContext2D().strokeRect(startPosX, startPosY, width, height);
    }

}
