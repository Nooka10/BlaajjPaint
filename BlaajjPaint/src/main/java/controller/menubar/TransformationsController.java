package controller.menubar;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import utils.Utils;

/**
 * contrôleur associé au fichier FXML TransformationsController.fxml et gérant l'ensemble des actions associées au sous-menu <b>Calque -> Transformations</b>.
 */
public class TransformationsController {
	
	@FXML
	public Menu transformations;
	@FXML
	private Button ValidateButton;
	@FXML
	private TextField degreesTextField;
	
	/**
	 * Initialise le contrôleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	@FXML
	private void initialize() {
		// Ajoute un changeListener à degreesTextField -> la méthode changed() est appelée à chaque fois que le texte de degreesTextField est modifié
		degreesTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.isEmpty()) {
					try {
						if (!degreesTextField.getText().equals("-")) {
							int value = Integer.parseInt(newValue); // parse en int ou lance une NumberFormatException si le parsing n'est pas possible
							if (value == 0 || value >= 360 || value <= -360) {
								ValidateButton.setDisable(true); // l'entrée est invalide, on désactive le bouton valider
							} else {
								ValidateButton.setDisable(false); // l'entrée est valide, on réactive le bouton valider
							}
						}
					} catch (NumberFormatException e) { // une erreur s'est produite pendant le parsing en int -> l'entrée est invalide
						degreesTextField.setText(oldValue); // on annule la dernière modification
						ValidateButton.setDisable(true); // l'entrée est invalide, on désactive le bouton valider
					}
				}
			}
		});
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Transformations -> Rotation -> Valider</b>. Effectue la rotation du calque actuellement
	 * sélectionné selon l'angle entré par l'utilisateur.
	 */
	@FXML
	public void handleValidateRotate() {
		if (!degreesTextField.getText().isEmpty()) {
			TransformationSave rs = new RotateSave(Integer.valueOf(degreesTextField.getText()), Rotate.Z_AXIS);
			rs.execute();
		}
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Transformations -> Symétrie verticale</b>. Effectue la symétrie du calque actuellement
	 * sélectionné selon l'axe horizontal.
	 */
	@FXML
	public void handleVerticalSymmetry() {
		TransformationSave rs = new VerticalSymmetrySave(180, Rotate.X_AXIS);
		rs.execute();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu <b>Calque -> Transformations -> Symétrie horizontale</b>. Effectue la symétrie du calque actuellement
	 * sélectionné selon l'axe vertical.
	 */
	@FXML
	public void handleHorizontalSymmetry() {
		TransformationSave rs = new HorizontalSymmetrySave(180, Rotate.Y_AXIS);
		rs.execute();
	}
	
	/**
	 * Classe abstraite implémentant une commande sauvegardant l'action d'un des sous-menu du menu <b>Calque -> Transformations</b>.
	 * Classe mère des sous-classes RotateSave, VerticalSymmetrySave et HorizontalSymmetrySave.
	 */
	private abstract class TransformationSave implements ICmd{
		protected Layer currentLayer;
		private Image imageBefore;
		private Image imageAfter;
		private int angleDegree;
		private Point3D axis;
		
		/**
		 * Construit une commande sauvegardant l'action d'un des sous-menu du menu <b>Calque -> Transformations</b>.
		 * @param angleDegree, l'angle appliqué à la transformation.
		 * @param axis, l'axe selon lequel est appliqué la transformation.
		 */
		private TransformationSave(int angleDegree, Point3D axis){
			currentLayer = Project.getInstance().getCurrentLayer();
			imageBefore = Utils.makeSnapshot(currentLayer);
			this.angleDegree = angleDegree;
			this.axis = axis;
		}
		
		@Override
		public void execute() {
			currentLayer.getGraphicsContext2D().clearRect(0, 0, currentLayer.getWidth(), currentLayer.getHeight());
			currentLayer.getGraphicsContext2D().save();
			Rotate r = new Rotate(angleDegree, currentLayer.getWidth() / 2, currentLayer.getHeight() / 2, 0, axis);
			currentLayer.getGraphicsContext2D().setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
			currentLayer.getGraphicsContext2D().drawImage(imageBefore, 0, 0);
			currentLayer.getGraphicsContext2D().restore();
			imageAfter = Utils.makeSnapshot(currentLayer);
			
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			currentLayer.getGraphicsContext2D().clearRect(0, 0, currentLayer.getWidth(), currentLayer.getHeight());
			currentLayer.getGraphicsContext2D().drawImage(imageBefore, 0, 0);
		}
		
		@Override
		public void redo() {
			currentLayer.getGraphicsContext2D().clearRect(0, 0, currentLayer.getWidth(), currentLayer.getHeight());
			currentLayer.getGraphicsContext2D().drawImage(imageAfter, 0, 0);
		}
	}
	
	/**
	 * Classe implémentant une commande sauvegardant l'action du menu <b>Calque -> Transformations -> Rotation -> Valider</b>.
	 * Effectue la rotation du calque actuellement sélectionné selon l'angle et l'axe fournis.
	 */
	private class RotateSave extends TransformationSave {
		/**
		 * Construit une commande sauvegardant l'action du menu <b>Calque -> Transformations -> Rotation -> Valider</b>.
		 * @param angleDegree, l'angle appliqué à la rotation.
		 * @param axis, l'axe selon lequel est appliqué la transformation.
		 */
		private RotateSave(int angleDegree, Point3D axis){
			super(angleDegree,axis);
		}
		
		@Override
		public String toString() {
			return "Rotation de " + currentLayer;
		}
	}
	
	/**
	 * Classe implémentant une commande sauvegardant l'action du menu <b>Calque -> Transformations -> Symétrie verticale</b>.
	 * Effectue la symétrie du calque actuellement sélectionné selon l'axe horizontal (axe des X).
	 */
	private class VerticalSymmetrySave extends TransformationSave {
		/**
		 * Construit une commande sauvegardant 'action du menu <b>Calque -> Transformations -> Symétrie verticale</b>.
		 * @param angleDegree, l'angle appliqué à la rotation.
		 * @param axis, l'axe selon lequel est appliqué la transformation.
		 */
		private VerticalSymmetrySave(int angleDegree, Point3D axis){
			super(angleDegree,axis);
		}
		
		@Override
		public String toString() {
			return "Symétrie verticale de " + currentLayer;
		}
	}
	
	/**
	 * Classe implémentant une commande sauvegardant l'action du menu <b>Calque -> Transformations -> Symétrie horizontale</b>.
	 * Effectue la symétrie du calque actuellement sélectionné selon l'axe horizontal (axe des X).
	 */
	private class HorizontalSymmetrySave extends TransformationSave {
		/**
		 * Construit une commande sauvegardant 'action du menu <b>Calque -> Transformations -> Symétrie horizontale</b>.
		 * @param angleDegree, l'angle appliqué à la rotation.
		 * @param axis, l'axe selon lequel est appliqué la transformation.
		 */
		private HorizontalSymmetrySave(int angleDegree, Point3D axis){
			super(angleDegree,axis);
		}
		
		@Override
		public String toString() {
			return "Symétrie horizontale de " + currentLayer;
		}
	}
}
