package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

import java.util.Set;
import java.util.function.DoubleBinaryOperator;

/**
 * Any SNorm or TNorm
 *
 * @see pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN.SNorm
 * @see pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN.TNorm
 * @see <a href="https://en.wikipedia.org/wiki/Fuzzy_set_operations#Aggregation_operations">https://en.wikipedia.org/wiki/Fuzzy_set_operations#Aggregation_operations</a></a>
 */
public abstract class Aggregation implements DoubleBinaryOperator {

    public static Set<Aggregation> ALL_AGGREGATIONS() {
        Set<Aggregation> _ALL_AGGREGATIONS = new java.util.HashSet<>(TNorm.ALL_T_NORMS);
        _ALL_AGGREGATIONS.addAll(SNorm.ALL_S_NORMS);

        return _ALL_AGGREGATIONS;
    }

}

