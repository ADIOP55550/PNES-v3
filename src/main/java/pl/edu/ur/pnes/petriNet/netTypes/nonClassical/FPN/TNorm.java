package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

import java.util.function.DoubleBinaryOperator;


/**
 * Represents T-Norm
 *
 * @see Aggregation
 * @see <a href="https://en.wikipedia.org/wiki/T-norm#Definition">https://en.wikipedia.org/wiki/T-norm#Definition</a>
 */
public interface TNorm extends DoubleBinaryOperator {
    /**
     * IDENTITY ELEMENT for all T-norms
     */
    double IDENTITY_ELEMENT = 1;

    Aggregation MAX_T_NORM = new MaxTNorm();
}
