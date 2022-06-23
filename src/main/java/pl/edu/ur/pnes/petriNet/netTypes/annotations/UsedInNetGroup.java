package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetGroup;

import java.lang.annotation.*;


/**
 * Marks annotated element to be used with given {@link NetGroup}
 * <p>
 * Has LOWER priority than {@link NotInNetGroup}
 * Has LOWER priority than {@link UsedInNetType}
 * Has LOWER priority than {@link NotInNetType}
 */


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Repeatable(UsedInNetGroups.class)
public @interface UsedInNetGroup {
    NetGroup value();
}
