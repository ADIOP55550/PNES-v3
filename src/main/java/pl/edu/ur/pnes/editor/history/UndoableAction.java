package pl.edu.ur.pnes.editor.history;

public abstract class UndoableAction implements Undoable {
    /**
     * Whenever the action is applied. Upon `undo` should be set to false, on `redo` back to true.
     */
    protected boolean applied = false;

    /**
     * @return Detailed description of the action. Defaults to regular (shorter) description.
     */
    public String details() {
        return description();
    }

    /**
     * Marks the undoable action as applied.
     * @return The undoable itself (this), for chaining fancy one-liners.
     */
    public Undoable asApplied() {
        applied = true;
        return this;
    }

    /**
     * Applies the action defined by undoable and marks as applied.
     * Using this method allow to prevent doubling applying code
     * (before adding the action to the history and redo method).
     * @return The undoable itself (this), for chaining fancy one-liners.
     */
    public Undoable andApply() {
        applied = false;
        redo();
        return this;
    }
}
