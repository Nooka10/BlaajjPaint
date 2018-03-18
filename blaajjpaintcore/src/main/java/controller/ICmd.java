package controller;

import utils.UndoException;

public interface ICmd {

    /*
     * Applique les modifications au model
     */
    public void execute();

    /*
     * Retourne le model à son état précédent l'exécution de la commande
     */
    public void undo() throws UndoException;

    /*
     * Retourne le model à son état suivant l'exécution de la commande
     */
    public void redo() throws UndoException;
}

