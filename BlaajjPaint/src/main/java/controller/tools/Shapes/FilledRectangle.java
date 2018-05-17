package controller.tools.Shapes;

import controller.Project;

/**
 * Classe implémentant un rectangle plein. Implémente le modèle Singleton.
 */
public class FilledRectangle extends ShapeDrawer {
    
    private static FilledRectangle toolInstance = null; // l'instance unique du singleton FilledRectangle
    
    /**
     * Constructeur privé (modèle singleton).
     */
    private FilledRectangle() {
        toolType = ToolType.FILLEDRECTANGLE;
        tooltipHistory = "Rectangle plein";
        nomForme = "Rectangle plein";
    }
	
	/**
	 * Retourne l'instance unique du singleton FilledRectangle.
	 * @return l'instance unique du singleton FilledRectangle.
	 */
    public static FilledRectangle getInstance() {
        if (toolInstance == null) {
            toolInstance = new FilledRectangle();
        }
        return toolInstance;
    }
    
    @Override
    protected void drawShape() {
        shapeLayer.getGraphicsContext2D().setFill(Project.getInstance().getCurrentColor());
        shapeLayer.getGraphicsContext2D().fillRect(startPosX, startPosY, width, height);
    }
}
