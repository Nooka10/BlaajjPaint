package controller.history;

import utils.UndoException;

/**
 *
 */
public abstract class ICmd {
	
	private static int nbCmd; // compteur de toutes les commandes créées depuis le lancement du programme
	private int id; // id unique à chaque commande
	
	/**
	 * Constructeur. Construit une commande et lui attribue son id unique
	 */
	public ICmd() {
		id = ++nbCmd;
	}
	
	/**
	 * Retourne l'id de la commande
	 * @return un entier, l'id de la commande
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Exécute la Cmd et l'enregistre dans le singleton RecordCmd
	 */
	public abstract void execute();
	
	/**
	 * Fait revenir l'objet à l'état dans lequel il était avant l'exécution de la commande
	 */
	public abstract void undo() throws UndoException;
	
	/**
	 * Fait revenir l'objet à l'état dans lequel il était après l'exécution de la commande
	 */
	public abstract void redo() throws UndoException;
}

