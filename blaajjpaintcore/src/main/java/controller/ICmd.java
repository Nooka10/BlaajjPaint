package controller;

public interface ICommand {

    /*
     * Applique les modifications au model
     */
    public void execute();

    /*
     * Retourne le model à son état précédent l'exécution de la commande
     */
    public void undo();

    /*
     * Retourne le model à son état suivant l'exécution de la commande
     */
    public void redo();
}
