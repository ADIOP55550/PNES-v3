package pl.edu.ur.pnes.petriNet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transition extends Node {
    private static int transitionCounter = 0;


    /**
     * inputs is a HashMap containing all the Transition's input connections in form
     * of key=Place value=Arc (between the Place and the Transition)
     */
    public final Map<Place, Arc> inputs = new HashMap<>();

    /**
     * outputs is a HashMap containing all the Transition's output connections in form
     * of key=Place value=Arc (between the Place and the Transition)
     */
    public final Map<Place, Arc> outputs = new HashMap<>();

    public Transition(Net net) {
        super(net);
        String newId;
        do {
            transitionCounter++;
            newId = "t" + transitionCounter;
        }
        while (net.isIdUsed(newId));
        this.setId(newId);
    }


    @Override
    public List<String> getClasses() {
        return List.of("transition");
    }
}
