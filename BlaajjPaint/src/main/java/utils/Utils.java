package utils;

import controller.Layer;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Utils {
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
	
	public static boolean checkWidthHeightValidity(TextField width, TextField height, Button validate) {
		if (width.getText().isEmpty() || height.getText().isEmpty() ||
				Integer.parseInt(width.getText()) == 0 || Integer.parseInt(height.getText()) == 0) {
			validate.setDisable(true);
			return false;
		} else {
			validate.setDisable(false);
			return true;
		}
	}
	
	// TODO: déplacer le snapshot de la gomme ici!
}
