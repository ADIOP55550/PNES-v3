package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetType;

import java.lang.annotation.*;

/**
 * Marks annotated element as not to be used with given {@link NetType}
 *
 * Has HIGHER priority than {@link UsedInNetGroup}
 * Has HIGHER priority than {@link UsedInNetType}
 * Has HIGHER priority than {@link NotInNetGroup}
 */

@AnnotationsPriority(3)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Repeatable(NotInNetTypes.class)
public @interface NotInNetType {
    NetType[] value();
}
