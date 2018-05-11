package controller.rightMenu;

import controller.MainViewController;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import utils.UndoException;

public class HistoryController {
	private static int currentUndoID = -1; // -1 sentinelle
	private int id;
	
	@FXML
	private HBox historyElem;
	
	@FXML
	private Label label;
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	void handleMouseClicked(MouseEvent event) {
		if (currentUndoID == -1) {
			RecordCmd.getInstance().getUndoStack().size();
		}
		try {
			if (currentUndoID < id) {
				for (ICmd cmd : RecordCmd.getInstance().getUndoStack()) {
					cmd.undo();
					if (cmd.getID() == id) {
						currentUndoID = id;
						break;
					}
				}
			} else {
				for (ICmd cmd : RecordCmd.getInstance().getRedoStack()) {
					cmd.redo();
					if (cmd.getID() == id) {
						currentUndoID = id;
						break;
					}
				}
			}
		} catch (UndoException e) {
			e.printStackTrace();
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
