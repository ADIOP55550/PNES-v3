package pl.edu.ur.pnes.petriNet;

import org.graphstream.ui.geom.Point3;

public abstract class Node extends NetElement {
    private Point3 position;

    public Point3 getPosition() {
        return position;
    }

    public void setPosition(Point3 position) {
        this.position = position;
    }

    Node(Net net) {
        super(net);
    }
}
