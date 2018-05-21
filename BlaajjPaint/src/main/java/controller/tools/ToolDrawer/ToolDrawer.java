package controller.tools.ToolDrawer;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import controller.tools.Tool;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.UndoException;
import utils.Utils;

/**
 * Classe abstraite implémentant un outil permettant de dessiner ou effacer. Classe mère du pinceau et de la gomme.
 */
public abstract class ToolDrawer extends Tool {
	protected int thickness = 2; // l'épaisseur de l'outil
	protected Strike currentStrike; // Le trait actuellement tiré
	
	/**
	 * Permet de régler l'épaisseur de l'outil. Doit être un nombre réel compris entre 1 et 200. Si la valeur passée est plus petite que 1, la valeur 1 sera donnée à
	 * l'épaisseur. Si la valeur passée est plus grande que 200, la valeur 200 sera donnée à l'épaisseur.
	 *
	 * @param thickness,
	 * 		l'épaisseur à donner à l'outil.
	 */
	public void setThickness(int thickness) {
		if (this.thickness > 200) {
			this.thickness = 200;
		} else if (this.thickness < 2) {
			this.thickness = 2;
		} else {
			this.thickness = thickness;
		}
		Project.getInstance().getCurrentLayer().getGraphicsContext2D().setLineWidth(thickness);
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant un trait de pinceau ou de gomme et définissant l'action à effectuer en cas d'appel à undo() ou redo() sur
	 * cette commande.
	 */
	class Strike implements ICmd {
		private Image undosave;
		private Image redosave = null;
		
		/**
		 * Construit une commande sauvegardant un trait de gomme ou de pinceau.
		 */
		Strike() {
			undosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT); // on fait un snapshot du calque
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
			redosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT); // on fait un snapshot du calque
			Utils.redrawSnapshot(Project.getInstance().getCurrentLayer(), undosave); // redessine le snapshot undosave sur le calque actuellement sélectionné
			undosave = null;
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			undosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT); // on fait un snapshot du calque
			Utils.redrawSnapshot(Project.getInstance().getCurrentLayer(), redosave); // redessine le snapshot redosave sur le calque actuellement sélectionné
			redosave = null;
		}
		
		@Override
		public String toString() {
			return "Drawing Tool";
		}
	}
}
