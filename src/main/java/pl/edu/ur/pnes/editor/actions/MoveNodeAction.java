package pl.edu.ur.pnes.editor.actions;

import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;
import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;

import java.util.List;

public class MoveNodeAction extends UndoableAction {
    public final VisualizerFacade visualizer;
    public final List<String> nodesIds;
    public final double[] offset;

    public MoveNodeAction(VisualizerFacade visualizer, List<String> nodesIds, double[] offset) {
        this.visualizer = visualizer;
        this.nodesIds = nodesIds;
        this.offset = offset;
    }

    @Override
    public String description() {
        if (nodesIds.size() > 1)
            return "Move %d nodes".formatted(nodesIds.size());
        else
            return "Move node";
    }

    @Override
    public void undo() {
        for (var id : nodesIds) {
            double[] position = visualizer.getNodePosition(id);
            position[0] -= offset[0];
            position[1] -= offset[2];
            visualizer.setNodePosition(id, position);
        }
        applied = false;
    }

    @Override
    public void redo() {
        for (var id : nodesIds) {
            double[] position = visualizer.getNodePosition(id);
            position[0] += offset[0];
            position[1] += offset[2];
            visualizer.setNodePosition(id, position);
        }
        applied = true;
    }
}
