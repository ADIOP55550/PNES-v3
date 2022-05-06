package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of Net Arc. It can be composed of multiple ArcParts connecting Gizmos.
 * However it has only one input and output.
 */
public class Arc extends NetElement {
    private static int placeCounter = 0;
    private DoubleProperty weight = new SimpleDoubleProperty(1);

    public final List<ArcPart> parts = new ArrayList<>();

    /**
     * Input of the Arc (not arrow end)
     */
    public Node input;

    /**
     * Output of the arc (arrow end)
     */
    public Node output;


    public Arc(Net net, Node input, Node output) {
        super(net);
        this.input = input;
        this.output = output;
        this.parts.add(new ArcPart(net, this, input, output));
        String newId;
        do {
            placeCounter++;
            newId = "a" + placeCounter;
        }
        // increment ids until free one is found
        while (net.isNameUsed(newId));
        this.setName(newId);
    }

    public double getWeight() {
        return weight.get();
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }
}
