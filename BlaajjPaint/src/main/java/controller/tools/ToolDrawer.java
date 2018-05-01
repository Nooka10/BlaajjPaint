/*
Author: Adrien
 */

// parent de la gomme et du pinceau

package controller.tools;

import controller.Project;
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
	
	
	protected Image undosave;
	protected Image redosave = null;
	protected SnapshotParameters params;
	
	public ToolDrawer(double thickness, double opacity) {
		this.thickness = thickness;
		this.opacity = opacity;
		
		// configuration des paramètres utilisés pour la sauvegarde du canevas
		params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
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
	
	protected void undoRedo(Image undo, Image redo) throws UndoException {
		if (undo == null) {
			// exécute le snapshot de l'état actuel du canvas
			undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
			
			//throw new UndoException();
		}
		redo = Project.getInstance().getCurrentLayer().snapshot(params, null);
		GraphicsContext gc = Project.getInstance().getCurrentLayer().getGraphicsContext2D();
		gc.drawImage(undo, 0, 0);
		undo = null;
	}
}
