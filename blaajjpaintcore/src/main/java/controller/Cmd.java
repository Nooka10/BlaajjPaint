package controller;

import utils.UndoException;

public abstract class ICmd {

    protected static long globalId = 0;

    protected long id = -1;
    protected long previousId = -1;

    /**Applique les modifications au model */
    public final void execute() {
        previousId = globalId;
        id = ++globalId;
    }

    public abstract void

    /** Retourne le model à son état précédent l'exécution de la commande */
    public abstract void undo() throws UndoException;

    /**Retourne le model à son état suivant l'exécution de la commande */
    public abstract void redo() throws UndoException;

    /** Retourne l'id de la commande si elle a été exécutée, sinon -1 */
    public long getId() { return id; }

    /** Retourne l'id de la commande précédent si celle-ci a été exécutée, sinon -1 */
    public long getPreviousId() { return previousId; }

    /** Retourne l'id global de la classe ICmd */
    public static long getGlobalId() { return globalId; }
}

