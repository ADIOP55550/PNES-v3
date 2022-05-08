package pl.edu.ur.pnes.editor.history;

public interface Undoable {
    /**
     * @return Vague and short description of the action.
     */
    String description();

    /**
     * Undoes the action.
     */
    void undo();

    /**
     * Redoes the action (assuming: after undoing).
     */
    void redo();
}
