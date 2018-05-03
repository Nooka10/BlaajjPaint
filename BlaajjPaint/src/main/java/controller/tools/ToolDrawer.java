/*
Author: Adrien
 */

// parent de la gomme et du pinceau

package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.UndoException;

import java.io.IOException;

/**
 * Classe mère du pinceau et de la gomme, permet aux enfants de réagire au changement de thickness et d'opacité
 *
 * @Author Adrien
 */
public abstract class ToolDrawer extends Tool {
	protected double opacity;
	protected double thickness;
	protected Trait currentTrait;
	
	public ToolDrawer(double thickness, double opacity) {
		this.thickness = thickness;
		this.opacity = opacity;
	}
	
	public void setOpacity(double opacity) {
		this.opacity = opacity;
		onOpacitySet();     // évenement qui est appelé au moment ou l'opacitée est changée
	}
	
	public void setThickness(double thickness) {
		this.thickness = thickness;
		onThicknessSet();   // évènement qu iest appellé au momen tou l'épaisseur est changée
	}
	
	/**
	 * Evènement appelé sur les enfants au moment ou l'opacité est changée Doit être surchargé par les enfants
	 *
	 * @Author Adrien
	 */
	abstract protected void onOpacitySet();
	
	/**
	 * Evènement appelé sur les enfants au moment ou l'épaisseur est changée Doit être surchargé par les enfants
	 *
	 * @Author Adrien
	 */
	abstract protected void onThicknessSet();
	
	
	class Trait implements ICmd {
		private Image undosave;
		private Image redosave = null;
		private SnapshotParameters params;
		
		public Trait() {
			this.undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
			
			// configuration des paramètres utilisés pour la sauvegarde du canevas
			params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
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
			redosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(undosave, 0, 0);
			undosave = null;
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(redosave, 0, 0);
			redosave = null;
		}
	}
}
