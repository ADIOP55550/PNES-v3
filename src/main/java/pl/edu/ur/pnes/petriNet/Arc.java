package pl.edu.ur.pnes.petriNet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * A representation of Net Arc. It can be composed of multiple ArcParts connecting Gizmos.
 * However it has only one input and output.
 */
public class Arc extends NetElement {
    private static int placeCounter = 0;
    public double weight = 0;

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
        while (net.isIdUsed(newId));
        this.setId(newId);
    }
}
