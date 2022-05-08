package pl.edu.ur.pnes.editor.actions;

import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;
import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;

public class MoveNodeAction extends UndoableAction {
    public final VisualizerFacade visualizer;
    public final Node[] nodes;
    public final double[] offset;

    private double[] getNodePosition(Node node) {
        return GraphPosLengthUtils.nodePosition(visualizer.getGraph(), node.getId());
    }
    private void setNodePosition(Node node, final double[] position) {
        GraphPosLengthUtils.nodePosition(visualizer.getGraph(), node.getId(), position);
    }

    public MoveNodeAction(VisualizerFacade visualizer, Node[] nodes, double[] offset) {
        this.visualizer = visualizer;
        this.nodes = nodes;
        this.offset = offset;
    }

    @Override
    public String description() {
        if (nodes.length > 1)
            return "Move %d nodes".formatted(nodes.length);
        else
            return "Move node";
    }

    @Override
    public void undo() {
        for (var node : nodes) {
            double[] position = getNodePosition(node);
            position[0] -= offset[0];
            position[1] -= offset[2];
            setNodePosition(node, position);
        }
        applied = false;
    }

    @Override
    public void redo() {
        for (var node : nodes) {
            double[] position = getNodePosition(node);
            position[0] += offset[0];
            position[1] += offset[2];
            setNodePosition(node, position);
        }
        applied = true;
    }
}
