package utils;

import java.lang.Exception;

/**
 * Classe représentant les exceptions levé par les  undos et redos
 */
public class UndoException extends Exception {
    public UndoException() { super(); }
    public UndoException(String message) { super(message); }
    public UndoException(String message, Throwable cause) { super(message, cause); }
    public UndoException(Throwable cause) { super(cause); }
}
