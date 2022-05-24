package pl.edu.ur.pnes.petriNet.simulator;

import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.petriNet.Net;

public class SimulatorFactory {
    public static SimulatorFacade create(Session session) {
        return new SimulatorFacade(new Simulator(session.net), session);
    }
}
