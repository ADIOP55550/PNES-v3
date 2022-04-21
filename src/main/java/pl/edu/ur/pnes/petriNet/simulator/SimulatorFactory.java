package pl.edu.ur.pnes.petriNet.simulator;

public class SimulatorFactory {
    public static SimulatorFacade create() {
        return new SimulatorFacade(new Simulator());
    }
}
