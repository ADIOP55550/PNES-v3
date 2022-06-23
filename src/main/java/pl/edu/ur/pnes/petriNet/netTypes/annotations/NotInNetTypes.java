package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@AnnotationsPriority(3)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotInNetTypes {
    NotInNetType[] value();
}
