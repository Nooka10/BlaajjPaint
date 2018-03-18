package controller;

import model.RecordCmd;
import utils.UndoException;

public abstract class Cmd {

    /** Sauve la Cmd dans le RecordCmd*/
    public final void save() {
        RecordCmd.getInstance().SaveCmd(this);
    }

    /** Retourne le model à son état précédent l'exécution de la commande */
    public abstract void undo() throws UndoException;

    /**Retourne le model à son état suivant l'exécution de la commande */
    public abstract void redo() throws UndoException;
}

