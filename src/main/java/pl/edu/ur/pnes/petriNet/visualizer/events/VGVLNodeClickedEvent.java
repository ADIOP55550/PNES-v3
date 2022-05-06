package pl.edu.ur.pnes.petriNet.visualizer.events;

import javafx.event.EventType;

public class VGVLNodeClickedEvent extends VGVLEvent {

    public String getClickedNodeId() {
        return clickedNodeId;
    }

    private final String clickedNodeId;


    public VGVLNodeClickedEvent(String clickedNodeId) {
        super(NODE_CLICKED);
        this.clickedNodeId = clickedNodeId;
    }
}
