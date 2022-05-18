package pl.edu.ur.pnes.editor.actions;

import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;

import java.util.List;

public class MoveNodesAction extends UndoableAction {
    public final VisualizerFacade visualizer;
    public final List<String> nodesIds;
    public final double[] offset;

    public MoveNodesAction(VisualizerFacade visualizer, List<String> nodesIds, double[] offset) {
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
    public String details() {
        if (nodesIds.size() > 1)
            return "Move %d nodes (".formatted(nodesIds.size())
                    + String.join(", ", nodesIds)
                    + ") by (%f, %f)".formatted(offset[0], offset[1]);
        else
            return "Move node %s by (%f, %f)".formatted(nodesIds.get(0), offset[0], offset[1]);
    }

    @Override
    public void undo() {
        for (var id : nodesIds) {
            double[] position = visualizer.getNodePosition(id);
            position[0] -= offset[0];
            position[1] -= offset[1];
            visualizer.setNodePosition(id, position);
        }
        applied = false;
    }

    @Override
    public void redo() {
        for (var id : nodesIds) {
            double[] position = visualizer.getNodePosition(id);
            position[0] += offset[0];
            position[1] += offset[1];
            visualizer.setNodePosition(id, position);
        }
        applied = true;
    }
}
