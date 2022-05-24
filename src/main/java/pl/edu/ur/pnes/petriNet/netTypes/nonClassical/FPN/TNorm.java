package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

/**
 * Represents T-Norm
 *
 * @see Aggregation
 * @see <a href="https://en.wikipedia.org/wiki/T-norm#Definition">https://en.wikipedia.org/wiki/T-norm#Definition</a>
 */
public abstract class TNorm extends Aggregation {
    /**
     * IDENTITY ELEMENT for all T-norms
     */
    public static final double IDENTITY_ELEMENT = 1;

    public static final TNorm MAX_T_NORM = new MaxTNorm();
}
