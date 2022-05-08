package pl.edu.ur.pnes.editor.history;

import java.util.ArrayList;

public class UndoHistory {
    ArrayList<Undoable> steps = new ArrayList<>();
    int lastAppliedIndex = 0;

    /**
     * @return How many steps can be undone.
     */
    public int countUndo() {
        return steps.size();
    }

    /**
     *
     * @return How many steps can be redone. Should be positive only after undoing some.
     */
    public int countRedo() {
        return lastAppliedIndex - steps.size() + 1;
    }

    /**
     * @return Last applied step to undo.
     * @throws IndexOutOfBoundsException - if there is no step to undo.
     */
    public Undoable peekUndo() {
        return peekUndo(1);
    }
    /**
     * @param n How many steps behind peek.
     * @return n-th last applied step to undo.
     * @throws IndexOutOfBoundsException - if there is not enough steps.
     */
    public Undoable peekUndo(int n) {
        return steps.get(lastAppliedIndex - n);
    }

    /**
     * @return Next step to redo.
     * @throws IndexOutOfBoundsException - if there is no step to redo.
     */
    public Undoable peekRedo() {
        return peekRedo(1);
    }
    /**
     * @param n How many steps forward peek.
     * @return n-th next (undone) step to redo.
     * @throws IndexOutOfBoundsException - if there is not enough steps to redo.
     */
    public Undoable peekRedo(int n) {
        return steps.get(lastAppliedIndex + n);
    }

    /**
     * Pushes next step into the history. If there was anything possible to redo, it gets forgotten.
     * @param step Step that can be undone.
     */
    public void push(Undoable step) {
        for (int i = steps.size() - 1; lastAppliedIndex < i; i--) {
            steps.remove(i);
        }
        steps.add(step);
        lastAppliedIndex += 1;
    }

    /**
     * Undoes last applied step, following down the history.
     * @return True if anything was undo, false if nothing was possible.
     */
    public boolean undo() {
        if (!steps.isEmpty()) return false;

        final var step = peekUndo();
        step.undo();
        lastAppliedIndex -= 1;
        return true;
    }
}
