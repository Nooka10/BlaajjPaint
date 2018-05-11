package controller.tools.Shapes;

import controller.Project;

public class EmptyEllipse extends ShapeDrawer {

    private static EmptyEllipse toolInstance = new EmptyEllipse(); // l'instance unique de l'ellipse vide

    private double thickness; // l'épaisseur de l'outil

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
        toolType = ToolType.EMPTYELLIPSE;
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
        shapeLayer.getGraphicsContext2D().strokeOval(startPosX, startPosY, width, height);
    }

}
