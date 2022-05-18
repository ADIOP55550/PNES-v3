package pl.edu.ur.pnes.editor.actions;

import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.Net;

public class AddArcAction extends UndoableAction {
    public final Net net;
    public final Arc arc;

    public AddArcAction(Net net, Arc arc) {
        this.net = net;
        this.arc = arc;
    }

    @Override
    public String description() {
        return "Add arc";
    }

    @Override
    public String details() {
        return "Add arc from node %s to node %s".formatted(arc.output.getName(), arc.input.getName());
    }

    @Override
    public void undo() {
        net.removeElement(arc);
        applied = false;
    }

    @Override
    public void redo() {
        apply();
    }

    public void apply() {
        net.addElement(arc);
        applied = true;
    }
}
