package utils;

import controller.Layer;
import controller.Project;
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
		double opacity = Project.getInstance().getCurrentLayer().getOpacity();
		Project.getInstance().getCurrentLayer().setOpacity(1);
		Image snapshot = Project.getInstance().getCurrentLayer().snapshot(params, null);
		Project.getInstance().getCurrentLayer().setOpacity(opacity);
		return snapshot;
	}
}
