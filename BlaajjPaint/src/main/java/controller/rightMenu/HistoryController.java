package controller.rightMenu;

import controller.history.RecordCmd;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class HistoryController {
	private static int currentUndoID = -1; // -1 sentinelle
	private int id;
	
	@FXML
	private HBox historyElem;
	
	@FXML
	private Label label;
	
	/**
	 * Initialise le controlleur. Appelé automatiquement par javaFX lors de la création du FXML.
	 */
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	void handleMouseClicked(MouseEvent event) {
		if (currentUndoID == -1) {
			currentUndoID = RecordCmd.getInstance().getUndoStack().size();
		}
		if (currentUndoID > id) {
			while (currentUndoID != id) {
				RecordCmd.getInstance().undo();
				currentUndoID--;
			}
		} else {
			while (currentUndoID != id) {
				RecordCmd.getInstance().redo();
				currentUndoID++;
			}
		}
	}
	
	public void setLabel(String label) {
		this.label.setText(label);
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void changeLabelOpacity(double opacity) {
		opacity /= 100;
		label.opacityProperty().setValue(opacity);
	}
}
