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
     * @return Last applied step to undo, or null if no such step could be found.
     */
    public Undoable peekUndo() {
        return peekUndo(1);
    }
    /**
     * @param n How many steps behind peek.
     * @return n-th last applied step to undo, or null if no such step could be found.
     */
    public Undoable peekUndo(int n) {
        try {
            return steps.get(lastAppliedIndex - n);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * @return Next step to redo, or null if no such step could be found.
     */
    public Undoable peekRedo() {
        return peekRedo(1);
    }
    /**
     * @param n How many steps forward peek.
     * @return n-th next (undone) step to redo, or null if no such step could be found.
     */
    public Undoable peekRedo(int n) {
        try {
            return steps.get(lastAppliedIndex + n);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
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
        final var step = peekUndo();
        if (step == null) return false;
        step.undo();
        lastAppliedIndex -= 1;
        return true;
    }

    /**
     * Redoes next undone step, following up the history.
     * @return True if anything was redo, false if nothing was possible.
     */
    public boolean redo() {
        final var step = peekRedo();
        if (step == null) return false;
        step.redo();
        lastAppliedIndex -= 1;
        return true;
    }
}
