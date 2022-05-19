package pl.edu.ur.pnes.editor.actions;

import javafx.geometry.Point3D;
import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.editor.history.UndoableWhileRunning;
import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.petriNet.Transition;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;

import java.util.List;
import java.util.stream.Collectors;

@UndoableWhileRunning
public class MoveNodesAction extends UndoableAction {
    public final VisualizerFacade visualizer;
    public final List<Node> nodes;
    public final Point3D offset;

    public MoveNodesAction(VisualizerFacade visualizer, List<Node> nodes, Point3D offset) {
        this.visualizer = visualizer;
        this.nodes = nodes;
        this.offset = offset;
    }

    @Override
    public String description() {
        if (nodes.size() > 1)
            return "Move %d nodes".formatted(nodes.size());
        else
            return "Move node";
    }

    @Override
    public String details() {
        if (nodes.size() > 1)
            return "Move %d nodes (".formatted(nodes.size())
                    + nodes.stream().map(Node::getName).collect(Collectors.joining(", "))
                    + ") by (%f, %f)".formatted(offset.getX(), offset.getY());
        else {
            final var node = nodes.get(0);
            return "Move %s %s by (%f, %f)".formatted(getNodeTypeString(node), node.getName(), offset.getX(), offset.getY());
        }
    }

    private String getNodeTypeString(Node node) {
        if (node instanceof Place)      return "place";
        if (node instanceof Transition) return "transition";
        return "node";
    }

    @Override
    public void undo() {
        for (var node : nodes) {
            visualizer.setNodePosition(node, visualizer.getNodePosition(node).subtract(offset));
        }
        applied = false;
    }

    @Override
    public void redo() {
        for (var node : nodes) {
            visualizer.setNodePosition(node, visualizer.getNodePosition(node).add(offset));
        }
        applied = true;
    }
}
