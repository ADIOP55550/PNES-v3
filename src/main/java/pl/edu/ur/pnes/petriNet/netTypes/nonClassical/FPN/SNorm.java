package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

import java.util.function.DoubleBinaryOperator;

/**
 * Represents S-Norm (T-conorm)
 *
 * @see Aggregation
 * @see <a href="https://en.wikipedia.org/wiki/T-norm#T-conorms">https://en.wikipedia.org/wiki/T-norm#T-conorms</a>
 */
public interface SNorm extends DoubleBinaryOperator {
    /**
     * IDENTITY ELEMENT for all S-Norms
     */
    double IDENTITY_ELEMENT = 0;

    Aggregation MIN_S_NORM = new MinSNorm();


}
