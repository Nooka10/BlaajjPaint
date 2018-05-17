package controller.rightMenu;

import controller.MainViewController;
import controller.history.RecordCmd;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Contrôleur associé au fichier FXML History.fxml et gérant l'ensemble des actions associées à une ligne de l'historique des commandes visible en haut à droite de la
 * GUI.
 */
public class HistoryController {
	private int id;
	private static int currentUndoID = -1;
	
	@FXML
	private HBox historyElem;
	
	@FXML
	private Label label;
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur un élément de la liste de l'historique de commandes en haut à droite de la GUI.
	 * Undo (ou redo) jusqu'à ce que le résultat de l'action correspondante à l'élément sur lequel a cliqué l'utilisateur soit visible.
	 */
	@FXML
	void handleOnMouseReleased() {
		if (currentUndoID == -1) {
			currentUndoID = RecordCmd.getInstance().getUndoStack().size() + RecordCmd.getInstance().getRedoStack().size(); // l'id de la dernière commande annulée (undo)
		}
		if (currentUndoID > id) {
			while (currentUndoID != id) {
				RecordCmd.getInstance().undo();
				currentUndoID--;
			}
		} else if (currentUndoID < id) {
			while (currentUndoID != id) {
				RecordCmd.getInstance().redo();
				currentUndoID++;
			}
			MainViewController.getInstance().getRightMenuController().setRedoWaiting(true);
		}
8	}
	
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
