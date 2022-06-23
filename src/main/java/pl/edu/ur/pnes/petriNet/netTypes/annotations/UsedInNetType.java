package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetType;

import java.lang.annotation.*;

/**
 * Marks annotated element to be used with given {@link NetType}
 * <p>
 * Has HIGHER priority than {@link UsedInNetGroup}
 * Has LOWER priority than {@link NotInNetType}
 * Has HIGHER priority than {@link NotInNetGroup}
 */

@AnnotationsPriority(2)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Repeatable(UsedInNetTypes.class)
public @interface UsedInNetType {
    NetType[] value();
}
