package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks annotated element as not to be used with given {@link NetType}
 *
 * Has HIGHER priority than {@link UsedInNetGroup}
 * Has HIGHER priority than {@link UsedInNetType}
 * Has HIGHER priority than {@link NotInNetGroup}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface NotInNetType {
    NetType[] value();
}
