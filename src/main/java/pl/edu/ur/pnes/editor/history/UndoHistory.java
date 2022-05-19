package pl.edu.ur.pnes.editor.history;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.editor.Mode;
import pl.edu.ur.pnes.editor.Session;

import java.util.ArrayList;

public class UndoHistory {
    private final static Logger logger = LogManager.getLogger();

    ArrayList<Undoable> steps = new ArrayList<>();
    int lastAppliedIndex = -1;
    final Session session;

    public Session getSession() {
        return session;
    }

    public UndoHistory(Session session) {
        this.session = session;
    }

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
            return steps.get(lastAppliedIndex - n + 1);
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
        logger.debug("pushed #%d %s".formatted(steps.size(), step.details()));
        lastAppliedIndex += 1;
    }

    /**
     * Undoes last applied step, following down the history.
     * @return True if anything was undo, false if nothing was possible.
     */
    public boolean undo() {
        final var step = peekUndo();
        if (step == null) return false;
        if (session.mode().get() == Mode.RUN && !step.getClass().isAnnotationPresent(UndoableWhileRunning.class)) {
            logger.debug("couldn't undo #%d %s because is not undoable while running".formatted(lastAppliedIndex + 1, step.description()));
            return false;
        }
        step.undo();
        logger.debug("undid #%d %s".formatted(lastAppliedIndex + 1, step.details()));
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
        if (session.mode().get() == Mode.RUN && !step.getClass().isAnnotationPresent(UndoableWhileRunning.class)) {
            logger.debug("couldn't redo #%d %s because is not undoable while running".formatted(lastAppliedIndex + 2, step.description()));
            return false;
        }
        step.redo();
        lastAppliedIndex += 1;
        logger.debug("redid #%d %s".formatted(lastAppliedIndex + 1, step.details()));
        return true;
    }

    public String dumpToString(int limit) {
        if (steps.size() == 0) {
            return "(history empty)";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = Math.max(0, steps.size() - limit); i < steps.size(); i++) {
            final Undoable step = steps.get(i);
            builder.append('#');
            builder.append(i + 1);
            builder.append('\t');
            builder.append(step.details());
            if (i == lastAppliedIndex) {
                builder.append("\t(current)");
            }
            else {
                builder.append('\t');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
