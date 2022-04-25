package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.HashMap;
import java.util.Map;

public class Transition extends Node {
    private static int transitionCounter = 0;
    private final BooleanProperty isReady = new SimpleBooleanProperty(false) {
        @Override
        public void set(boolean b) {
            super.set(b);
            if (b) classesList.add("ready");
            else classesList.remove("ready");
        }
    };

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
    public final BooleanProperty lastActivated = new SimpleBooleanProperty() {
        @Override
        public void set(boolean b) {
            super.set(b);
            if (b) classesList.add("activated");
            else classesList.remove("activated");
        }
    };

    public Transition(Net net) {
        super(net);
        String newId;
        do {
            transitionCounter++;
            newId = "t" + transitionCounter;
        }
        while (net.isIdUsed(newId));
        this.setId(newId);
        classesList.add("transition");
    }

    public BooleanProperty readyProperty() {
        return isReady;
    }

    public boolean isReady() {
        return isReady.get();
    }

    public void setReady(boolean value) {
        isReady.set(value);
    }
}
