package pl.edu.ur.pnes.petriNet.simulator;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.petriNet.Transition;

import java.util.Random;

public class SimulatorFacade {

    private final Simulator simulator;
    private final Logger logger = LogManager.getLogger(SimulatorFacade.class);
    private final ObservableList<Transition> transitionsThatCanBeActivated = FXCollections.observableArrayList();

    private boolean firstStep = true;

    /*
    TODO: Add event dispatcher and turn step into events:
        Via event filters you can intercept event before occurring
        Via event handlers you can invoke actions after event occurred
     */


    SimulatorFacade(Simulator simulator) {
        this.simulator = simulator;
        transitionsThatCanBeActivated.addListener((ListChangeListener<Transition>) c -> {
            logger.debug("transitionsThatCanBeActivated changed!");
            while (c.next()) {
                if (!c.wasPermutated() && !c.wasUpdated()) {
                    for (Transition removedItem : c.getRemoved()) {
                        logger.debug("Not ready: {}", removedItem.getId());
                        removedItem.setReady(false);
                    }
                    for (Transition addedItem : c.getAddedSubList()) {
                        logger.debug("Ready: {}", addedItem.getId());
                        addedItem.setReady(true);
                    }
                }
            }
        });
    }


    public void automaticStep() {
        this.logger.info("automatic step fired");

        calculateTransitionsThatCanBeActivated();

        if (firstStep) {
            firstStep = false;
            System.out.println("firstStep");
            return;
        }

        var r = new Random();
        this.logger.debug("Can activate {} transitions", transitionsThatCanBeActivated.size());

        if (transitionsThatCanBeActivated.isEmpty()) {
            this.logger.warn("No transitions can be activated");
            return;
        }

        var transition = transitionsThatCanBeActivated.get(r.nextInt(transitionsThatCanBeActivated.size()));

        this.logger.debug("Chosen transition: {}", transition.getId());

        try {
            simulator.fireTransition(transition);
        } catch (TransitionCannotBeActivatedException e) {
            e.printStackTrace();
        }
    }

    private void calculateTransitionsThatCanBeActivated() {
        this.transitionsThatCanBeActivated.setAll(simulator.net.getTransitionsThatCanBeActivated().toList());
    }


    public void manualStep(Transition transitionToBeActivated) {
        try {
            simulator.fireTransition(transitionToBeActivated);
        } catch (TransitionCannotBeActivatedException e) {
            e.printStackTrace();
        }
        calculateTransitionsThatCanBeActivated();
    }


}
