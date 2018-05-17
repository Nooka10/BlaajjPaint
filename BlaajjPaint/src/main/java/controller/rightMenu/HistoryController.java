package controller.rightMenu;

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
		if (currentUndoID == -1) { // currentUndoId encore non initialisé
			currentUndoID = RecordCmd.getInstance().getUndoStack().size(); // initialisé à la taille de la undoStack
		}
		
		if (currentUndoID > id) { // l'utilisateur souhaite effectuer des undos
			while (currentUndoID != id) {
				RecordCmd.getInstance().undo();
				currentUndoID--;
			}
		} else if (currentUndoID <= id) { // l'utilisateur souhaite effectuer des redos
			while (currentUndoID <= id) {
				RecordCmd.getInstance().redo();
				currentUndoID++;
			}
		}
	}
	
	/**
	 * Permet d'indiquer au contrôleur le nombre de commandes enregistrées dans la undoStack.
	 * @param nbCmd, le nombre de commandes enregistrées dans la undoStack.
	 */
	void setCurrentUndoId(int nbCmd){
		currentUndoID = nbCmd;
	}
	
	/**
	 * Permet de définir le texte du label.
	 * @param label, le texte à afficher dans le label.
	 */
	public void setLabel(String label) {
		this.label.setText(label);
	}
	
	/**
	 * Permet de définir l'id du label. L'id correspond à son index dans la undoStack.
	 * @param id, l'index de l'élément dans la undoStack.
	 */
	void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Modifie l'opacité du label afin de différencier les label undo-able et les labels redo-able dans la liste de l'historique en haut à droite de la GUI.
	 */
	void setRedoOpacity() {
		label.opacityProperty().setValue(0.6);
	}
}
