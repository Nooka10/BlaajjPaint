package controller.rightMenu;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;

/**
 * Contrôleur associé au fichier FXML Layer.fxml et gérant l'ensemble des actions associées à une ligne de la liste des calques
 * visible en bas à droite de la GUI.
 */
public class LayerController {
	
	@FXML
	private HBox layerElem;
	
	@FXML
	private CheckBox visibility; // la checkbox gérant la visibilité du calque. Cochée, le calque est visible, décochée, le calque est masqué
	
	@FXML
	private Label label;
	
	private Layer layerName; // le nom du calque, affiché dans le label
	
	/**
	 * Permet de définir le background de cet élément de la liste des calques visible en bas à droite de la GUI.
	 * @param background, le background à afficher.
	 */
	void setBackground(Background background) {
		layerElem.setBackground(background);
	}
	
	/**
	 * Permet de définir le nom du calque que doit afficher le label.
	 * @param layerName, le nom du calque à afficher.
	 */
	void setLayerName(Layer layerName) {
		this.layerName = layerName;
		visibility.setSelected(layerName.isVisible());
		label.setText(layerName.toString());
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur un élément de la liste des calques en bas à droite de la GUI.
	 * Définit le calque comme calque actif et adapte le slider et le textField de l'opacité en fonction de l'opacité de ce calque.
	 */
	@FXML
	void handleMouseClicked() {
		Project.getInstance().setCurrentLayer(layerName);
		MainViewController.getInstance().getRightMenuController().setOpacitySlider(layerName.getLayerOpacity()); // on règle le slider de l'opacité
		MainViewController.getInstance().getRightMenuController().setOpacityTextField(String.valueOf(layerName.getLayerOpacity())); // on règle le textfield de l'opacité
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur la checkbox d'un élément de la liste des calques en bas à droite de la GUI.
	 * Affiche le calque si l'utilisateur coche la checkbox, le masque s'il la décoche.
	 */
	@FXML
	void handleVisibilityChange() {
		layerName.setVisible(visibility.isSelected());
		Project.getInstance().drawWorkspace(); // un calque a été masqué/démasqué, on redessine l'espace de travail.
	}
}
