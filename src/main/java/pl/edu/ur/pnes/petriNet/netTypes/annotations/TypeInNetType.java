package pl.edu.ur.pnes.petriNet.netTypes.annotations;

import pl.edu.ur.pnes.petriNet.netTypes.NetType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(TypesInTypesContainer.class)

public @interface TypeInNetType {
    Class<?> type();

    NetType netType();
}
