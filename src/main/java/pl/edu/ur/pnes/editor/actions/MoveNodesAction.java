package pl.edu.ur.pnes.editor.actions;

import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;

import java.util.List;
import java.util.stream.Collectors;

public class MoveNodesAction extends UndoableAction {
    public final VisualizerFacade visualizer;
    public final List<Node> nodes;
    public final double[] offset;

    public MoveNodesAction(VisualizerFacade visualizer, List<Node> nodes, double[] offset) {
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
                    + ") by (%f, %f)".formatted(offset[0], offset[1]);
        else
            return "Move node %s by (%f, %f)".formatted(nodes.get(0), offset[0], offset[1]);
    }

    @Override
    public void undo() {
        for (var node : nodes) {
            double[] position = visualizer.getNodePosition(node);
            position[0] -= offset[0];
            position[1] -= offset[1];
            visualizer.setNodePosition(node, position);
        }
        applied = false;
    }

    @Override
    public void redo() {
        for (var node : nodes) {
            double[] position = visualizer.getNodePosition(node);
            position[0] += offset[0];
            position[1] += offset[1];
            visualizer.setNodePosition(node, position);
        }
        applied = true;
    }
}
