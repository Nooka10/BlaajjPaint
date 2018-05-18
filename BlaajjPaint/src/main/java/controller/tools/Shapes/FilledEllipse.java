package controller.tools.Shapes;

import controller.Project;

/**
 * Classe implémentant une ellipse pleine. Implémente le modèle Singleton.
 */
public class FilledEllipse extends ShapeDrawer {
	
	private static FilledEllipse toolInstance; // l'instance unique du singleton FilledEllipse
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private FilledEllipse() {
		toolType = ToolType.FILLEDELLIPSE;
		tooltipHistory = "Ellipse pleine";
		nomForme = "Ellipse pleine";
	}
	
	/**
	 * Retourne l'instance unique du singleton FilledEllipse.
	 * @return l'instance unique du singleton FilledEllipse.
	 */
	public static FilledEllipse getInstance() {
		if (toolInstance == null) {
			toolInstance = new FilledEllipse();
		}
		return toolInstance;
	}
	
	@Override
	protected void drawShape() {
		shapeLayer.getGraphicsContext2D().setFill(Project.getInstance().getCurrentColor());
		shapeLayer.getGraphicsContext2D().fillOval(startPosX, startPosY, width, height);
	}
	
}
