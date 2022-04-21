package pl.edu.ur.pnes.petriNet;

import pl.edu.ur.pnes.petriNet.simulator.SimulatorFacade;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;

import java.util.*;
import java.util.stream.Stream;

public abstract class Net {
    SimulatorFacade netSimulator;
    VisualizerFacade netVisualizer;

    Set<String> usedIds = new HashSet<>();


    public boolean isIdUsed(String newId) {
        return this.usedIds.contains(newId);
    }

    public Rules activationRule = Rules.R1;

    List<Arc> arcs = new ArrayList<>();
    List<Place> places = new ArrayList<>();
    List<Transition> transitions = new ArrayList<>();
    Queue<NetElement> toBeRedrawn = new ArrayDeque<>();

    public List<Arc> getArcs() {
        return arcs;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public Stream<NetElement> allElementsStream() {
        return Stream.concat(
                arcs.stream(),
                Stream.concat(
                        places.stream(),
                        transitions.stream()
                )
        );
    }

    public Stream<Node> allNodesStream() {
        return Stream.concat(
                places.stream(),
                transitions.stream()
        );
    }

    public void markForRedraw(NetElement element) {
        if (toBeRedrawn.contains(element))
            return;
        toBeRedrawn.add(element);
    }

    public void addElement(NetElement element) {
        if (element instanceof Arc) {
            arcs.add((Arc) element);
            return;
        }
        if (element instanceof Place) {
            places.add((Place) element);
            return;
        }
        if (element instanceof Transition) {
            transitions.add((Transition) element);
//            return;
        }
    }
}
