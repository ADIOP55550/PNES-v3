package pl.edu.ur.pnes.petriNet.simulator;


import pl.edu.ur.pnes.petriNet.Transition;

import java.util.function.Predicate;

public enum Rules {
    R1(transition ->
            transition.inputs.entrySet().stream().allMatch(
                    placeArcEntry -> placeArcEntry.getKey().getTokens() >= placeArcEntry.getValue().getWeight()
            )
    ),
    R2(transition -> {
        // todo implement
        return true;
    }),
    R3(transition -> {
        // todo implement
        return true;
    });


    public boolean test(Transition t) {
        return this.predicate.test(t);
    }


    private final Predicate<Transition> predicate;

    Rules(Predicate<Transition> predicate) {
        this.predicate = predicate;
    }
}
