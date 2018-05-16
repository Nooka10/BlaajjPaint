package controller.menubar;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Utils;

/**
 * Controller associé au fichier FXML ResizeLayer.fxml et controlant l'ensemble des actions associées à la fenêtre de redimensionnement ouvert
 * lorsque l'utilisateur clique sur le menu <b>Calque -> Redimensionner</b>.
 */
public class ResizeLayerController {
	
	@FXML
	private TextField textFieldWidth;
	
	@FXML
	private TextField textFieldHeight;
	
	@FXML
	private CheckBox checkBoxRatio;
	
	@FXML
	private CheckBox checkBoxResizeImage;
	
	@FXML
	private Button validateResizeButton;
	
	@FXML
	public Button cancelButton;
	
	private double ratioImage;
	
	/**
	 * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		double width = Project.getInstance().getCurrentLayer().getWidth();
		double height = Project.getInstance().getCurrentLayer().getHeight();
		
		ratioImage = width / height;
		
		// affiche la taille du calque actuel
		textFieldWidth.setText(Integer.toString((int) width));
		textFieldHeight.setText(Integer.toString((int) height));
		
		// Ajoute un changeListener à textFieldWidth -> la méthode changed() est appelée à chaque fois que le text de textFieldWidth est modifié
		textFieldWidth.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) { // vrai si l'utilisateur n'a pas entré un chiffre
					textFieldWidth.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
				} else if (Utils.checkWidthHeightValidity(textFieldWidth, textFieldHeight, validateResizeButton)) { // vrai si l'utilisateur a entré un chiffre et que la largeur et la hauteur sont des entrées valides
					calculateHeightValue();
				}
			}
		});
		
		// Ajoute un changeListener à textFieldHeight -> la méthode changed() est appelée à chaque fois que le text de textFieldHeight est modifié
		textFieldHeight.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) { // vrai si l'utilisateur n'a pas entré un chiffre
					textFieldHeight.setText(oldValue); // on annule la dernière frappe -> seul les chiffres sont autorisés
				} else {
					Utils.checkWidthHeightValidity(textFieldWidth, textFieldHeight, validateResizeButton); // vrai si l'utilisateur a entré un chiffre et que la largeur et la hauteur sont des entrées valides
				}
			}
		});
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique dans la checkBox "Garder le ratio". Active/désactive le textField Hauteur.
	 */
	public void checkBoxRatioChange() {
		textFieldHeight.setDisable(checkBoxRatio.isSelected());
		
		calculateHeightValue();
	}
	
	/**
	 * Calcule la valeur a afficher dans le champs hauteur en fonction de la largeur, si la checkbox "Garder le ratio" est cochée.
	 */
	private void calculateHeightValue() {
		// vrai si le checkBox "Ajuster l'image" est sélectionné et que textfieldWidth n'est pas vide --> l'utilisateur ne choisi pas la hauteur
		if (checkBoxRatio.isSelected() && !textFieldWidth.getText().isEmpty() && Integer.parseInt(textFieldWidth.getText()) != 0) {
			// la hauteur est calculée à partir du ratio et de la largeur choisie par l'utilisateur
				textFieldHeight.setText(Integer.toString((int) (Math.round(Double.valueOf(textFieldWidth.getText()) / ratioImage))));
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton "Valider".
	 * Effectue le redimensionnement si la largeur et la hauteur entrées sont valides.
	 * Redimensionne tous les calques du projet si la checkbox "Ajuster l'image" est cochée, sinon, ne redimensionne que le calque actuellement sélectionné.
	 * Ferme la fenêtre une fois le redimenssionnement effectué.
	 */
	@FXML
	public void validateResize() {
		if (Utils.checkWidthHeightValidity(textFieldWidth, textFieldHeight, validateResizeButton)) {
			ResizeSave rs = new ResizeSave();
			Layer currentLayer = Project.getInstance().getCurrentLayer();
			
			if (checkBoxResizeImage.isSelected()) {
				Image image = Utils.makeSnapshot(Project.getInstance().getCurrentLayer());
				ImageView image2 = new ImageView(image);
				
				image2.setFitWidth(Double.valueOf(textFieldWidth.getText()));
				image2.setFitHeight(Double.valueOf(textFieldHeight.getText()));
				
				currentLayer.getGraphicsContext2D().clearRect(0, 0, currentLayer.getWidth(), currentLayer.getHeight());
				currentLayer.setWidth(Double.valueOf(textFieldWidth.getText()));
				currentLayer.setHeight(Double.valueOf(textFieldHeight.getText()));
				
				currentLayer.getGraphicsContext2D().drawImage(image2.getImage(), 0, 0, image2.getFitWidth(), image2.getFitHeight());
				
			} else {
				currentLayer.setWidth(Double.valueOf(textFieldWidth.getText()));
				currentLayer.setHeight(Double.valueOf(textFieldHeight.getText()));
			}
			rs.execute();
			
			cancel();
		}
	}
	
	/**
	 * Méthode appelée lrosque l'utilisateur clique sur le bouton "Annuler". Annule le le redimenssionnement et ferme la fenêtre.
	 */
	public void cancel() {
		Stage stage = (Stage) validateResizeButton.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant l'action du redimenssionnement.
	 */
	private class ResizeSave extends ICmd{
		private Layer currentLayer;
		private double oldWidth;
		private double oldHeight;
		private Image oldImage;
		private double newWidth;
		private double newHeight;
		private Image newImage;
		
		/**
		 * Construit une commande sauvegardant l'action du redimenssionnement.
		 */
		private ResizeSave() {
			currentLayer = Project.getInstance().getCurrentLayer();
			oldWidth = currentLayer.getWidth();
			oldHeight = currentLayer.getHeight();
			oldImage = Utils.makeSnapshot(currentLayer);
		}
		
		@Override
		public void execute() {
			newWidth = currentLayer.getWidth();
			newHeight = currentLayer.getHeight();
			newImage = Utils.makeSnapshot(currentLayer);
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			currentLayer.getGraphicsContext2D().clearRect(0, 0, newWidth, newHeight);
			currentLayer.setWidth(oldWidth);
			currentLayer.setHeight(oldHeight);
			currentLayer.getGraphicsContext2D().drawImage(oldImage, 0, 0);
		}
		
		@Override
		public void redo() {
			currentLayer.getGraphicsContext2D().clearRect(0, 0, oldWidth, oldHeight);
			currentLayer.setWidth(newWidth);
			currentLayer.setHeight(newHeight);
			currentLayer.getGraphicsContext2D().drawImage(newImage, 0, 0);
		}
		
		@Override
		public String toString() {
			return "Redimensionnement de " + currentLayer.toString();
		}
		
	}
}