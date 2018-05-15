package utils;

import controller.Layer;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SnapshotMaker {
	private static SnapshotParameters params;
	
	static {
		// configuration des paramètres utilisés pour la sauvegarde du canevas
		params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
	}
	
	public static Image makeSnapshot(Layer layer){
		double opacity = layer.getOpacity();
		layer.setOpacity(1);
		Image snapshot = layer.snapshot(params, null);
		layer.setOpacity(opacity);
		return snapshot;
	}
	
	// TODO: déplacer le snapshot de la gomme ici!
}
