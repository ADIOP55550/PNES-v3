package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks annotated element to be used with given {@link NetType}
 * <p>
 * Has HIGHER priority than {@link UsedInNetGroup}
 * Has LOWER priority than {@link NotInNetType}
 * Has HIGHER priority than {@link NotInNetGroup}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface UsedInNetType {
    NetType[] value();
}
