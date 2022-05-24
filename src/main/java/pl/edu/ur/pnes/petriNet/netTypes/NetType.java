package pl.edu.ur.pnes.petriNet.netTypes;

import pl.edu.ur.pnes.petriNet.netTypes.annotations.UsedInNetType;

import java.util.function.Predicate;

/**
 * Describes a net type. Has information about its group, name, description, tokens property type, tokens validator
 * Can be used with {@link pl.edu.ur.pnes.petriNet.netTypes.annotations.NotInNetType} and {@link UsedInNetType} to conditionally enable and disable fields
 *
 * @see NetGroup
 * @see UsedInNetType
 * @see pl.edu.ur.pnes.petriNet.netTypes.annotations.NotInNetType
 */
public enum NetType {
    /**
     * Most classical Petri net
     */
    PN(
            "Perti Net",
            NetGroup.CLASSICAL,
            "A regular Petri net with integer tokens",
            Integer.class,
            integer -> integer >= 0
    ),
    /**
     * Fuzzy Petri Net
     */
    FPN(
            "Fuzzy Perti Net",
            NetGroup.NON_CLASSICAL,
            "A fuzzy Petri net with double token values in range [0-1]",
            Double.class,
            aDouble -> aDouble >= 0 && aDouble <= 1
    );


    /**
     * Group of this net
     */
    public NetGroup netGroup;
    /**
     * Name to be displayed in UI
     */
    public String longName;
    /**
     * Hint for this net type (in the UI)
     */
    public String hint;
    /**
     * Type of tokens property used in this net
     */
    public Class<?> tokensType;
    /**
     * Validator that can be used to validate user input in properties panel
     */
    public Predicate<?> tokensValueValidator;

    /**
     *
     * @param longName Name to be displayed in UI
     * @param netGroup Group of this net
     * @param hint Hint for this net type (in the UI)
     * @param tokensType Type of tokens property used in this net
     * @param tokensValueValidator Validator that can be used to validate user input in properties panel
     * @param <T> tokens type
     */
    <T> NetType(String longName, NetGroup netGroup, String hint, Class<T> tokensType, Predicate<T> tokensValueValidator) {
        this.netGroup = netGroup;
        this.longName = longName;
        this.hint = hint;
        this.tokensType = tokensType;
        this.tokensValueValidator = tokensValueValidator;
    }
}
