package pl.edu.ur.pnes.editor;

import pl.edu.ur.pnes.editor.history.UndoHistory;

/**
 * Represents editor session state while working with individual file.
 */
public class Session {
    final public UndoHistory undoHistory = new UndoHistory();

    // TODO: what file we are working with
    // TODO: is file modified and should be saved
}
