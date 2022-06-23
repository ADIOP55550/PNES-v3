package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import pl.edu.ur.pnes.petriNet.events.*;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.UsedInNetType;
import pl.edu.ur.pnes.petriNet.simulator.Rules;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Net {
    private static final Logger logger = LogManager.getLogger(Net.class);

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

    {
        internalEventsHandler.addEventFilter(NetEvent.ANY, event -> {
            logger.debug("NET EVENT: {}", event.getEventType().getName());
        });
    }

    /*
    so called S1/S2 rule
        S1 - keeps input values
        S2 - removes input values (set 0)
     */
    @UsedInNetType(NetType.FPN)
    private Boolean removeInputsOnTransitionFire = true;

    /**
     * Whether to remove input values on transition firing
     *
     * @return Whether to remove input values on transition firing
     * @see #removeInputsOnTransitionFire
     */
    public Boolean getRemoveInputsOnTransitionFire() {
        return removeInputsOnTransitionFire;
    }

    /**
     * Sets whether to remove input values on transition firing
     *
     * @param removeInputsOnTransitionFire Whether to remove input values on transition firing
     * @see #removeInputsOnTransitionFire
     */
    public void setRemoveInputsOnTransitionFire(Boolean removeInputsOnTransitionFire) {
        this.removeInputsOnTransitionFire = removeInputsOnTransitionFire;
    }

    /**
     * Current NetType, fires {@link NetTypeChangedEvent} on change
     */
    private final ObjectProperty<NetType> netType = new SimpleObjectProperty<>(NetType.PN) {
        @Override
        public void set(NetType newValue) {
            super.set(newValue);
            fireEvent(new NetTypeChangedEvent(newValue, newValue.netGroup));
        }
    };

    /**
     * Get current net type
     *
     * @return current net type
     */
    public NetType getNetType() {
        return netType.get();
    }

    /**
     * Net type property
     *
     * @return property containing current net type
     */
    public ObjectProperty<NetType> netTypeProperty() {
        return netType;
    }

    /**
     * Set new net type and fire {@link NetTypeChangedEvent}
     *
     * @param netType new NetType
     */
    public void setNetType(NetType netType) {
        this.netType.set(netType);
    }

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

    public void addElement(Node element, @NotNull Point3D position) {
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


            this.internalEventsHandler.fireEvent(new NetElementAddedEvent(element));
            return;
        }
        if (element instanceof Place) {
            places.add((Place) element);
            this.internalEventsHandler.fireEvent(new NetElementAddedEvent(element));
            return;
        }
        if (element instanceof Transition) {
            transitions.add((Transition) element);
            this.internalEventsHandler.fireEvent(new NetElementAddedEvent(element));
        }
    }

    public void removeElement(NetElement element) {
        if (this.allElementsStream().anyMatch(v -> Objects.equals(v, element))) {
            if (element instanceof Place place) {
                places.remove(place);
                place.inputs.forEach((transition, arc) -> this.removeElement(arc));
                place.outputs.forEach((transition, arc) -> this.removeElement(arc));
            }
            if (element instanceof Transition transition) {
                transitions.remove(transition);
                transition.inputs.forEach((place, arc) -> this.removeElement(arc));
                transition.outputs.forEach((place, arc) -> this.removeElement(arc));
            }
            if (element instanceof Arc arc)
                arcs.remove(arc);
            this.internalEventsHandler.fireEvent(new NetElementRemovedEvent(element));
        }
    }


    /**
     * Find all transitions that can be activated in the next step
     *
     * @return Stream of transitions that can be activated in the next step
     */
    public Stream<Transition> getTransitionsThatCanBeActivated() {
        System.out.println("Get transitions that can be activated");
        return getTransitions().stream()
                .filter(getCanActivateTester());
    }

    public Predicate<Transition> getCanActivateTester() {
        return switch (getNetType()) {
            case PN -> t -> activationRule.test(t);
            case FPN -> t ->
                    // there is at least one input with tokens > 0
                    t.inputs.entrySet().stream().anyMatch(placeArcEntry -> placeArcEntry.getKey().getTokensAs(Double.class) > 0.000001)
                            // and input condition is satisfied
                            && t.getFuzzyInputValue() >= t.inputTreshold
            ;

            //noinspection UnnecessaryDefault
            default -> throw new IllegalStateException("Unhandled net type");
        };
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Net net)) return false;
        return lastId == net.lastId && usedNames.equals(net.usedNames) && activationRule == net.activationRule && getArcs().equals(net.getArcs()) && getPlaces().equals(net.getPlaces()) && getTransitions().equals(net.getTransitions());
    }

    /**
     * Calculates Net hash based on the {@link #lastId} and lengths of {@link #places} {@link #transitions} and {@link #arcs}
     *
     * @return hash code of the net
     */
    @Override
    public int hashCode() {
        return Objects.hash(lastId, getArcs().size(), getPlaces().size(), getTransitions().size(), getNetType());
    }
}
