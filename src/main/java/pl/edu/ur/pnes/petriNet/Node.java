package pl.edu.ur.pnes.petriNet;

import org.graphstream.ui.geom.Point3;
import org.jetbrains.annotations.NotNull;

public abstract class Node extends NetElement {
    private Point3 position = new Point3(0, 0, 0);

    /**
     * Returns current position of the Node int the net.
     * Note that the position of the Node in the graph may be different.
     * To get the position from the graph use {@link org.graphstream.graph.Node#getAttribute(String)}
     * @return Point3 representing position of the Node in the net
     */
    public Point3 getPosition() {
        return position;
    }

    /**
     * Sets position of this Node
     * @pnes.CausesRedraw
     * @param position Point3 representing the new node position
     */
    public void setPosition(@NotNull Point3 position) {
        this.position = position;
        this.needsRedraw.set(true);
    }

    Node(Net net) {
        super(net);
    }

    /**
     * Checks if this node can be on the input side of a new Arc going to the other Node.
     *
     * It cannot if:
     * - it is the same Node
     * - it is already connected to this Node (directly)
     * - it is of the same type as output Node (Transition->Transition or Place->Place)
     * @param other the Node this Node is to be connected to
     * @return true if this node can be connected to other Node, false otherwise
     * @throws UnsupportedOperationException if Node is not (yet) meant for connecting
     */
    public boolean canBeConnectedTo(Node other){
        throw new UnsupportedOperationException();
    };
}
