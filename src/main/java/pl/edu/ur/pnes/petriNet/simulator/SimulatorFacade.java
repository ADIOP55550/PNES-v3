package pl.edu.ur.pnes.petriNet.simulator;

import pl.edu.ur.pnes.petriNet.Transition;

import java.util.Random;
import java.util.stream.Stream;

public class SimulatorFacade {

    private final Simulator simulator;

    SimulatorFacade(Simulator simulator) {
        this.simulator = simulator;
    }


    public void automaticStep() {
        var r = new Random();
        var transitionsThatCanBeActivated = getTransitionsThatCanBeActivated();

    }

    private Stream<Transition> getTransitionsThatCanBeActivated() {
        return simulator.net.getTransitions().stream()
                .filter(t -> simulator.net.activationRule.test(t));
    }

    public void manualStep(Transition transitionToBeActivated) {

    }


}
