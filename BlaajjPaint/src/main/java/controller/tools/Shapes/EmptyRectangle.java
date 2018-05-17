package controller.tools.Shapes;

import controller.Project;

/**
 * Classe implémentant un rectangle vide. Implémente le modèle Singleton.
 */
public class EmptyRectangle extends ShapeDrawer {
	
	private static EmptyRectangle toolInstance = null; // l'instance unique du singleton EmptyRectangle
	
	private double thickness = 1; // l'épaisseur de l'outil
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private EmptyRectangle() {
		toolType = ToolType.EMPTYRECTANGLE;
		tooltipHistory = "Rectangle vide";
		nomForme = "Rectangle vide";
	}
	
	/**
	 * Retourne l'instance unique du singleton EmptyRectangle.
	 * @return l'instance unique du singleton EmptyRectangle.
	 */
	public static EmptyRectangle getInstance() {
		if (toolInstance == null) {
			toolInstance = new EmptyRectangle();
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
		shapeLayer.getGraphicsContext2D().strokeRect(startPosX, startPosY, width, height);
	}
	
}
