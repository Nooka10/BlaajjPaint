package utils;

import controller.Layer;
import controller.Project;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Classe réunissant des outils pour les snapshot et des validations etc.
 */
public class Utils {
	private static SnapshotParameters params;
	
	static {
		// configuration des paramètres utilisés pour la sauvegarde du canevas
		params = new SnapshotParameters();
	}
	
	public static Image makeSnapshot(Layer layer, Color color){
		return makeSnapshot(layer, color, null);
	}
	
	public static WritableImage makeSnapshot(Layer layer, Color color, WritableImage image){
		params.setFill(color);
		double opacity = layer.getOpacity();
		layer.setOpacity(1);
		WritableImage snapshot = layer.snapshot(params, image);
		layer.setOpacity(opacity);
		return snapshot;
	}
	
	/**
	 * Efface le calque actuellement sélectionné et y dessine le contenu de l'image passée en paramètre.
	 * @param img, l'image à redessiner sur le calque actuellement sélectionné.
	 */
	public static void redrawSnapshot(Layer layer, Image img){
		// efface le contenu du calque actuellement sélectionné
		layer.getGraphicsContext2D().clearRect(0, 0, layer.getWidth(), layer.getHeight());
		layer.getGraphicsContext2D().drawImage(img, 0, 0); // redessine le calque avec le contenu d'img
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
	
	public static boolean checkTextFieldValueGTZero(TextField textField, Button validate) {
		if (textField.getText().isEmpty() || Integer.parseInt(textField.getText()) == 0) {
			validate.setDisable(true);
			return false;
		} else {
			validate.setDisable(false);
			return true;
		}
	}
	
	public static boolean checkTextFieldValueGTZero(TextField textField) {
		return !(textField.getText().isEmpty() || Integer.parseInt(textField.getText()) == 0);
	}
}
