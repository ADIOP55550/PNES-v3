package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import pl.edu.ur.pnes.petriNet.netTypes.NetGroup;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.UsedInNetGroup;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.UsedInNetType;
import pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN.Aggregation;
import pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN.SNorm;
import pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN.TNorm;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntBinaryOperator;

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
        while (net.isNameUsed(newId));
        this.setName(newId);
        classesList.add("transition");
    }

    /**
     * Get input value based on input places aggregeted using {@link #inputAggregation}
     *
     * @return calculated input value
     */
    @UsedInNetType(NetType.FPN)
    public double getFuzzyInputValue() {
        // get correct identity
        var identityElement = this.inputAggregation.getClass().isAssignableFrom(SNorm.class) ? SNorm.IDENTITY_ELEMENT : TNorm.IDENTITY_ELEMENT;
        // aggregate with Transition aggregation
        return this.inputs
                .entrySet().stream()
                .mapToDouble(placeArcEntry -> placeArcEntry.getKey().getTokensAs(Double.class) * placeArcEntry.getValue().getWeight()) // get all weights and multiply by arc weights
                .reduce(identityElement, this.inputAggregation);
    }

    /**
     * Get output value calculated by using {@link #internalTNorm} on input value and {@link #beta} coefficient
     *
     * @return Calculated output value
     */
    @UsedInNetType(NetType.FPN)
    public double getFuzzyOutputValue() {
        var inputValue = this.getFuzzyInputValue();
        return this.internalTNorm.applyAsDouble(inputValue, this.beta);
    }


    /**
     * Truth degree (beta) coefficient
     */
    @UsedInNetType(NetType.FPN)
    public Double beta = 1d;

    /**
     * Aggregation used to aggregate all of this transition input values
     *
     * @see #getFuzzyInputValue()
     * @see SNorm
     * @see #inputs
     */
    @UsedInNetType(NetType.FPN)
    public Aggregation inputAggregation = SNorm.MIN_S_NORM;

    /**
     * T-Norm used to calculate output value of this transition
     *
     * @see #getFuzzyOutputValue()
     */
    @UsedInNetType(NetType.FPN)
    public TNorm internalTNorm = TNorm.MAX_T_NORM;

    /**
     * S-Norm used to store new value in this transition output places
     *
     * @see SNorm
     * @see #outputs
     */
    @UsedInNetType(NetType.FPN)
    public SNorm outputSNorm = SNorm.MIN_S_NORM;

    /**
     * Threshold which determines if this transition can be fired
     *
     * @see Net#getTransitionsThatCanBeActivated()
     */
    @UsedInNetType(NetType.FPN)
    public Double inputTreshold = 0d;


    /**
     * Function used to sum current place value with arc input
     */
    @UsedInNetGroup(NetGroup.CLASSICAL)
    private IntBinaryOperator adder = (left, right) -> left + right;

    /**
     * Function used to remove tokens from place with arc
     */
    @UsedInNetGroup(NetGroup.CLASSICAL)
    private IntBinaryOperator subtractor = (left, right) -> left - right;


    @UsedInNetGroup(NetGroup.CLASSICAL)
    public IntBinaryOperator getSubtractor() {
        return subtractor;
    }

    @UsedInNetGroup(NetGroup.CLASSICAL)
    public void setSubtractor(IntBinaryOperator subtractor) {
        this.subtractor = subtractor;
    }

    @UsedInNetGroup(NetGroup.CLASSICAL)
    public IntBinaryOperator getAdder() {
        return adder;
    }

    @UsedInNetGroup(NetGroup.CLASSICAL)
    public void setAdder(IntBinaryOperator adder) {
        this.adder = adder;
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


    public boolean canBeConnectedTo(Node other) {
        return !(
                (Objects.equals(this, other))
                        || other instanceof Transition
                        || this.outputs.keySet().stream().anyMatch(v -> Objects.equals(v, other))
        );
    }
}
