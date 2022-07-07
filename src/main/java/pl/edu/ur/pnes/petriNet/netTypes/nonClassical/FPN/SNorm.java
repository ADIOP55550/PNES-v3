package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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


    public static Set<SNorm> ALL_S_NORMS;

    static {
        ALL_S_NORMS = Arrays.stream(SNorm.class.getDeclaredFields()).filter(field -> field.getType().equals(SNorm.class)).map(f -> {
            try {
                return (SNorm) f.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not get field " + f.getName());
            }
        }).collect(Collectors.toUnmodifiableSet());
    }
}
