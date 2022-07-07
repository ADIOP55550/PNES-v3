package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static final TNorm MIN_T_NORM = new MinTNorm();

    public static Set<TNorm> ALL_T_NORMS;

    static {
        ALL_T_NORMS = Arrays.stream(TNorm.class.getDeclaredFields()).filter(field -> field.getType().equals(TNorm.class)).map(f -> {
            try {
                return (TNorm) f.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not get field " + f.getName());
            }
        }).collect(Collectors.toUnmodifiableSet());
    }
}
