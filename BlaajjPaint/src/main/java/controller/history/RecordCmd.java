package controller.history;

import controller.MainViewController;
import utils.UndoException;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe implémentant le modèle Singleton et gèrant l'historique des commandes.
 */
public class RecordCmd {
	
	private static final int MAX_CMD_HISTORY = 100; // taille maximale des piles undo et redo
	
	private LinkedList<ICmd> undoStack = new LinkedList<>(); // pile des commandes exécutées
	
	private LinkedList<ICmd> redoStack = new LinkedList<>(); // pile des commandes ré-exécutables
	
	private static final Logger LOG = Logger.getLogger(RecordCmd.class.getName()); // Le logger de cette classe
	
	private static RecordCmd recordCmdInstance; // L'instance unique du singleton RecordCmd
	
	/**
	 * Constructeur privé (modèle Singleton).
	 */
	private RecordCmd() {
	}
	
	/**
	 * Retourne l'instance unique du singleton RecordCmd. La crée si elle n'existe pas déjà.
	 * @return l'instance unique du singleton RecordCmd.
	 */
	public static RecordCmd getInstance() {
		if (recordCmdInstance == null) {
			recordCmdInstance = new RecordCmd();
		}
		return recordCmdInstance;
	}
	
	/**
	 * Appelle la fonction <b>undo()</b> sur la dernière <b>Cmd</b> sauvée. Si aucune <b>Cmd</b> ne se trouve dans la pile, il ne fait rien. Si le handleUndo lèves
	 * une exception celle-ci est capturée et consignée dans les logs et la <b>Cmd</b> concernée retourne sur la pile.
	 */
	public void undo() {
		if (!undoStack.isEmpty()) { // si la pile des handleUndo n'est pas vide
			ICmd cmdToUndo = undoStack.pop();
			try {
				// on essaie de handleUndo
				cmdToUndo.undo();
				
				// si ça a passé on ajoute la commande a la pile des handleRedo
				redoStack.push(cmdToUndo);
				
				// on met à jour la liste de l'historique
				MainViewController.getInstance().getRightMenuController().updateHistoryList();
			} catch (UndoException e) { // Si ça ne passe pas
				LOG.log(Level.SEVERE, "Can't handleUndo commande !");
			}
		} else {
			LOG.log(Level.INFO, "Nothing to handleUndo.");
		}
	}
	
	/**
	 * Apelle la fonction <b>redo()</b> sur le dernier <b>Cmd</b> de la pile de handleRedo. Si aucune <b>Cmd</b> ne se trouves dans la pile il ne fait rien. Si le
	 * handleRedo lèves une exception celle-ci est capturée et consignée dans les logs et l'<b>Cmd</b> concernée retourne sur la pile.
	 */
	public void redo() {
		// si la pile de handleRedo n'est pas vide
		if (!redoStack.isEmpty()) {
			// on essaie de handleUndo
			ICmd cmdToRedo = redoStack.pop();
			try {
				cmdToRedo.redo();
				undoStack.push(cmdToRedo);
				MainViewController.getInstance().getRightMenuController().updateHistoryList();
			}
			// Si ça ne passe pas on remet la commande sur la pile de handleUndo
			catch (UndoException e) {
				LOG.log(Level.SEVERE, "Can't handleRedo commande !");
				//redoStack.push(cmdToRedo);
			}
		} else {
			LOG.log(Level.INFO, "Nothing to handleRedo.");
		}
	}
	
	/**
	 * Ajoutes la <b>Cmd</b> sur la pile des commandes
	 *
	 * @param cmd la <b>ICmd</b> qui doit être ajoutée à la pile
	 */
	public void saveCmd(ICmd cmd) {
		if (undoStack.size() > MAX_CMD_HISTORY) {
			undoStack.removeLast();
		}
		
		undoStack.push(cmd);
		redoStack.clear();
		MainViewController.getInstance().getRightMenuController().updateHistoryList();
	}
	
	/**
	 * Retourne la pile contenant, au plus, les MAX_CMD_HISTORY dernières commandes enregistrées pouvant être annulées.
	 * @return la pile des dernières commandes enregistrées pouvant être annulées.
	 */
	public LinkedList<ICmd> getUndoStack() {
		return undoStack;
	}
	
	/**
	 * Retourne la pile contenant, au plus, les MAX_CMD_HISTORY dernières commandes ayant été annulées (undo).
	 * @return la pile des dernières commandes ayant été annulées (undo).
	 */
	public LinkedList<ICmd> getRedoStack() {
		return redoStack;
	}
	
	/**
	 * Réinitialise l'état de la classe.
	 */
	public void clear() {
		undoStack.clear();
		redoStack.clear();
		MainViewController.getInstance().getRightMenuController().clearHistoryList(); // vide l'historique sans le redessiner
	}
}
