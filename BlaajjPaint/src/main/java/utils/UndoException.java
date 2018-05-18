package utils;

/**
 * Classe implémentant l'exceptions levée en cas de problème dans les undos/redos.
 */
public class UndoException extends Exception {
	/**
	 * Constructeur de l'exception levée en cas d'erreur lors des undo/redo.
	 */
	public UndoException() { super(); }
}
