package pl.edu.ur.pnes.petriNet.simulator;

import pl.edu.ur.pnes.petriNet.Net;

public class SimulatorFactory {
    public static SimulatorFacade create(Net net) {
        return new SimulatorFacade(new Simulator(net));
    }
}
