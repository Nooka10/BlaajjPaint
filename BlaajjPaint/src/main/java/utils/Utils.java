package utils;

import controller.Layer;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Classe réunissant diverses fontions utiliaires utilisées par de nombreuses classes du projet.
 */
public class Utils {
	private static SnapshotParameters params;
	
	static {
		params = new SnapshotParameters(); // configure les paramètres utilisés pour les snapshot des calques
	}
	
	/**
	 * Permet de faire un snapshot du calque passée en paramètre.
	 *
	 * @param layer,
	 * 		le calque dont on souhaite faire un snapshot.
	 * @param color,
	 * 		la couleur de l'arrière-plan du calque.
	 *
	 * @return le snapshot du calque (sous forme d'image).
	 */
	public static Image makeSnapshot(Layer layer, Color color) {
		return makeSnapshot(layer, color, null);
	}
	
	/**
	 * Permet de faire un snapshot du calque passée en paramètre.
	 *
	 * @param layer,
	 * 		le calque dont on souhaite faire un snapshot.
	 * @param color,
	 * 		la couleur de l'arrière-plan du calque.
	 * @param image,
	 * 		l'image sur laquelle sera enregistré le snapshot.
	 *
	 * @return le snapshot du calque (sous forme d'image).
	 */
	public static WritableImage makeSnapshot(Layer layer, Color color, WritableImage image) {
		params.setFill(color);
		double opacity = layer.getOpacity();
		layer.setOpacity(1);
		boolean isVisible = layer.isVisible();
		layer.setVisible(true);
		WritableImage snapshot = layer.snapshot(params, image);
		layer.setVisible(isVisible);
		layer.setOpacity(opacity);
		return snapshot;
	}
	
	/**
	 * Efface le calque actuellement sélectionné et y dessine le contenu de l'image passée en paramètre.
	 *
	 * @param layer,
	 * 		le calque sur lequel on souhaite dessiner l'image.
	 * @param img,
	 * 		l'image que l0on souhaite dessiner sur le calque.
	 */
	public static void redrawSnapshot(Layer layer, Image img) {
		layer.getGraphicsContext2D().clearRect(0, 0, layer.getWidth(), layer.getHeight()); // efface le contenu du calque actuellement sélectionné
		layer.getGraphicsContext2D().drawImage(img, 0, 0); // redessine le calque avec le contenu d'img
	}
	
	/**
	 * Retourne vrai si la valeur des deux textFields passés en paramètre sont des largeur/hauteur valides pour initialiser un projet, false sinon. Si la largeur et/ou la
	 * hauteur est/sont invalide(s), désactive le bouton validate passé en paramètre.
	 *
	 * @param width,
	 * 		le textField contenant la valeur de la largeur.
	 * @param height,
	 * 		le textField contenant la valeur de la hauteur.
	 * @param validate,
	 * 		le bouton de validation associé.
	 *
	 * @return vrai si la valeur des deux textFields passés en paramètre sont des largeur/hauteur valides pour initialiser un projet, false sinon.
	 */
	public static boolean checkWidthHeightValidity(TextField width, TextField height, Button validate) {
		if (checkTextFieldValueGTZero(width) && checkTextFieldValueGTZero(height)) {
			validate.setDisable(false); // active le bouton validate
			return true; // hauteur et largeur valides -> retourne true
		} else {
			validate.setDisable(true); // désactive le bouton validate
			return false; // hauteur et/ou largeur invalide -> retourne false
		}
	}
	
	/**
	 * Retourne vrai si la valeur du textField passé en paramètre n'est pas vide et que sa valeur est plus grande que 0, false sinon. Désactive le bouton passé en
	 * paramètre si le textField n'est pas valide.
	 *
	 * @param textField,
	 * 		le textField dont on souhaite valider la valeur.
	 * @param validate,
	 * 		le bouton que l'on souhaite verrouiller si la valeur du textField n'est pas valide.
	 *
	 * @return vrai si la valeur du textField passé en paramètre n'est pas vide et que sa valeur est plus grande que 0, false sinon.
	 */
	public static boolean checkTextFieldValueGTZero(TextField textField, Button validate) {
		if (checkTextFieldValueGTZero(textField)) {
			validate.setDisable(false);
			return true;
		} else {
			validate.setDisable(true);
			return false;
		}
	}
	
	/**
	 * Retourne vrai si la valeur du textField passé en paramètre n'est pas vide et que sa valeur est plus grande que 0, false sinon.
	 *
	 * @param textField,
	 * 		le textField dont on souhaite valider la valeur.
	 *
	 * @return vrai si la valeur du textField passé en paramètre n'est pas vide et que sa valeur est plus grande que 0, false sinon.
	 */
	public static boolean checkTextFieldValueGTZero(TextField textField) {
		return !(textField.getText().isEmpty() || Integer.parseInt(textField.getText()) <= 0);
	}
}
