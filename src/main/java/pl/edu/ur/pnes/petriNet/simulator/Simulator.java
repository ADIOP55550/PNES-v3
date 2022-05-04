package pl.edu.ur.pnes.petriNet.simulator;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Transition;

import java.util.Objects;
import java.util.Random;
import java.util.function.DoubleBinaryOperator;

class Simulator {

    private boolean firstStep = true;
    private final Random r = new Random();

    final Net net;
    private final Logger logger = LogManager.getLogger(Simulator.class);
    private Transition lastActivatedTransitions = null;
    final ObservableList<Transition> transitionsThatCanBeActivated = FXCollections.observableArrayList();
    private DoubleBinaryOperator adder = (left, right) -> left + right;
    private DoubleBinaryOperator subtractor = (left, right) -> left - right;

    public Simulator(Net net) {
        this.net = net;
        transitionsThatCanBeActivated.addListener((ListChangeListener<Transition>) c -> {
            logger.debug("transitionsThatCanBeActivated changed!");
            while (c.next()) {
                if (c.wasPermutated() || c.wasUpdated()) continue;

                for (Transition removedItem : c.getRemoved()) removedItem.setReady(false);
                for (Transition addedItem : c.getAddedSubList()) addedItem.setReady(true);
            }
        });
    }

    public void setSubtractor(DoubleBinaryOperator subtractor) {
        this.subtractor = subtractor;
    }

    public void setAdder(DoubleBinaryOperator adder) {
        this.adder = adder;
    }

    void fireTransition(Transition transition) throws TransitionCannotBeActivatedException {
        logger.info("Transition fired! " + transition.getName());


        if (lastActivatedTransitions != null) {
            lastActivatedTransitions.lastActivated.set(false);
            lastActivatedTransitions = null;
        }

        if (!net.activationRule.test(transition)) {
            logger.error("Transition " + transition.getName() + " could not be activated!");
            throw new TransitionCannotBeActivatedException("This transition cannot be activated");
        }

        transition.inputs.forEach((place, arc) -> {
            logger.debug("changing input Place " + place.getName());
            place.setTokens(this.subtractor.applyAsDouble(place.getTokens(), arc.getWeight()));
        });

        transition.outputs.forEach((place, arc) -> {
            logger.debug("changing output Place " + place.getName());
            place.setTokens(this.adder.applyAsDouble(place.getTokens(), arc.getWeight()));
        });

        lastActivatedTransitions = transition;
        transition.lastActivated.set(true);
    }


    void automaticStep() {
        this.logger.info("automatic step fired");

        if (this.checkIfDone()) return;

        if (this.firstStep) {
            this.firstStep = false;
            System.out.println("firstStep");
            return;
        }

        this.logger.debug("Can activate {} transitions", this.transitionsThatCanBeActivated.size());

        var transition = this.transitionsThatCanBeActivated.get(this.r.nextInt(this.transitionsThatCanBeActivated.size()));

        this.logger.debug("Chosen transition: {}", transition.getName());

        try {
            fireTransition(transition);
        } catch (TransitionCannotBeActivatedException e) {
            e.printStackTrace();
        }

        this.calculateTransitionsThatCanBeActivated();
    }

    boolean checkIfDone() {
        this.calculateTransitionsThatCanBeActivated();

        if (this.transitionsThatCanBeActivated.isEmpty()) {
            this.logger.warn("No transitions can be activated");
            return true;
        }
        return false;
    }

    void calculateTransitionsThatCanBeActivated() {
        this.transitionsThatCanBeActivated.setAll(net.getTransitionsThatCanBeActivated().toList());
    }

    NetSnapshot getSnapshot() {
        return new NetSnapshot(net);
    }

    void restoreSnapshotIfPossible(NetSnapshot snapshot) {
        if (snapshot == null)
            return;
        restoreSnapshot(snapshot);
    }

    private void restoreSnapshot(NetSnapshot snapshot) {
        Objects.requireNonNull(snapshot).restoreTo(net);
        clearTransitionsThatCanBeActivated();
    }

    private void clearTransitionsThatCanBeActivated() {
        transitionsThatCanBeActivated.clear();
    }

    void fireTransition(Transition transition) throws TransitionCannotBeActivatedException {
        logger.info("Transition fired! " + transition.getId());


        if (lastActivatedTransitions != null) {
            lastActivatedTransitions.lastActivated.set(false);
            lastActivatedTransitions = null;
        }

        if (!net.activationRule.test(transition)) {
            logger.error("Transition " + transition.getId() + " could not be activated!");
            throw new TransitionCannotBeActivatedException("This transition cannot be activated");
        }

        transition.inputs.forEach((place, arc) -> {
            logger.debug("changing input Place " + place.getId());
            place.setTokens(place.getTokens() - arc.getWeight());
        });

        transition.outputs.forEach((place, arc) -> {
            logger.debug("changing output Place " + place.getId());
            place.setTokens(place.getTokens() + arc.getWeight());
        });

        lastActivatedTransitions = transition;
        transition.lastActivated.set(true);
    }


}
