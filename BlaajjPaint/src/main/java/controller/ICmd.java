package controller;

import utils.UndoException;

public interface ICmd {


    /** Execute la Cmd et la sauve dans le RecordCmd*/
    void execute();

    /** Retourne le model à son état précédent l'exécution de la commande */
    void undo() throws UndoException;

    /**Retourne le model à son état suivant l'exécution de la commande */
    void redo() throws UndoException;
}

