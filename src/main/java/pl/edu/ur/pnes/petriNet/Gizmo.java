package pl.edu.ur.pnes.petriNet;

/**
 * It is a helper class representing invisible Node. It allows for ArcParts to be routed
 * and angled however user wishes. It serves no logic purpose.
 */
public class Gizmo extends Node {
    private static int gizmoCounter = 0;

    Gizmo(Net net) {
        super(net);
        String newId;
        do {
            gizmoCounter++;
            newId = "g" + gizmoCounter;
        }
        while (net.isNameUsed(newId));
        this.setName(newId);
    }
}
