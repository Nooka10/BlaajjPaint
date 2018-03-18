package model;

import controller.ICmd;
import utils.UndoException;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordCmd {

    /** La taille max de chaque pile */
    private static final int MAX_CMD_HISTORY = 100;

    /** La pile des commandes exécutées */
    private Stack<ICmd> undoStack = new Stack<ICmd>();

    /** La pile des commandes réexécutables */
    private Stack<ICmd> redoStack = new Stack<ICmd>();

    /** Le logger qu de cette classe */
    private static final Logger LOG = Logger.getLogger(RecordCmd.class.getName());

    public void undo(){

        // si la pile des undo n'est pas vide
        if(!undoStack.empty()){
            ICmd cmdToUndo = undoStack.pop();
            try {

                // on essaie de undo
                cmdToUndo.undo();

                // si ça a passé on ajoute la commande a la pile des redo
                redoStack.push(cmdToUndo);
            }
            // Si ça ne passe pas on remet la commande sur la pile de undo
            catch (UndoException e){
                LOG.log(Level.SEVERE, "Can't undo commande !");
                undoStack.push(cmdToUndo);
            }
        }
        else {
            LOG.log(Level.INFO, "Nothing to undo.");
        }
    }

    public void redo(){
        ICmd cmdToUndo = undoStack.pop();

        if(cmdToUndo != null){
            // on essaie de undo
            try {
                cmdToUndo.undo();
            }
            // Si ça ne passe pas on remet la commande sur la pile de undo
            catch (UndoException e){
                LOG.log(Level.SEVERE, "Can't undo commande !");
                undoStack.push(cmdToUndo);
            }
        }
        else {
            LOG.log(Level.INFO, "Nothing to undo.");
        }
    }

    /*
        Ajoutes la commande sur la undoStack
     */
    public void SaveCmd(ICmd cmd){
        undoStack.push(cmd);
    }
}
