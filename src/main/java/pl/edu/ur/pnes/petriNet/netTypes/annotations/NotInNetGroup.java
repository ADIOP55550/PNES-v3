package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetGroup;

import java.lang.annotation.*;

/**
 * Marks annotated element as not to be used with given {@link NetGroup}
 *
 * Has HIGHER priority than {@link UsedInNetGroup}
 * Has LOWER priority than {@link UsedInNetType}
 * Has LOWER priority than {@link NotInNetType}
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Repeatable(NotInNetGroups.class)
public @interface NotInNetGroup {
    NetGroup value();
}
