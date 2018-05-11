/*
Author: Adrien
 */
package controller.history;

import utils.UndoException;

public abstract class ICmd {
	
	private static int nbCmd;
	private int id;
	
	public ICmd() {
		id = ++nbCmd;
	}
	
	public int getID() {
		return id;
	}
	
	/**
	 * Execute la Cmd et la sauve dans le RecordCmd
	 */
	public abstract void execute();
	
	/**
	 * Retourne le model à son état précédent l'exécution de la commande
	 */
	public abstract void undo() throws UndoException;
	
	/**
	 * Retourne le model à son état suivant l'exécution de la commande
	 */
	public abstract void redo() throws UndoException;
}

