package controller.history;

import controller.MainViewController;
import controller.rightMenu.RightMenuController;
import utils.UndoException;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe Singleton gérant l'historique des commandes. L'utiliser uniquement avec getCurrentTool(), le constructeur est public
 */
public class RecordCmd {
	
	/**
	 * La taille max de chaque pile
	 */
	private static final int MAX_CMD_HISTORY = 100;
	
	/**
	 * La pile des commandes exécutées
	 */
	private LinkedList<ICmd> undoStack = new LinkedList<>();
	
	/**
	 * La pile des commandes réexécutables
	 */
	private LinkedList<ICmd> redoStack = new LinkedList<>();
	
	/**
	 * Le logger de cette classe
	 */
	private static final Logger LOG = Logger.getLogger(RecordCmd.class.getName());
	
	/**
	 * L'instance unique de la classe
	 */
	private static RecordCmd instance = null;
	
	/**
	 * Constructeur privé (modèle Singleton)
	 */
	private RecordCmd() {
	}
	
	/**
	 * retourne l'instance unique du singleton RecordCmd. La crée au besoin la première fois.
	 */
	public static RecordCmd getInstance() {
		
		if (instance == null) {
			instance = new RecordCmd();
		}
		
		return instance;
	}
	
	/**
	 * Appelle la fonction <b>handleUndo()</b> sur la dernière <b>Cmd</b> sauvée. Si aucune <b>Cmd</b> ne se trouve dans la pile, il ne fait rien.
	 * Si le handleUndo lèves une exception celle-ci est capturée et consignée dans les logs et la <b>Cmd</b> concernée retourne sur la pile.
	 */
	public void undo() {
		// si la pile des handleUndo n'est pas vide
		if (!undoStack.isEmpty()) {
			ICmd cmdToUndo = undoStack.pop();
			try {
				// on essaie de handleUndo
				cmdToUndo.undo();
				
				// si ça a passé on ajoute la commande a la pile des handleRedo
				redoStack.push(cmdToUndo);
				MainViewController.getInstance().getRightMenuController().undoHistory();
			}
			// Si ça ne passe pas on remet la commande sur la pile de handleUndo
			catch (UndoException e) {
				LOG.log(Level.SEVERE, "Can't handleUndo commande !");
				
				// vider les deux piles
				// undoStack.clear();
				// redoStack.clear();
				
			}
		} else {
			LOG.log(Level.INFO, "Nothing to handleUndo.");
		}
	}
	
	public void clearRedo() {
		redoStack.clear();
	}
	
	
	/**
	 * Apelle la fonction <b>handleRedo()</b> sur le dernier <b>Cmd</b> de la pile de handleRedo. Si aucune <b>Cmd</b> ne se trouves dans la pile il ne fait rien. Si le
	 * handleRedo lèves une exception celle-ci est capturée et consignée dans les logs et l'<b>Cmd</b> concernée retourne sur la pile.
	 */
	public void redo() {
		System.out.println("redo");
		// si la pile de handleRedo n'est pas vide
		if (!redoStack.isEmpty()) {
			// on essaie de handleUndo
			ICmd cmdToRedo = redoStack.pop();
			try {
				cmdToRedo.redo();
				undoStack.push(cmdToRedo);
				MainViewController.getInstance().getRightMenuController().addUndoHistory(cmdToRedo);
			}
			// Si ça ne passe pas on remet la commande sur la pile de handleUndo
			catch (UndoException e) {
				LOG.log(Level.SEVERE, "Can't handleRedo commande !");
				//redoStack.push(cmdToRedo);
			}
		} else {
			LOG.log(Level.INFO, "Nothing to handleRedo.");
			
			// vider les deux piles
			//  undoStack.clear();
			//redoStack.clear();
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
		
		MainViewController.getInstance().getRightMenuController().addUndoHistory(cmd);
	}
	
	public LinkedList<ICmd> getUndoStack() {
		return undoStack;
	}
	
	public LinkedList<ICmd> getRedoStack() {
		return redoStack;
	}
	
	/**
	 * Nettoie la classe (remet à 0)
	 */
	public void clear() {
		undoStack.clear();
		redoStack.clear();
		MainViewController.getInstance().getRightMenuController().clearHistoryList();
	}
}
