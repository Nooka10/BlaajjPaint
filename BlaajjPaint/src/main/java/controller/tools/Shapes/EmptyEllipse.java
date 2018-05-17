package controller.tools.Shapes;

import controller.Project;

/**
 * Classe implémentant une ellipse vide. Implémente le modèle Singleton.
 */
public class EmptyEllipse extends ShapeDrawer {
    
    private static EmptyEllipse toolInstance = null; // l'instance unique du singleton EmptyEllipse
    
    private double thickness = 1; // l'épaisseur de l'outil
    
    /**
     * Constructeur privé (modèle singleton).
     */
    private EmptyEllipse() {
        toolType = ToolType.EMPTYELLIPSE;
        tooltipHistory = "Ellipse vide";
        nomForme = "Ellipse vide";
    }
    
    /**
     * Retourne l'instance unique du singleton EmptyEllipse.
     * @return l'instance unique du singleton EmptyEllipse.
     */
    public static EmptyEllipse getInstance() {
        if (toolInstance == null) {
            toolInstance = new EmptyEllipse();
        }
        return toolInstance;
    }
    
	/**
	 * Retourne l'épaisseur de la bordure de la forme.
	 * @return l'épaisseur de la bordure de la forme.
	 */
	public double getThickness(){
		return thickness;
	}
 
	/**
	 * Permet de définir l'épaisseur de la bordure de la forme.
	 * @param thickness, l'épaisseur de la bordure de la forme.
	 */
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
