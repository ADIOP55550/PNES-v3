package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

/**
 * Represents S-Norm (T-conorm)
 *
 * @see Aggregation
 * @see <a href="https://en.wikipedia.org/wiki/T-norm#T-conorms">https://en.wikipedia.org/wiki/T-norm#T-conorms</a>
 */
public abstract class SNorm extends Aggregation {
    /**
     * IDENTITY ELEMENT for all S-Norms
     */
    public static final double IDENTITY_ELEMENT = 0;

    public static final SNorm MAX_S_NORM = new MaxSNorm();
}
