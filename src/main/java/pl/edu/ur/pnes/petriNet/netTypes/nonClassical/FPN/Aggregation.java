package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

import static java.lang.Double.NaN;

/**
 * Any SNorm or TNorm
 *
 * @see pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN.SNorm
 * @see pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN.TNorm
 * @see <a href="https://en.wikipedia.org/wiki/Fuzzy_set_operations#Aggregation_operations">https://en.wikipedia.org/wiki/Fuzzy_set_operations#Aggregation_operations</a></a>
 */
public interface Aggregation extends SNorm, TNorm {
    double IDENTITY_ELEMENT = NaN;

}
