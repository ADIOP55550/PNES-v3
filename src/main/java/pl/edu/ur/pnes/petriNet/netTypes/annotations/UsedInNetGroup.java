package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks annotated element to be used with given {@link NetGroup}
 * <p>
 * Has LOWER priority than {@link NotInNetGroup}
 * Has LOWER priority than {@link UsedInNetType}
 * Has LOWER priority than {@link NotInNetType}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface UsedInNetGroup {
    NetGroup[] value();
}
