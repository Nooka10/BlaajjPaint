package model;

import controller.ICmd;
<<<<<<< HEAD
=======
import controller.Cmd;
>>>>>>> UI
import utils.UndoException;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Classe Singleton gérant l'historique des commandes. L'utiliser uniquement avec getInstance(), le constructeur est publique*/
public class RecordCmd {

    /** La taille max de chaque pile */
    private static final int MAX_CMD_HISTORY = 100;

    /** La pile des commandes exécutées */
    private LinkedList<ICmd> undoStack = new LinkedList<ICmd>();

    /** La pile des commandes réexécutables */
    private LinkedList<ICmd> redoStack = new LinkedList<ICmd>();

    /** Le logger qu de cette classe */
    private static final Logger LOG = Logger.getLogger(RecordCmd.class.getName());

    /** L'instance unique de la classe */
    private static RecordCmd instance = null;

    /** Constructeur privé ! */
    private RecordCmd() {}

    /** retourne l'instance unique du singleton RecordCmd. La crée au besoin la première fois. */
    public static RecordCmd getInstance() {

        if(instance == null){
            instance = new RecordCmd();
        }

        return instance;
    }

    /** Apelle la fonction <b>undo()</b> sur la dernière <b>Cmd</b> sauvée.
     * Si aucune <b>Cmd</b> ne se trouves dans la pile il ne fait rien. Si
     * le undo lèves une exception celle-ci est capturée et consignée dans les logs
     * et l'<b>Cmd</b> concernée retourne sur la pile.*/
    public void undo(){

        // si la pile des undo n'est pas vide
        if(!undoStack.isEmpty()){
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

                // vider les deux piles
                undoStack.clear();
                redoStack.clear();

            }
        }
        else {
            LOG.log(Level.INFO, "Nothing to undo.");
        }
    }


    /** Apelle la fonction <b>redo()</b> sur le dernier <b>Cmd</b> de la pile de redo.
     * Si aucune <b>Cmd</b> ne se trouves dans la pile il ne fait rien. Si
     * le redo lèves une exception celle-ci est capturée et consignée dans les logs
     * et l'<b>Cmd</b> concernée retourne sur la pile.*/
    public void redo(){

        // si la pile de redo n'est pas vide
        if(!redoStack.isEmpty()){
            // on essaie de undo
            ICmd cmdToRedo = redoStack.pop();
            try {
                cmdToRedo.redo();
            }
            // Si ça ne passe pas on remet la commande sur la pile de undo
            catch (UndoException e){
                LOG.log(Level.SEVERE, "Can't redo commande !");
                redoStack.push(cmdToRedo);
            }
        }
        else {
            LOG.log(Level.INFO, "Nothing to redo.");

            // vider les deux piles
            undoStack.clear();
            redoStack.clear();
        }
    }

    /** Ajoutes la <b>Cmd</b> sur la pile des commandes
     * @param cmd   la <b>ICmd</b> qui doit être ajoutée à la pile
     */
    public void saveCmd(ICmd cmd){

        if(undoStack.size() > MAX_CMD_HISTORY){
            undoStack.removeLast();
        }
        undoStack.push(cmd);
        redoStack.clear();
    }
}
