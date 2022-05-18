package pl.edu.ur.pnes.petriNet.events;

import javafx.geometry.Point3D;
import pl.edu.ur.pnes.petriNet.Node;

public class NodesMovedEvent extends NetEvent {
    public final Node[] nodes;
    public final Point3D offset;

    public NodesMovedEvent(Node[] nodes, Point3D offset) {
        super(NODES_MOVED);
        this.nodes = nodes;
        this.offset = offset;
    }
}
