package controller.tools.ToolDrawer;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import controller.tools.Tool;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.UndoException;

/**
 * Classe abstraite implémentant un outil permettant de dessiner ou effacer. Classe mère du pinceau et de la gomme.
 */
public abstract class ToolDrawer extends Tool {
	protected double thickness = 1; // l'épaisseur de l'outil
	protected Trait currentTrait; // Le trait actuellement tiré
	
	/**
	 * Permet de régler l'épaisseur de l'outil
	 *
	 * @param thickness, l'épaisseur à donner à l'outil. Doit être un nombre réel compris entre 1 et 200 Si la valeur passée est plus petite que 1, la valeur 1 sera
	 *                   donnée à l'épaisseur Si la valeur passée est plus grande que 200, la valeur 200 sera donnée à l'épaisseur
	 */
	public void setThickness(double thickness) {
		this.thickness = thickness;
		Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
	}
	
	class Trait implements ICmd {
		private Image undosave;
		private Image redosave = null;
		private SnapshotParameters params;
		
		public Trait() {
			// configuration des paramètres utilisés pour la sauvegarde du canevas
			params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);

			System.out.println("Snap Trait");

			this.undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
		}
		
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() throws UndoException {
			if (undosave == null) {
				throw new UndoException();
			}

			System.out.println("Trait undo");
			redosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(0,0, Project.getInstance().getDimension().width, Project.getInstance().getDimension().width);
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(undosave, 0, 0);
			undosave = null;
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			System.out.println("Trait redo");
			undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
            Project.getInstance().getCurrentLayer().getGraphicsContext2D().clearRect(0,0, Project.getInstance().getDimension().width, Project.getInstance().getDimension().width);
            Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(redosave, 0, 0);
			redosave = null;
		}

		@Override
		public String toString() {
			return "Drawing Tool";
		}
	}
}
