package pl.edu.ur.pnes.editor.history;

public abstract class UndoableAction implements Undoable {
    /**
     * Whenever whe action is applied. Upon `undo` should be set to false, on `redo` back to true.
     */
    protected boolean applied = true;
}
