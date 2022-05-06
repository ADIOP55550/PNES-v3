package pl.edu.ur.pnes.petriNet;

import java.util.Objects;

public class ArcPart extends NetElement {
    private final Net net;
    Arc parent;
    Node input;
    Node output;
    boolean isFinal;

    // CONSTRUCTORS

    public ArcPart(Net net, Arc parent, Node input, Node output) {
        super(net);
        this.net = net;
        this.parent = parent;
        this.input = input;
        this.output = output;
        // ArcPart is final when its output is the same as its parents output.
        isFinal = Objects.equals(this.output, parent.output);
    }

    // METHODS


    // GETTERS AND SETTERS

    public Arc getParent() {
        return parent;
    }

    public Node getInput() {
        return input;
    }

    /**
     * Sets this ArcPart input to the given Node and marks node for recalculation if needed
     *
     * @param input new input element
     */
    public void setInput(Node input) {
        this.input = input;
        needsRedraw.set(true);
    }

    public Node getOutput() {
        return output;
    }

    /**
     * Sets this ArcPart output to the given Node and recalculates isFinal and marks node for recalculation if needed
     *
     * @param output new output element
     */
    public void setOutput(Node output) {
        this.output = output;
        isFinal = Objects.equals(this.output, parent.output);
        needsRedraw.set(true);
    }

    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Sets isFinal parameter and marks node for recalculation if needed
     *
     * @param aFinal is node final (does it have an arrow at the end)
     */
    public void setFinal(boolean aFinal) {
        if (aFinal != isFinal)
            needsRedraw.set(true);
        isFinal = aFinal;
    }

}
