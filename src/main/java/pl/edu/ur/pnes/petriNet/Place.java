package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.ur.pnes.petriNet.events.NetEvent;
import pl.edu.ur.pnes.petriNet.events.NetTypeChangedEvent;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.EditableInGUI;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.GetterFor;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.SetterFor;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.TypeInNetType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Place extends Node {
    private static int placeCounter = 0;
    @EditableInGUI(useGetter = true, useSetter = true)
    @TypeInNetType(type = Double.class, netType = NetType.FPN)
    @TypeInNetType(type = Integer.class, netType = NetType.PN)
    private final ObjectProperty<Object> tokens = new SimpleObjectProperty<>(0);
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
            // increment ids until free one is found
        }
        while (net.isNameUsed(newId));


        this.setName(newId);

        net.addEventHandler(NetEvent.TYPE_CHANGED, this::netTypeChanged);

        // invoke once to set initial values
        this.netTypeChanged(new NetTypeChangedEvent(net.getNetType(), net.getNetType().netGroup));
    }


    @Override
    public ObservableList<String> getClasses() {
        return FXCollections.observableArrayList("place");
    }

    /**
     * Determines if given tokens type can be used in current net type
     *
     * @param type tokens type to check
     * @return true if given tokens type can be used, else false
     * @see pl.edu.ur.pnes.petriNet.netTypes.NetType
     * @see #tokensProperty()
     */
    public boolean canUseTokensAs(Class<?> type) {
        return net.getNetType().tokensType.equals(type);
    }

    /**
     * Reads tokensProperty as specified type. Throws if type is incompatible with current net type
     *
     * @param type Attempt will be made to read tokens in this type
     * @param <T>  tokens type
     * @return tokens as T type if they can be accessed
     * @throws IllegalStateException if tokens cannot be accessed as given type
     */
    public <T> T getTokensAs(Class<T> type) throws IllegalStateException {
        if (!canUseTokensAs(type))
            throw new IllegalStateException("Cannot access tokens as " + type.getSimpleName() + " in net type " + this.net.getNetType());

        //noinspection unchecked
        return (T) this.tokens.get();
    }

    /**
     * Returns tokens as an underlying object
     *
     * @return uncast tokens value
     */
    @GetterFor("tokens")
    public Object getTokens() {
        return this.tokens.get();
    }

    /**
     * Gets string representation of tokens value
     *
     * @return string representing current tokens value
     */
    public String getTokensAsString() {
        return this.tokens.getValue().toString();
    }

    /**
     * Tries to set tokens used specified type
     *
     * @param type  Attempt will be made to set tokens in this type
     * @param value New value for tokens to be set to
     * @param <T>   type of tokens
     * @throws IllegalStateException if tokens cannot be set as given type in current net type
     */
    public <T> void setTokensAs(Class<T> type, T value) throws IllegalStateException {
        if (!canUseTokensAs(type))
            throw new IllegalStateException("Cannot set tokens as " + type.getSimpleName() + " in net type " + this.net.getNetType());

        this.tokens.set(value);
    }

    /**
     * Set tokens object
     * Warning! it does not check if set type is correct!
     *
     * @param value raw new value for tokens
     */
    @SetterFor("tokens")
    public void setTokens(Object value) {
        this.tokens.set(value);
    }


    /**
     * Gets tokensProperty as a cast to specified type property. Throws if type is incompatible with current net type
     *
     * @param type Attempt will be made to read tokensProperty in this type
     * @param <T>  tokens type
     * @return tokens as T type if they can be accessed
     * @throws IllegalStateException if tokens cannot be accessed as given type
     */
    public <T> ObjectProperty<T> tokensPropertyAs(Class<T> type) {
        if (!canUseTokensAs(type))
            throw new IllegalStateException("Cannot access tokens property as " + type.getSimpleName() + " in net type " + this.net.getNetType());

        //noinspection unchecked
        return (ObjectProperty<T>) this.tokens;
    }

    /**
     * Gets raw tokens property
     *
     * @return raw tokens property
     */
    public ObjectProperty<Object> tokensProperty() {
        return this.tokens;
    }

    /**
     * Handles net type changing
     */
    private void netTypeChanged(NetTypeChangedEvent event) {
        switch (event.getNewType()) {
            case PN -> {
                try {
                    this.setTokensAs(Integer.class, (Integer) this.getTokens());
                } catch (ClassCastException __) {
                    this.setTokensAs(Integer.class, 0);
                }
            }
            case FPN -> {
                try {
                    this.setTokensAs(Double.class, (Double) this.getTokens());
                } catch (ClassCastException __) {
                    this.setTokensAs(Double.class, 0d);
                }
            }
        }
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

    public boolean canBeConnectedTo(Node other) {
        return !(
                (Objects.equals(this, other))
                        || other instanceof Place
                        || this.outputs.keySet().stream().anyMatch(v -> Objects.equals(v, other))
        );
    }
}
