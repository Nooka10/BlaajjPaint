package controller.history;

import utils.UndoException;

/**
 * Interface définissant les méthodes d'une commande. Dans notre projet, de nombreuses actions sont enregistrées et stoquées dans l'historique. Ces "sauvegardes"
 * implémentent l'interface <b>ICmd</b> et surchargent les méthodes <b>execute()</b>, <b>undo()</b> et <b>redo()</b> afin qu'on puisse respectivement appliquer, défaire
 * ou réappliquer une modification.
 */
public interface ICmd {
	/**
	 * Exécute la commande et l'enregistre dans le singleton RecordCmd.
	 */
	void execute();
	
	/**
	 * Fait revenir l'objet à l'état dans lequel il était avant l'exécution de la commande.
	 */
	void undo() throws UndoException;
	
	/**
	 * Fait revenir l'objet à l'état dans lequel il était après l'exécution de la commande.
	 */
	void redo() throws UndoException;
	
	/**
	 * Retourne le nom de la commande sous forme de String.
	 *
	 * @return le nom de la commande sous forme de String.
	 */
	String toString();
}

