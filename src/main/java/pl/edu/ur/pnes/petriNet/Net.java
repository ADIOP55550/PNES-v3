package pl.edu.ur.pnes.petriNet;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.graphstream.ui.geom.Point3;
import pl.edu.ur.pnes.petriNet.events.NetElementAddedEvent;
import pl.edu.ur.pnes.petriNet.events.NetElementRemovedEvent;
import pl.edu.ur.pnes.petriNet.events.NetEventsHandler;
import pl.edu.ur.pnes.petriNet.simulator.Rules;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFacade;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;

import java.util.*;
import java.util.stream.Stream;

public abstract class Net {
    SimulatorFacade netSimulator;
    VisualizerFacade netVisualizer;

    int lastId = 0;

    Set<String> usedNames = new HashSet<>();

    public boolean isNameUsed(String newId) {
        return this.usedNames.contains(newId);
    }

    public Rules activationRule = Rules.R1;

    List<Arc> arcs = new ArrayList<>();
    List<Place> places = new ArrayList<>();
    List<Transition> transitions = new ArrayList<>();


    NetEventsHandler internalEventsHandler = new NetEventsHandler();

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

    public Stream<Node> getAllNodesStream() {
        return Stream.concat(
                places.stream(),
                transitions.stream()
        );
    }

    public NetEventsHandler getInternalEventsHandler() {
        return internalEventsHandler;
    }

    public void addElements(Collection<NetElement> elements) {
        elements.forEach(this::addElement);
    }

    public void addElements(NetElement... elements) {
        Arrays.stream(elements).forEachOrdered(this::addElement);
    }

    public void addElement(Node element, Point3 position) {
        element.setPosition(position);
        addElement(element);
    }

    public void addElement(NetElement element) {
        if (element instanceof Arc arc) {
            arcs.add(arc);

            if (arc.input == null || arc.output == null)
                return;
            if (arc.input instanceof Place) {
                // Place --- Arc --> Transition
                var input = this.places.stream().filter(place -> Objects.equals(place, arc.input)).findFirst().orElseThrow(() -> new IllegalArgumentException("No place with id " + arc.input.getId() + " found in net. Add it to the net before adding arc " + arc.getId()));
                var output = this.transitions.stream().filter(transition -> Objects.equals(transition, arc.output)).findFirst().orElseThrow(() -> new IllegalArgumentException("No transition with id " + arc.output.getId() + " found in net. Add it to the net before adding arc " + arc.getId()));
                input.outputs.put(output, arc);
                output.inputs.put(input, arc);
            }
            if (arc.input instanceof Transition) {
                // Transition --- Arc --> Place
                var output = this.places.stream()
                        .filter(place -> Objects.equals(place, arc.output))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No place with id " + arc.output.getName() + " found in net. Add it to the net before adding arc " + arc.getName()));
                var input = this.transitions.stream()
                        .filter(transition -> Objects.equals(transition, arc.input))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No transition with id " + arc.input.getName() + " found in net. Add it to the net before adding arc " + arc.getName()));
                input.outputs.put(output, arc);
                output.inputs.put(input, arc);
            }


            return;
        }
        if (element instanceof Place) {
            places.add((Place) element);
            return;
        }
        if (element instanceof Transition) {
            transitions.add((Transition) element);
        }
        this.internalEventsHandler.fireEvent(new NetElementAddedEvent(element));
    }

    public void removeElement(NetElement element) {
        if (this.allElementsStream().anyMatch(v -> Objects.equals(v, element))) {
            if (element instanceof Place) {
                places.remove(element);
                ((Place) element).inputs.forEach((transition, arc) -> this.removeElement(arc));
                ((Place) element).outputs.forEach((transition, arc) -> this.removeElement(arc));
            }
            if (element instanceof Transition) {
                transitions.remove(element);
                ((Transition) element).inputs.forEach((transition, arc) -> this.removeElement(arc));
                ((Transition) element).outputs.forEach((transition, arc) -> this.removeElement(arc));
            }
            if (element instanceof Arc)
                arcs.remove(element);
            this.internalEventsHandler.fireEvent(new NetElementRemovedEvent(element));
        }
    }


    public Stream<Transition> getTransitionsThatCanBeActivated() {
        return getTransitions().stream()
                .filter(t -> activationRule.test(t));
    }

    public Optional<NetElement> getElementById(String id) {
        return allElementsStream().filter(e -> Objects.equals(e.getId(), id)).findAny();
    }

    public <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        internalEventsHandler.addEventHandler(eventType, eventHandler);
    }

    public <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        internalEventsHandler.removeEventHandler(eventType, eventHandler);
    }

    public <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        internalEventsHandler.addEventFilter(eventType, eventFilter);
    }

    public <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        internalEventsHandler.removeEventFilter(eventType, eventFilter);
    }

    public <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        internalEventsHandler.setEventHandler(eventType, eventHandler);
    }

    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return internalEventsHandler.buildEventDispatchChain(tail);
    }

    public void fireEvent(Event event) {
        internalEventsHandler.fireEvent(event);
    }
}
