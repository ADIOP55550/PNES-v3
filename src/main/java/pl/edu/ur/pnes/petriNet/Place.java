package pl.edu.ur.pnes.petriNet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Place extends Node {
    private static int placeCounter = 0;
    private double tokens = 0;

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
        while (net.isIdUsed(newId));
        this.setId(newId);
    }


    @Override
    public List<String> getClasses() {
        return List.of("place");
    }

    double getTokens() {
        return tokens;
    }

    void setTokens(double tokens) {
        this.tokens = tokens;
    }
}
