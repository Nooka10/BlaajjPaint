package controller.menubar;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import utils.Utils;

/**
 * Controller associé au fichier FXML Transformations.fxml et controlant l'ensemble des actions associées au sous menu <b>Calque -> Transformations</b>.
 */
public class Transformations {
	
	@FXML
	public Menu transformations;
	@FXML
	private TextField degrees;
	
	/**
	 * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	private abstract class TransformationSave extends ICmd{
		protected Layer currentLayer;
		private Image imageBefore;
		private Image imageAfter;
		private int angleDegree;
		private Point3D axis;
		
		
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
		
		abstract public String toString();
	}
	
	private class RotateSave extends TransformationSave {
		private RotateSave(int angleDegree, Point3D axis){
			super(angleDegree,axis);
		}
		
		public String toString() {
			return "Rotation de " + currentLayer;
		}
	}
	
	private class VerticalSymmetySave extends TransformationSave {
		private VerticalSymmetySave(int angleDegree, Point3D axis){
			super(angleDegree,axis);
		}
		
		public String toString() {
			return "Symétrie verticale de " + currentLayer;
		}
	}
	
	private class HorizontalSymmetrySave extends TransformationSave {
		private HorizontalSymmetrySave(int angleDegree, Point3D axis){
			super(angleDegree,axis);
		}
		public String toString() {
			return "Symétrie horizontale de " + currentLayer;
		}
	}
	
	/**
	 * Admet juste les nombres ainsi que les nombres négatifs pour le degré de la rotation
	 */
	@FXML
	private void initialize() {
		degrees.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
			                    String newValue) {
				if (!newValue.matches("[-]?\\d*")) {
					degrees.setText(oldValue);
				}
			}
		});
	}
	
	
	@FXML
	public void handleValidateRotate(ActionEvent event) {
		if (!degrees.getText().isEmpty()) {
			TransformationSave rs = new RotateSave(Integer.valueOf(degrees.getText()), Rotate.Z_AXIS);
			rs.execute();
		}
	}
	
	@FXML
	public void handleVerticalSymmetry() {
		TransformationSave rs = new VerticalSymmetySave(180, Rotate.X_AXIS);
		rs.execute();
	}
	
	@FXML
	public void handleHorizontalSymmetry() {
		TransformationSave rs = new HorizontalSymmetrySave(180, Rotate.Y_AXIS);
		rs.execute();
	}
}
