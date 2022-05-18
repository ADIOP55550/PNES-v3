package pl.edu.ur.pnes.editor.actions;

import javafx.geometry.Point3D;
import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.petriNet.Transition;

import java.lang.reflect.InvocationTargetException;

public class AddNodeAction extends UndoableAction {
    public final Net net;
    public final Point3D position;
    public final Node node;

    public AddNodeAction(Net net, Node node, Point3D position) {
        this.net = net;
        this.node = node;
        this.position = position;
    }

    @Override
    public String description() {
        return "Add %s".formatted(getNodeTypeString(node));
    }

    @Override
    public String details() {
        return "Add %s at (%f, %f)".formatted(getNodeTypeString(node), position.getX(), position.getY());
    }

    private String getNodeTypeString(Node node) {
        if (node instanceof Place)      return "place";
        if (node instanceof Transition) return "transition";
        return "node";
    }

    @Override
    public void undo() {
        net.removeElement(node);
        applied = false;
    }

    @Override
    public void redo() {
        apply();
    }

    public void apply() {
        net.addElement(node, position);
        applied = true;
    }
}
