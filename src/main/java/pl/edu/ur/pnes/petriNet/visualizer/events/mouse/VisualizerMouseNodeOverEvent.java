package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

public class VisualizerMouseNodeOverEvent extends VisualizerMouseEvent {

    public String getClickedNodeId() {
        return clickedNodeId;
    }

    private final String clickedNodeId;


    public VisualizerMouseNodeOverEvent(String clickedNodeId) {
        super(MOUSE_NODE_OVER);
        this.clickedNodeId = clickedNodeId;
    }
}
