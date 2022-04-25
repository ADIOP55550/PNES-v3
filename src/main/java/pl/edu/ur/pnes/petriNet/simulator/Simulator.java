package pl.edu.ur.pnes.petriNet.simulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Transition;

class Simulator {

    final Net net;
    private final Logger logger = LogManager.getLogger(Simulator.class);
    private Transition lastActivatedTransitions = null;

    public Simulator(Net net) {
        this.net = net;
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
//            place.needsRedraw.set(true);
        });

        transition.outputs.forEach((place, arc) -> {
            logger.debug("changing output Place " + place.getId());
            place.setTokens(place.getTokens() + arc.getWeight());
//            place.needsRedraw.set(true);
        });

        lastActivatedTransitions = transition;
        transition.lastActivated.set(true);
//        transition.needsRedraw.set(true);
    }


}
