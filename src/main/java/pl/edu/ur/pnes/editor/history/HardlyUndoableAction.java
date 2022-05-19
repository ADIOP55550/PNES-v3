package pl.edu.ur.pnes.editor.history;

public abstract class HardlyUndoableAction implements Undoable {
    @Override
    public void undo() {
        // TODO: save selection details and stuff; load state; apply the same selection (and stuff).
    }
}
