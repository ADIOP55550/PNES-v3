package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Place extends Node {
    private static int placeCounter = 0;
    private final DoubleProperty tokens = new SimpleDoubleProperty(0);
    private final DoubleProperty capacity = new SimpleDoubleProperty(Double.MAX_VALUE);

    /**
     * inputs is a HashMap containing all the Place's input connections in form
     * of key=Transition value=Arc (between the Place and the Transition)
     */
    public final Map<Transition, Arc> inputs = new HashMap<>();

    /**
     * outputs is a HashMap containing all the Place's output connections in form
     * of key=Transition value=Arc (between the Place and the Transition)
     */
    public final Map<Transition, Arc> outputs = new HashMap<>();

    public Place(Net net) {
        super(net);
        String newId;
        do {
            placeCounter++;
            newId = "p" + placeCounter;
        }
        // increment ids until free one is found
        while (net.isNameUsed(newId));
        this.setName(newId);
    }


    @Override
    public List<String> getClasses() {
        return List.of("place");
    }

    public double getTokens() {
        return tokens.get();
    }

    public DoubleProperty tokensProperty() {
        return tokens;
    }

    public void setTokens(double tokens) {
        this.tokens.set(tokens);
    }

    public double getCapacity() {
        return capacity.get();
    }

    public DoubleProperty capacityProperty() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity.set(capacity);
    }

}
